package Gan_Yu.power_GY;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import Gan_Yu.helpers_GY.ModHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CountlessTrialsPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("CountlessTrialsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CountlessTrialsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.priority = 999; // 高优先级确保先消耗再获得
        this.img = new Texture("GanYu/img/powers/CountlessTrialsPower.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 当使用攻击牌时，获得1层蓄力
        if (card.type == AbstractCard.CardType.ATTACK) {
            // 先消耗蓄力再获得蓄力
            if (this.owner.hasPower(ModHelper.makePath("ChargePower"))) {
                final com.megacrit.cardcrawl.powers.AbstractPower existing = this.owner.getPower(ModHelper.makePath("ChargePower"));
                    if (existing instanceof Gan_Yu.power_GY.ChargePower) {
                    // 通过调用 ChargePower.onAttackPlay() 来请求消费，让 ChargePower 自行管理 consumeQueued，避免重复排队
                    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                        @Override
                        public void update() {
                            ((Gan_Yu.power_GY.ChargePower) existing).onAttackPlay();
                            this.isDone = true;
                        }
                    });
                } else {
                    AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, ModHelper.makePath("ChargePower")));
                }
            }

            // 获得1层蓄力（延迟加入真正的ApplyPowerAction，确保消费动作及其产生的移除/修正先执行）
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    System.out.println("[DEBUG] CountlessTrialsPower delayed action: applying 1 ChargePower (prefer stack if exists)");
                    String cpId = Gan_Yu.helpers_GY.ModHelper.makePath("ChargePower");
                    com.megacrit.cardcrawl.powers.AbstractPower existing = CountlessTrialsPower.this.owner.getPower(cpId);
                    if (existing instanceof Gan_Yu.power_GY.ChargePower) {
                        // Prefer stacking on existing ChargePower to avoid creating a new instance
                        ((Gan_Yu.power_GY.ChargePower) existing).stackPower(1);
                    } else {
                        AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(CountlessTrialsPower.this.owner, CountlessTrialsPower.this.owner,
                                new ChargePower(CountlessTrialsPower.this.owner, 1), 1));
                    }
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束时移除该能力
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                    this.owner, this.owner, this));
        }
    }
}