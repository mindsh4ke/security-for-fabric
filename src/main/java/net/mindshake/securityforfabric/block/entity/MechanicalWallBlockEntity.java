package net.mindshake.securityforfabric.block.entity;

import net.mindshake.securityforfabric.block.MechanicalWallBlock;
import net.mindshake.securityforfabric.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MechanicalWallBlockEntity extends BlockEntity implements IAnimatable {

    private final AnimationFactory manager = new AnimationFactory(this);

    public MechanicalWallBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MECHANICAL_WALL_BLOCKENTITY, pos, state);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<MechanicalWallBlockEntity>(this, "controller", 0, this::predicate));
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController<?> controller = event.getController();
        if (this.getCachedState().get(MechanicalWallBlock.POWERED)) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.mechanical_wall.open", false).addAnimation("animation.mechanical_wall.opened", true));
        }
        else {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.mechanical_wall.close", false).addAnimation("animation.mechanical_wall.closed", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.manager;
    }
}
