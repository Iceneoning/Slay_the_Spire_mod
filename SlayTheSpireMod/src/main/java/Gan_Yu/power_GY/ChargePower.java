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
        updateStrength(0, this.amount);
        updateDescription();
        triggerChargeGained(this.amount);
    }

    @Override
    public void stackPower(int stackAmount) {
        int oldAmount = this.amount;
        this.amount += stackAmount;

        if (this.amount > 2) {
            this.amount = 2; // 蓄力上限为2
        }
        
        // 更新力量值，只应用变化量
        updateStrength(oldAmount, this.amount);
        // 触发获得蓄力的效果
        triggerChargeGained(stackAmount);
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
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, strengthDiff), strengthDiff));
        }
    }
    
    public void onAttackPlay() {
        // 打出攻击牌时移除所有蓄力（只要有蓄力就消耗）
        if (this.amount > 0) {
            int consumedAmount = this.amount;
            this.amount = 0;
            // 使用异步操作避免ConcurrentModificationException
            addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    // 移除蓄力提供的力量 (每层蓄力提供6点力量)
                    addToBot(new ApplyPowerAction(ChargePower.this.owner, ChargePower.this.owner, 
                        new StrengthPower(ChargePower.this.owner, -consumedAmount * BASE_STRENGTH_PER_CHARGE), -consumedAmount * BASE_STRENGTH_PER_CHARGE));
                    updateDescription();
                    // 触发相关Power效果
                    triggerPowersOnConsumed(consumedAmount);
                    this.isDone = true;
                }
            });
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
            // 使用addToBot避免ConcurrentModificationException
            addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    int consumedAmount = ChargePower.this.amount;
                    ChargePower.this.amount = 0;
                    addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -consumedAmount * BASE_STRENGTH_PER_CHARGE), -consumedAmount * BASE_STRENGTH_PER_CHARGE));
                    updateDescription();
                    // 触发相关Power效果
                    triggerPowersOnConsumed(consumedAmount);
                    this.isDone = true;
                }
            });
        }
        return damageAmount;
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
        if (this.owner.hasPower("Gan_Yu:EndPower")) {
            AbstractPower endPower = this.owner.getPower("Gan_Yu:EndPower");
            try {
                endPower.getClass().getMethod("onChargeConsumed", int.class).invoke(endPower, consumedAmount);
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
                                this.owner = ChargePower.this.owner;
                                // 设置必要的纹理属性以避免空指针异常
                                try {
                                    this.img = new Texture("GanYu/img/powers/LoseChargePower.png");
                                } catch (Exception e) {
                                    // 如果纹理加载失败，使用默认的空纹理
                                    this.img = new Texture("images/powers/32/ritual.png");
                                }
                                this.type = PowerType.BUFF;
                            }

                            @Override
                            public void updateDescription() {
                                this.description = "本回合已消耗蓄力";
                            }

                            @Override
                            public void atEndOfTurn(boolean isPlayer) {
                                // 回合结束时移除标记
                                if (isPlayer) {
                                    // 使用addToBot避免在遍历powers时直接修改列表
                                    addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                                            this.owner, this.owner, this));
                                }
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