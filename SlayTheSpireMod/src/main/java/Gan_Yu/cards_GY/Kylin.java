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
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class Kylin extends CustomCard {
    public static final String ID = ModHelper.makePath("Kylin");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/Kylin.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int REGEN_AMOUNT = 5;
    private static final int DEXTERITY_AMOUNT = 3;
    private static final int UPGRADE_REGEN_AMOUNT = 7;
    private static final int UPGRADE_DEXTERITY_AMOUNT = 4;

    public Kylin() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = REGEN_AMOUNT;
        this.baseDamage = DEXTERITY_AMOUNT; // 使用damage字段存储敏捷值，避免再定义一个变量
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用再生
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new RegenPower(p, this.magicNumber), this.magicNumber));
        
        // 应用敏捷
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new DexterityPower(p, this.baseDamage), this.baseDamage));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_REGEN_AMOUNT - REGEN_AMOUNT);
            this.baseDamage = UPGRADE_DEXTERITY_AMOUNT;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new Kylin();
    }
}