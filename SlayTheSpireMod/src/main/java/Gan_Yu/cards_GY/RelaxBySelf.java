package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;

public class RelaxBySelf extends CustomCard {
    public static final String ID = ModHelper.makePath("RelaxBySelf");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/RelaxBySelf.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int DRAW_AMOUNT = 4;
    private static final int ENERGY_GAIN = 3;
    private static final int UPGRADE_DRAW_AMOUNT = 5;
    private static final int UPGRADE_ENERGY_GAIN = 4;

    public RelaxBySelf() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // 消耗牌
        this.exhaust = true;
        this.baseMagicNumber = DRAW_AMOUNT;
        this.magicNumber = DRAW_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 抽牌
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        
        // 立即回复能量 (基础值为3，升级后为4)
        int energyGain = this.upgraded ? UPGRADE_ENERGY_GAIN : ENERGY_GAIN;
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(energyGain));
        
        // 生成一张虚空卡牌并放入抽牌堆
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW_AMOUNT - DRAW_AMOUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new RelaxBySelf();
    }
}