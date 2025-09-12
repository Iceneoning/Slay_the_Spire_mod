package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import Gan_Yu.power_GY.ChargePower;

public class Blessedness extends CustomCard {
    public static final String ID = ModHelper.makePath("Blessedness");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/Blessedness.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int CHARGE_AMOUNT = 2;
    private static final int CARD_DRAW = 2;
    private static final int UPGRADE_CARD_DRAW = 3;

    public Blessedness() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = CARD_DRAW;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得2层蓄力
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new ChargePower(p, CHARGE_AMOUNT), CHARGE_AMOUNT));
        // 抽牌
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CARD_DRAW - CARD_DRAW);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new Blessedness();
    }
}