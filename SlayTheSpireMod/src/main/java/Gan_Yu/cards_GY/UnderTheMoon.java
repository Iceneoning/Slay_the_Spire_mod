package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import Gan_Yu.power_GY.ChargePower;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;

public class UnderTheMoon extends CustomCard {
    public static final String ID = ModHelper.makePath("UnderTheMoon");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/UnderTheMoon.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int CHARGE_AMOUNT = 1;
    private static final int UPGRADE_CHARGE_AMOUNT = 1;
    private static final int ENERGY_GAIN = 1;

    public UnderTheMoon() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = CHARGE_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得蓄力
        if (p.hasPower(ChargePower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, p.getPower(ChargePower.POWER_ID), this.magicNumber));
        } else {
            addToBot(new ApplyPowerAction(p, p, new ChargePower(p, this.magicNumber), this.magicNumber));
        }
        
        // 获得能量
        addToBot(new GainEnergyAction(ENERGY_GAIN));
        
        // 往抽牌堆中加入状态牌
        addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false));
        addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, false));
        addToBot(new MakeTempCardInDrawPileAction(new Burn(), 1, true, false));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CHARGE_AMOUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnderTheMoon();
    }
}