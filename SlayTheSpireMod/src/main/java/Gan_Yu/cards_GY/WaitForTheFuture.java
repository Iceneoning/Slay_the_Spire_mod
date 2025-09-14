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
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import Gan_Yu.power_GY.ChargePower;

public class WaitForTheFuture extends CustomCard {
    public static final String ID = ModHelper.makePath("WaitForTheFuture");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/WaitForTheFuture.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int BASE_BLOCK = 18;
    private static final int UPGRADE_BLOCK = 4; // 升级增加4点格挡，总计12点
    private static final int CHARGE_AMOUNT = 2;

    public WaitForTheFuture() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = this.magicNumber = CHARGE_AMOUNT;
        this.isEthereal = true; // 虚无属性
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得格挡
        AbstractDungeon.actionManager.addToBottom(
            new GainBlockAction(p, p, this.block));
        
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
            this.upgradeBlock(UPGRADE_BLOCK);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new WaitForTheFuture();
    }
}