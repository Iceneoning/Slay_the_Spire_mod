package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

import Gan_Yu.helpers_GY.ModHelper;

public class ChargePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("ChargePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    // 蓄力消耗标记的ID
    private static final String CONSUMED_MARKER_ID = ModHelper.makePath("ChargeConsumedThisTurn");
    
    // 每层蓄力提供的基础力量值（固定值，不应被其他Power修改）
    private static final int BASE_STRENGTH_PER_CHARGE = 6;

    // 防止重复排队消费动作的标志
    private boolean consumeQueued = false;

    public ChargePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.canGoNegative = false;
        this.img = new Texture("GanYu/img/powers/ChargePower.png");
        this.type = PowerType.BUFF;
        // 注意：不要在构造函数中直接应用力量或触发获得效果。
        // ApplyPowerAction 在处理已有同名Power时会调用 stackPower，而传入的 power 实例
        // 会在构造时运行，这会导致重复应用（构造器 + stackPower），从而产生力量翻倍的问题。
        // 所以把首次应用的逻辑放到 onInitialApplication() 中执行，只有真正被添加到 owner 时才触发。
    }

    @Override
    public void onInitialApplication() {
        // 当这个Power实例真正被添加到生物身上时才应用力量和触发获得效果
        System.out.println("[DEBUG] ChargePower.onInitialApplication: amount=" + this.amount + ", owner=" + (this.owner != null ? this.owner.name : "null"));
        updateStrength(0, this.amount);
        updateDescription();
        triggerChargeGained(this.amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        int oldAmount = this.amount;
        int newAmount = this.amount + stackAmount;
        if (newAmount > 2) {
            newAmount = 2; // 蓄力上限为2
        }
        int actualGain = newAmount - oldAmount;
        this.amount = newAmount;

        // 更新力量值，只应用变化量
        System.out.println("[DEBUG] ChargePower.stackPower: oldAmount=" + oldAmount + ", newAmount=" + this.amount + ", actualGain=" + actualGain);
        updateStrength(oldAmount, this.amount);
        // 仅当实际增加时才触发获得蓄力的效果
        if (actualGain > 0) {
            triggerChargeGained(actualGain);
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
    
    private void updateStrength(int oldAmount, int newAmount) {
        // 计算旧的力量值和新的力量值
        int oldStrength = oldAmount * BASE_STRENGTH_PER_CHARGE;
        int newStrength = newAmount * BASE_STRENGTH_PER_CHARGE;
        
        // 计算力量变化值
        int strengthDiff = newStrength - oldStrength;
        
        // 只有当力量值发生变化时才应用变化
        if (strengthDiff != 0) {
            System.out.println("[DEBUG] ChargePower.updateStrength: oldAmount=" + oldAmount + ", newAmount=" + newAmount + ", strengthDiff=" + strengthDiff + ", owner=" + (this.owner != null ? this.owner.name : "null"));
            // 立即将力量应用到目标上，使用 addToTop 以确保在随后立即发生的动作（例如本回合的伤害）之前生效
            System.out.println("[DEBUG] ChargePower.updateStrength: queueing ApplyPowerAction(Strength " + strengthDiff + ") via addToTop");
            addToTop(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, strengthDiff), strengthDiff));
        }
    }
    
    public void onAttackPlay() {
        // 打出攻击牌时移除所有蓄力（只要有蓄力就消耗）
        if (this.amount > 0) {
            // 将消费动作加入队列（如果未排队）
            if (!this.consumeQueued) {
                System.out.println("[DEBUG] ChargePower.onAttackPlay: queuing consume action. current amount=" + this.amount + ", owner=" + (this.owner != null ? this.owner.name : "null"));
                this.consumeQueued = true;
                addToBot(getConsumeAction());
            }
        }
    }
    
    // 添加onUseCard方法，确保在使用任何卡牌时都能检测到攻击牌
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            // 使用addToBot避免ConcurrentModificationException
            addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    onAttackPlay();
                    this.isDone = true;
                }
            });
        }
    }
    
    // 添加onLoseHp方法，当生命值损失时消耗蓄力
    @Override
    public int onLoseHp(int damageAmount) {
        // 生命值损失时移除所有蓄力
        if (this.amount > 0 && damageAmount > 0) {
            if (!this.consumeQueued) {
                System.out.println("[DEBUG] ChargePower.onLoseHp: queuing consume action due to losing hp. damageAmount=" + damageAmount + ", current amount=" + this.amount);
                this.consumeQueued = true;
                addToBot(getConsumeAction());
            }
        }
        return damageAmount;
    }

    // 返回一个消费蓄力的动作（一次性消费当前所有层数）
    public com.megacrit.cardcrawl.actions.AbstractGameAction getConsumeAction() {
        return new com.megacrit.cardcrawl.actions.AbstractGameAction() {
            @Override
            public void update() {
                // 在执行动作时读取当前的蓄力层数
                final int consumedAmount = ChargePower.this.amount;
                // 在执行动作时再清零
                ChargePower.this.amount = 0;
        // 移除蓄力提供的力量
        System.out.println("[DEBUG] ChargePower.getConsumeAction: executing consume. consumedAmount=" + consumedAmount + ", removing strength=" + (-consumedAmount * BASE_STRENGTH_PER_CHARGE) + ", owner=" + (ChargePower.this.owner != null ? ChargePower.this.owner.name : "null"));
        System.out.println("[DEBUG] ChargePower.getConsumeAction: queueing ApplyPowerAction(Strength " + (-consumedAmount * BASE_STRENGTH_PER_CHARGE) + ") via addToBot");
        addToBot(new ApplyPowerAction(ChargePower.this.owner, ChargePower.this.owner,
            new StrengthPower(ChargePower.this.owner, -consumedAmount * BASE_STRENGTH_PER_CHARGE), -consumedAmount * BASE_STRENGTH_PER_CHARGE));
        // 先移除自身Power，确保后续由其他Power（如 EndPower）加入的新 ChargePower 不会被立即删除
        addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(ChargePower.this.owner, ChargePower.this.owner, ChargePower.this.ID));
        updateDescription();
        // 触发相关Power效果（此时已有移除动作排在队列中，后续由 EndPower 队列加入的添加动作会在移除动作之后执行）
        triggerPowersOnConsumed(consumedAmount);
                ChargePower.this.consumeQueued = false;
                this.isDone = true;
            }
        };
    }
    
    private void triggerPowersOnConsumed(int consumedAmount) {
        // 当蓄力被消耗时触发相关Power效果
        // 触发HandyPower效果
        if (this.owner.hasPower("Gan_Yu:HandyPower")) {
            AbstractPower handyPower = this.owner.getPower("Gan_Yu:HandyPower");
            try {
                handyPower.getClass().getMethod("onChargeConsumed", int.class).invoke(handyPower, consumedAmount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }
        
        // 触发GoAllOutPower效果
        if (this.owner.hasPower("Gan_Yu:GoAllOutPower")) {
            AbstractPower goAllOutPower = this.owner.getPower("Gan_Yu:GoAllOutPower");
            try {
                goAllOutPower.getClass().getMethod("onChargeConsumed", int.class).invoke(goAllOutPower, consumedAmount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }
        
        // 触发AttackToDefendPower效果
        if (this.owner.hasPower("Gan_Yu:AttackToDefendPower")) {
            AbstractPower attackToDefendPower = this.owner.getPower("Gan_Yu:AttackToDefendPower");
            try {
                attackToDefendPower.getClass().getMethod("onChargeConsumed", int.class).invoke(attackToDefendPower,
                        consumedAmount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }

        // 触发PrayPower效果
        if (this.owner.hasPower("Gan_Yu:PrayPower")) {
            AbstractPower prayPower = this.owner.getPower("Gan_Yu:PrayPower");
            try {
                prayPower.getClass().getMethod("onChargeConsumed", int.class).invoke(prayPower, consumedAmount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }

        if (this.owner.hasPower("Gan_Yu:EndPower")) {
            AbstractPower endPower = this.owner.getPower("Gan_Yu:EndPower");
            try {
                endPower.getClass().getMethod("onChargeConsumed", int.class).invoke(endPower, consumedAmount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }
        // 触发CountlessTrialsPower效果：先消耗再获得
        if (this.owner.hasPower("Gan_Yu:CountlessTrialsPower")) {
            AbstractPower countless = this.owner.getPower("Gan_Yu:CountlessTrialsPower");
            try {
                countless.getClass().getMethod("onChargeConsumed", int.class).invoke(countless, consumedAmount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }
        
        // 添加蓄力消耗标记
        addChargeConsumedMarker();
    }
    
    
    private void addChargeConsumedMarker() {
        // 添加一个标记表示本回合消耗过蓄力
        if (!this.owner.hasPower(CONSUMED_MARKER_ID)) {
            // 使用addToBot避免ConcurrentModificationException
            addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    // 再次检查以防止重复添加
                    if (!ChargePower.this.owner.hasPower(CONSUMED_MARKER_ID)) {
                        AbstractPower marker = new AbstractPower() {
                            {
                                this.name = "蓄力消耗标记";
                                this.ID = CONSUMED_MARKER_ID;
                                this.description = CardCrawlGame.languagePack.getPowerStrings(CONSUMED_MARKER_ID).DESCRIPTIONS[0];
                                this.owner = ChargePower.this.owner;
                                try {
                                    this.img = new Texture("GanYu/img/powers/LoseChargePower.png");
                                } catch (Exception e) {
                                    this.img = new Texture("images/powers/32/ritual.png");
                                }
                                this.type = PowerType.BUFF;
                            }

                            @Override
                            public void updateDescription() {
                                this.description = CardCrawlGame.languagePack.getPowerStrings(CONSUMED_MARKER_ID).DESCRIPTIONS[0];
                            }

                            @Override
                            public void atEndOfTurn(boolean isPlayer) {
                                if (isPlayer) {
                                    addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this));
                                }
                            }

                            @Override
                            public void atStartOfTurn() {
                                // 也在回合开始移除，防止在非玩家回合被消耗后残留
                                addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this));
                            }
                        };
                        ChargePower.this.owner.powers.add(marker);
                    }
                    this.isDone = true;
                }
            });
        }
    }
    
    private void triggerChargeGained(int amount) {
        // 当蓄力被获得时触发相关Power效果
        // 触发AttackToDefendPower效果
        if (this.owner.hasPower("Gan_Yu:AttackToDefendPower")) {
            AbstractPower attackToDefendPower = this.owner.getPower("Gan_Yu:AttackToDefendPower");
            try {
                attackToDefendPower.getClass().getMethod("onChargeGained", int.class).invoke(attackToDefendPower, amount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }
    }
}