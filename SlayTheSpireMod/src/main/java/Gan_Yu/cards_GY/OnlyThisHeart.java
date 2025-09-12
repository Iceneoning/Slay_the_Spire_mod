package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;

public class OnlyThisHeart extends CustomCard {
    public static final String ID = ModHelper.makePath("OnlyThisHeart");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/OnlyThisHeart.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int DRAW_AMOUNT = 2;

    public OnlyThisHeart() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = DRAW_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查本回合是否消耗过蓄力
        // 通过检查玩家是否有"ChargeConsumedThisTurn"标记来判断
        if (p.hasPower(ModHelper.makePath("ChargeConsumedThisTurn"))) {
            // 如果本回合消耗过蓄力，抽2张牌
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 升级后费用减少1点 (从1费变为0费)
            this.upgradeBaseCost(0);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new OnlyThisHeart();
    }
}