package Gan_Yu.power_GY;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Gan_Yu.helpers_GY.ModHelper;

public class PrayPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("PrayPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PrayPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1; // 初始堆叠为1
        updateDescription();
        loadRegion("draw");
    }

    @Override
    public void updateDescription() {
        // 如果有多个堆叠，可在描述中简单显示基础描述（本地化字符串可扩展以包含数量）
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount < 0) this.amount = 0;
        updateDescription();
    }

    public void onChargeConsumed(int consumedAmount) {
        // 当消耗蓄力时，按消耗层数与堆叠数抽牌
        if (consumedAmount > 0 && this.amount > 0) {
            int drawCount = this.amount;
            this.addToBot(new DrawCardAction(drawCount));
        }
    }
}