package Gan_Yu.modcore_GY;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import Gan_Yu.cards_GY.AttackToDefend;
import Gan_Yu.cards_GY.Blessedness;
import Gan_Yu.cards_GY.CalmAndCollected;
import Gan_Yu.cards_GY.Cheer;
import Gan_Yu.cards_GY.Cool;
import Gan_Yu.cards_GY.CountlessTrials;
import Gan_Yu.cards_GY.Dartle;
import Gan_Yu.cards_GY.Defend;
import Gan_Yu.cards_GY.End;
import Gan_Yu.cards_GY.EndTheWork;
import Gan_Yu.cards_GY.Frost;
import Gan_Yu.cards_GY.FrostBlooms;
import Gan_Yu.cards_GY.FrozenThousand;
import Gan_Yu.cards_GY.GoAllOut;
import Gan_Yu.cards_GY.GoWithMe;
import Gan_Yu.cards_GY.Hail;
import Gan_Yu.cards_GY.Handy;
import Gan_Yu.cards_GY.HappyTime;
import Gan_Yu.cards_GY.HardWork;
import Gan_Yu.cards_GY.HeartProtective;
import Gan_Yu.cards_GY.Hide;
import Gan_Yu.cards_GY.HideBow;
import Gan_Yu.cards_GY.IceArmor;
import Gan_Yu.cards_GY.IceArrow;
import Gan_Yu.cards_GY.IceAttack;
import Gan_Yu.cards_GY.IceLotus;
import Gan_Yu.cards_GY.Kylin;
import Gan_Yu.cards_GY.MilkTea;
import Gan_Yu.cards_GY.NightSnow;
import Gan_Yu.cards_GY.OnlyThisHeart;
import Gan_Yu.cards_GY.Pray;
import Gan_Yu.cards_GY.Ready;
import Gan_Yu.cards_GY.RelaxBySelf;
import Gan_Yu.cards_GY.RiverDaySnow;
import Gan_Yu.cards_GY.SmileFace;
import Gan_Yu.cards_GY.StormShooting;
import Gan_Yu.cards_GY.Strike;
import Gan_Yu.cards_GY.TheGodOnMoon;
import Gan_Yu.cards_GY.UnderTheMoon;
import Gan_Yu.cards_GY.WaitForTheFuture;
import Gan_Yu.cards_GY.Winter;
import Gan_Yu.cards_GY.WorkOverTime;
import Gan_Yu.character_GY.GanYu;
import Gan_Yu.character_GY.GanYu.PlayerColorEnum;
import Gan_Yu.relics_GY.*;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;

import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

@SpireInitializer
public class Gan_Yu implements EditCardsSubscriber, EditStringsSubscriber,
EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber { // 添加EditKeywordsSubscriber接口

    public static final Color MY_COLOR = new Color(62.0f / 255.0f, 237.0f / 255.0f, 231.0f / 255.0f, 1.0f); // 定义卡牌颜色
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "GanYu/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "GanYu/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "GanYu/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "GanYu/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "GanYu/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "GanYu/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "GanYu/img/1024/bg_attack_1024.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "GanYu/img/1024/bg_power_1024.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "GanYu/img/1024/bg_skill_1024.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "GanYu/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = "GanYu/img/char/cost_orb.png";

    public Gan_Yu() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        // 这里注册颜色
        BaseMod.addColor(GAN_YU_GREEN, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,BG_ATTACK_512,BG_SKILL_512,BG_POWER_512,ENEYGY_ORB,BG_ATTACK_1024,BG_SKILL_1024,BG_POWER_1024,BIG_ORB,SMALL_ORB);
    }

    public static void initialize() {
        new Gan_Yu();
    }

     @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new GanYu(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, PlayerColorEnum.GAN_YU);
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new CalmAndCollected());
        BaseMod.addCard(new IceLotus());
        BaseMod.addCard(new RelaxBySelf());
        BaseMod.addCard(new Handy());
        BaseMod.addCard(new Hide());
        BaseMod.addCard(new Ready());
        BaseMod.addCard(new HideBow());
        BaseMod.addCard(new Kylin());
        BaseMod.addCard(new Hail());
        BaseMod.addCard(new IceArmor());
        BaseMod.addCard(new HeartProtective());
        BaseMod.addCard(new HardWork());
        BaseMod.addCard(new FrostBlooms());
        BaseMod.addCard(new GoAllOut());
        BaseMod.addCard(new AttackToDefend());
        BaseMod.addCard(new WorkOverTime());
        BaseMod.addCard(new Dartle());
        BaseMod.addCard(new FrozenThousand());
        BaseMod.addCard(new OnlyThisHeart());
        BaseMod.addCard(new Frost());
        BaseMod.addCard(new IceArrow());
        BaseMod.addCard(new HappyTime());
        BaseMod.addCard(new EndTheWork());
        BaseMod.addCard(new Blessedness());
        BaseMod.addCard(new CountlessTrials());
        BaseMod.addCard(new Winter());
        BaseMod.addCard(new IceAttack());
        BaseMod.addCard(new StormShooting());
        BaseMod.addCard(new End());
        BaseMod.addCard(new Pray());
        BaseMod.addCard(new Cool());
        BaseMod.addCard(new Cheer());
        BaseMod.addCard(new NightSnow());
        BaseMod.addCard(new MilkTea());
        BaseMod.addCard(new WaitForTheFuture());
        BaseMod.addCard(new SmileFace());
        BaseMod.addCard(new RiverDaySnow());
        BaseMod.addCard(new GoWithMe());
        BaseMod.addCard(new UnderTheMoon());
        BaseMod.addCard(new TheGodOnMoon());
    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        // 卡牌
        BaseMod.loadCustomStringsFile(CardStrings.class, "GanYu/localization/" + lang + "/cards.json");
        // 能力
        BaseMod.loadCustomStringsFile(PowerStrings.class, "GanYu/localization/" + lang + "/powers.json");
        // 角色
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "GanYu/localization/" + lang + "/characters.json");
        // 遗物
        BaseMod.loadCustomStringsFile(RelicStrings.class, "GanYu/localization/" + lang + "/relics.json");
    }
    
       @Override
       public void receiveEditKeywords() {
           Gson gson = new Gson();

           String json = Gdx.files.internal("GanYu/localization/ZHS/keywords.json")
                   .readString(String.valueOf(StandardCharsets.UTF_8));
           Keyword[] keywords = gson.fromJson(json, Keyword[].class);
           if (keywords != null) {
               for (Keyword keyword : keywords) {
                   // 这个id要全小写
                   BaseMod.addKeyword("ganyu", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
               }
           }
       }
    
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new AMoze(), GAN_YU_GREEN);
        BaseMod.addRelic(new WorkOverTimeRelic(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new KylinArrow(), GAN_YU_GREEN);
    }
}