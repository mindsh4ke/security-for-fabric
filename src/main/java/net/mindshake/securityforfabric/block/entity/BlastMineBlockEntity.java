package net.mindshake.securityforfabric.block.entity;

import net.mindshake.securityforfabric.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

public class BlastMineBlockEntity extends BlockEntity {
    public BlastMineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLAST_MINE_BLOCKENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, BlastMineBlockEntity entity) {
        if (!world.isClient) {
            List<Entity> entities = world.getOtherEntities(null, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1.1, pos.getZ() + 1));
            if (entities.size() > 0) {
                explode((ServerWorld) world, pos);
            }
        }
    }

    public static void explode(ServerWorld world, BlockPos pos) {
        Explosion.DestructionType destructionType = Explosion.DestructionType.BREAK;
        float f = 2f;
        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), (float)3f * f, destructionType);
        world.breakBlock(pos, false);
    }
}
