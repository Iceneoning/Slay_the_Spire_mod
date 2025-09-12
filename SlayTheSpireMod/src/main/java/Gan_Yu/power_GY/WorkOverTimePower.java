package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Gan_Yu.helpers_GY.ModHelper;

public class WorkOverTimePower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("WorkOverTimePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public WorkOverTimePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = new Texture("GanYu/img/powers/WorkOverTimePower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        // 每回合开始时触发效果
        if (this.owner.isPlayer) {
            // 获得1层蓄力
            if (!this.owner.hasPower(ModHelper.makePath("ChargePower"))) {
                addToBot(new ApplyPowerAction(this.owner, this.owner, new ChargePower(this.owner, 1), 1));
            } else {
                // 如果已有蓄力，增加1层
                addToBot(new ApplyPowerAction(this.owner, this.owner, 
                    this.owner.getPower(ModHelper.makePath("ChargePower")), 1));
            }
            
            // 回复1点能量
            addToBot(new GainEnergyAction(1));
            
            // 抽1张牌
            addToBot(new DrawCardAction(this.owner, 1));
        }
    }
}