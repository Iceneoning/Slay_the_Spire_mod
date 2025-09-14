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
import Gan_Yu.power_GY.AttackToDefendPower;

public class AttackToDefend extends CustomCard {
    public static final String ID = ModHelper.makePath("AttackToDefend");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/AttackToDefend.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int BLOCK_AMOUNT = 4;
    private static final int UPGRADE_BLOCK_AMOUNT = 2;

    public AttackToDefend() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = this.block = BLOCK_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(AttackToDefendPower.POWER_ID)) {
            final int add = this.baseBlock;
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    com.megacrit.cardcrawl.powers.AbstractPower ap = p.getPower(AttackToDefendPower.POWER_ID);
                    if (ap instanceof Gan_Yu.power_GY.AttackToDefendPower) {
                        ((Gan_Yu.power_GY.AttackToDefendPower) ap).addBaseBlockAndStack(add);
                    } else {
                        addToTop(new ApplyPowerAction(p, p, new AttackToDefendPower(p, add), 1));
                    }
                    this.isDone = true;
                }
            });
        } else {
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new AttackToDefendPower(p, this.baseBlock), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMOUNT);
            this.isInnate = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new AttackToDefend();
    }
}