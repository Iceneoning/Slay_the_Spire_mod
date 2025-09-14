// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/SwitchToBlooming.java
package camellya.cards;

import basemod.abstracts.CustomCard;
import camellya.power.BloomingFormPower;
import camellya.power.WitheringFormPower;
import camellya.relics.CamelliaFlower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class SwitchToBlooming extends CustomCard {
    public static final String ID = "camellya:SwitchToBlooming";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int STRENGTH_AMOUNT = 1;
    private static final int WEAK_AMOUNT = 1;
    private static final int UPGRADE_STRENGTH = 1;

    public SwitchToBlooming() {
        super(ID, NAME, "camellyaResources/img/cards/SwitchToBlooming.png", COST, DESCRIPTION, CardType.SKILL, CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = STRENGTH_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 移除凋零形态
        addToBot(new RemoveSpecificPowerAction(p, p, WitheringFormPower.POWER_ID));
        // 检查是否有"一朵椿花"遗物，如果有则增加额外的力量
        int strengthAmount = this.magicNumber;
        if (p.hasRelic(CamelliaFlower.ID)) {
            strengthAmount += 1; // 额外增加1点力量（总共2点）
        }
        // 添加力量
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));

        // 添加盛放形态
        addToBot(new ApplyPowerAction(p, p, new BloomingFormPower(p, 1), 1));

        // 对所有敌人施加虚弱
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, WEAK_AMOUNT, false), WEAK_AMOUNT));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwitchToBlooming();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_STRENGTH);
        }
    }
}