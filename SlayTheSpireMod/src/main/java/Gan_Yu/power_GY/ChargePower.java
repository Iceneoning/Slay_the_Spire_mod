package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.cards.AbstractCard;

import Gan_Yu.helpers_GY.ModHelper;

public class ChargePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("ChargePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    

    public ChargePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.canGoNegative = false;
        this.img = new Texture("GanYu/img/powers/ChargePower.png");
        
        // 应用力量增益
        updateStrength();
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount > 2) {
            this.amount = 2; // 蓄力上限为2
        }
        updateStrength();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
    
    private void updateStrength() {
        // 更新力量值 (每层蓄力提供6点力量)
        if (this.amount > 0) {
            if (this.owner.hasPower(StrengthPower.POWER_ID)) {
                // 如果已有力量，计算需要增加的力量值
                int currentStrength = this.owner.getPower(StrengthPower.POWER_ID).amount;
                int targetStrength = this.amount * 6;
                int difference = targetStrength - currentStrength;
                
                if (difference > 0) {
                    // 增加力量
                    addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, difference)));
                } else if (difference < 0) {
                    // 减少力量
                    addToBot(new ReducePowerAction(this.owner, this.owner, StrengthPower.POWER_ID, -difference));
                }
            } else {
                // 没有力量时直接应用
                addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount * 6)));
            }
        } else {
            // 没有蓄力时移除所有力量
            if (this.owner.hasPower(StrengthPower.POWER_ID)) {
                int currentStrength = this.owner.getPower(StrengthPower.POWER_ID).amount;
                addToBot(new ReducePowerAction(this.owner, this.owner, StrengthPower.POWER_ID, currentStrength));
            }
        }
    }
    
    public void onAttackPlay() {
        // 打出攻击牌时移除所有蓄力
        if (this.amount > 0) {
            int consumedAmount = this.amount;
            this.amount = 0;
            updateStrength();
            updateDescription();
            // 触发HandyPower效果
            triggerHandyPower(consumedAmount);
        }
    }
    
    // 添加onUseCard方法，确保在使用任何卡牌时都能检测到攻击牌
    public void onUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            onAttackPlay();
        }
    }
    
    private void triggerHandyPower(int consumedAmount) {
        // 当蓄力被消耗时触发HandyPower效果
        if (this.owner.hasPower("Gan_Yu:HandyPower")) {
            AbstractPower handyPower = this.owner.getPower("Gan_Yu:HandyPower");
            // 使用反射调用onChargeConsumed方法（避免直接依赖）
            try {
                handyPower.getClass().getMethod("onChargeConsumed", int.class).invoke(handyPower, consumedAmount);
            } catch (Exception e) {
                // 如果方法不存在或调用失败，忽略错误
            }
        }
    }
}