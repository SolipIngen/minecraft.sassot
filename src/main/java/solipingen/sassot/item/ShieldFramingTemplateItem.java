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


public class ShieldFramingTemplateItem extends SmithingTemplateItem {

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    private static final String TRANSLATION_KEY = Util.createTranslationKey("item", new Identifier("smithing_template"));
    private static final Text SHIELD_FRAMING_TEXT = Text.translatable(Util.createTranslationKey("upgrade", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "shield_framing"))).formatted(TITLE_FORMATTING);
    private static final Text SHIELD_FRAMING_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.applies_to"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text SHIELD_FRAMING_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.ingredients"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text SHIELD_FRAMING_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.base_slot_description")));
    private static final Text SHIELD_FRAMING_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.additions_slot_description")));
    private static final Identifier EMPTY_SLOT_SHIELD_TEXTURE = new Identifier("item/empty_armor_slot_shield");
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");
    private static final Identifier EMPTY_SLOT_EMERALD_TEXTURE = new Identifier("item/empty_slot_emerald");
    private static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = new Identifier("item/empty_slot_diamond");
    private final Text baseSlotDescriptionText;
    private final Text additionsSlotDescriptionText;
    private final List<Identifier> emptyBaseSlotTextures;
    private final List<Identifier> emptyAdditionsSlotTextures;


    public ShieldFramingTemplateItem(Text appliesToText, Text ingredientsText, Text titleText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures) {
        super(appliesToText, ingredientsText, titleText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures);
        this.baseSlotDescriptionText = baseSlotDescriptionText;
        this.additionsSlotDescriptionText = additionsSlotDescriptionText;
        this.emptyBaseSlotTextures = emptyBaseSlotTextures;
        this.emptyAdditionsSlotTextures = emptyAdditionsSlotTextures;
    }

    public static ShieldFramingTemplateItem createShieldFramingTemplate() {
        return new ShieldFramingTemplateItem(SHIELD_FRAMING_APPLIES_TO_TEXT, SHIELD_FRAMING_INGREDIENTS_TEXT, SHIELD_FRAMING_TEXT, SHIELD_FRAMING_BASE_SLOT_DESCRIPTION_TEXT, SHIELD_FRAMING_ADDITIONS_SLOT_DESCRIPTION_TEXT, 
        ShieldFramingTemplateItem.getFramingEmptyBaseSlotTextures(), ShieldFramingTemplateItem.getFramingEmptyAdditionsSlotTextures());
    }

    private static List<Identifier> getFramingEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_SHIELD_TEXTURE);
    }

    private static List<Identifier> getFramingEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT_TEXTURE, EMPTY_SLOT_DIAMOND_TEXTURE, EMPTY_SLOT_EMERALD_TEXTURE);
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