package net.mabbouxj.colorful_journey.data.client;

import joptsimple.internal.Strings;
import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        for (RegistryObject<Item> registryObject : ModItems.ALL_COLORED_VARIANTS_ITEMS) {
            ResourceLocation registry = registryObject.get().getRegistryName();
            String textureLoc = registry.getNamespace() + ":item/" + registry.getPath();
            textureLoc = removeColorSuffix(textureLoc);
            getBuilder(registry.toString())
                    .parent(itemGenerated)
                    .texture("layer0", textureLoc);
        }

        for (RegistryObject<BlockItem> registryObject: ModItems.ALL_COLORED_VARIANTS_BLOCK_ITEMS) {
            ResourceLocation registry = registryObject.get().getRegistryName();
            String blockModelName = removeColorSuffix(registry.getPath());
            ModelFile blockModel = getExistingFile(modLoc("block/" + blockModelName));
            getBuilder(registry.getPath()).parent(blockModel);
        }

    }

    private String removeColorSuffix(String str) {
        String[] splitted = str.split("_");
        return Strings.join(Arrays.copyOf(splitted, splitted.length - 1), "_");
    }

}
