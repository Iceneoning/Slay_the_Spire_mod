package camellya.modcore;

import static camellya.characters.MyCharacter.PlayerColorEnum.CAMELLYA_GREEN;
import static camellya.characters.MyCharacter.PlayerColorEnum.MY_CHARACTER;
import static com.megacrit.cardcrawl.core.Settings.language;

import java.nio.charset.StandardCharsets;

import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import camellya.cards.*;
import camellya.characters.MyCharacter;
import camellya.relics.CamelliaFlower;
import camellya.relics.MyRelic;
import camellya.relics.WoodenDoll;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.RelicStrings;


@SpireInitializer // 加载mod的注解
public class came_llya implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber,
        EditRelicsSubscriber, EditKeywordsSubscriber {
    // 除以255得出需要的参数。你也可以直接写出计算值。
    public static final Color MY_COLOR = new Color(174.0F / 255.0F, 2.0F / 255.0F, 11.0F / 255.0F, 1.0F);
    // 人物选择界面按钮的图片
    private static final String MY_CHARACTER_BUTTON = "camellyaResources/img/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "camellyaResources/img/char/Character_Portrait.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "camellyaResources/img/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "camellyaResources/img/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "camellyaResources/img/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "camellyaResources/img/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "camellyaResources/img/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "camellyaResources/img/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "camellyaResources/img/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "camellyaResources/img/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = "camellyaResources/img/char/cost_orb.png";

    // 构造方法
    public came_llya() {
        BaseMod.subscribe(this); // 告诉basemod你要订阅事件
        // 这里注册颜色
        BaseMod.addColor(CAMELLYA_GREEN, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR,
                BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENEYGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024,
                BIG_ORB, SMALL_ORB);
    }

    // 注解需要调用的方法，必须写
    public static void initialize() {
        new came_llya();
    }

    // 当basemod开始注册mod卡牌时，便会调用这个函数
    @Override
    public void receiveEditCards() {
        // 向basemod注册卡牌
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Block());
        BaseMod.addCard(new FlowerCard());
        BaseMod.addCard(new SwitchToBlooming());
        BaseMod.addCard(new SwitchToWithering());
        BaseMod.addCard(new HarryLuWhirlwind()); // 添加这行
        BaseMod.addCard(new LunDa());
        BaseMod.addCard(new SharpFlowerBranch());
        BaseMod.addCard(new BarbedFlowerBranch()); // 添加这一行
        BaseMod.addCard(new RoseSwordsman()); // 添加这一行
        BaseMod.addCard(new HuaChao()); // 添加这一行
        BaseMod.addCard(new LuoHong()); // 添加这一行
        BaseMod.addCard(new LoveFlower()); // 添加这一行
        BaseMod.addCard(new WillowWhip()); // 添加这一行
        BaseMod.addCard(new SuffocatingVine()); // 添加这一行
        BaseMod.addCard(new BloodsuckingVine()); // 添加这一行
        BaseMod.addCard(new BarricadeVine()); // 添加这一行
        BaseMod.addCard(new FallenCamellia()); // 添加这一行
        BaseMod.addCard(new OleanderBranch()); // 添加这一行
        BaseMod.addCard(new ReedAutumn());
        BaseMod.addCard(new FallenLeaves()); // 添加这一行
        BaseMod.addCard(new OneDayFlower());
        BaseMod.addCard(new BloomingAshes());//技能从下面开始
        BaseMod.addCard(new ColdResistant());  // 耐寒
        BaseMod.addCard(new HeatRelief());     // 解暑
        BaseMod.addCard(new ThornDense());     // 荆棘密布
        BaseMod.addCard(new SpringBirth());
        BaseMod.addCard(new SummerGrowth());
        BaseMod.addCard(new AutumnHarvest());
        BaseMod.addCard(new WinterStorage());
        BaseMod.addCard(new Redemption());
        BaseMod.addCard(new BloomingWitheringWaltz());
        BaseMod.addCard(new BotanicalCosmos());
        BaseMod.addCard(new SoundSleep());
        BaseMod.addCard(new FlowerSecret());
        BaseMod.addCard(new SpringBeauty());
        BaseMod.addCard(new SpringHeart());
        BaseMod.addCard(new ShoreKeeperBlessing());
        BaseMod.addCard(new RestAndRecover());
        BaseMod.addCard(new FertileSoil());
        BaseMod.addCard(new Photosynthesis());
        BaseMod.addCard(new CamelliaFertilizer());
        BaseMod.addCard(new BlowAgain());
        BaseMod.addCard(new BeyondWithering());
    }

    // 当开始添加人物时，调用这个方法
    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT,
                MY_CHARACTER);
    }

    public void receiveEditStrings() {
        String lang;
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else {
            lang = "ENG";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, "camellyaResources/localization/" + lang + "/cards.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                "camellyaResources/localization/" + lang + "/characters.json");
        // 添加注册json
        BaseMod.loadCustomStringsFile(RelicStrings.class, "camellyaResources/localization/" + lang + "/relics.json");
        // 添加能力字符串注册
        BaseMod.loadCustomStringsFile(com.megacrit.cardcrawl.localization.PowerStrings.class,
                "camellyaResources/localization/" + lang + "/powers.json");
    }

    @Override
    public void receiveEditRelics() {
        // 将遗物添加到自定义颜色池中，使其成为角色独有遗物
        BaseMod.addRelicToCustomPool(new MyRelic(), CAMELLYA_GREEN);
        BaseMod.addRelicToCustomPool(new CamelliaFlower(), CAMELLYA_GREEN);
         // 添加木雕玩偶遗物
        BaseMod.addRelicToCustomPool(new WoodenDoll(), CAMELLYA_GREEN);
  
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ENG";
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        }

        String json = Gdx.files.internal("camellyaResources/localization/" + lang + "/Keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword("camellya", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

}
