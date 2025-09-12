package Gan_Yu.action_GY;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
public class PermUpgradeCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction");
    public static final String[] TEXT = uiStrings.TEXT;

    public PermUpgradeCardAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            // 查找可以升级的卡牌
            ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.canUpgrade()) {
                    upgradableCards.add(c);
                }
            }
            
            if (upgradableCards.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            // 只有一张可升级卡牌时直接升级
            if (upgradableCards.size() == 1) {
                AbstractCard toUpgrade = upgradableCards.get(0);
                toUpgrade.upgrade();
                toUpgrade.applyPowers();
                toUpgrade.superFlash();
                this.isDone = true;
                return;
            }
            
            // 多张可升级卡牌时让玩家选择
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
            tickDuration();
            return;
        }
        
        // 处理玩家选择的卡牌
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                c.upgrade();
                c.applyPowers();
                c.superFlash();
                AbstractDungeon.player.hand.addToTop(c);
            }
            
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        
        tickDuration();
    }
}