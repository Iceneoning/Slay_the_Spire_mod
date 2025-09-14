package Gan_Yu.cards_GY;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import Gan_Yu.helpers_GY.ModHelper;
import Gan_Yu.power_GY.ChargePower;
import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;

public class RiverDaySnow extends CustomCard {
    public static final String ID = ModHelper.makePath("RiverDaySnow");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "GanYu/img/cards/RiverDaySnow.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = GAN_YU_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int DAMAGE = 5;
    private static final int CHARGE_AMOUNT = 1;
    private static final int UPGRADE_CHARGE_AMOUNT = 1;
    private static final int HITS = 3;

    public RiverDaySnow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = CHARGE_AMOUNT;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 0) 在使用攻击牌的瞬间，禁止 ChargePower 在 onUseCard 中提前消费：
        //    提前把 consumeQueued 设为 true（若已存在 ChargePower），之后我们在本牌动作末尾再统一消费。
        if (p.hasPower(ChargePower.POWER_ID)) {
            try {
                Gan_Yu.power_GY.ChargePower cp0 = (Gan_Yu.power_GY.ChargePower) p.getPower(ChargePower.POWER_ID);
                java.lang.reflect.Field f = cp0.getClass().getDeclaredField("consumeQueued");
                f.setAccessible(true);
                f.setBoolean(cp0, true);
            } catch (Exception ignored) {}
        }

        // 1) 先给予蓄力（ChargePower 会在 onInitialApplication 中 addToTop Strength，从而在后续伤害前生效）
        addToBot(new ApplyPowerAction(p, p, new ChargePower(p, this.magicNumber), this.magicNumber));

        // 2) 多段随机打击：每一段在“执行时”选择目标并计算伤害（applyPowers），确保吃到当前力量加成；
        //    即便上一击击杀了目标，后续依旧会随机命中存活敌人。
        for (int i = 0; i < HITS; i++) {
            addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    // 若已无可选敌人，直接结束
                    if (com.megacrit.cardcrawl.dungeons.AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                        this.isDone = true;
                        return;
                    }
                    // 执行时选择一个随机存活目标
                    AbstractMonster target = com.megacrit.cardcrawl.dungeons.AbstractDungeon.getMonsters()
                        .getRandomMonster(null, true, com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRandomRng);
                    if (target != null) {
                        // 现在计算伤害：包含当前的力量等加成
                        com.megacrit.cardcrawl.cards.DamageInfo info = new com.megacrit.cardcrawl.cards.DamageInfo(p, RiverDaySnow.this.baseDamage, RiverDaySnow.this.damageTypeForTurn);
                        info.applyPowers(p, target);
                        // 在栈顶立即结算本次打击
                        addToTop(new com.megacrit.cardcrawl.actions.common.DamageAction(target, info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    }
                    this.isDone = true;
                }
            });
            // 节奏等待，让连击更清晰
            addToBot(new com.megacrit.cardcrawl.actions.utility.WaitAction(0.18F));
        }
        // 3) 在三段伤害全部入队后，显式安排一次“延后消费”动作（若此时存在 ChargePower）。
        addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
            @Override
            public void update() {
                if (p.hasPower(ChargePower.POWER_ID)) {
                    Gan_Yu.power_GY.ChargePower cpNow = (Gan_Yu.power_GY.ChargePower) p.getPower(ChargePower.POWER_ID);
                    // 在栈顶立即排入消费（以保证紧随其后执行）
                    addToTop(cpNow.getConsumeAction());
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_CHARGE_AMOUNT);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RiverDaySnow();
    }
}