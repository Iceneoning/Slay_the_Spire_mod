package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class FrozenThousand extends CustomCard {
    public static final String ID = ModHelper.makePath("FrozenThousand");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/FrozenThousand.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int BASE_DAMAGE = 18;
    private static final int UPGRADE_DAMAGE = 25;
    private static final int VULNERABLE_AMOUNT = 2;

    public FrozenThousand() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = BASE_DAMAGE;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.utility.SFXAction("ATTACK_HEAVY"));
        AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        
        // 给予所有敌人2层易伤
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(monster, p, new VulnerablePower(monster, VULNERABLE_AMOUNT, false), VULNERABLE_AMOUNT));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.baseDamage = UPGRADE_DAMAGE;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new FrozenThousand();
    }
}