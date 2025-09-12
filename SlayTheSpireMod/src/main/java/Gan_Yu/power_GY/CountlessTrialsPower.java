package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import Gan_Yu.helpers_GY.ModHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CountlessTrialsPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("CountlessTrialsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CountlessTrialsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.priority = 999; // 高优先级确保先消耗再获得
        this.img = new Texture("GanYu/img/powers/CountlessTrialsPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 当使用攻击牌时，获得1层蓄力
        if (card.type == AbstractCard.CardType.ATTACK) {
            // 先消耗蓄力再获得蓄力
            if (this.owner.hasPower(ModHelper.makePath("ChargePower"))) {
                // 移除现有蓄力
                AbstractDungeon.actionManager.addToTop(
                    new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                        this.owner, this.owner, ModHelper.makePath("ChargePower")));
            }
            
            // 获得1层蓄力
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(this.owner, this.owner, 
                    new ChargePower(this.owner, 1), 1));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束时移除该能力
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                    this.owner, this.owner, this));
        }
    }
}