package net.mindshake.securityforfabric.block;

import net.mindshake.securityforfabric.entity.TurretEntity;
import net.mindshake.securityforfabric.registry.ModBlocks;
import net.mindshake.securityforfabric.registry.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class TurretBlock extends Block {
    public TurretBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        assert placer != null;
        createTurretEntity(world, (PlayerEntity) placer, pos);
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        Objects.requireNonNull(getTurretEntity(world, pos)).remove(Entity.RemovalReason.DISCARDED);
        if (!player.isCreative())
            dropStack(world, pos, new ItemStack(ModBlocks.TURRET_BLOCK));
        super.onBreak(world, pos, state, player);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        TurretEntity entity = getTurretEntity(world, pos);
        if (!player.isSneaking()) {
            if (entity == null)
                entity = createTurretEntity(world, player, pos);
            Objects.requireNonNull(entity).onInteract(player);
        } else {
            if (Objects.requireNonNull(entity).isOwner(player)) {
                this.onBreak(world, pos, state, player);
                world.breakBlock(pos, true, player);
            } else {
                player.sendMessage(new TranslatableText("message.securityforfabric.cannot_break"), true);
            }
        }
        return ActionResult.success(world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.securityforfabric.turret_0"));
            tooltip.add(new TranslatableText("tooltip.securityforfabric.sneak_to_break"));
        } else {
            tooltip.add(new TranslatableText("tooltip.securityforfabric.shift_for_more_info"));
        }
        super.appendTooltip(stack, world, tooltip, options);
    }

    private TurretEntity getTurretEntity (World world, BlockPos pos) {
        List<Entity> entities = world.getOtherEntities(null, new Box(pos.getX(), pos.getY(), pos.getZ(),pos.getX()+1, pos.getY()+2, pos.getZ()+1));
        for (Entity entity : entities) {
            if (entity instanceof TurretEntity)
                return (TurretEntity) entity;
        }
        return null;
    }

    private TurretEntity createTurretEntity (World world, PlayerEntity placer, BlockPos pos) {
        TurretEntity entity = ModEntities.TURRET.create(world);
        entity.setOwnerUUID(placer.getUuidAsString());
        entity.updatePosition(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f);
        world.spawnEntity(entity);
        return entity;
    }
}
