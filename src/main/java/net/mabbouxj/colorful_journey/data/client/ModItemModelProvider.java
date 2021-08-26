package net.mabbouxj.colorful_journey.data.client;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ColorfulJourney.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        for (RegistryObject<? extends Item> registryObject : ModItems.COLORED_VARIANTS_ITEMS) {
            builder(itemGenerated, registryObject.get().getRegistryName().toString());
        }

    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", name.replaceAll(":", ":item/"));
    }

}
