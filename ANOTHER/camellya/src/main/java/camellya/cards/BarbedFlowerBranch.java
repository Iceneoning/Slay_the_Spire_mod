// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/BarbedFlowerBranch.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class BarbedFlowerBranch extends CustomCard {
    public static final String ID = "camellya:BarbedFlowerBranch";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    // 基础数值，用于计算8+Y/10+2Y公式
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_DAMAGE_BONUS = 2; // 升级时基础伤害增加2点(从8变为10)
    private static final int DAMAGE_PER_DEXTERITY_LOW = 1;
    private static final int DAMAGE_PER_DEXTERITY_HIGH = 2;
    // 虚弱层数
    private static final int VULNERABLE_AMOUNT = 2;
    private static final int UPGRADE_PLUS_VULNERABLE = 1;

    public BarbedFlowerBranch() {
        super(ID, NAME, "camellyaResources/img/cards/BarbedFlowerBranch.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = this.magicNumber = VULNERABLE_AMOUNT;
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
            // 升级版显示: 10 + 2 * 敏捷
            displayDamage = 10 + 2 * dexterity;
        } else {
            // 基础版显示: 8 + 敏捷
            displayDamage = 8 + dexterity;
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
            // 升级版显示: 10 + 2 * 敏捷
            displayDamage = 10 + 2 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 10 + 2 * dexterity - strength;
        } else {
            // 基础版显示: 8 + 敏捷
            displayDamage = 8 + dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 8 + dexterity - strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        this.baseDamage = actualDamage;

        // 计算虚弱层数
        int vulnerableAmount = this.magicNumber;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        this.rawDescription = "对目标敌人造成 " + displayDamage + " 点伤害。给予其 " + vulnerableAmount + " 层 虚弱 。";
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
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        // 给目标施加虚弱
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BarbedFlowerBranch();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级时改变描述，显示10+2Y而不是8+Y，并增加虚弱层数
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
            initializeDescription();
        }
    }
}