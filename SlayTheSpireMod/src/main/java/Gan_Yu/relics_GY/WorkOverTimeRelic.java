package Gan_Yu.relics_GY;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import Gan_Yu.helpers_GY.ModHelper;

import basemod.abstracts.CustomRelic;

public class WorkOverTimeRelic extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath("WorkOverTimeRelic");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "GanYu/img/relics/WorkOverTimeRelic.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public WorkOverTimeRelic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new WorkOverTimeRelic();
    }
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        // 闪光提示并在回合开始时给予1层蓄力
        this.flash();
        // 每回合开始时，获得1点能量
        this.addToBot(new GainEnergyAction(1));
        // 每回合开始时，往抽牌堆中加入1张眩晕（Dazed）并洗入抽牌堆
        this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
    }
}