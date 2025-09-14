// WinterStorage.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import camellya.power.WitheringFormPower;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class WinterStorage extends CustomCard {
    public static final String ID = "camellya:WinterStorage";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 1;
    private static final int ATTACK_TIMES = 2;
    private static final int UPGRADE_PLUS_ATTACK_TIMES = 1;

    public WinterStorage() {
        super(ID, NAME, "camellyaResources/img/cards/WinterStorage.png", COST, DESCRIPTION,
                CardType.SKILL, CAMELLYA_GREEN, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = ATTACK_TIMES;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WitheringFormPower(p, this.magicNumber)));
    }

    // @Override
    // public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    //     boolean canUse = super.canUse(p, m);
    //     if (!canUse) {
    //         return false;
    //     }

    //     // 检查手中是否还有其他技能牌
    //     for (AbstractCard c : p.hand.group) {
    //         if (c != this && c.type == CardType.SKILL) {
    //             return false;
    //         }
    //     }

    //     return true;
    // }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_ATTACK_TIMES);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WinterStorage();
    }
}