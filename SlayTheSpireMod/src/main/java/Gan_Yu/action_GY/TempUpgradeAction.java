package Gan_Yu.action_GY;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

public class TempUpgradeAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction");
    public static final String[] TEXT = uiStrings.TEXT;
    
    private AbstractPlayer p;

    public TempUpgradeAction(boolean upgraded, int numOfCards) {
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            // 检查手牌中是否有可以升级的牌
            ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
            for (AbstractCard c : this.p.hand.group) {
                if (c.canUpgrade()) {
                    upgradableCards.add(c);
                }
            }
            
            if (upgradableCards.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            // 如果只有一张可升级的牌，直接升级它
            if (upgradableCards.size() == 1) {
                upgradableCards.get(0).upgrade();
                upgradableCards.get(0).applyPowers();
                this.isDone = true;
                return;
            }
            
            // 否则让玩家选择要升级的牌
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, true, false, false);
            tickDuration();
            return;
        }
        
        // 处理玩家选择的牌
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator<AbstractCard> selectedCards = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();
            
            while (selectedCards.hasNext()) {
                AbstractCard c = selectedCards.next();
                c.upgrade();
                c.applyPowers();
                this.p.hand.addToTop(c);
            }
            
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        
        tickDuration();
    }
}