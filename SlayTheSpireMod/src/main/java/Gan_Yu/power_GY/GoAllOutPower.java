package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Gan_Yu.helpers_GY.ModHelper;

public class GoAllOutPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("GoAllOutPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GoAllOutPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.img = new Texture("GanYu/img/powers/GoAllOutPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] +amount+DESCRIPTIONS[1] ;
    }

    public void onChargeConsumed(int amount) {
        // 当蓄力被消耗时调用，无论消耗多少层都只获得1点能量
        // 改为按自身堆叠数触发：this.amount 表示要获得的能量数（可被 stackPower 增加）
        addToBot(new GainEnergyAction(this.amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount < 0) this.amount = 0;
        updateDescription();
    }
}