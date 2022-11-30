package net.mindshake.securityforfabric.entity;

import net.mindshake.securityforfabric.entity.goal.BasicTurretAttackGoal;
import net.mindshake.securityforfabric.entity.goal.TurretTargetGoal;
import net.mindshake.securityforfabric.entity.projectile.TurretBulletEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TurretEntity extends MobEntity implements IAnimatable, RangedAttackMob {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final BasicTurretAttackGoal<TurretEntity> turretAttackGoal =  new BasicTurretAttackGoal<TurretEntity>(this, 0, 10, 15.0f);

    private static final TrackedData<String> OWNER_UUID;
    private static final TrackedData<Boolean> IS_ACTIVE;

    static {
        OWNER_UUID = DataTracker.registerData(TurretEntity.class, TrackedDataHandlerRegistry.STRING);

        IS_ACTIVE = DataTracker.registerData(TurretEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    /*-----------------------------------SETUP-------------------------------*/

    public TurretEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isAffectedBySplashPotions() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    public float getEffectiveExplosionResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, float max) {
        return 99999;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 360000.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 99f);
    }

    /*-----------------------------------AI-------------------------------*/
    @Override
    protected void initGoals() {
        this.targetSelector.add(2, new TurretTargetGoal<>((MobEntity)this, HostileEntity.class, false));

    }

    @Override
    public void lookAtEntity(Entity targetEntity, float maxYawChange, float maxPitchChange) {
        if (isActive())
            super.lookAtEntity(targetEntity, maxYawChange, maxPitchChange);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        PersistentProjectileEntity projectileEntity = this.createProjectile(this, world);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - projectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        projectileEntity.setVelocity(d, e + g * (double)0.2f, f, 1.6f, 0);
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(projectileEntity);
        if (world.isClient())
            spawnParticles(7);
    }

    private PersistentProjectileEntity createProjectile (LivingEntity shooter, World world) {
        return new TurretBulletEntity(world, shooter);
    }

    /*protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
    }*/

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() != null) {
            lookAt(this.getCommandSource().getEntityAnchor(), getTarget().getPos());
        }
    }

    private void spawnParticles(int amount) {
        int i = 0x262626;
        if (i == -1 || amount <= 0) {
            return;
        }
        double d = (double)(i >> 16 & 0xFF) / 255.0;
        double e = (double)(i >> 8 & 0xFF) / 255.0;
        double f = (double)(i >> 0 & 0xFF) / 255.0;
        for (int j = 0; j < amount; ++j) {
            this.world.addParticle(ParticleTypes.SMOKE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), d, e, f);
        }
    }

    @Override
    public boolean canTakeDamage() {
        return false;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        onInteract(player);
        return ActionResult.success(world.isClient());
    }

    public void onInteract (PlayerEntity player) {
        if (!world.isClient()) {
            if (isOwner(player)) {
                setActive(!isActive());
                if (isActive()) {
                    this.goalSelector.add(0,turretAttackGoal);
                } else {
                    this.goalSelector.remove(turretAttackGoal);
                }
            } else {
                player.sendMessage(MutableText.of(new TranslatableTextContent("message.securityforfabric.cannot_use")), true);
            }
        }
    }

    public boolean isOwner (PlayerEntity player) {
        return player.getUuidAsString().equals(this.getOwnerUUID());
    }

    @Override
    protected void pushAway(Entity entity) { }

    /*---------------------------------PROPERTIES----------------------------*/

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return true;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    /*------------------------------------RENDER-----------------------------------------*/

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController<?> controller = event.getController();
        if (isActive()) {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.turret.start", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            return PlayState.CONTINUE;
        } else {
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.turret.stop", ILoopType.EDefaultLoopTypes.PLAY_ONCE).addAnimation("animation.turret.stopped", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<TurretEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    /*------------------------------------DATA-----------------------------------------*/

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(OWNER_UUID, "");
        this.dataTracker.startTracking(IS_ACTIVE, false);
        super.initDataTracker();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("ownerUUID", dataTracker.get(OWNER_UUID));
        nbt.putBoolean("isActive", dataTracker.get(IS_ACTIVE));
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        dataTracker.set(OWNER_UUID, nbt.getString("ownerUUID"));
        dataTracker.set(IS_ACTIVE, nbt.getBoolean("isActive"));
        super.readNbt(nbt);
    }


    public String getOwnerUUID() {
        return dataTracker.get(OWNER_UUID);
    }

    public void setOwnerUUID(String ownerUUID) {
        dataTracker.set(OWNER_UUID, ownerUUID);
    }

    public boolean isActive() {
        return dataTracker.get(IS_ACTIVE);
    }

    public void setActive(boolean active) {
        dataTracker.set(IS_ACTIVE, active);
    }
}
