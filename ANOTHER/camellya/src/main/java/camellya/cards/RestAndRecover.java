/// RestAndRecover.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import camellya.power.RestAndRecoverPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class RestAndRecover extends CustomCard {
    public static final String ID = "camellya:RestAndRecover";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 3;
    private static final int HEAL_AMOUNT = 2;
    private static final int UPGRADE_HEAL_AMOUNT = 3;
    private static final int STRENGTH_AMOUNT = 1;

    public RestAndRecover() {
        super(ID, NAME, "camellyaResources/img/cards/RestAndRecover.png", COST, DESCRIPTION,
                CardType.POWER, CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = HEAL_AMOUNT;
        this.baseBlock = STRENGTH_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用休养恢复Power（每回合结束时生效）
        addToBot(new ApplyPowerAction(p, p, new RestAndRecoverPower(p, this.magicNumber, this.block), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_HEAL_AMOUNT - HEAL_AMOUNT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RestAndRecover();
    }
}