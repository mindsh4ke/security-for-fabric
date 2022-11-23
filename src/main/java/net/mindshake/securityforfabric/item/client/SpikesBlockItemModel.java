package net.mindshake.securityforfabric.item.client;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.item.SpikesBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SpikesBlockItemModel extends AnimatedGeoModel<SpikesBlockItem> {
    @Override
    public Identifier getModelResource(SpikesBlockItem object) {
        return new Identifier(Main.MODID, "geo/spikes.geo.json");
    }

    @Override
    public Identifier getTextureResource(SpikesBlockItem object) {
        return new Identifier(Main.MODID, "textures/block/spikes.png");
    }

    @Override
    public Identifier getAnimationResource(SpikesBlockItem animatable) {
        return new Identifier(Main.MODID, "animations/spikes.animation.json");
    }
}
