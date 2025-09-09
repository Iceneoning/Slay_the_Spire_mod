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

import Gan_Yu.cards_GY.CalmAndCollected;
import Gan_Yu.cards_GY.Defend;
import Gan_Yu.cards_GY.Hail;
import Gan_Yu.cards_GY.Handy;
import Gan_Yu.cards_GY.Hide;
import Gan_Yu.cards_GY.HideBow;
import Gan_Yu.cards_GY.IceLotus;
import Gan_Yu.cards_GY.Kylin;
import Gan_Yu.cards_GY.Ready;
import Gan_Yu.cards_GY.RelaxBySelf;
import Gan_Yu.cards_GY.Strike;
import Gan_Yu.character_GY.GanYu;
import Gan_Yu.character_GY.GanYu.PlayerColorEnum;
import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;

import static Gan_Yu.character_GY.GanYu.PlayerColorEnum.GAN_YU_GREEN;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

@SpireInitializer
public class Gan_Yu implements EditCardsSubscriber, EditStringsSubscriber,
EditCharactersSubscriber, EditKeywordsSubscriber { // 添加EditKeywordsSubscriber接口

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
    }

    @Override
    public void receiveEditStrings() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "GanYu/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "GanYu/localization/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "GanYu/localization/" + lang + "/characters.json");
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
}