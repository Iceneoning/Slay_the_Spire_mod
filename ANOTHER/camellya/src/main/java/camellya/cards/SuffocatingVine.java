package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class SuffocatingVine extends CustomCard {
    public static final String ID = "camellya:SuffocatingVine";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    // 基础数值
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_BASE_DAMAGE = 11;
    private static final int DAMAGE_PER_DEXTERITY_LOW = 2;
    private static final int DAMAGE_PER_DEXTERITY_HIGH = 3;

    // 窒息效果层数基础值
    private static final int BASE_SUFFOCATING_STACK = 1;
    private static final int UPGRADE_SUFFOCATING_STACK = 2;
    private static final int SUFFOCATING_STACK_PER_DEXTERITY_LOW = 1;
    private static final int SUFFOCATING_STACK_PER_DEXTERITY_HIGH = 2;

    public SuffocatingVine() {
        super(ID, NAME, "camellyaResources/img/cards/SuffocatingVine.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = this.magicNumber = BASE_SUFFOCATING_STACK;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 获取玩家当前的敏捷值（伤害基于敏捷）
        int dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount
                : 0;

        // 获取玩家当前的力量值（游戏会自动加上力量值，我们需要抵消它）
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;

        // 确保数值不为负
        if (dexterity < 0)
            dexterity = 0;
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值（基于敏捷）
        int displayDamage;
        int displaySuffocating;
        if (this.upgraded) {
            // 升级版显示: 11 + 3 * 敏捷
            displayDamage = 11 + 3 * dexterity;
            // 升级版窒息层数: 2 + 2 * 敏捷
            displaySuffocating = 2 + 2 * dexterity;
        } else {
            // 基础版显示: 8 + 2 * 敏捷
            displayDamage = 8 + 2 * dexterity;
            // 基础版窒息层数: 1 + 敏捷
            displaySuffocating = 1 + dexterity;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength来抵消
        this.baseDamage = displayDamage - strength;

        // 设置窒息层数
        this.baseMagicNumber = displaySuffocating;

        super.calculateCardDamage(mo);

        // 恢复基础值，以便下次计算
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.baseMagicNumber = this.upgraded ? UPGRADE_SUFFOCATING_STACK : BASE_SUFFOCATING_STACK;
    }

    @Override
    public void applyPowers() {
        // 获取玩家当前的敏捷值（伤害基于敏捷）
        int dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount
                : 0;

        // 获取玩家当前的力量值（游戏会自动加上力量值，我们需要抵消它）
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;

        // 确保数值不为负
        if (dexterity < 0)
            dexterity = 0;
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值（基于敏捷）
        int displayDamage;
        int displaySuffocating;
        int actualDamage;

        if (this.upgraded) {
            // 升级版显示: 11 + 3 * 敏捷
            displayDamage = 11 + 3 * dexterity;
            // 升级版窒息层数: 2 + 2 * 敏捷
            displaySuffocating = 2 + 2 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 11 + 3 * dexterity - strength;
        } else {
            // 基础版显示: 8 + 2 * 敏捷
            displayDamage = 8 + 2 * dexterity;
            // 基础版窒息层数: 1 + 敏捷
            displaySuffocating = 1 + dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 8 + 2 * dexterity - strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        this.baseDamage = actualDamage;

        // 设置窒息层数
        this.baseMagicNumber = displaySuffocating;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        this.rawDescription = "对目标敌人造成 " + displayDamage + " 点伤害，并施加 " + displaySuffocating + " 点 窒息 ";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.baseMagicNumber = this.upgraded ? UPGRADE_SUFFOCATING_STACK : BASE_SUFFOCATING_STACK;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对目标敌人造成伤害
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.POISON));

        // 施加游戏内置的窒息效果
        addToBot(new ApplyPowerAction(m, p, new ConstrictedPower(m, p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SuffocatingVine();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级时改变描述
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}