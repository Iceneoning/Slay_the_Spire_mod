package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class HeatRelief extends CustomCard {
    public static final String ID = "camellya:HeatRelief";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int HEAL_AMOUNT = 10;
    private static final int BLOCK_AMOUNT = 10;
    private static final int UPGRADE_HEAL = 5;
    private static final int UPGRADE_BLOCK = 5;

    public HeatRelief() {
        super(ID, NAME, "camellyaResources/img/cards/HeatRelief.png", COST, DESCRIPTION, CardType.SKILL, CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.SELF);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 回复生命值
        addToBot(new HealAction(p, p, HEAL_AMOUNT));

        // 获得护甲
        addToBot(new GainBlockAction(p, p, BLOCK_AMOUNT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HeatRelief();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeBaseCost(0);
        }
    }
}