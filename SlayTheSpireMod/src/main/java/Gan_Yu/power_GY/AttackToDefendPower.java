package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Gan_Yu.helpers_GY.ModHelper;

public class AttackToDefendPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("AttackToDefendPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AttackToDefendPower(AbstractCreature owner, int blockAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = blockAmount;
        this.img = new Texture("GanYu/img/powers/AttackToDefendPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
    
    public void onChargeConsumed(int amount) {
        // 当蓄力被消耗时触发
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(this.owner, this.owner, this.amount * amount));
    }
    
    public void onChargeGained(int amount) {
        // 当蓄力被获得时触发
        AbstractDungeon.actionManager.addToBottom(
            new GainBlockAction(this.owner, this.owner, this.amount * amount));
    }
}