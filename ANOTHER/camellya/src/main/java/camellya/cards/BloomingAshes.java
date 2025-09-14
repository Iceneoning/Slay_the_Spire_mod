package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class BloomingAshes extends CustomCard {
    public static final String ID = "camellya:BloomingAshes";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 3;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CAMELLYA_GREEN;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ALL_ENEMY;

    private static final int BASE_DAMAGE_LOW = 24;
    private static final int BASE_DAMAGE_HIGH = 28;
    private static final int DAMAGE_PER_STRENGTH_LOW = 2;
    private static final int DAMAGE_PER_STRENGTH_HIGH = 3;
    private static final int STATUS_DURATION_BASE = 2;
    private static final int POISON_AMOUNT = 4;

    public BloomingAshes() {
        super(ID, NAME, "camellyaResources/img/cards/BloomingAshes.png", COST, DESCRIPTION, TYPE, COLOR, RARITY,
                TARGET);
        this.baseDamage = BASE_DAMAGE_LOW;
        this.exhaust = true; // 消耗品标签
        this.isMultiDamage = true; // 多目标伤害标记
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 获取力量值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        if (this.upgraded) {
            // 升级版显示: 28 + 3 * 力量
            displayDamage = 28 + 3 * strength;
        } else {
            // 基础版显示: 24 + 2 * 力量
            displayDamage = 24 + 2 * strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength
        this.baseDamage = displayDamage - strength;

        super.calculateCardDamage(mo);

        // 恢复基础伤害值
        this.baseDamage = this.upgraded ? BASE_DAMAGE_HIGH : BASE_DAMAGE_LOW;
    }

    @Override
    public void applyPowers() {
        // 获取力量值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;
        if (strength < 0)
            strength = 0;

        // 获取敏捷值用于计算虚弱和易伤回合数
        int dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount
                : 0;
        if (dexterity < 0)
            dexterity = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        int actualDamage;

        if (this.upgraded) {
            // 升级版显示: 28 + 3 * 力量
            displayDamage = 28 + 3 * strength;
            // 实际伤害值（游戏会自动加上strength）
            actualDamage = 28 + 2 * strength;
        } else {
            // 基础版显示: 24 + 2 * 力量
            displayDamage = 24 + 2 * strength;
            // 实际伤害值（游戏会自动加上strength）
            actualDamage = 24 + strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        this.baseDamage = actualDamage;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 计算虚弱和易伤回合数
        int weakDuration = dexterity + STATUS_DURATION_BASE;
        int vulnerableDuration = dexterity + STATUS_DURATION_BASE;

        // 更新描述显示计算后的数值
        this.rawDescription = " 消耗 。对所有敌人造成 " + displayDamage + " 点伤害，施加 " + weakDuration + " 回合 虚弱 、"
                + vulnerableDuration + " 回合 易伤 和 " + POISON_AMOUNT + " 点 camellya:注毒 。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始值
        this.baseDamage = this.upgraded ? BASE_DAMAGE_HIGH : BASE_DAMAGE_LOW;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人造成伤害
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));

        // 获取力量值用于计算虚弱回合数
        int strength = p.getPower(StrengthPower.POWER_ID) != null ? p.getPower(StrengthPower.POWER_ID).amount : 0;
        if (strength < 0)
            strength = 0;

        // 获取敏捷值用于计算易伤回合数
        int dexterity = p.getPower(DexterityPower.POWER_ID) != null ? p.getPower(DexterityPower.POWER_ID).amount : 0;
        if (dexterity < 0)
            dexterity = 0;

        // 对所有敌人施加效果
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                // 施加虚弱(敏捷值+2回合)
                addToBot(
                        new ApplyPowerAction(monster, p,
                                new WeakPower(monster, dexterity + STATUS_DURATION_BASE, false),
                                dexterity + STATUS_DURATION_BASE));

                // 施加易伤(敏捷值+2回合)
                addToBot(new ApplyPowerAction(monster, p,
                        new VulnerablePower(monster, dexterity + STATUS_DURATION_BASE, false),
                        dexterity + STATUS_DURATION_BASE));

                // 施加注毒4层
                addToBot(new ApplyPowerAction(monster, p, new camellya.power.PoisonPower(monster, POISON_AMOUNT),
                        POISON_AMOUNT));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloomingAshes();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

}