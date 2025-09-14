package camellya.cards;

import basemod.abstracts.CustomCard;
import camellya.power.PoisonPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class FlowerCard extends CustomCard {
    public static final String ID = "camellya:FlowerCard";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 5;
    private static final int UPGRADE_DAMAGE_BONUS = 2; // 升级时伤害增加2点(从5变为7)
    private static final int BASE_POISON = 1;
    private static final int UPGRADE_POISON_BONUS = 1; // 升级时注毒增加1层(从1变为2)

    public FlowerCard() {
        super(ID, NAME, "camellyaResources/img/cards/FlowerCard.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.baseMagicNumber = this.magicNumber = BASE_POISON;
        this.isMultiDamage = true;
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
            // 升级版显示: 7 + 2 * 力量
            displayDamage = 7 + 2 * strength;
        } else {
            // 基础版显示: 5 + 力量
            displayDamage = 5 + strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength
        this.baseDamage = displayDamage - strength;

        super.calculateCardDamage(mo);

        // 恢复基础伤害值
        if (this.upgraded) {
            this.baseDamage = 7;
        } else {
            this.baseDamage = 5;
        }
    }

    @Override
    public void applyPowers() {
        // 获取玩家当前的力量值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;
        if (strength < 0)
            strength = 0;

        // 计算描述中要显示的数值
        int displayDamage;
        int poisonValue;

        if (this.upgraded) {
            // 升级版显示: 7 + 2 * 力量
            displayDamage = 7 + 2 * strength;
            // 升级版: 2 + 力量
            poisonValue = 2 + strength;
        } else {
            // 基础版显示: 5 + 力量
            displayDamage = 5 + strength;
            // 基础版: 1 + 力量
            poisonValue = 1 + strength;
        }

        // 设置实际伤害值，使最终伤害与显示数值一致
        // 由于游戏会自动加上strength，我们需要从显示数值中减去strength
        this.baseDamage = displayDamage - strength;

        // 保存原始值
        String originalDescription = this.rawDescription;

        // 更新描述显示计算后的数值
        this.rawDescription = "对所有敌人造成 " + displayDamage + " 点伤害，并施加 " + poisonValue + " 层 注毒 。";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始描述
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人造成伤害
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_HEAVY));

        // 计算注毒层数
        int strength = p.getPower(StrengthPower.POWER_ID) != null ? p.getPower(StrengthPower.POWER_ID).amount : 0;
        // 确保力量值不为负
        if (strength < 0)
            strength = 0;

        // 计算注毒层数: 1+X 或 2+X
        int poisonAmount = (this.upgraded ? BASE_POISON + UPGRADE_POISON_BONUS : BASE_POISON) + strength;

        // 对所有敌人施加注毒效果
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new PoisonPower(monster, poisonAmount), poisonAmount));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlowerCard();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}