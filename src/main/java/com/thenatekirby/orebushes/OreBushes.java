package com.thenatekirby.orebushes;

import com.thenatekirby.babel.core.MutableResourceLocation;
import com.thenatekirby.orebushes.config.OreBushesConfig;
import com.thenatekirby.orebushes.item.FertilizerItem;
import com.thenatekirby.orebushes.registry.OreBushRegistry;
import com.thenatekirby.orebushes.registration.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// ====---------------------------------------------------------------------------====

@Mod("orebushes")
public class OreBushes {
    public static final String MOD_ID = "orebushes";
    public static final MutableResourceLocation MOD = new MutableResourceLocation(MOD_ID);

    private static final Logger LOGGER = LogManager.getLogger();
    public static Logger getLogger() {
        return LOGGER;
    }

    // ====---------------------------------------------------------------------------====

    public OreBushes() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().register(new OreBushesModels()));

        OreBushesTags.init();

        OreBushesBlocks.register();
        OreBushesItems.register();

        OreBushesConfig.init();
    }

    // ====---------------------------------------------------------------------------====
    // region Event Handling

    private void setup(final FMLCommonSetupEvent event) {
        FertilizerItem.registerDispenserBehavior();
        OreBushesConditions.onRegisterLootConditions();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        OreBushesModels.onClientSetup();
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            OreBushRegistry.INSTANCE.onRegisterBlocks(blockRegistryEvent.getRegistry());
        }

        @SubscribeEvent
        public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
            OreBushRegistry.INSTANCE.onRegisterItems(event.getRegistry());
        }

        @SubscribeEvent
        public static void onRegisterRecipeSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            OreBushesConditions.onRegisterRecipeConditions();
        }

        @SubscribeEvent
        public static void onRegisterGlobalLootSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
            OreBushesLoot.onRegisterGlobalLoot(event);
        }
    }

    // endregion
}
