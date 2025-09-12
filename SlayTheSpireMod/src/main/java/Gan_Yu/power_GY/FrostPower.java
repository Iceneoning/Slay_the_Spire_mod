package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import Gan_Yu.helpers_GY.ModHelper;

public class FrostPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("FrostPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FrostPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = new Texture("GanYu/img/powers/FrostPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        // 当玩家在本回合造成伤害时，为目标添加一层易伤
        if (info.type != DamageInfo.DamageType.THORNS && info.owner == this.owner && target != this.owner) {
            this.flash();
            addToBot(new ApplyPowerAction(target, this.owner, new VulnerablePower(target, 1, false), 1));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束时移除该能力
        if (isPlayer) {
            addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }
}