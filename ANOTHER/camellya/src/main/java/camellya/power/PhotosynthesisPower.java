// PhotosynthesisPower.java
package camellya.power;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

public class PhotosynthesisPower extends AbstractPower {
    public static final String POWER_ID = "camellya:Photosynthesis";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack
            .getPowerStrings("camellya:Photosynthesis");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int energyPerTurn = 0; // 当前每回合获得的能量数
    private int baseEnergyGain; // 基础能量增长值

    public PhotosynthesisPower(AbstractCreature owner, AbstractCreature source, int baseEnergyGain) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.baseEnergyGain = baseEnergyGain;
        this.type = PowerType.BUFF;
        this.amount = -1; // 无限层数

        this.img = new Texture("camellyaResources/img/powers/Photosynthesis84.png");

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.energyPerTurn + DESCRIPTIONS[1];
    }

    // 在每回合开始时触发效果，递增获得能量
    @Override
    public void atStartOfTurn() {
        if (this.owner.isPlayer) {
            flash();
            // 增加本回合的能量获取量
            energyPerTurn += baseEnergyGain;
            // 应用能量增加效果
            addToBot(new ApplyPowerAction(this.owner, this.owner, new EnergizedPower(this.owner, energyPerTurn),
                    energyPerTurn));
            updateDescription();
        }
    }
}