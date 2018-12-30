package com.github.alexthe666.oldworldblues.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TeleporterWasteland extends Teleporter {
    public World world;
    public BlockPos pos;

    public TeleporterWasteland(WorldServer world, BlockPos pos) {
        super(world);
        this.world = world;
        this.pos = pos;
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
        this.placeInPortal(entity, pos.getX(), pos.getY(), pos.getZ(), entity.rotationYaw);
        return false;
    }

    public void placeInPortal(Entity entity, double x, double y, double z, float yaw) {
        BlockPos pos = this.world.getTopSolidOrLiquidBlock(new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z)));
        entity.setLocationAndAngles(0.5F, 90.5F, 0.5F, yaw, 0);
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
        return false;
    }
}
