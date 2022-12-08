package net.mindshake.securityforfabric.block.entity;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.block.SensorBlock;
import net.mindshake.securityforfabric.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class SensorBlockEntity extends BlockEntity {

    private String ownerUUID = "";

    public SensorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SENSOR_BLOCKENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, SensorBlockEntity entity) {
        if (!world.isClient()) {
            if (!state.get(SensorBlock.ACTIVE)) {
                List<Entity> entities = world.getOtherEntities(null, new Box(pos.getX() - 4, pos.getY() - 4, pos.getZ() - 4, pos.getX() + 4, pos.getY() + 4, pos.getZ() + 4));
                if (entities.size() > 0 && !entity.ownerIsOnlyChecked(entities)) {
                    world.setBlockState(pos,state.cycle(SensorBlock.ACTIVE));
                }
            }
        }
    }

    private boolean ownerIsOnlyChecked (List<Entity> entities) {
        if (entities.size() == 1) {
            if (entities.get(0) instanceof PlayerEntity) {
                return ((PlayerEntity)entities.get(0)).getUuidAsString().equals(getOwnerUUID());
            }
        }
        return false;
    }

    /*********************DATA******************/
    @javax.annotation.Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setOwnerUUID(nbt.getString("ownerUUID"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("ownerUUID", getOwnerUUID());
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}
