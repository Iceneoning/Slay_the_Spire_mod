// Redemption.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class Redemption extends CustomCard {
    public static final String ID = "camellya:Redemption";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 1;
    private static final int ENERGY_GAIN = 2;
    private static final int UPGRADE_PLUS_ENERGY = 1;

    public Redemption() {
        super(ID, NAME, "camellyaResources/img/cards/Redemption.png", COST, DESCRIPTION,
                CardType.SKILL, CAMELLYA_GREEN, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = ENERGY_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 在下一回合获得能量，这里使用一个自定义Action或Power来实现
        addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(p, p,
                new com.megacrit.cardcrawl.powers.EnergizedPower(p, this.magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_ENERGY);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new Redemption();
    }
}