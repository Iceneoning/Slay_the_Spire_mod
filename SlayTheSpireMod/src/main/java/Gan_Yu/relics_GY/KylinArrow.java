package Gan_Yu.relics_GY;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import Gan_Yu.helpers_GY.ModHelper;
import Gan_Yu.power_GY.ChargePower;

import basemod.abstracts.CustomRelic;

public class KylinArrow extends CustomRelic {
    public static final String ID = ModHelper.makePath("KylinArrow");
    private static final String IMG_PATH = "GanYu/img/relics/KylinArrow.png";
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public KylinArrow() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        System.out.println("[KylinArrow] onEquip called");
        System.out.println("[KylinArrow] player relics before action: size=" + AbstractDungeon.player.relics.size());
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            AbstractRelic r = AbstractDungeon.player.relics.get(i);
            System.out.println("[KylinArrow] relic[" + i + "]=" + (r == null ? "null" : r.relicId));
        }
        // 异步替换：在 action 队列中执行以避免直接在 UI 迭代时修改列表
        // 等待一小段时间以确保 UI 完成重组
    AbstractDungeon.actionManager.addToBottom(new WaitAction(Settings.ACTION_DUR_MED));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                System.out.println("[KylinArrow] replacement action running");
                String targetId = ModHelper.makePath("AMoze");
                int oldIndex = -1;
                for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                    AbstractRelic r = AbstractDungeon.player.relics.get(i);
                    if (r != null && targetId.equals(r.relicId)) {
                        oldIndex = i;
                        break;
                    }
                }
                System.out.println("[KylinArrow] found oldIndex=" + oldIndex + ", relics.size=" + AbstractDungeon.player.relics.size());
                if (oldIndex == -1) {
                    System.out.println("[KylinArrow] no AMoze found, nothing to replace");
                    this.isDone = true;
                    return;
                }

                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, KylinArrow.this));

                String oldId = AbstractDungeon.player.relics.get(oldIndex).relicId;
                AbstractDungeon.player.loseRelic(oldId);

                int existingIndex = -1;
                for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                    AbstractRelic r = AbstractDungeon.player.relics.get(i);
                    if (r != null && KylinArrow.this.relicId.equals(r.relicId)) {
                        existingIndex = i;
                        break;
                    }
                }
                System.out.println("[KylinArrow] existingIndex=" + existingIndex + ", relics.size post-removal=" + AbstractDungeon.player.relics.size());
                if (existingIndex != -1) {
                    AbstractRelic found = AbstractDungeon.player.relics.remove(existingIndex);
                    int insertIndex = Math.min(oldIndex, AbstractDungeon.player.relics.size());
                    AbstractDungeon.player.relics.add(insertIndex, found);
                    System.out.println("[KylinArrow] moved existing relic from " + existingIndex + " to " + insertIndex);
                } else {
                    int insertIndex = Math.min(oldIndex, AbstractDungeon.player.relics.size());
                    AbstractDungeon.player.relics.add(insertIndex, KylinArrow.this);
                    System.out.println("[KylinArrow] inserted this at " + insertIndex);
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void obtain() {
        super.obtain();
        // Fallback: schedule a replacement action after obtain completes to handle different obtain flows
        // obtain 后备路径也加入短等待
    AbstractDungeon.actionManager.addToBottom(new WaitAction(Settings.ACTION_DUR_MED));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                System.out.println("[KylinArrow] obtain-based replacement action running");
                String targetId = ModHelper.makePath("AMoze");
                int oldIndex = -1;
                for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                    AbstractRelic r = AbstractDungeon.player.relics.get(i);
                    if (r != null && targetId.equals(r.relicId)) {
                        oldIndex = i;
                        break;
                    }
                }
                System.out.println("[KylinArrow] (obtain) found oldIndex=" + oldIndex + ", relics.size=" + AbstractDungeon.player.relics.size());
                if (oldIndex == -1) {
                    this.isDone = true;
                    return;
                }

                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, KylinArrow.this));

                String oldId = AbstractDungeon.player.relics.get(oldIndex).relicId;
                AbstractDungeon.player.loseRelic(oldId);

                int existingIndex = -1;
                for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                    AbstractRelic r = AbstractDungeon.player.relics.get(i);
                    if (r != null && KylinArrow.this.relicId.equals(r.relicId)) {
                        existingIndex = i;
                        break;
                    }
                }
                System.out.println("[KylinArrow] (obtain) existingIndex=" + existingIndex + ", relics.size post-removal=" + AbstractDungeon.player.relics.size());
                if (existingIndex != -1) {
                    AbstractRelic found = AbstractDungeon.player.relics.remove(existingIndex);
                    int insertIndex = Math.min(oldIndex, AbstractDungeon.player.relics.size());
                    AbstractDungeon.player.relics.add(insertIndex, found);
                    System.out.println("[KylinArrow] (obtain) moved existing relic from " + existingIndex + " to " + insertIndex);
                } else {
                    int insertIndex = Math.min(oldIndex, AbstractDungeon.player.relics.size());
                    AbstractDungeon.player.relics.add(insertIndex, KylinArrow.this);
                    System.out.println("[KylinArrow] (obtain) inserted this at " + insertIndex);
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 每打出1张非攻击牌，获得1层蓄力
        if (card.type != AbstractCard.CardType.ATTACK) {
            this.flash();
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ChargePower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KylinArrow();
    }
}
