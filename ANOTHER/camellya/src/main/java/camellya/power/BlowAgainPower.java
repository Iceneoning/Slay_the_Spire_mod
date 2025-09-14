// BlowAgainPower.java
package camellya.power;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BlowAgainPower extends AbstractPower {
    public static final String POWER_ID = "camellya:BlowAgain";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int healAmount;
    private int strengthAmount;
    private boolean hasTriggeredThisTurn = false; // 标记本回合是否已经触发过效果

    public BlowAgainPower(AbstractCreature owner, int healAmount, int strengthAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.healAmount = healAmount;
        this.strengthAmount = strengthAmount;
        this.type = PowerType.BUFF;
        this.amount = -1; // 无限层数

        this.img = new Texture("camellyaResources/img/powers/BlowAgain84.png");

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.healAmount + DESCRIPTIONS[1] + this.strengthAmount + DESCRIPTIONS[2];
    }

    // 监听受到伤害事件
    @Override
    public int onLoseHp(int damage) {
        // 如果本回合还没有触发过效果且确实受到了伤害
        if (!hasTriggeredThisTurn && damage > 0) {
            hasTriggeredThisTurn = true;
            flash();

            // 回复生命值
            addToBot(new HealAction(this.owner, this.owner, this.healAmount));

            // 获得力量
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.strengthAmount),
                    this.strengthAmount));
        }

        // 返回实际损失的生命值（不修改）
        return damage;
    }

    // 在每个回合开始时重置触发状态
    @Override
    public void atStartOfTurn() {
        hasTriggeredThisTurn = false;
    }
}