// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/BloodsuckingVine.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class BloodsuckingVine extends CustomCard {
    public static final String ID = "camellya:BloodsuckingVine";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    // 基础数值，用于计算8+X/10+2X公式
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_BASE_DAMAGE = 10;
    private static final int DAMAGE_PER_STRENGTH_LOW = 1;
    private static final int DAMAGE_PER_STRENGTH_HIGH = 2;
    private static final int HEAL_PERCENTAGE_LOW = 20; // 20%
    private static final int HEAL_PERCENTAGE_HIGH = 30; // 30%

    public BloodsuckingVine() {
        super(ID, NAME, "camellyaResources/img/cards/BloodsuckingVine.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.isMultiDamage = true;
        this.exhaust = true; // 虚无品
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 获取玩家当前的力量值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;

        // 确保力量值不为负
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        if (this.upgraded) {
            // 升级版显示: 10 + 2 * 力量
            displayDamage = 10 + 2 * strength;
        } else {
            // 基础版显示: 8 + 力量
            displayDamage = 8 + strength;
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
        // 获取玩家当前的力量值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;

        // 确保力量值不为负
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        int actualDamage;

        if (this.upgraded) {
            // 升级版显示: 10 + 2 * 力量
            displayDamage = 10 + 2 * strength;
            // 实际伤害值（游戏会自动加上strength）
            actualDamage = 10 + strength;
        } else {
            // 基础版显示: 8 + 力量
            displayDamage = 8 + strength;
            // 实际伤害值（游戏会自动加上strength）
            actualDamage = 8;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        this.baseDamage = actualDamage;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        String healText = this.upgraded ? "30%" : "20%";
        this.rawDescription = " 虚无 。对全场敌人造成 " + displayDamage + " 点伤害，并将造成未被格挡的伤害的" + healText + "用于回复自己的生命值。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对全场敌人造成伤害
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.POISON));

        // 计算总伤害并回复生命值
        int totalDamage = 0;
        for (int damage : this.multiDamage) {
            totalDamage += damage;
        }

        // 根据升级状态计算回复量: 20% 或 30%，向下取整
        int healPercentage = this.upgraded ? HEAL_PERCENTAGE_HIGH : HEAL_PERCENTAGE_LOW;
        int healAmount = totalDamage * healPercentage / 100;

        // 回复生命值
        addToBot(new HealAction(p, p, healAmount));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloodsuckingVine();
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