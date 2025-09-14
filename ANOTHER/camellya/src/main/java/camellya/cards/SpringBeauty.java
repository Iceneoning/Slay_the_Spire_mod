// SpringBeauty.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class SpringBeauty extends CustomCard {
    public static final String ID = "camellya:SpringBeauty";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int STRENGTH_DEXTERITY = 2;
    private static final int UPGRADE_STRENGTH_DEXTERITY = 2; // 升级时增加2点（从2到4）
    private static final int PLATED_ARMOR = 4;
    private static final int UPGRADE_PLATED_ARMOR = 1; // 升级时增加1层（从4到5）

    public SpringBeauty() {
        super(ID, NAME, "camellyaResources/img/cards/SpringBeauty.png", COST, DESCRIPTION,
                CardType.SKILL, CAMELLYA_GREEN, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = STRENGTH_DEXTERITY;
        this.baseBlock = PLATED_ARMOR; // 使用block存储护甲值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 增加力量和敏捷
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));
        // 获得多层护甲
        addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.block), this.block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH_DEXTERITY); // 从2点增加到4点力量和敏捷
            this.upgradeBlock(UPGRADE_PLATED_ARMOR); // 从4层增加到5层护甲
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpringBeauty();
    }
}