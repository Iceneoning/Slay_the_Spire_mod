package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Gan_Yu.helpers_GY.ModHelper;

public class EndPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("EndPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private int chargeAmount;

    public EndPower(AbstractCreature owner, int chargeAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.chargeAmount = chargeAmount;
        this.amount = 1;
        this.priority = 999; // 高优先级
        this.img = new Texture("GanYu/img/powers/EndPower.png");
        updateDescription();
        
        // 确保蓄力设置为指定层数
        maintainCharge();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + chargeAmount + DESCRIPTIONS[1];
    }
    
    // 当蓄力被消耗时调用此方法
    public void onChargeConsumed(int amount) {
        // 当蓄力被消耗后，立即恢复到指定层数
        maintainCharge();
    }
    
    // 每当需要维持蓄力时调用此方法
    private void maintainCharge() {
        // 直接操作powers列表来确保完全移除并重新创建
        if (this.owner.hasPower(ModHelper.makePath("ChargePower"))) {
            // 先移除现有的蓄力
            AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(this.owner, this.owner, ModHelper.makePath("ChargePower")));
            
            // 在移除之后，确保创建全新的蓄力实例
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    // 检查是否还有蓄力power，如果没有则添加新的
                    if (!owner.hasPower(ModHelper.makePath("ChargePower"))) {
                        AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(owner, owner, new ChargePower(owner, chargeAmount), chargeAmount));
                    }
                    this.isDone = true;
                }
            });
        } else {
            // 如果没有蓄力，直接添加
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(this.owner, this.owner, new ChargePower(this.owner, this.chargeAmount), this.chargeAmount));
        }
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束时移除该能力
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}