package camellya.relics;

import basemod.abstracts.CustomRelic;
import camellya.helpers.ModHelper;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

// 继承CustomRelic
public class MyRelic extends CustomRelic {
    // 遗物ID（此处的ModHelper在"04 - 本地化"中提到）
    public static final String ID = ModHelper.makePath("MyRelic");
    // 图片路径（大小128x128，可参考同目录的图片）
    private static final String IMG_PATH = "camellyaResources/img/relics/MyRelic.png";
    // 遗物未解锁时的轮廓。可以不使用。如果要使用，取消注释
    // private static final String OUTLINE_PATH =
    // "ExampleModResources/img/relics/MyRelic_Outline.png";
    // 遗物类型改为Boss遗物
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    // 添加一个标记，用于跟踪上次增加的是哪种属性
    private boolean lastAddedStrength = false;

    public MyRelic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH),
        // ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        // 检查角色当前的力量值
        int currentStrength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount
                : 0;

        // 检查角色当前的敏捷值
        int currentDexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID) != null
                ? AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount
                : 0;

        // 交替增加力量和敏捷，并添加检测机制
        if (lastAddedStrength) {
            // 上次增加的是力量，这次应该增加敏捷（如果敏捷不超过5）
            if (currentDexterity < 5) {
                this.flash();
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new DexterityPower(AbstractDungeon.player, 1), 1));
            }
            lastAddedStrength = false;
        } else {
            // 上次增加的是敏捷，这次应该增加力量（如果力量不超过5）
            if (currentStrength < 5) {
                this.flash();
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new StrengthPower(AbstractDungeon.player, 1), 1));
            }
            lastAddedStrength = true;
        }
    }

    public AbstractRelic makeCopy() {
        return new MyRelic();
    }
}