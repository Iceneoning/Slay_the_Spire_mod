package Gan_Yu.relics_GY;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import Gan_Yu.helpers_GY.ModHelper;
import Gan_Yu.power_GY.ChargePower;

import basemod.abstracts.CustomRelic;

public class AMoze extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = ModHelper.makePath("AMoze");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "GanYu/img/relics/AMoze.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public AMoze() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new AMoze();
    }
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        // 只在战斗开始时给予1层蓄力
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, 1), 1));
    }
}