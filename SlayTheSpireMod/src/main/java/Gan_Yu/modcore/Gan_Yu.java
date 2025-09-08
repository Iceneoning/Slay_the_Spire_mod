package Gan_Yu.modcore;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;

import Gan_Yu.cards.Strike;
import Gan_Yu.character.GanYu;
import Gan_Yu.character.GanYu.PlayerColorEnum;
import static Gan_Yu.character.GanYu.PlayerColorEnum.GAN_YU_GREEN;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditStringsSubscriber;

import com.badlogic.gdx.graphics.Color;

@SpireInitializer
public class Gan_Yu implements EditCardsSubscriber, EditStringsSubscriber,
EditCharactersSubscriber { // 实现接口

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
    private static final String BG_ATTACK_1024 = "GanYu/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "GanYu/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "GanYu/img/1024/bg_skill.png";
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
    }

    public void receiveEditRelics() {
        String lang;
        if (Settings.language == GameLanguage.ZHS) {
            lang = "ZHS"; // 如果语言设置为简体中文，则加载ZHS文件夹的资源
        } else {
            lang = "ENG"; // 如果没有相应语言的版本，默认加载英语
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "GanYu/localization/" + lang + "/cards.json"); 
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
        // 这里添加注册本地化文本
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "GanYu/localization/" + lang + "/characters.json");
    }
}
