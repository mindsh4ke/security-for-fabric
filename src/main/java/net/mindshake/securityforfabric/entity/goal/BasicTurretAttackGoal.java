package net.mindshake.securityforfabric.entity.goal;

import net.mindshake.securityforfabric.entity.TurretEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;

import java.util.EnumSet;

public class BasicTurretAttackGoal <T extends MobEntity> extends Goal {
    private final T actor;
    private int attackInterval;
    private int combatTicks = -1;

    public BasicTurretAttackGoal(T actor, double speed, int attackInterval, float range) {
        this.actor = actor;
        this.attackInterval = attackInterval;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public void setAttackInterval(int attackInterval) {
        this.attackInterval = attackInterval;
    }

    @Override
    public boolean canStart() {
        if (((MobEntity)this.actor).getTarget() == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return (this.canStart() || !((MobEntity)this.actor).getNavigation().isIdle());
    }

    @Override
    public void start() {
        super.start();
        ((MobEntity)this.actor).setAttacking(true);
    }

    @Override
    public void stop() {
        super.stop();
        ((MobEntity)this.actor).setAttacking(false);
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = ((MobEntity)this.actor).getTarget();

        this.combatTicks++;

        ((MobEntity)this.actor).lookAtEntity(livingEntity, 360f, 360.0f);
        if (combatTicks >= attackInterval) {
            combatTicks = 0;
            ((RangedAttackMob)this.actor).attack(livingEntity, 3f);
        }
    }
}