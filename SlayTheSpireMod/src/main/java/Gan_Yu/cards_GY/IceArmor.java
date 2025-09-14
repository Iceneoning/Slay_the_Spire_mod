package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;

import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import Gan_Yu.helpers_GY.ModHelper;
import Gan_Yu.power_GY.IceArmorPower;

public class IceArmor extends CustomCard {
    public static final String ID = ModHelper.makePath("IceArmor");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/IceArmor.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int BASE_BLOCK = 15;
    private static final int UPGRADE_BLOCK = 5;
    private static final int WEAK_AMOUNT = 2;
    private static final int UPGRADE_WEAK_AMOUNT = 1;

    public IceArmor() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = this.magicNumber = WEAK_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得格挡
        AbstractDungeon.actionManager.addToBottom(
            (AbstractGameAction) new GainBlockAction((AbstractCreature) p, (AbstractCreature) p, this.block));
        
        // 应用冰甲能力
        if (p.hasPower(IceArmorPower.POWER_ID)) {
            final int add = this.magicNumber;
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    com.megacrit.cardcrawl.powers.AbstractPower ap = p.getPower(IceArmorPower.POWER_ID);
                    if (ap instanceof Gan_Yu.power_GY.IceArmorPower) {
                        ((Gan_Yu.power_GY.IceArmorPower) ap).addWeakAndStack(add);
                    } else {
                        addToTop(new ApplyPowerAction(p, p, new IceArmorPower(p, add), add));
                    }
                    this.isDone = true;
                }
            });
        } else {
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new IceArmorPower(p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK);
            this.upgradeMagicNumber(UPGRADE_WEAK_AMOUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new IceArmor();
    }
}