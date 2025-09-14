// 文件路径: h:/vs_code/camellya/src/main/java/camellya/cards/SwitchToWithering.java
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
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;

public class SwitchToWithering extends CustomCard {
    public static final String ID = "camellya:SwitchToWithering";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int DEXTERITY_AMOUNT = 1;
    private static final int VULNERABLE_AMOUNT = 1;
    private static final int UPGRADE_DEXTERITY = 1;

    public SwitchToWithering() {
        super(ID, NAME, "camellyaResources/img/cards/SwitchToWithering.png", COST, DESCRIPTION, CardType.SKILL, CAMELLYA_GREEN, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = DEXTERITY_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 移除盛放形态
        addToBot(new RemoveSpecificPowerAction(p, p, BloomingFormPower.POWER_ID));

        // 检查是否有"一朵椿花"遗物，如果有则增加额外的敏捷
        int dexterityAmount = this.magicNumber;
        if (p.hasRelic(CamelliaFlower.ID)) {
            dexterityAmount += 1; // 额外增加1点敏捷（总共2点）
        }
        // 添加敏捷
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));

        // 添加凋零形态
        addToBot(new ApplyPowerAction(p, p, new WitheringFormPower(p, 1), 1));

        // 对所有敌人施加易伤
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, VULNERABLE_AMOUNT, false), VULNERABLE_AMOUNT));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwitchToWithering();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DEXTERITY);
        }
    }
}