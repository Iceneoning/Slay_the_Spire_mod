package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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
import camellya.power.PoisonPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class ReedAutumn extends CustomCard {
    public static final String ID = "camellya:ReedAutumn";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.ATTACK;
    private static final AbstractCard.CardColor COLOR = CAMELLYA_GREEN;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int BASE_DAMAGE = 11;
    private static final int UPGRADE_BASE_DAMAGE = 14;
    private static final int DAMAGE_PER_DEXTERITY_LOW = 2;
    private static final int DAMAGE_PER_DEXTERITY_HIGH = 3;
    private static final int POISON_AMOUNT = 3;

    public ReedAutumn() {
        super(ID, NAME, "camellyaResources/img/cards/ReedAutumn.png", COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = BASE_DAMAGE;
        this.exhaust = true; // 消耗品
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
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
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
        this.rawDescription = "以萧瑟秋风席卷一名敌人，对其造成 " + displayDamage + " 点伤害，同时使其获得 " + POISON_AMOUNT + " 点 camellya:注毒";
        this.initializeDescription();

        // 调用父类方法处理游戏系统的力量加成
        super.applyPowers();

        // 恢复原始值
        this.baseDamage = this.upgraded ? UPGRADE_BASE_DAMAGE : BASE_DAMAGE;
        this.rawDescription = originalDescription;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对敌人造成伤害
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // 施加注毒效果
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, POISON_AMOUNT), POISON_AMOUNT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReedAutumn();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}