package net.mindshake.securityforfabric.block.entity;

import net.mindshake.securityforfabric.block.FanBlock;
import net.mindshake.securityforfabric.entity.TurretEntity;
import net.mindshake.securityforfabric.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FanBlockEntity extends BlockEntity implements IAnimatable {

    private final AnimationFactory manager = new AnimationFactory(this);
    private List<Entity> inBoxEntities = new ArrayList<>();

    public FanBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FAN_BLOCKENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, FanBlockEntity entity) {
        if (!state.get(FanBlock.POWERED))
            return;

        Direction facing = state.get(FanBlock.FACING);
        Vec3d dir = new Vec3d(0,0,0);
        Box fanBox = new Box(0,0,0,0,0,0);

        switch (facing) {
            case UP -> {
                dir = new Vec3d(0,0.1f,0);
                fanBox = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 6, pos.getZ() + 1);
            }
            case SOUTH -> {
                dir = new Vec3d(0,0f,0.1);
                fanBox = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 6);
            }
            case NORTH -> {
                dir = new Vec3d(0,0f,-0.1);
                fanBox = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() - 6);
            }
            case EAST -> {
                dir = new Vec3d(0.1f,0f,0);
                fanBox = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 6, pos.getY() + 1, pos.getZ() + 1);
            }
            case WEST -> {
                dir = new Vec3d(-0.1f,0f,0);
                fanBox = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() - 6, pos.getY() + 1, pos.getZ() + 1);
            }
        }


        for (int i = 0; i < entity.getInBoxEntities().size(); i++) {
            if (!(entity.getInBoxEntities().get(i) instanceof TurretEntity)) {
                if (fanBox.contains(entity.getInBoxEntities().get(i).getPos())) {
                    entity.getInBoxEntities().get(i).move(MovementType.SELF, dir);
                    if (!entity.getInBoxEntities().get(i).hasNoGravity() && facing.equals(Direction.UP))
                        entity.getInBoxEntities().get(i).setNoGravity(true);
                } else {
                    if (entity.getInBoxEntities().get(i).hasNoGravity() && facing.equals(Direction.UP))
                        entity.getInBoxEntities().get(i).setNoGravity(false);
                }
            }
        }

        entity.setInBoxEntities(world.getOtherEntities(null, fanBox));
    }

    public List<Entity> getInBoxEntities() {
        return inBoxEntities;
    }

    public void setInBoxEntities(List<Entity> inBoxEntities) {
        this.inBoxEntities = inBoxEntities;
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!getCachedState().get(FanBlock.POWERED)) {
            return PlayState.STOP;
        }
        AnimationController<?> controller = event.getController();
        controller.transitionLengthTicks = 0;
        controller.setAnimation(new AnimationBuilder().addAnimation("animation.fan.work", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(
                new AnimationController<FanBlockEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return manager;
    }
}
