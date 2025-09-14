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
        // 检查当前是否已有 ChargePower，并只在层数不同时应用差值
        String cpId = ModHelper.makePath("ChargePower");
        if (this.owner.hasPower(cpId)) {
            AbstractPower cp = this.owner.getPower(cpId);
            int current = cp.amount;
            int delta = this.chargeAmount - current;
            if (delta == 0) {
                // 已经是期望的层数，不做任何处理，避免重复应用力量
                return;
            }
            // 只应用差值（正数则增加层数，负数则减少层数），由 ChargePower.stackPower 处理力量差异
            System.out.println("[DEBUG] EndPower.maintainCharge: current=" + current + ", target=" + this.chargeAmount + ", delta=" + delta);
            // 调用已有 ChargePower 的 stackPower，而不是创建新的 ChargePower 实例，避免触发 onInitialApplication 导致重复 updateStrength
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    AbstractPower existing = EndPower.this.owner.getPower(cpId);
                    if (existing instanceof Gan_Yu.power_GY.ChargePower) {
                        ((Gan_Yu.power_GY.ChargePower) existing).stackPower(delta);
                    } else {
                        // 若意外不存在，则退回为直接添加
                        AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(EndPower.this.owner, EndPower.this.owner, new ChargePower(EndPower.this.owner, delta), delta));
                    }
                    this.isDone = true;
                }
            });
        } else {
            // 如果没有蓄力，直接添加指定层数
            System.out.println("[DEBUG] EndPower.maintainCharge: no existing ChargePower, applying " + this.chargeAmount + " layers");
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