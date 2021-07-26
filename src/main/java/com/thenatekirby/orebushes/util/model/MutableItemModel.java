package com.thenatekirby.orebushes.util.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

// ====---------------------------------------------------------------------------====
// TODO: Move To Babel

public class MutableItemModel extends BlockModel {
    private final BlockModel model;

    @SuppressWarnings("deprecation")
    public MutableItemModel(BlockModel model) {
        super(model.getParentLocation(), model.getElements(), model.textureMap, model.hasAmbientOcclusion, model.getGuiLight(), model.getTransforms(), model.getOverrides());
        this.model = model;
        this.name = model.name;
        this.parent = model.parent;
    }

    @SuppressWarnings("deprecation")
    public MutableItemModel withRetexturedLayer(String key, String value) {
        BlockModel newModel = new BlockModel(
                this.model.getParentLocation(),
                new ArrayList<>(),
                Maps.newHashMap(this.model.textureMap),
                this.model.hasAmbientOcclusion(),
                this.model.getGuiLight(),
                this.model.getTransforms(),
                Lists.newArrayList(this.model.getOverrides())
        );
        newModel.name = this.model.name;
        newModel.parent = this.model.parent;

        newModel.textureMap.put(key, Either.left(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(value))));
        return new MutableItemModel(newModel);
    }
}
