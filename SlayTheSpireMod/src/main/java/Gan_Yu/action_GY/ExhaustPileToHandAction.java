package Gan_Yu.action_GY;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ExhaustPileToHandAction extends AbstractGameAction {
    private static final String[] TEXT = {"选择一张要放入手牌的卡牌"}; // 直接定义文本，避免依赖UIStrings
    
    private AbstractPlayer p;
    private boolean isRandom;

    public ExhaustPileToHandAction(int amount, boolean isRandom) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.isRandom = isRandom;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.exhaustPile.isEmpty() || this.p.hand.size() >= 10) {
                this.isDone = true;
                return;
            }

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            
            // 将消耗牌堆中的牌添加到临时组中
            for (AbstractCard c : this.p.exhaustPile.group) {
                tmp.addToTop(c);
            }

            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (this.isRandom) {
                // 随机选择牌
                for (int i = 0; i < this.amount; i++) {
                    if (!tmp.isEmpty()) {
                        AbstractCard card = tmp.getRandomCard(AbstractDungeon.cardRandomRng);
                        this.p.exhaustPile.removeCard(card);
                        this.p.hand.addToTop(card);
                        card.unfadeOut();
                    }
                }
                this.isDone = true;
                return;
            }

            // 让玩家选择牌
            if (tmp.size() <= this.amount) {
                // 如果消耗牌堆中的牌数量小于等于需要的牌数，全部拿回手牌
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>();
                for (AbstractCard c : tmp.group) {
                    cardsToMove.add(c);
                }
                for (AbstractCard c : cardsToMove) {
                    this.p.exhaustPile.removeCard(c);
                    this.p.hand.addToTop(c);
                    c.unfadeOut();
                }
                this.isDone = true;
                return;
            }

            // 打开选择界面
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
            tickDuration();
            return;
        }

        // 处理玩家选择的牌
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                this.p.exhaustPile.removeCard(c);
                this.p.hand.addToTop(c);
                c.unfadeOut();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.p.hand.refreshHandLayout();
        }
        this.isDone = true;
    }
}