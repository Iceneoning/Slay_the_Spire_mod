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
import Gan_Yu.power_GY.ChargePower;

public class Ready extends CustomCard {
    public static final String ID = ModHelper.makePath("Ready");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/Ready.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int CHARGE_AMOUNT = 1;

    public Ready() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = CHARGE_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用蓄力
        if (p.hasPower(ChargePower.POWER_ID)) {
            // 如果已有蓄力，增加层数
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, p.getPower(ChargePower.POWER_ID), this.magicNumber));
        } else {
            // 如果没有蓄力，应用新的蓄力
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new ChargePower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0); // 升级时费用减1
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new Ready();
    }
}