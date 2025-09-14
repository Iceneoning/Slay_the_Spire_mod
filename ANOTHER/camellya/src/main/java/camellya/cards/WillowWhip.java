// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/WillowWhip.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class WillowWhip extends CustomCard {
    public static final String ID = "camellya:WillowWhip";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int BASE_DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    // 攻击次数
    private static final int ATTACK_COUNT = 3;
    // 丢弃手牌数量
    private static final int DISCARD_AMOUNT = 1;

    public WillowWhip() {
        super(ID, NAME, "camellyaResources/img/cards/WillowWhip.png", COST, DESCRIPTION, CardType.ATTACK,
                CAMELLYA_GREEN, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = BASE_DAMAGE;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 挥舞长鞭3次，每次对全场敌人造成伤害
        for (int i = 0; i < ATTACK_COUNT; i++) {
            addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        // 将1张手牌放入废牌堆
        addToBot(new DiscardAction(p, p, DISCARD_AMOUNT, false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new WillowWhip();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}