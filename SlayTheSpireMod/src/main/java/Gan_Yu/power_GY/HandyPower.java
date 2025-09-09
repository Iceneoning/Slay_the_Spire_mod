package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import Gan_Yu.helpers_GY.ModHelper;

public class HandyPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("HandyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private int strengthAmount;

    public HandyPower(AbstractCreature owner, int strengthAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.strengthAmount = strengthAmount;
        this.amount = 1;
        this.canGoNegative = false;
        this.img = new Texture("GanYu/img/powers/HandyPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + strengthAmount + DESCRIPTIONS[1];
    }
    
    public void onChargeConsumed(int amount) {
        // 当蓄力被消耗时调用
        if (amount >= 2) {
            // 每消耗两层蓄力，获得指定数量的力量
            int strengthToGain = (amount / 2) * strengthAmount;
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthToGain), strengthToGain));
        }
    }
}