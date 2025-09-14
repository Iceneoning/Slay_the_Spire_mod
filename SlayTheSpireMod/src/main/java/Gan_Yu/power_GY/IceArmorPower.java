package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import Gan_Yu.helpers_GY.ModHelper;

public class IceArmorPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("IceArmorPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // totalWeak 存储所有施加该Power时传入的 weak 数值之和
    private int totalWeak;
    private boolean triggered = false; // 标记是否已触发一次效果

    public IceArmorPower(AbstractCreature owner, int weakAmount) {
    this.name = NAME;
    this.ID = POWER_ID;
    this.owner = owner;
    this.totalWeak = weakAmount;
    this.type = PowerType.BUFF;
    this.img = new Texture("GanYu/img/powers/IceArmorPower.png");
    updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.totalWeak + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
    // 当被敌方攻击并且实际造成伤害时，给予攻击者虚弱，但只触发一次
    // 调试输出，帮助排查未触发的问题
    try {
        System.out.println("[IceArmor] onAttacked called. attacker=" + info.owner + ", type=" + info.type + ", damageAmount=" + damageAmount + ", output=" + info.output + ", triggered=" + triggered);
    } catch (Exception e) {
        // 忽略打印异常
    }

        if (!triggered
        && info.owner != null
        && info.owner != this.owner
        && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            // 使用 totalWeak
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, this.owner,
                new WeakPower(info.owner, this.totalWeak, false), this.totalWeak));
            System.out.println("[IceArmor] Applied Weak to " + info.owner + " amount=" + this.totalWeak);
            triggered = true; // 标记已触发，防止再次触发
    }
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        // 在玩家的下个回合开始时移除冰甲能力
        addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    // 合并新的 weak 值并增加堆叠次数
    public void addWeakAndStack(int addWeak) {
        this.totalWeak += addWeak;
        this.amount += 1;
        updateDescription();
    }
}