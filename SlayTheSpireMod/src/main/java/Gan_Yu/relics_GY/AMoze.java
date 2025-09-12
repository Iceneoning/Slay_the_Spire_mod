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

        // 如果当前房间是 Boss 房，则在安全时机将本遗物替换为 KylinArrow（原位替换）
        if (AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss) {
            // 异步在 action 队列替换以避免 UI 并发问题
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    try {
                        String targetId = AMoze.ID;
                        int oldIndex = -1;
                        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                            AbstractRelic r = AbstractDungeon.player.relics.get(i);
                            if (r != null && targetId.equals(r.relicId)) {
                                oldIndex = i;
                                break;
                            }
                        }
                        if (oldIndex == -1) {
                            this.isDone = true;
                            return;
                        }

                        // 移除旧遗物（AMoze）并在原位 instantObtain 一个新的 KylinArrow，不触发其 onEquip
                        String oldId = AbstractDungeon.player.relics.get(oldIndex).relicId;
                        AbstractDungeon.player.loseRelic(oldId);
                        KylinArrow newRelic = new KylinArrow();
                        newRelic.instantObtain(AbstractDungeon.player, oldIndex, false);

                        // 去除这个临时触发的 this（如果存在重复）
                        AbstractDungeon.player.relics.remove(AMoze.this);
                    } catch (Exception e) {
                        System.out.println("[AMoze] boss-replace failed: " + e);
                    }
                    this.isDone = true;
                }
            });
        }
    }
}