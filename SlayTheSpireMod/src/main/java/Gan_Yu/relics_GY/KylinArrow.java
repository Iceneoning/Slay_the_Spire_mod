package Gan_Yu.relics_GY;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

import Gan_Yu.helpers_GY.ModHelper;
import Gan_Yu.power_GY.ChargePower;

import basemod.abstracts.CustomRelic;

public class KylinArrow extends CustomRelic {
    public static final String ID = ModHelper.makePath("KylinArrow");
    private static final String IMG_PATH = "GanYu/img/relics/KylinArrow.png";
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    // 每回合通过打非攻击牌获得蓄力的上限
    private static final int MAX_PER_TURN = 4;
    private int gainedThisTurn = 0;

    public KylinArrow() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 初始化每回合计数器
        this.gainedThisTurn = 0;
        this.counter = MAX_PER_TURN;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 每打出1张非攻击牌，获得1层蓄力
        if (card.type != AbstractCard.CardType.ATTACK) {
            if (this.gainedThisTurn < MAX_PER_TURN) {
                this.flash();
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, 1), 1));
                this.gainedThisTurn += 1;
                this.counter = Math.max(0, MAX_PER_TURN - this.gainedThisTurn);
            } else {
                // 达到本回合上限，不再获得
            }
        }
    }

    @Override
    public void atTurnStart() {
        // 回合开始时重置计数
        this.gainedThisTurn = 0;
        this.counter = MAX_PER_TURN;
    }

    @Override
    public void obtain() {
        // 安全替换：如果玩家持有 AMoze，则将本遗物放到 AMoze 的位置（与原版 BOSS 遗物替换模式一致）
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(AMoze.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                AbstractRelic r = AbstractDungeon.player.relics.get(i);
                if (r != null && AMoze.ID.equals(r.relicId)) {
                    // 用 instantObtain 在同一槽位替换；第三个参数为 true 以调用 onEquip 流程
                    this.instantObtain(AbstractDungeon.player, i, true);
                    return;
                }
            }
        }
        // 默认逻辑
        super.obtain();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KylinArrow();
    }
}
