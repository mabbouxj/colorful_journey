package net.mabbouxj.colorful_journey.data;

import net.mabbouxj.colorful_journey.ColorfulJourney;
import net.mabbouxj.colorful_journey.data.client.ModBlockModelProvider;
import net.mabbouxj.colorful_journey.data.client.ModBlockStateProvider;
import net.mabbouxj.colorful_journey.data.client.ModItemModelProvider;
import net.mabbouxj.colorful_journey.data.loot.ModLootTablesProvider;
import net.mabbouxj.colorful_journey.data.tag.ModBlockTagProvider;
import net.mabbouxj.colorful_journey.data.tag.ModItemTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ColorfulJourney.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {

    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ModBlockTagProvider blockTagsProvider = new ModBlockTagProvider(gen, existingFileHelper);

        gen.addProvider(new ModLootTablesProvider(gen));
        gen.addProvider(blockTagsProvider);
        gen.addProvider(new ModItemTagProvider(gen, blockTagsProvider, existingFileHelper));
        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
        gen.addProvider(new ModBlockModelProvider(gen, existingFileHelper));
    }

}
