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
            String cpId = ModHelper.makePath("ChargePower");
            // 若已有蓄力：只请求消费，真正的“再获得”在消费完成的回调 onChargeConsumed 中处理
            if (this.owner.hasPower(cpId) && this.owner.getPower(cpId) instanceof Gan_Yu.power_GY.ChargePower) {
                final Gan_Yu.power_GY.ChargePower existing = (Gan_Yu.power_GY.ChargePower) this.owner.getPower(cpId);
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                    @Override
                    public void update() {
                        existing.onAttackPlay();
                        this.isDone = true;
                    }
                });
            } else {
                // 若当前没有蓄力，直接获得1层
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(CountlessTrialsPower.this.owner, CountlessTrialsPower.this.owner,
                        new ChargePower(CountlessTrialsPower.this.owner, 1), 1));
            }
        }
    }

    // 供 ChargePower 在消费完成后回调：完成“先消耗再获得”的后半段
    public void onChargeConsumed(int consumedAmount) {
        // 每次使用攻击牌应当固定获得1层（与描述一致），而不是根据 consumedAmount
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(this.owner, this.owner, new ChargePower(this.owner, 1), 1));
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