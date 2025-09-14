// FertileSoil.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class FertileSoil extends CustomCard {
    public static final String ID = "camellya:FertileSoil";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 1;
    private static final int CARDS_TO_DRAW = 1;
    private static final int UPGRADE_CARDS_TO_DRAW = 2;

    public FertileSoil() {
        super(ID, NAME, "camellyaResources/img/cards/FertileSoil.png", COST, DESCRIPTION,
                CardType.POWER, CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cardsToDraw = this.upgraded ? UPGRADE_CARDS_TO_DRAW : CARDS_TO_DRAW;
        // 应用沃土Power
        addToBot(new ApplyPowerAction(p, p, new camellya.power.FertileSoilPower(p, cardsToDraw), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FertileSoil();
    }
}