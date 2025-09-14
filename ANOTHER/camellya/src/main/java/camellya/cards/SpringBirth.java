// SpringBirth.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class SpringBirth extends CustomCard {
    public static final String ID = "camellya:SpringBirth";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final int COST = 1;
    private static final int HEAL = 6;
    private static final int UPGRADE_PLUS_HEAL = 3;
    private static final int DEXTERITY = 2;
    private static final int UPGRADE_PLUS_DEXTERITY = 2;

    public SpringBirth() {
        super(ID, NAME, "camellyaResources/img/cards/SpringBirth.png", COST, DESCRIPTION,
                CardType.SKILL, CAMELLYA_GREEN, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = HEAL;
        this.baseBlock = DEXTERITY; // 使用block字段存储敏捷值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HealAction(p, p, this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.block)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            this.upgradeBlock(UPGRADE_PLUS_DEXTERITY);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpringBirth();
    }
}