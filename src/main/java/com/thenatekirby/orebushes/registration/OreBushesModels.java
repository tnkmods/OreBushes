package com.thenatekirby.orebushes.registration;

import com.google.common.base.Stopwatch;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.block.OreBushBlock;
import com.thenatekirby.orebushes.registry.OreBushRegistry;
import com.thenatekirby.orebushes.util.model.MutableItemModel;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("unused")
public class OreBushesModels {
    private static final ResourceLocation BLOCK_ATLAS = new ResourceLocation("minecraft", "textures/atlas/blocks.png");

    private static final ResourceLocation BERRY_BLANK = OreBushes.MOD.withPath("item/berry_blank");
    private static final ResourceLocation BERRY_OVERLAY = OreBushes.MOD.withPath("item/berry_overlay");
    private static final ResourceLocation SEED_BLANK = OreBushes.MOD.withPath("item/seed_blank");
    private static final ResourceLocation GROUNDS_BLANK = OreBushes.MOD.withPath("item/grounds_blank");

    private static final Multimap<ResourceLocation, ResourceLocation> BUSH_MODELS = LinkedHashMultimap.create();

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public void onRegisterModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(BERRY_BLANK);
        ModelLoader.addSpecialModel(SEED_BLANK);
        ModelLoader.addSpecialModel(GROUNDS_BLANK);

        OreBushRegistry.INSTANCE.getAllOreBushes().forEach(oreBush -> {
            oreBush.getBushBlock().ifPresent(block -> {
                block.getStateContainer().getValidStates().forEach(state -> {
                    String propString = BlockModelShapes.getPropertyMapString(state.getValues());
                    ModelResourceLocation defaultLocation = new ModelResourceLocation(OreBushes.MOD_ID + ":berry_bush_blank", propString);
                    ModelLoader.addSpecialModel(defaultLocation);
                    BUSH_MODELS.put(defaultLocation, new ModelResourceLocation(block.getRegistryName(), propString));
                });
            });
        });
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        Stopwatch timer = Stopwatch.createStarted();

        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
        ModelBakery bakery = event.getModelLoader();
        IBakedModel modelMissing = registry.get(ModelLoader.MODEL_MISSING);

        BUSH_MODELS.asMap().forEach((id, resourceLocations) -> {
            IBakedModel defaultModel = registry.getOrDefault(id, modelMissing);
            if (defaultModel.equals(modelMissing)) {
                OreBushes.getLogger().info("Missing Model For {}", id);
            }

            resourceLocations.forEach(modelLocation -> registry.put(modelLocation, defaultModel));
        });

        IUnbakedModel berryModel = bakery.getUnbakedModel(BERRY_BLANK);
        MutableItemModel berryModelWrapper = new MutableItemModel((BlockModel) berryModel);
        ItemModelGenerator generator = new ItemModelGenerator();

        IUnbakedModel seedModel = bakery.getUnbakedModel(SEED_BLANK);
        MutableItemModel seedModelWrapper = new MutableItemModel((BlockModel) seedModel);

        OreBushRegistry.INSTANCE.getAllOreBushes().forEach(oreBush -> {
            if (!oreBush.isEnabled()) {
                return;
            }

            oreBush.getBerryItem().ifPresent(berry -> {
                Item berryItem = berry.asItem();
                ResourceLocation berryId = berryItem.getRegistryName();

                if (berryId != null) {
                    ModelResourceLocation modelResourceLocation = new ModelResourceLocation(berryId, "inventory");
                    IBakedModel existingModel = registry.get(berryId);

                    if (existingModel == null) {
                        MutableItemModel mutableModel = berryModelWrapper
                                .withRetexturedLayer("layer0", BERRY_BLANK.toString())
                                .withRetexturedLayer("layer1", BERRY_OVERLAY.toString());

                        BlockModel generated = generator.makeItemModel(bakery.getSpriteMap()::getSprite, mutableModel);
                        IBakedModel bakedModel = generated.bakeModel(bakery, generated, bakery.getSpriteMap()::getSprite, ModelRotation.X0_Y0, modelResourceLocation, false);
                        registry.replace(modelResourceLocation, bakedModel);
                    }
                }
            });

            oreBush.getSeedItem().ifPresent(seed -> {
                Item seedItem = seed.asItem();
                ResourceLocation seedId = seedItem.getRegistryName();

                if (seedId != null) {
                    ModelResourceLocation modelResourceLocation = new ModelResourceLocation(seedId, "inventory");
                    IBakedModel existingModel = registry.get(seedId);

                    if (existingModel == null) {
                        MutableItemModel mutableModel = seedModelWrapper
                                .withRetexturedLayer("layer0", SEED_BLANK.toString());

                        BlockModel generated = generator.makeItemModel(bakery.getSpriteMap()::getSprite, mutableModel);
                        IBakedModel bakedModel = generated.bakeModel(bakery, generated, bakery.getSpriteMap()::getSprite, ModelRotation.X0_Y0, modelResourceLocation, false);
                        registry.replace(modelResourceLocation, bakedModel);
                    }
                }
            });

            oreBush.getGroundsItem().ifPresent(mulch -> {
                Item mulchItem = mulch.asItem();
                ResourceLocation mulchId = mulchItem.getRegistryName();

                if (mulchId != null) {
                    ModelResourceLocation modelResourceLocation = new ModelResourceLocation(mulchId, "inventory");
                    IBakedModel existingModel = registry.get(mulchId);

                    if (existingModel == null) {
                        MutableItemModel mutableModel = seedModelWrapper
                                .withRetexturedLayer("layer0", GROUNDS_BLANK.toString());

                        BlockModel generated = generator.makeItemModel(bakery.getSpriteMap()::getSprite, mutableModel);
                        IBakedModel bakedModel = generated.bakeModel(bakery, generated, bakery.getSpriteMap()::getSprite, ModelRotation.X0_Y0, modelResourceLocation, false);
                        registry.replace(modelResourceLocation, bakedModel);
                    }
                }
            });
        });

        OreBushes.getLogger().info("OreBushes generated models in {} ms", timer.elapsed(TimeUnit.MILLISECONDS));
        timer.stop();
    }

    @SubscribeEvent
    public void onTextureStich(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(BLOCK_ATLAS)) {
            event.addSprite(BERRY_BLANK);
            event.addSprite(SEED_BLANK);
            event.addSprite(GROUNDS_BLANK);
        }
    }

    @SubscribeEvent
    public void onHandleColors(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();

        OreBushRegistry.INSTANCE.getAllOreBushes().forEach(oreBush -> {
            if (oreBush.isEnabled()) {
                oreBush.getBerryItem().ifPresent(provider -> itemColors.register((stack, tint) -> tint == 1 ? oreBush.getMaterial().getInfo().getColor() : -1, provider.asItem()));
                oreBush.getSeedItem().ifPresent(provider -> itemColors.register((stack, tint) -> oreBush.getMaterial().getInfo().getColor(), provider.asItem()));
                oreBush.getGroundsItem().ifPresent(provider -> itemColors.register((stack, tint) -> oreBush.getMaterial().getInfo().getColor(), provider.asItem()));
            }
        });
    }

    @SubscribeEvent
    public void onHandleColors(ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();

        OreBushRegistry.INSTANCE.getAllOreBushes().forEach(oreBush -> {
            if (oreBush.isEnabled()) {
                oreBush.getBushBlock().ifPresent(provider -> blockColors.register(OreBushBlock::getBlockColor, provider.getBlock()));
            }
        });
    }

    public static void onClientSetup() {
        OreBushRegistry.INSTANCE.getAllOreBushes().forEach(oreBush -> {
            if (oreBush.isEnabled()) {
                oreBush.getBushBlock().ifPresent(block -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutoutMipped()));
            }
        });
    }
}
