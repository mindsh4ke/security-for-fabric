package net.mindshake.securityforfabric.block;

import net.mindshake.securityforfabric.block.entity.SmartChestBlockEntity;
import net.mindshake.securityforfabric.registry.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmartChestBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public SmartChestBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        SmartChestBlockEntity blockEntity = ((SmartChestBlockEntity)world.getBlockEntity(pos));
        boolean isOwner = blockEntity.getOwnerUUID().equals(player.getUuidAsString());

        if (player.isSneaking() && isOwner) {
            if (!player.isCreative())
                dropStack(world, pos, new ItemStack(ModBlocks.SMART_CHEST));
            world.breakBlock(pos, true);
        } else if (player.isSneaking() && !isOwner) {
            player.sendMessage(MutableText.of(new TranslatableTextContent("message.securityforfabric.cannot_break")), true);
        } else if (isOwner) {
            if (!world.isClient()) {
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

                if (screenHandlerFactory != null) {
                    //With this call the server will request the client to open the appropriate Screenhandler
                    player.openHandledScreen(screenHandlerFactory);
                }
            }
        } else {
            player.sendMessage(MutableText.of(new TranslatableTextContent("message.securityforfabric.cannot_use")), true);
        }

        return ActionResult.success(world.isClient());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        PlayerEntity player = (PlayerEntity)placer;
        ((SmartChestBlockEntity)world.getBlockEntity(pos)).setOwnerUUID(player.getUuidAsString());
        ((SmartChestBlockEntity)world.getBlockEntity(pos)).setOwnerName(player.getDisplayName().getString());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SmartChestBlockEntity(pos, state);
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
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SmartChestBlockEntity) {
                ItemScatterer.spawn(world, pos, (SmartChestBlockEntity)blockEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
