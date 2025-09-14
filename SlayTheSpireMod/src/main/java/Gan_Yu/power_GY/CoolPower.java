package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Gan_Yu.helpers_GY.ModHelper;

public class CoolPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("CoolPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private static final int HP_LOSS = 20;

    public CoolPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.img = new Texture("GanYu/img/powers/CoolPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        int total = HP_LOSS * Math.max(1, this.amount);
        this.description = DESCRIPTIONS[0] + total + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束时受到20点“无来源且可被格挡”的伤害
        if (isPlayer) {
            this.flash();
            // 使用自定义安全动作直接造成可被格挡的伤害，避免 DamageAction 在某些上下文产生 NPE
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    if (CoolPower.this.owner != null && !CoolPower.this.owner.isDeadOrEscaped()) {
                        int total = HP_LOSS * Math.max(1, CoolPower.this.amount);
                        DamageInfo info = new DamageInfo(CoolPower.this.owner, total, DamageInfo.DamageType.NORMAL);
                        // 这里确实是让角色自身受到其造成的伤害，而不是直接失去生命
                        CoolPower.this.owner.damage(info);
                    }
                    this.isDone = true;
                }
            });

            // 移除这个Power，因为它只需要触发一次
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount < 1) this.amount = 1;
        updateDescription();
    }
}