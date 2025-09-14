// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/LuoHong.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class LuoHong extends CustomCard {
    public static final String ID = "camellya:LuoHong";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 0;
    // 基础数值，用于计算4+2X/6+2X公式
    private static final int BASE_DAMAGE = 4;
    private static final int DAMAGE_PER_STRENGTH = 2;

    public LuoHong() {
        super(ID, NAME, "camellyaResources/img/cards/LuoHong.png", COST, DESCRIPTION, CardType.ATTACK, CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.isMultiDamage = true;
        this.exhaust = true; // 消耗品
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 计算实际伤害: 基础值 + 2 * 力量值
        int strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null ?
                AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount : 0;

        // 确保力量值不为负
        if (strength < 0) strength = 0;

        // 计算伤害: 4+2X 或 6+2X
        this.baseDamage = BASE_DAMAGE + DAMAGE_PER_STRENGTH * strength;

        // 如果已升级，基础值增加2点（从4变为6）
        if (this.upgraded) {
            this.baseDamage += 2;
        }

        super.calculateCardDamage(mo);

        // 恢复基础伤害值，以便下次计算
        this.baseDamage = BASE_DAMAGE;
        if (this.upgraded) {
            this.baseDamage += 2;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对全场敌人造成伤害
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE));

        // 获得数值等于造成总伤害一半的护甲（向下取整）
        // 计算总伤害
        int totalDamage = 0;
        for (int damage : this.multiDamage) {
            totalDamage += damage;
        }

        // 获得等于总伤害一半的护甲（向下取整）
        int blockAmount = totalDamage / 2;
        addToBot(new GainBlockAction(p, p, blockAmount));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LuoHong();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级时改变描述，显示6+2X而不是4+2X
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}