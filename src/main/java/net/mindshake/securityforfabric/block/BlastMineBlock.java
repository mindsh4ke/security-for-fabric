package net.mindshake.securityforfabric.block;

import net.mindshake.securityforfabric.block.entity.BlastMineBlockEntity;
import net.mindshake.securityforfabric.block.entity.SensorBlockEntity;
import net.mindshake.securityforfabric.block.entity.SpikesBlockEntity;
import net.mindshake.securityforfabric.registry.ModBlockEntities;
import net.mindshake.securityforfabric.registry.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.explosion.Explosion;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BlastMineBlock extends BlockWithEntity {

    private BlockPos pos;
    private boolean isFullBlock;

    public BlastMineBlock(Settings settings, boolean isFullBlock) {
        super(settings);
        this.isFullBlock = isFullBlock;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        this.pos = pos;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient)
            explodeBasic((ServerWorld) world, pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
            explodeBasic((ServerWorld) world, pos);
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public static void explodeBasic(ServerWorld world, BlockPos pos) {
        if (!world.isClient) {
            Explosion.DestructionType destructionType = Explosion.DestructionType.BREAK;
            float f = 2f;
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), (float)3f * f, destructionType);
            world.breakBlock(pos, false);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.getBlockState(pos.down()).isOf(Blocks.AIR) || isFullBlock;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            explodeBasic(world, pos);
            world.breakBlock(pos, false);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.createAndScheduleBlockTick(pos, this, 1);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (isFullBlock) {
            return VoxelShapes.fullCube();
        }
        return Block.createCuboidShape(4,0,4,12,2,12);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlastMineBlockEntity(pos,state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.BLAST_MINE_BLOCKENTITY, BlastMineBlockEntity::tick);
    }
}
