package camellya.relics;

import basemod.abstracts.CustomRelic;
import camellya.helpers.ModHelper;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

// 一朵椿花遗物类
public class CamelliaFlower extends CustomRelic {
    // 遗物ID
    public static final String ID = ModHelper.makePath("CamelliaFlower");
    // 图片路径
    private static final String IMG_PATH = "camellyaResources/img/relics/CamelliaFlower.png";
    // 遗物类型 - 罕见遗物
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public CamelliaFlower() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new CamelliaFlower();
    }
}