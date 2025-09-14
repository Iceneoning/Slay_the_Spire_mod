package camellya.relics;

import basemod.abstracts.CustomRelic;
import camellya.helpers.ModHelper;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

// 木雕玩偶遗物类
public class WoodenDoll extends CustomRelic {
    // 遗物ID
    public static final String ID = ModHelper.makePath("WoodenDoll");
    // 图片路径
    private static final String IMG_PATH = "camellyaResources/img/relics/WoodenDoll.png";
    // 遗物类型 - 初始遗物
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    // 格挡值
    private static final int BLOCK_AMOUNT = 4;

    public WoodenDoll() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK_AMOUNT));
    }

    public AbstractRelic makeCopy() {
        return new WoodenDoll();
    }
}