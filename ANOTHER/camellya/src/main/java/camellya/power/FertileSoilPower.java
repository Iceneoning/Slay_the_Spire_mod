// FertileSoilPower.java
package camellya.power;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FertileSoilPower extends AbstractPower {
    public static final String POWER_ID = "camellya:FertileSoil";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int cardsToDraw;

    public FertileSoilPower(AbstractCreature owner, int cardsToDraw) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.cardsToDraw = cardsToDraw;
        this.type = PowerType.BUFF;

       // 只使用一张图片
        this.img = new Texture("camellyaResources/img/powers/FertileSoil84.png");
        
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.cardsToDraw + DESCRIPTIONS[1];
    }

    // 监听卡牌被消耗的事件
    @Override
    public void onExhaust(AbstractCard card) {
        flash();
        // 每当有一张牌被消耗时，抽cardsToDraw张牌
        addToBot(new DrawCardAction(this.owner, this.cardsToDraw));
    }
}