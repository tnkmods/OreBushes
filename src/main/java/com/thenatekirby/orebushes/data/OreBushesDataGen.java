package com.thenatekirby.orebushes.data;

import com.thenatekirby.orebushes.OreBushes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

// ====---------------------------------------------------------------------------====

@Mod.EventBusSubscriber(modid = OreBushes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreBushesDataGen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();

        if (event.includeServer()) {
            OreBushesBlockTagProvider blockTagProvider = new OreBushesBlockTagProvider(dataGenerator, event.getExistingFileHelper());
            dataGenerator.addProvider(blockTagProvider);
            dataGenerator.addProvider(new OreBushesItemTagProvider(dataGenerator, blockTagProvider, event.getExistingFileHelper()));
            dataGenerator.addProvider(new OreBushesRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new OreBushesExternalRecipeProvider(dataGenerator));
            dataGenerator.addProvider(new OreBushesMaterialsProvider(dataGenerator));
            dataGenerator.addProvider(new OreBushesGlobalLootProvider(dataGenerator));
            dataGenerator.addProvider(new OreBushesLootTablesProvider(dataGenerator));
        }
    }
}
