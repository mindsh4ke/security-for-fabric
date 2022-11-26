package net.mindshake.securityforfabric.block.entity;

import net.mindshake.securityforfabric.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class SpikesBlockEntity extends BlockEntity implements IAnimatable {

    private int hurtTick = 0;
    public boolean hasTarget = false;
    private AnimationBuilder closeAnimation = new AnimationBuilder();
    private AnimationBuilder openAnimation = new AnimationBuilder();
    private AnimationBuilder holdAnimation = new AnimationBuilder();
    private final AnimationFactory manager = GeckoLibUtil.createFactory(this);

    public SpikesBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPIKES_BLOCKENTITY, pos, state);
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController<?> controller = event.getController();
        controller.transitionLengthTicks = 0;
        if (hasTarget) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.spikes.open", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.spikes.hold", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.spikes.close", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.spikes.closed", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    public static void tick(World world, BlockPos pos, BlockState state, SpikesBlockEntity entity) {
        List<Entity> entities = world.getOtherEntities(null, new Box(pos.getX(), pos.getY(), pos.getZ(),pos.getX()+1, pos.getY()+0.5, pos.getZ()+1));
        entity.hasTarget = entities.size() > 0;
        if (entity.hasTarget) {
            entity.hurtTick++;
            if (entity.hurtTick > 18) {
                for (int i = 0; i < entities.size(); i++) {
                    entities.get(i).damage(DamageSource.GENERIC, 4);
                }
                entity.hurtTick = 0;
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<SpikesBlockEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.manager;
    }
}
