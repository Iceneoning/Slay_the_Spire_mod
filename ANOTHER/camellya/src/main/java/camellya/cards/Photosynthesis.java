// Photosynthesis.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import camellya.power.PhotosynthesisPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class Photosynthesis extends CustomCard {
    public static final String ID = "camellya:Photosynthesis";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int ENERGY_GAIN = 1;

    public Photosynthesis() {
        super(ID, NAME, "camellyaResources/img/cards/Photosynthesis.png", COST, DESCRIPTION,
                CardType.POWER, CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = ENERGY_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用每回合递增获得能量的效果（类似Deva Form）
        addToBot(new ApplyPowerAction(p, p, new PhotosynthesisPower(p, p, this.magicNumber), this.magicNumber));
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
        return new Photosynthesis();
    }
}