// BotanicalCosmos.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class BotanicalCosmos extends CustomCard {
    public static final String ID = "camellya:BotanicalCosmos";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int BLOCK = 12;
    private static final int DEXTERITY = 1;

    public BotanicalCosmos() {
        super(ID, NAME, "camellyaResources/img/cards/BotanicalCosmos.png", COST, DESCRIPTION,
                CardType.SKILL, CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = this.magicNumber = DEXTERITY;
        this.isEthereal = true; // 设置为虚无品
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.isEthereal = false; // 升级后不再是虚无品
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BotanicalCosmos();
    }
}