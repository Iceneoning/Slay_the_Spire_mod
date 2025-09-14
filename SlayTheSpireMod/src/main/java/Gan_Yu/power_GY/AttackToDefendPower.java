package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
// Use a direct AbstractGameAction to add block so it's not modified by Dexterity
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import Gan_Yu.helpers_GY.ModHelper;

public class AttackToDefendPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("AttackToDefendPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // totalBaseBlock 存储已应用的所有 AttackToDefend 卡的基础格挡值之和
    private int totalBaseBlock;

    public AttackToDefendPower(AbstractCreature owner, int baseBlock) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // 初始 totalBaseBlock 为该张卡的 baseBlock
        this.totalBaseBlock = baseBlock;
        // this.amount 表示该 Power 的堆叠次数（即有多少张此类卡被合并）
        this.amount = 1;
        this.img = new Texture("GanYu/img/powers/AttackToDefendPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.totalBaseBlock + DESCRIPTIONS[1];
    }
    
    public void onChargeConsumed(int amount) {
        // 当蓄力被消耗时触发
        // 触发时按：totalBaseBlock * consumedLayers
        int consumedLayers = Math.max(0, amount);
        int totalBlock = this.totalBaseBlock * consumedLayers;
        if (totalBlock > 0) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    // Directly add block to the owner to avoid Dexterity modifying this amount
                    AttackToDefendPower.this.owner.addBlock(totalBlock);
                    this.isDone = true;
                }
            });
        }
    }
    
    public void onChargeGained(int amount) {
        // 当蓄力被获得时触发
        // 受到获得蓄力的通知时，按：totalBaseBlock * gainedLayers
        int gainedLayers = Math.max(0, amount);
        int totalBlock = this.totalBaseBlock * gainedLayers;
        if (totalBlock > 0) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    // Directly add block to the owner to avoid Dexterity modifying this amount
                    AttackToDefendPower.this.owner.addBlock(totalBlock);
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        // this.amount 作为堆叠次数，stackPower 累加堆叠（不改变 totalBaseBlock）
        this.amount += stackAmount;
        if (this.amount < 1) this.amount = 1;
        updateDescription();
    }

    // 合并新卡的 baseBlock 到 totalBaseBlock 并增加堆叠数
    public void addBaseBlockAndStack(int addBaseBlock) {
        this.totalBaseBlock += addBaseBlock;
        this.amount += 1;
        if (this.amount < 1) this.amount = 1;
        updateDescription();
    }
}