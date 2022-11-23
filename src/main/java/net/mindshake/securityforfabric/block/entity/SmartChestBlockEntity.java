package net.mindshake.securityforfabric.block.entity;

import net.mindshake.securityforfabric.registry.ModBlockEntities;
import net.mindshake.securityforfabric.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SmartChestBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

    private String ownerUUID, ownerName;

    public SmartChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMART_CHEST_BLOCKENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return MutableText.of(new TranslatableTextContent("title.securityforfabric.smart_chest", getOwnerName()));
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        playSound(world, pos, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN);
        return GenericContainerScreenHandler.createGeneric9x3(syncId, inv, this);
    }

    static void playSound(World world, BlockPos pos, SoundEvent soundEvent) {
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getY() + 0.5;
        double f = (double)pos.getZ() + 0.5;
        world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        setOwnerName(nbt.getString("ownerName"));
        setOwnerUUID(nbt.getString("ownerUUID"));
        Inventories.readNbt(nbt, this.inventory);
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString("ownerName", getOwnerName());
        nbt.putString("ownerUUID", getOwnerUUID());
        Inventories.writeNbt(nbt,this.inventory);
        super.writeNbt(nbt);
    }

    @javax.annotation.Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
