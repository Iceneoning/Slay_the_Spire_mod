// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/SharpFlowerBranch.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class SharpFlowerBranch extends CustomCard {
    public static final String ID = "camellya:SharpFlowerBranch";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 2;
    // 基础数值，用于计算11+2Y/14+3Y公式
    private static final int BASE_DAMAGE = 11;
    private static final int UPGRADE_DAMAGE_BONUS = 3; // 升级时基础伤害增加3点(从11变为14)
    private static final int DAMAGE_PER_DEXTERITY_LOW = 2; // 基础版系数
    private static final int DAMAGE_PER_DEXTERITY_HIGH = 3; // 升级版系数
    // 抽牌数量
    private static final int DRAW_AMOUNT = 2;

    public SharpFlowerBranch() {
        super(ID, NAME, "camellyaResources/img/cards/SharpFlowerBranch.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
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
        if (this.upgraded) {
            // 升级版显示: 14 + 3 * 敏捷
            displayDamage = 14 + 3 * dexterity;
        } else {
            // 基础版显示: 11 + 2 * 敏捷
            displayDamage = 11 + 2 * dexterity;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength来抵消
        this.baseDamage = displayDamage - strength;

        super.calculateCardDamage(mo);

        // 恢复基础伤害值
        this.baseDamage = BASE_DAMAGE + (this.upgraded ? UPGRADE_DAMAGE_BONUS : 0);
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
        int actualDamage;

        if (this.upgraded) {
            // 升级版显示: 14 + 3 * 敏捷
            displayDamage = 14 + 3 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 14 + 3 * dexterity - strength;
        } else {
            // 基础版显示: 11 + 2 * 敏捷
            displayDamage = 11 + 2 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 11 + 2 * dexterity - strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        this.baseDamage = actualDamage;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        this.rawDescription = "对目标敌人造成 " + displayDamage + " 点伤害。若该回合内未使用过其他攻击牌，则从牌堆中抽取 " + DRAW_AMOUNT + " 张牌。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始值
        this.baseDamage = BASE_DAMAGE + (this.upgraded ? UPGRADE_DAMAGE_BONUS : 0);
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对目标敌人造成伤害
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // 检查本回合是否使用过其他攻击牌
        if (!hasPlayedAttackThisTurn()) {
            // 从牌堆中抽取2张牌
            addToBot(new DrawCardAction(p, DRAW_AMOUNT));
        }
    }

    /**
     * 检查本回合是否使用过其他攻击牌
     */
    private boolean hasPlayedAttackThisTurn() {
        // 遍历本回合已使用的卡牌
        for (AbstractCard card : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            // 如果是攻击牌且不是当前这张牌，则返回true
            if (card.type == CardType.ATTACK && card != this) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new SharpFlowerBranch();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级时改变描述，显示14+3Y而不是11+2Y
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}