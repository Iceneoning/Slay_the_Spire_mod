package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import Gan_Yu.helpers_GY.ModHelper;

public class HandyPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("HandyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    // totalStrength stores the sum of strength amounts from all applied Handy cards
    private int totalStrength;

    public HandyPower(AbstractCreature owner, int strengthAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // 初始时 totalStrength 等于传入的该张卡的强度值
        this.totalStrength = strengthAmount;
        // this.amount 表示堆叠次数（多少张 Handy 卡合并在一起）
        this.amount = 1;
        this.canGoNegative = false;
        this.img = new Texture("GanYu/img/powers/HandyPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.totalStrength + DESCRIPTIONS[1];
    }
    
    public void onChargeConsumed(int amount) {
        // 当蓄力被消耗时调用
        // 改为按自身堆叠触发：如果此Power的设计是按每两层蓄力触发一次效果，则使用消耗层数除以2再乘以自身强度。
        // 但如果希望此Power与自身堆叠相关（例如 this.amount 为堆叠数），则应使用 this.amount 来决定触发强度。
        // 下面实现：优先使用 consumed amount 来计算触发次数，但按自身 strengthAmount 为每次触发给定的力量；
        // 同时，如果此Power自身被堆叠（this.amount > 1），也倍增效果。
        int times = amount / 2; // 每两层蓄力触发一次
        if (times > 0 && this.totalStrength > 0) {
            // 按所有已应用 Handy 卡的强度之和触发
            int strengthToGain = times * this.totalStrength;
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthToGain), strengthToGain));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        // stackPower 仍然累加堆叠次数，但不会更改 totalStrength（除非你希望按不同逻辑）
        this.amount += stackAmount;
        if (this.amount < 0) this.amount = 0;
        updateDescription();
    }

    // 当尝试在已有 HandyPower 上再次使用 Handy 卡时，应调用此方法来合并新卡的 strengthValue
    public void addStrengthAndStack(int additionalStrength) {
        this.totalStrength += additionalStrength;
        this.amount += 1;
        updateDescription();
    }
}