package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import Gan_Yu.power_GY.ForbiddenAttackPower;

public class MilkTea extends CustomCard {
    public static final String ID = ModHelper.makePath("MilkTea");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/MilkTea.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int HEAL_AMOUNT = 12;
    private static final int UPGRADE_HEAL_AMOUNT = 16;

    public MilkTea() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = HEAL_AMOUNT;
        this.exhaust = true; // 消耗牌
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 回复生命值 (基础12点，升级后16点)
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
        
        // 应用奶茶能力，阻止本回合打出攻击牌
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new ForbiddenAttackPower(p), 1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_HEAL_AMOUNT - HEAL_AMOUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new MilkTea();
    }
}