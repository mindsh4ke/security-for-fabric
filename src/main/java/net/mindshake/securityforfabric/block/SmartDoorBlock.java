package net.mindshake.securityforfabric.block;

import net.mindshake.securityforfabric.block.entity.SmartDoorBlockEntity;
import net.mindshake.securityforfabric.registry.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmartDoorBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);

    public SmartDoorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER).with(OPEN, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        boolean bl = !state.get(OPEN);
        switch (direction) {
            default: {
                return bl ? WEST_SHAPE : NORTH_SHAPE;
            }
            case SOUTH: {
                return bl ? NORTH_SHAPE :EAST_SHAPE;
            }
            case WEST: {
                return bl ? EAST_SHAPE : SOUTH_SHAPE;
            }
            case NORTH:
        }
        return bl ? SOUTH_SHAPE : WEST_SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            if (neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf) {
                return state.with(FACING, neighborState.get(FACING)).with(OPEN, neighborState.get(OPEN));
            }
            return Blocks.AIR.getDefaultState();
        }
        if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            onBreakInCreative(world, pos, state, player);
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING, OPEN);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        if (Screen.hasShiftDown()) {
            tooltip.add(MutableText.of(new TranslatableTextContent("tooltip.securityforfabric.smart_door_0")));
            tooltip.add(MutableText.of(new TranslatableTextContent("tooltip.securityforfabric.smart_door_1")));
            tooltip.add(MutableText.of(new TranslatableTextContent("tooltip.securityforfabric.sneak_to_break")));
        } else {
            tooltip.add(MutableText.of(new TranslatableTextContent("tooltip.securityforfabric.shift_for_more_info")));
        }
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        switch (type) {
            case LAND: {
                return state.get(OPEN);
            }
            case WATER: {
                return false;
            }
            case AIR: {
                return state.get(OPEN);
            }
        }
        return false;
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            boolean bl = world.isReceivingRedstonePower(blockPos) || world.isReceivingRedstonePower(blockPos.up());
            return this.getDefaultState().with(FACING, ctx.getPlayerFacing()).with(OPEN, bl).with(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), (BlockState)state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
        PlayerEntity player = (PlayerEntity)placer;
        ((SmartDoorBlockEntity)world.getBlockEntity(pos)).setOwnerUUID(player.getUuidAsString());
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState;
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER && (blockState = world.getBlockState(blockPos = pos.down())).isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
            BlockState blockState2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED) != false ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        SmartDoorBlockEntity blockEntity;
        if (state.get(HALF).equals(DoubleBlockHalf.UPPER))
            blockEntity = ((SmartDoorBlockEntity) world.getBlockEntity(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())));
        else
            blockEntity = ((SmartDoorBlockEntity) world.getBlockEntity(pos));

        if (!player.isSneaking()) {
            //Interact when not sneaking
            if (blockEntity.getOwnerUUID().equals(player.getUuidAsString())) {
                state = (BlockState) state.cycle(OPEN);
                world.syncWorldEvent(player, state.get(OPEN) ? this.getOpenSoundEventId() : this.getCloseSoundEventId(), pos, 0);
                world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
                world.emitGameEvent((Entity) player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                return ActionResult.success(world.isClient);
            } else if (!blockEntity.getOwnerUUID().equals(player.getUuidAsString()) && !blockEntity.getOwnerUUID().equals("")) {
                player.sendMessage(MutableText.of(new TranslatableTextContent("message.securityforfabric.cannot_use")), true);
            }
            return ActionResult.success(world.isClient);
        } else {
            //Break when sneaking
            if (blockEntity.getOwnerUUID().equals(player.getUuidAsString())) {
                if (!player.isCreative())
                    dropStack(world, pos, new ItemStack(ModBlocks.SMART_DOOR));
                world.breakBlock(pos, true);
            } else {
                player.sendMessage(MutableText.of(new TranslatableTextContent("message.securityforfabric.cannot_break")), true);
            }
        }

        return ActionResult.success(world.isClient);
    }

    public boolean isOpen(BlockState state) {
        return state.get(OPEN);
    }

    private void playOpenCloseSound(World world, BlockPos pos, boolean open) {
        world.syncWorldEvent(null, open ? this.getOpenSoundEventId() : this.getCloseSoundEventId(), pos, 0);
    }

    private int getCloseSoundEventId() {
        return WorldEvents.IRON_DOOR_CLOSES;
    }

    private int getOpenSoundEventId() {
        return WorldEvents.IRON_DOOR_OPENS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean isOpen = state.get(OPEN);
        if (!this.getDefaultState().isOf(block)) {
            if (isOpen) {
                this.playOpenCloseSound(world, pos, isOpen);
                world.emitGameEvent(null, isOpen ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
            world.setBlockState(pos, (BlockState)((BlockState)state).with(OPEN, isOpen), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return blockState.isSideSolidFullSquare(world, blockPos, Direction.UP);
        }
        return blockState.isOf(this);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (state.get(HALF).equals(DoubleBlockHalf.UPPER)) {
            return null;
        }
        return new SmartDoorBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
