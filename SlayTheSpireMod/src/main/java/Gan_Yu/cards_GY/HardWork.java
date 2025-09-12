package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import Gan_Yu.action_GY.PermUpgradeCardAction;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HardWork extends CustomCard {
    public static final String ID = ModHelper.makePath("HardWork");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/HardWork.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int UPGRADE_CARD_COUNT = 1;
    private static final int UPGRADE_CARD_COUNT_UPG = 2;

    public HardWork() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.baseMagicNumber = UPGRADE_CARD_COUNT;
        this.magicNumber = UPGRADE_CARD_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 升级指定数量的手牌（永久升级）
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new PermUpgradeCardAction());
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CARD_COUNT_UPG - UPGRADE_CARD_COUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new HardWork();
    }
}