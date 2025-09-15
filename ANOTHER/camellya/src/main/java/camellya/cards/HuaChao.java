// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/HuaChao.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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

public class HuaChao extends CustomCard {
    public static final String ID = "camellya:HuaChao";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 0;
    // 基础数值，用于计算4+X+Y/5+2X+2Y公式
    private static final int BASE_DAMAGE = 4;
    private static final int UPGRADE_BASE_DAMAGE = 5;
    private static final int BASE_BLOCK = 6;
    private static final int DAMAGE_PER_STRENGTH_LOW = 1;
    private static final int DAMAGE_PER_STRENGTH_HIGH = 2;
    private static final int DAMAGE_PER_DEXTERITY_LOW = 1;
    private static final int DAMAGE_PER_DEXTERITY_HIGH = 2;
    // 攻击目标数量
    private static final int TARGET_COUNT = 2;

    public HuaChao() {
        super(ID, NAME, "camellyaResources/img/cards/HuaChao.png", COST, DESCRIPTION, CardType.ATTACK, CAMELLYA_GREEN,
                CardRarity.COMMON, CardTarget.ALL_ENEMY);
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

        // 确保数值不为负
        if (strength < 0)
            strength = 0;
        if (dexterity < 0)
            dexterity = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        if (this.upgraded) {
            // 升级版显示: 5 + 2 * 力量 + 2 * 敏捷
            displayDamage = 5 + 2 * strength + 2 * dexterity;
        } else {
            // 基础版显示: 4 + 力量 + 敏捷
            displayDamage = 4 + strength + dexterity;
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

        // 确保数值不为负
        if (strength < 0)
            strength = 0;
        if (dexterity < 0)
            dexterity = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        int actualDamage;

        if (this.upgraded) {
            // 升级版显示: 5 + 2 * 力量 + 2 * 敏捷
            displayDamage = 5 + 2 * strength + 2 * dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 5 + 2 * strength + 2 * dexterity - strength;
        } else {
            // 基础版显示: 4 + 力量 + 敏捷
            displayDamage = 4 + strength + dexterity;
            // 实际伤害值（游戏会自动加上strength，我们需要减去strength来抵消）
            actualDamage = 4 + strength + dexterity - strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        this.baseDamage = actualDamage;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        this.rawDescription = "对场上随机" + TARGET_COUNT + "名敌人造成 " + displayDamage + " 点伤害，然后获得 !B! 点护甲。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对场上随机2名敌人造成伤害
        for (int i = 0; i < TARGET_COUNT; i++) {
            addToBot(new DamageRandomEnemyAction(new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        // 获得护甲
        addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HuaChao();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级时改变描述，并将护甲从6点提升到7点
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.upgradeBlock(1);
            initializeDescription();
        }
    }
}