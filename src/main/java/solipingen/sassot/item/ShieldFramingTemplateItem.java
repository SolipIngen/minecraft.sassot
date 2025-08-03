package solipingen.sassot.item;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ShieldFramingTemplateItem extends SmithingTemplateItem {

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    private static final String TRANSLATION_KEY = Util.createTranslationKey("item", Identifier.of("smithing_template"));
    private static final Text SHIELD_FRAMING_TEXT = Text.translatable(Util.createTranslationKey("upgrade", Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "shield_framing"))).formatted(TITLE_FORMATTING);
    private static final Text SHIELD_FRAMING_APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.applies_to"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text SHIELD_FRAMING_INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.ingredients"))).formatted(DESCRIPTION_FORMATTING);
    private static final Text SHIELD_FRAMING_BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.base_slot_description")));
    private static final Text SHIELD_FRAMING_ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(Util.createTranslationKey("item", Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID,"smithing_template.shield_framing.additions_slot_description")));
    private static final Identifier EMPTY_SLOT_SHIELD_TEXTURE = Identifier.of("container/slot/shield");
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = Identifier.of("container/slot/ingot");
    private static final Identifier EMPTY_SLOT_EMERALD_TEXTURE = Identifier.of("container/slot/emerald");
    private static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = Identifier.of("container/slot/diamond");
    private final Text baseSlotDescriptionText;
    private final Text additionsSlotDescriptionText;
    private final List<Identifier> emptyBaseSlotTextures;
    private final List<Identifier> emptyAdditionsSlotTextures;


    public ShieldFramingTemplateItem(Text appliesToText, Text ingredientsText, Text baseSlotDescriptionText, Text additionsSlotDescriptionText, List<Identifier> emptyBaseSlotTextures, List<Identifier> emptyAdditionsSlotTextures, Item.Settings settings) {
        super(appliesToText, ingredientsText, baseSlotDescriptionText, additionsSlotDescriptionText, emptyBaseSlotTextures, emptyAdditionsSlotTextures, settings);
        this.baseSlotDescriptionText = baseSlotDescriptionText;
        this.additionsSlotDescriptionText = additionsSlotDescriptionText;
        this.emptyBaseSlotTextures = emptyBaseSlotTextures;
        this.emptyAdditionsSlotTextures = emptyAdditionsSlotTextures;
    }

    public static ShieldFramingTemplateItem createShieldFramingTemplate(Item.Settings settings) {
        return new ShieldFramingTemplateItem(SHIELD_FRAMING_APPLIES_TO_TEXT, SHIELD_FRAMING_INGREDIENTS_TEXT, SHIELD_FRAMING_BASE_SLOT_DESCRIPTION_TEXT, SHIELD_FRAMING_ADDITIONS_SLOT_DESCRIPTION_TEXT,
        ShieldFramingTemplateItem.getFramingEmptyBaseSlotTextures(), ShieldFramingTemplateItem.getFramingEmptyAdditionsSlotTextures(), settings);
    }

    private static List<Identifier> getFramingEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_SHIELD_TEXTURE);
    }

    private static List<Identifier> getFramingEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT_TEXTURE, EMPTY_SLOT_DIAMOND_TEXTURE, EMPTY_SLOT_EMERALD_TEXTURE);
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


}