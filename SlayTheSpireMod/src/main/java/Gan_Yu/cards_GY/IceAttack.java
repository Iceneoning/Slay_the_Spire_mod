package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Gan_Yu.helpers_GY.ModHelper;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class IceAttack extends CustomCard {
    public static final String ID = ModHelper.makePath("IceAttack");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/IceAttack.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int BASE_DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 8;
    private static final int VULNERABLE_AMOUNT = 1;
    private static final int UPGRADE_VULNERABLE_AMOUNT = 2;

    public IceAttack() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = this.magicNumber = VULNERABLE_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对敌人造成伤害
    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.utility.SFXAction("ATTACK_HEAVY"));
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        // 给予易伤效果
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE - BASE_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_VULNERABLE_AMOUNT - VULNERABLE_AMOUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new IceAttack();
    }
}
