// AutumnHarvest.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Iterator;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class AutumnHarvest extends CustomCard {
    public static final String ID = "camellya:AutumnHarvest";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final int COST = 1;
    private static final int COST_UPGRADE = 0;
    private static final int DEBUFF_TURNS = 3;

    public AutumnHarvest() {
        super(ID, NAME, "camellyaResources/img/cards/AutumnHarvest.png", COST, DESCRIPTION,
                CardType.SKILL, CAMELLYA_GREEN, CardRarity.BASIC, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = DEBUFF_TURNS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters.iterator();
        while (monsters.hasNext()) {
            AbstractMonster monster = monsters.next();
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, this.magicNumber, false)));
                addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, this.magicNumber, false)));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(COST_UPGRADE);
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AutumnHarvest();
    }
}