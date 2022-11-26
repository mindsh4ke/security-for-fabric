package net.mindshake.securityforfabric.item;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.entity.WalkingMineEntity;
import net.mindshake.securityforfabric.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WalkingMineSpawner extends Item {


    public WalkingMineSpawner(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos);
        context.getPlayerFacing();
        BlockPos blockPos2 = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);

        WalkingMineEntity entity = ModEntities.WALKING_MINE.create(world);
        float yaw = 0;
        Main.LOGGER.info(context.getPlayerFacing().asString());
        switch (context.getPlayerFacing()) {
            case NORTH:
                entity.setDirection(0);
                break;
            case SOUTH:
                entity.setDirection(1);
                break;
            case EAST:
                entity.setDirection(2);
                break;
            case WEST:
                entity.setDirection(3);
                break;
        }
        Main.LOGGER.info(entity.getDirection() +  "dddir");
        world.spawnEntity(entity);
        entity.updatePosition(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());

        itemStack.decrement(1);
        world.emitGameEvent((Entity)context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);

        return ActionResult.success(world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.securityforfabric.walking_mine_item_0"));
            tooltip.add(new TranslatableText("tooltip.securityforfabric.walking_mine_item_1"));
        } else {
            tooltip.add(new TranslatableText("tooltip.securityforfabric.shift_for_more_info"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
