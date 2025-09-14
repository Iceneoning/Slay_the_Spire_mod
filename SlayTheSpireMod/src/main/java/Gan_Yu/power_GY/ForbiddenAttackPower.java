package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import Gan_Yu.helpers_GY.ModHelper;

public class ForbiddenAttackPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("ForbiddenAttackPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ForbiddenAttackPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.DEBUFF; // 将其视为负面效果，因为它限制了玩家的行动
        this.img = new Texture("GanYu/img/powers/ForbiddenAttackPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束时移除该效果
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        // 当抽到攻击牌时，将其设置为不可.playable
        if (card.type == AbstractCard.CardType.ATTACK) {
            card.cantUseMessage = DESCRIPTIONS[1]; // "由于奶茶的效果，本回合无法打出攻击牌";
        }
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        // 如果是攻击牌，则不能打出
        if (card.type == AbstractCard.CardType.ATTACK) {
            card.cantUseMessage = DESCRIPTIONS[0]; // "由于奶茶的效果，本回合无法打出攻击牌";
            return false;
        }
        return true;
    }
}