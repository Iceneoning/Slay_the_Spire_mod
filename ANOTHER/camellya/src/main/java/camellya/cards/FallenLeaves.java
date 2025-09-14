// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/FallenLeaves.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class FallenLeaves extends CustomCard {
    public static final String ID = "camellya:FallenLeaves";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 3;
    // 基础数值，用于计算28+3X+2Y/32+4X+3Y公式
    private static final int BASE_DAMAGE = 28;
    private static final int UPGRADE_BASE_DAMAGE = 32;
    private static final int DAMAGE_PER_STRENGTH_LOW = 3;
    private static final int DAMAGE_PER_STRENGTH_HIGH = 4;
    private static final int DAMAGE_PER_DEXTERITY_LOW = 2;
    private static final int DAMAGE_PER_DEXTERITY_HIGH = 3;

    // 易伤层数
    private static final int VULNERABLE_AMOUNT = 2;
    private static final int UPGRADE_VULNERABLE_AMOUNT = 3;

    public FallenLeaves() {
        super(ID, NAME, "camellyaResources/img/cards/FallenLeaves.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = this.magicNumber = VULNERABLE_AMOUNT;
        this.isMultiDamage = true;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 获取玩家当前的力量值和敏捷值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;
        int dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount
                : 0;

        // 确保属性值不为负
        if (strength < 0)
            strength = 0;
        if (dexterity < 0)
            dexterity = 0;

        // 计算描述中要显示的伤害数值
        int displayDamage;
        if (this.upgraded) {
            // 升级版显示: 32 + 4 * 力量 + 3 * 敏捷
            displayDamage = 32 + 4 * strength + 3 * dexterity;
        } else {
            // 基础版显示: 28 + 3 * 力量 + 2 * 敏捷
            displayDamage = 28 + 3 * strength + 2 * dexterity;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength来抵消
        this.baseDamage = displayDamage - strength;

        super.calculateCardDamage(mo);

        // 恢复基础伤害值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
    }

    @Override
    public void applyPowers() {
        // 获取玩家当前的力量值和敏捷值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;
        int dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount
                : 0;

        // 确保属性值不为负
        if (strength < 0)
            strength = 0;
        if (dexterity < 0)
            dexterity = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        int actualDamage;
        int vulnerableAmount = this.magicNumber;

        if (this.upgraded) {
            // 升级版显示: 32 + 4 * 力量 + 3 * 敏捷
            displayDamage = 32 + 4 * strength + 3 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 32 + 4 * strength + 3 * dexterity - strength;
        } else {
            // 基础版显示: 28 + 3 * 力量 + 2 * 敏捷
            displayDamage = 28 + 3 * strength + 2 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 28 + 3 * strength + 2 * dexterity - strength;
        }

        // 设置实际数值，使最终效果与显示数值一致
        this.baseDamage = actualDamage;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        this.rawDescription = "对全场敌人造成 " + displayDamage + " 点伤害和 " + vulnerableAmount + " 回合 易伤 效果。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复基础值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对全场敌人造成伤害
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));

        // 对所有敌人施加易伤效果
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, this.magicNumber, false),
                        this.magicNumber));
            }
        }
    }

    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new FallenLeaves();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级时改变描述，显示32+4X+3Y而不是28+3X+2Y，并增加易伤层数
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.upgradeMagicNumber(UPGRADE_VULNERABLE_AMOUNT - VULNERABLE_AMOUNT);
            initializeDescription();
        }
    }
}