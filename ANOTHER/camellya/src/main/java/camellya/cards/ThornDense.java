package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower; // 使用游戏内置的荆棘能力

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class ThornDense extends CustomCard {
    public static final String ID = "camellya:ThornDense";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 2;
    private static final int BLOCK_AMOUNT = 9;
    private static final int THORN_AMOUNT = 3;
    private static final int UPGRADE_BLOCK = 3;
    private static final int UPGRADE_THORN = 2;

    public ThornDense() {
        super(ID, NAME, "camellyaResources/img/cards/ThornDense.png", COST, DESCRIPTION, CardType.SKILL, CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = BLOCK_AMOUNT;
        this.baseMagicNumber = this.magicNumber = THORN_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得护甲
        addToBot(new GainBlockAction(p, p, this.block));

        // 获得荆棘(使用游戏内置能力)
        addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ThornDense();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_THORN);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}