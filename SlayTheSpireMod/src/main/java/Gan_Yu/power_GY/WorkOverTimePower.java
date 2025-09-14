package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
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
        // 该Power表示一个倒计时效果：从当前回合开始触发，总共触发4次后致死。
        // 为向后兼容，如果传入的 amount < 4 则默认为4。
        this.amount = (amount >= 4) ? amount : 4;
        this.type = PowerType.BUFF;
        this.img = new Texture("GanYu/img/powers/WorkOverTimePower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        // 描述已在 powers.json 中定义。这里构造最终描述：
        // DESCRIPTIONS[0] = "...上限提升"
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        // 每回合开始时触发效果
        if (this.owner.isPlayer) {
            // 每回合触发：获得2层蓄力、回复3能量、抽2张牌
            addToBot(new ApplyPowerAction(this.owner, this.owner, new Gan_Yu.power_GY.ChargePower(this.owner, 2), 2));
            addToBot(new GainEnergyAction(3));
            addToBot(new DrawCardAction(this.owner, 2));

            // 触发后减少剩余回合数；若达到0则杀死玩家
            this.amount -= 1;
            updateDescription();
            if (this.amount <= 0) {
                // 使用 LoseHPAction 直接消耗玩家当前生命值以确保死亡（绕过格挡）
                int hp = Math.max(0, this.owner.currentHealth);
                if (hp > 0) {
                    addToBot(new LoseHPAction(this.owner, this.owner, hp));
                }
            }
        }
    }

    @Override
    public void onInitialApplication() {
        // 当Power被真正添加到玩家身上时，立即生效一次（从当前回合开始）
        if (this.owner != null && this.owner.isPlayer) {
            addToBot(new ApplyPowerAction(this.owner, this.owner, new Gan_Yu.power_GY.ChargePower(this.owner, 2), 2));
            addToBot(new GainEnergyAction(3));
            addToBot(new DrawCardAction(this.owner, 2));

            // 既然已经在当前回合触发一次，则消耗一次计数
            this.amount -= 1;
            updateDescription();
            if (this.amount <= 0) {
                int hp = Math.max(0, this.owner.currentHealth);
                if (hp > 0) {
                    addToBot(new LoseHPAction(this.owner, this.owner, hp));
                }
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        // 如果再次获得该Power，则扩展剩余回合
        this.amount += stackAmount;
        if (this.amount < 0) this.amount = 0;
        updateDescription();
    }
}