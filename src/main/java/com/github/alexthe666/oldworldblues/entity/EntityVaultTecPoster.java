package com.github.alexthe666.oldworldblues.entity;

import com.github.alexthe666.oldworldblues.init.OWBItems;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityVaultTecPoster extends EntityHanging implements IEntityAdditionalSpawnData {
	public EntityVaultTecPoster.Variant variant;

	public EntityVaultTecPoster(World world) {
		super(world);
	}

	public EntityVaultTecPoster(World world, BlockPos pos, EnumFacing facing) {
		super(world, pos);
		List<EntityVaultTecPoster.Variant> validVariants = Lists.newArrayList();
		for (EntityVaultTecPoster.Variant variant : EntityVaultTecPoster.Variant.values()) {
			this.variant = variant;
			this.updateFacingWithBoundingBox(facing);
			if (this.onValidSurface()) {
				validVariants.add(variant);
			}
		}
		if (!validVariants.isEmpty()) {
			this.variant = validVariants.get(this.rand.nextInt(validVariants.size()));
		}
		this.updateFacingWithBoundingBox(facing);
	}

	@SideOnly(Side.CLIENT)
	public EntityVaultTecPoster(World world, BlockPos pos, EnumFacing facing, String title) {
		this(world, pos, facing);
		for (EntityVaultTecPoster.Variant variant : EntityVaultTecPoster.Variant.values()) {
			if (variant.title.equals(title)) {
				this.variant = variant;
				break;
			}
		}
		this.updateFacingWithBoundingBox(facing);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setString("Variant", this.variant.title);
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		String variantName = compound.getString("Variant");
		for (EntityVaultTecPoster.Variant variant : EntityVaultTecPoster.Variant.values()) {
			if (variant.title.equals(variantName)) {
				this.variant = variant;
			}
		}
		if (this.variant == null) {
			this.variant = Variant.VAULTFOREVER;
		}
		super.readEntityFromNBT(compound);
	}

	@Override
	public int getWidthPixels() {
		return this.variant.sizeX;
	}

	@Override
	public int getHeightPixels() {
		return this.variant.sizeY;
	}

	@Override
	public void onBroken(Entity brokenEntity) {
		if (this.world.getGameRules().getBoolean("doEntityDrops")) {
			this.playSound(SoundEvents.BLOCK_CLOTH_BREAK, 1.0F, 1.0F);
			if (brokenEntity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) brokenEntity;
				if (player.capabilities.isCreativeMode) {
					return;
				}
			}
			this.entityDropItem(new ItemStack(OWBItems.VAULT_TEC_POSTER), 0.0F);
		}
	}

	@Override
	public void playPlaceSound() {
		this.playSound(SoundEvents.BLOCK_CLOTH_BREAK, 1.0F, 1.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
	}

	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		BlockPos positionOffset = new BlockPos(x - this.posX, y - this.posY, z - this.posZ);
		BlockPos newPosition = this.hangingPosition.add(positionOffset);
		this.setPosition(newPosition.getX(), newPosition.getY(), newPosition.getZ());
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeByte(this.variant.ordinal());
		buffer.writeLong(this.hangingPosition.toLong());
		buffer.writeByte(this.facingDirection.getHorizontalIndex());
	}

	@Override
	public void readSpawnData(ByteBuf buffer) {
		this.variant = Variant.values()[buffer.readByte()];
		this.hangingPosition = BlockPos.fromLong(buffer.readLong());
		this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(buffer.readByte()));
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(OWBItems.VAULT_TEC_POSTER, 1);
	}

	public enum Variant {
		VAULTFOREVER("VaultForever", 16, 16, 0, 0),
		AMERICALIVESON("AmericaLivesOn", 16, 16, 16, 0),
		UNCLESAM("UncleSam", 16, 16, 32, 0),
		WEMUSTWIN("WeMustWin", 16, 16, 48, 0),
		PESTCONTROL("PestControl", 16, 16, 64, 0),
		HARDWORK("Social", 16, 32, 0, 64);
		public final String title;
		public final int sizeX;
		public final int sizeY;
		public final int offsetX;
		public final int offsetY;

		Variant(String title, int xSize, int ySize, int textureX, int textureY) {
			this.title = title;
			this.sizeX = xSize;
			this.sizeY = ySize;
			this.offsetX = textureX;
			this.offsetY = textureY;
		}
	}
}