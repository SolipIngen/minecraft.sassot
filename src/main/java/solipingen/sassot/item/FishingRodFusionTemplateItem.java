package solipingen.sassot.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class FishingRodFusionTemplateItem extends SmithingTemplateItem {

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    private static final String TRANSLATION_KEY = Util.createTranslationKey("item", new Identifier("smithing_template"));
    private static final Text FISHING_ROD_FUSION_TEXT = Text.translatable(Util.createTranslationKey("upgrade", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "fishing_rod_fusion"))).formatted(TITLE_FORMATTING);
    private static final Text FISHING_ROD_FUSION_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.fishing_rod_fusion.applies_to"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text FISHING_ROD_FUSION_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.fishing_rod_fusion.ingredients"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text FISHING_ROD_FUSION_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.fishing_rod_fusion.base_slot_description")));
    private static final Text FISHING_ROD_FUSION_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.fishing_rod_fusion.additions_slot_description")));
    private static final Identifier EMPTY_SLOT_FISHING_ROD_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "item/empty_slot_fishing_rod");
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");
    private final Text baseSlotDescriptionText;
    private final Text additionsSlotDescriptionText;
    private final List<Identifier> emptyBaseSlotTextures;
    private final List<Identifier> emptyAdditionsSlotTextures;


    public FishingRodFusionTemplateItem(Text appliesToText, Text ingredientsText, Text titleText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures) {
        super(appliesToText, ingredientsText, titleText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures);
        this.baseSlotDescriptionText = baseSlotDescriptionText;
        this.additionsSlotDescriptionText = additionsSlotDescriptionText;
        this.emptyBaseSlotTextures = emptyBaseSlotTextures;
        this.emptyAdditionsSlotTextures = emptyAdditionsSlotTextures;
    }

    public static FishingRodFusionTemplateItem createFishingRodFusionTemplate() {
        return new FishingRodFusionTemplateItem(FISHING_ROD_FUSION_APPLIES_TO_TEXT, FISHING_ROD_FUSION_INGREDIENTS_TEXT, FISHING_ROD_FUSION_TEXT, FISHING_ROD_FUSION_BASE_SLOT_DESCRIPTION_TEXT, FISHING_ROD_FUSION_ADDITIONS_SLOT_DESCRIPTION_TEXT, 
        FishingRodFusionTemplateItem.getFramingEmptyBaseSlotTextures(), FishingRodFusionTemplateItem.getFramingEmptyAdditionsSlotTextures());
    }

    private static List<Identifier> getFramingEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_FISHING_ROD_TEXTURE);
    }

    private static List<Identifier> getFramingEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT_TEXTURE);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Text getBaseSlotDescription() {
        return this.baseSlotDescriptionText;
    }

    @Override
    public Text getAdditionsSlotDescription() {
        return this.additionsSlotDescriptionText;
    }

    @Override
    public List<Identifier> getEmptyBaseSlotTextures() {
        return this.emptyBaseSlotTextures;
    }

    @Override
    public List<Identifier> getEmptyAdditionsSlotTextures() {
        return this.emptyAdditionsSlotTextures;
    }

    @Override
    public String getTranslationKey() {
        return TRANSLATION_KEY;
    }

}