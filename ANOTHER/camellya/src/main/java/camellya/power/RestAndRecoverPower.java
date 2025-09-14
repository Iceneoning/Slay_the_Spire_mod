// RestAndRecoverPower.java
package camellya.power;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class RestAndRecoverPower extends AbstractPower {
    public static final String POWER_ID = "camellya:RestAndRecover";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int healAmount;
    private int strengthAmount;

    public RestAndRecoverPower(AbstractCreature owner, int healAmount, int strengthAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.healAmount = healAmount;
        this.strengthAmount = strengthAmount;
        this.type = PowerType.BUFF;

       // 只使用一张图片
        this.img = new Texture("camellyaResources/img/powers/RestAndRecover84.png");
        
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.healAmount + DESCRIPTIONS[1] + this.strengthAmount + DESCRIPTIONS[2];
    }

    // 在每回合结束时触发效果
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            flash();
            // 回复生命值
            addToBot(new HealAction(this.owner, this.owner, this.healAmount));
            // 增加力量
            addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.strengthAmount), this.strengthAmount));
        }
    }
}