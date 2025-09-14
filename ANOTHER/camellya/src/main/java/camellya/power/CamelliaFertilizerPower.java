// CamelliaFertilizerPower.java
package camellya.power;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CamelliaFertilizerPower extends AbstractPower {
    public static final String POWER_ID = "camellya:CamelliaFertilizer";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int blockAmount;
    private int dexterityAmount;

    public CamelliaFertilizerPower(AbstractCreature owner, int blockAmount, int dexterityAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.blockAmount = blockAmount;
        this.dexterityAmount = dexterityAmount;
        this.type = PowerType.BUFF;
        this.amount = -1; // 无限层数

        this.img = new Texture("camellyaResources/img/powers/CamelliaFertilizer84.png");

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.blockAmount + DESCRIPTIONS[1] + this.dexterityAmount
                + DESCRIPTIONS[2];
    }

    // 当有怪物死亡时触发
    public void onMonsterDeath(AbstractMonster m) {
        if (m != null) {
            flash();
            // 获得护甲
            addToBot(new GainBlockAction(this.owner, this.owner, this.blockAmount));
            // 获得敏捷
            addToBot(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.dexterityAmount),
                    this.dexterityAmount));
        }
    }
}