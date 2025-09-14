package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class ColdResistant extends CustomCard {
    public static final String ID = "camellya:ColdResistant";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int ARTIFACT_AMOUNT = 1;
    private static final int STRENGTH_AMOUNT = 1;
    private static final int UPGRADE_STRENGTH = 1;

    public ColdResistant() {
        super(ID, NAME, "camellyaResources/img/cards/ColdResistant.png", COST, DESCRIPTION, CardType.SKILL,
                CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = STRENGTH_AMOUNT;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 添加一层人工制品
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, ARTIFACT_AMOUNT), ARTIFACT_AMOUNT));

        // 增加力量
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ColdResistant();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_STRENGTH);
            upgradeBaseCost(UPGRADE_COST);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}