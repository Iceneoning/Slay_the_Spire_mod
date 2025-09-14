// BlowAgain.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class BlowAgain extends CustomCard {
    public static final String ID = "camellya:BlowAgain";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int BASE_HEAL = 4;
    private static final int UPGRADE_HEAL = 6;
    private static final int BASE_STRENGTH = 1;
    private static final int UPGRADE_STRENGTH = 2;

    public BlowAgain() {
        super(ID, NAME, "camellyaResources/img/cards/BlowAgain.png", COST, DESCRIPTION,
                CardType.POWER, CAMELLYA_GREEN, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用吹又生Power
        int healAmount = this.upgraded ? UPGRADE_HEAL : BASE_HEAL;
        int strengthAmount = this.upgraded ? UPGRADE_STRENGTH : BASE_STRENGTH;
        addToBot(new ApplyPowerAction(p, p, new camellya.power.BlowAgainPower(p, healAmount, strengthAmount)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlowAgain();
    }
}