package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import Gan_Yu.power_GY.ChargePower;
import Gan_Yu.power_GY.CoolPower;

public class Cool extends CustomCard {
    public static final String ID = ModHelper.makePath("Cool");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/Cool.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int CHARGE_AMOUNT = 2;
    private static final int ENERGY_GAIN = 3;
    private static final int DRAW_AMOUNT = 3;
    private static final int UPGRADE_ENERGY_GAIN = 4;
    private static final int UPGRADE_DRAW_AMOUNT = 4;

    public Cool() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true; // 消耗牌
        this.baseMagicNumber = this.magicNumber = DRAW_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得2层蓄力
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new ChargePower(p, CHARGE_AMOUNT), CHARGE_AMOUNT));
            
        // 回复能量 (基础3点，升级后4点)
        int energyGain = this.upgraded ? UPGRADE_ENERGY_GAIN : ENERGY_GAIN;
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(energyGain));
        
        // 抽牌 (基础3张，升级后4张)
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        
        // 应用CoolPower，用于在回合结束时受到伤害
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new CoolPower(p), 1));
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
        return new Cool();
    }
}