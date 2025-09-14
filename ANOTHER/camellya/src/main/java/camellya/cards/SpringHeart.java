// SpringHeart.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class SpringHeart extends CustomCard {
    public static final String ID = "camellya:SpringHeart";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    public SpringHeart() {
        super(ID, NAME, "camellyaResources/img/cards/SpringHeart.png", COST, DESCRIPTION,
                CardType.SKILL, CAMELLYA_GREEN, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查是否有被消耗的牌
        if (!AbstractDungeon.player.exhaustPile.isEmpty()) {
            // 选择一张被消耗的牌
            AbstractCard cardToReturn = AbstractDungeon.player.exhaustPile.getRandomCard(AbstractDungeon.cardRandomRng);
            if (cardToReturn != null) {
                // 复制这张牌并设置费用为0
                AbstractCard copyCard = cardToReturn.makeStatEquivalentCopy();
                copyCard.setCostForTurn(0);
                copyCard.freeToPlayOnce = true;

                // 将复制的牌加入手牌
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(copyCard,
                        (float) (Math.random() * 300.0F + 200.0F),
                        (float) (Math.random() * 300.0F + 200.0F)));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpringHeart();
    }
}