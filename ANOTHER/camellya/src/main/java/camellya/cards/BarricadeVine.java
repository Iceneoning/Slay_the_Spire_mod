// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/BarricadeVine.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class BarricadeVine extends CustomCard {
    public static final String ID = "camellya:BarricadeVine";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 3;
    // 基础数值，用于计算12+2X+2Y/16+2X+2Y公式
    private static final int BASE_DAMAGE = 12;
    private static final int UPGRADE_BASE_DAMAGE = 16;
    private static final int DAMAGE_PER_STRENGTH_LOW = 2;
    private static final int DAMAGE_PER_STRENGTH_HIGH = 2;
    private static final int DAMAGE_PER_DEXTERITY_LOW = 2;
    private static final int DAMAGE_PER_DEXTERITY_HIGH = 2;

    // 护甲基础值
    private static final int BASE_BLOCK = 3;
    private static final int UPGRADE_BASE_BLOCK = 5;
    private static final int BLOCK_PER_STRENGTH_LOW = 1;
    private static final int BLOCK_PER_STRENGTH_HIGH = 2;
    private static final int BLOCK_PER_DEXTERITY_LOW = 2;
    private static final int BLOCK_PER_DEXTERITY_HIGH = 3;

    public BarricadeVine() {
        super(ID, NAME, "camellyaResources/img/cards/BarricadeVine.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseBlock = BASE_BLOCK;
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
        int displayBlock;
        if (this.upgraded) {
            // 升级版显示: 16 + 2 * 力量 + 2 * 敏捷
            displayDamage = 16 + 2 * strength + 2 * dexterity;
            // 升级版护甲显示: 5 + 2 * 力量 + 3 * 敏捷
            displayBlock = 5 + 2 * strength + 3 * dexterity;
        } else {
            // 基础版显示: 12 + 2 * 力量 + 2 * 敏捷
            displayDamage = 12 + 2 * strength + 2 * dexterity;
            // 基础版护甲显示: 3 + 力量 + 2 * 敏捷
            displayBlock = 3 + strength + 2 * dexterity;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength来抵消
        this.baseDamage = displayDamage - strength;

        // 设置护甲值
        this.baseBlock = displayBlock;

        super.calculateCardDamage(mo);

        // 恢复基础值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.baseBlock = this.upgraded ? UPGRADE_BASE_BLOCK : BASE_BLOCK;
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
        int displayBlock;
        int actualDamage;

        if (this.upgraded) {
            // 升级版显示: 16 + 2 * 力量 + 2 * 敏捷
            displayDamage = 16 + 2 * strength + 2 * dexterity;
            // 升级版护甲显示: 5 + 2 * 力量 + 3 * 敏捷
            displayBlock = 5 + 2 * strength + 3 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 16 + 2 * strength + 2 * dexterity - strength;
        } else {
            // 基础版显示: 12 + 2 * 力量 + 2 * 敏捷
            displayDamage = 12 + 2 * strength + 2 * dexterity;
            // 基础版护甲显示: 3 + 力量 + 2 * 敏捷
            displayBlock = 3 + strength + 2 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 12 + 2 * strength + 2 * dexterity - strength;
        }

        // 设置实际数值，使最终效果与显示数值一致
        this.baseDamage = actualDamage;
        this.baseBlock = displayBlock;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        this.rawDescription = "对目标敌人造成 " + displayDamage + " 点伤害，并获得 " + displayBlock + " 点护甲。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复基础值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.baseBlock = this.upgraded ? UPGRADE_BASE_BLOCK : BASE_BLOCK;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对目标敌人造成伤害
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        // 获得护甲
        addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BarricadeVine();
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