package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class OneDayFlower extends CustomCard {
    public static final String ID = "camellya:OneDayFlower";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 4;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CAMELLYA_GREEN;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ALL_ENEMY;

    private static final int BASE_DAMAGE = 48;
    private static final int UPGRADE_BASE_DAMAGE = 57;
    private static final int STRENGTH_MULTIPLIER_LOW = 3;
    private static final int STRENGTH_MULTIPLIER_HIGH = 4;
    private static final int STRENGTH_GAIN = 4;
    private static final int UPGRADE_STRENGTH_GAIN = 6;

    public OneDayFlower() {
        super(ID, NAME, "camellyaResources/img/cards/OneDayFlower.png", COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = STRENGTH_GAIN;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
        this.exhaust = true; // 添加消耗词条
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        if (this.upgraded) {
            // 升级版显示: 57 + 4 * 力量
            displayDamage = 57 + 4 * strength;
        } else {
            // 基础版显示: 48 + 3 * 力量
            displayDamage = 48 + 3 * strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength
        this.baseDamage = displayDamage - strength;

        super.calculateCardDamage(mo);

        // 恢复基础伤害值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
    }

    @Override
    public void applyPowers() {
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        int actualDamage;

        if (this.upgraded) {
            // 升级版显示: 57 + 4 * 力量
            displayDamage = 57 + 4 * strength;
            // 实际伤害值（游戏会自动加上strength）
            actualDamage = 57 + 3 * strength;
        } else {
            // 基础版显示: 48 + 3 * 力量
            displayDamage = 48 + 3 * strength;
            // 实际伤害值（游戏会自动加上strength）
            actualDamage = 48 + 2 * strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        this.baseDamage = actualDamage;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 计算力量获取值
        int strengthGain = this.magicNumber;

        // 更新描述显示计算后的数值
        this.rawDescription = " 消耗 。对所有敌人造成 " + displayDamage + " 点伤害。获得 " + strengthGain + " 点 力量 。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                        AbstractGameAction.AttackEffect.FIRE));

        // 给自己增加力量
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH_GAIN - STRENGTH_GAIN); // 升级时增加力量获取
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}