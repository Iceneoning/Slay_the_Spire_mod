// CamelliaFertilizer.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class CamelliaFertilizer extends CustomCard {
    public static final String ID = "camellya:CamelliaFertilizer";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int BASE_BLOCK = 6;
    private static final int UPGRADE_BLOCK = 8;
    private static final int BASE_DEXTERITY = 1;
    private static final int UPGRADE_DEXTERITY = 2;

    public CamelliaFertilizer() {
        super(ID, NAME, "camellyaResources/img/cards/CamelliaFertilizer.png", COST, DESCRIPTION,
                CardType.POWER, CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用椿花养料Power
        int blockAmount = this.upgraded ? UPGRADE_BLOCK : BASE_BLOCK;
        int dexterityAmount = this.upgraded ? UPGRADE_DEXTERITY : BASE_DEXTERITY;
        addToBot(new ApplyPowerAction(p, p,
                new camellya.power.CamelliaFertilizerPower(p, blockAmount, dexterityAmount)));
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
        return new CamelliaFertilizer();
    }
}