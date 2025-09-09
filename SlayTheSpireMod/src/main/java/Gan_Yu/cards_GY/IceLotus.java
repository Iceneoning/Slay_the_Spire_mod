package Gan_Yu.cards_GY;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.WeakPower;
import Gan_Yu.helpers_GY.ModHelper;
import basemod.abstracts.CustomCard;
import Gan_Yu.power_GY.ChargePower;

public class IceLotus extends CustomCard {
    public static final String ID = ModHelper.makePath("IceLotus");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/IceLotus.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    
    private static final int BASE_DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 5;
    private static final int BASE_BLOCK = 15;
    private static final int UPGRADE_BLOCK = 5;
    private static final int WEAK_AMOUNT = 1;
    private static final int UPGRADE_WEAK_AMOUNT = 2;

    public IceLotus() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = BASE_DAMAGE;
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = this.magicNumber = WEAK_AMOUNT;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_WEAK_AMOUNT - WEAK_AMOUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人造成伤害
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        
        // 获得格挡
        addToBot(new GainBlockAction(p, p, this.block));
        
        // 给予所有敌人虚弱效果
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, this.magicNumber, false), this.magicNumber));
            }
        }
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 检查是否有蓄力效果
        if (AbstractDungeon.player.hasPower(ChargePower.POWER_ID)) {
            ChargePower chargePower = (ChargePower) AbstractDungeon.player.getPower(ChargePower.POWER_ID);
            int chargeAmount = chargePower.amount;
            
            if (chargeAmount >= 1) {
                // 蓄力1层：额外造成6点伤害
                this.baseDamage += 6;
            }
            
            if (chargeAmount >= 2) {
                // 蓄力2层：额外造成12点伤害
                this.baseDamage += 12;
            }
        }
        
        super.calculateCardDamage(mo);
        
        // 恢复原始伤害值，避免影响下次计算
        this.baseDamage = BASE_DAMAGE + (this.upgraded ? UPGRADE_DAMAGE : 0);
    }
    
    @Override
    public com.megacrit.cardcrawl.cards.AbstractCard makeCopy() {
        return new IceLotus();
    }
}