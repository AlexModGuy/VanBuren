package com.github.alexthe666.oldworldblues.client.render.item;

import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderLocker;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class OWBTEISR extends TileEntityItemStackRenderer {

    public static final RenderPipBoy3000IV RENDER_PIP_BOY_3000_IV = new RenderPipBoy3000IV();
    public static final RenderVaultDoor RENDER_VAULT_DOOR = new RenderVaultDoor();
    public static final RenderInteriorVaultDoor RENDER_INTERIOR_VAULT_DOOR = new RenderInteriorVaultDoor();
    public static final RenderLocker RENDER_LOCKER = new RenderLocker();

    public void renderByItem(ItemStack itemStackIn) {
        if(itemStackIn.getItem() == OWBItems.PIPBOY3000IV){
            RENDER_PIP_BOY_3000_IV.renderItem(itemStackIn);
        }
        if(itemStackIn.getItem() == Item.getItemFromBlock(OWBBlocks.VAULT_DOOR)){
            GL11.glScalef(0.325F, 0.325F, 0.325F);
            RENDER_VAULT_DOOR.renderVaultDoor(null, 180, -2.3, -0.8, 2, false);
            if (itemStackIn.getTagCompound() != null && itemStackIn.getTagCompound().getInteger("Number") < 1000) {
                RENDER_VAULT_DOOR.renderVaultDoorNumber(null, itemStackIn.getTagCompound().getInteger("Number"), 180, -2.3, -0.8, 2);
            }
        }
        if(itemStackIn.getItem() == Item.getItemFromBlock(OWBBlocks.INTERIOR_VAULT_DOOR)){
            GL11.glScalef(0.325F, 0.325F, 0.325F);
            RENDER_INTERIOR_VAULT_DOOR.render(null, -1D, 1, 2, 0, 0, 0);
        }
        if(itemStackIn.getItem() == Item.getItemFromBlock(OWBBlocks.LOCKER_BOTTOM)){
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            RENDER_LOCKER.render(null, 0.75F, -0.4F, 0, 0, 0, 0);
        }
    }
}
