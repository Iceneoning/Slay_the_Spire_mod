package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;

import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import Gan_Yu.helpers_GY.ModHelper;
import Gan_Yu.power_GY.ChargePower;

public class IceArrow extends CustomCard {
    public static final String ID = ModHelper.makePath("IceArrow");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/IceArrow.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public IceArrow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 12;
    }

    @Override
    public void upgrade() {
        if (this.upgraded)
            return;
        this.upgradeName();
        this.upgradeDamage(4); // 从12升级到16
        this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                DamageType.NORMAL)));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }

        // 检查玩家是否有蓄力且蓄力值至少为1
        if (p.hasPower(ChargePower.POWER_ID)) {
            ChargePower chargePower = (ChargePower) p.getPower(ChargePower.POWER_ID);
            if (chargePower.amount >= 1) {
                return true;
            }
        }

        // 如果没有蓄力或者蓄力不足1，则不能使用
        this.cantUseMessage = "需要至少1层蓄力才能打出这张牌";
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new IceArrow();
    }
}