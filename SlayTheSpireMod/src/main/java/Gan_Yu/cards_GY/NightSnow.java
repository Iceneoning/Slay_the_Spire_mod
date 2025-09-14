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
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class NightSnow extends CustomCard {
    public static final String ID = ModHelper.makePath("NightSnow");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/NightSnow.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int CHARGE_AMOUNT = 1; // 蓄力基础层数
    private static final int DEBUFF_AMOUNT = 2; // 虚弱/易伤基础层数
    private static final int UPGRADE_CHARGE_AMOUNT = 2; // 升级后的蓄力层数
    private static final int UPGRADE_DEBUFF_AMOUNT = 3; // 升级后的虚弱/易伤层数

    // 使用本地字段代替不存在的 secondaryMagicNumber
    private int debuffAmount;

    public NightSnow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CHARGE_AMOUNT;
    this.debuffAmount = DEBUFF_AMOUNT;
        this.exhaust = true; // 消耗牌
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得蓄力 (基础1层，升级后2层)
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new ChargePower(p, this.magicNumber), this.magicNumber));
        
    // 给予所有敌人虚弱和易伤效果
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                // 施加虚弱
                AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(monster, p, new WeakPower(monster, this.debuffAmount, false), this.debuffAmount));
                
                // 施加易伤
                AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(monster, p, new VulnerablePower(monster, this.debuffAmount, false), this.debuffAmount));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            // 蓄力层数从1提升到2
            this.upgradeMagicNumber(UPGRADE_CHARGE_AMOUNT - CHARGE_AMOUNT);
            // 同步提升减益层数到3
            this.debuffAmount = UPGRADE_DEBUFF_AMOUNT;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new NightSnow();
    }
}