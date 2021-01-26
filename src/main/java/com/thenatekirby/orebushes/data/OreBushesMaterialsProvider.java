package com.thenatekirby.orebushes.data;

import com.thenatekirby.orebushes.OreBushes;
import com.thenatekirby.orebushes.data.bushes.OreBushData;
import com.thenatekirby.orebushes.util.datagen.IFinishedMaterial;
import com.thenatekirby.orebushes.util.datagen.OreBushMaterialBuilder;
import net.minecraft.data.DataGenerator;

import java.util.function.Consumer;

// ====---------------------------------------------------------------------------====

public class OreBushesMaterialsProvider extends com.thenatekirby.orebushes.util.datagen.OreBushesMaterialsProvider {
    OreBushesMaterialsProvider(DataGenerator generator) {
        super(OreBushes.MOD_ID, generator);
    }

    @Override
    public void registerMaterials(Consumer<IFinishedMaterial> consumer) {
        for (OreBushData data : OreBushData.all()) {
            if (data.getRequiredMods().isEmpty()) {
                registerMaterial(data, consumer);
            } else {
                for (String modId: data.getRequiredMods()) {
                    registerMaterialWithMod(data, modId, consumer);
                }
            }
        }
    }

    private void registerMaterial(OreBushData data, Consumer<IFinishedMaterial> consumer) {
        String name = data.getName();

        OreBushMaterialBuilder.builder(data.getMaterialId())
                .withName(name.substring(0, 1).toUpperCase() + name.substring(1))
                .withType(data.getType())
                .withRarity(data.getRarity())
                .withTier(data.getTier())
                .withColor(data.getColor())
                .isGrindable(data.isGrindable())
                .build(consumer);
    }

    private void registerMaterialWithMod(OreBushData data, String modId, Consumer<IFinishedMaterial> consumer) {
        String name = data.getName();

        OreBushMaterialBuilder.builder(data.getMaterialId())
                .withName(name.substring(0, 1).toUpperCase() + name.substring(1))
                .withType(data.getType())
                .withRarity(data.getRarity())
                .withTier(data.getTier())
                .withColor(data.getColor())
                .isGrindable(data.isGrindable())
                .withRequiredMod(modId)
                .build(consumer, OreBushes.MOD.withPath(modId + "/" + data.getMaterialId().getPath()));
    }
}
