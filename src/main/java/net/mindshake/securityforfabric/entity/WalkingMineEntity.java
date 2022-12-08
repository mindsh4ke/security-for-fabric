package net.mindshake.securityforfabric.entity;

import net.mindshake.securityforfabric.block.BlastMineBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class WalkingMineEntity extends MobEntity implements IAnimatable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final TrackedData<Integer> DIRECTION;

    static  {
        DIRECTION = DataTracker.registerData(WalkingMineEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    private int ticksAmount = 0;

    /*-----------------------------------SETUP-------------------------------*/

    public WalkingMineEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }


    public static DefaultAttributeContainer.Builder setAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 99f);
    }

    @Override
    protected void onKilledBy(@Nullable LivingEntity adversary) {
        explode();
        super.onKilledBy(adversary);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        switch (getDirection()) {
            case 0 -> {
                setYaw(180);
            }
            case 1 -> {
                setYaw(0);
            }
            case 2 -> {
                setYaw(270);
            }
            case 3 -> {
                setYaw(90);
            }
        }

        this.applyMovementInput(new Vec3d(0,0,1), 0f);
        ticksAmount++;
        if (ticksAmount % 20 == 0)  {
            if (!getWorld().isClient())
                getWorld().playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 1,1,true);
        }
        if (ticksAmount > 200) {
            explode();
        }
    }

    private void explode () {
        this.remove(RemovalReason.KILLED);
        if (!getWorld().isClient())
            BlastMineBlock.explodeBasic((ServerWorld) getWorld(), new BlockPos(getBlockPos()));
    }

    /*------------------------------------RENDER-----------------------------------------*/

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController<?> controller = event.getController();
        controller.setAnimation(new AnimationBuilder().addAnimation("animation.walking_mine.move", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<WalkingMineEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(DIRECTION, 0);
        super.initDataTracker();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setDirection(nbt.getInt("direction"));
        this.ticksAmount = nbt.getInt("ticksAmount");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("direction", getDirection());
        nbt.putInt("ticksAmount", this.ticksAmount);
        return super.writeNbt(nbt);
    }

    public int getDirection() {
        return dataTracker.get(DIRECTION);
    }

    public void setDirection(int direction) {
        dataTracker.set(DIRECTION, direction);
    }
}
