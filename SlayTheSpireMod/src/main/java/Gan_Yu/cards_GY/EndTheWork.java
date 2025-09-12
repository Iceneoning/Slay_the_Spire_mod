package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import Gan_Yu.power_GY.ChargePower;

public class EndTheWork extends CustomCard {
    public static final String ID = ModHelper.makePath("EndTheWork");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/EndTheWork.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int ENERGY_GAIN = 1;
    private static final int CARD_DRAW = 1;
    private static final int UPGRADE_CARD_DRAW = 2;

    public EndTheWork() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = CARD_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 回复1点能量
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
        // 抽牌
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }

        // 检查蓄力是否为2
        if (p.hasPower(ChargePower.POWER_ID)) {
            ChargePower chargePower = (ChargePower) p.getPower(ChargePower.POWER_ID);
            if (chargePower.amount == 2) {
                return true;
            }
        }

        this.cantUseMessage = "只有当蓄力为2时才能打出";
        return false;
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
        return new EndTheWork();
    }
}