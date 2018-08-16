package com.github.alexthe666.oldworldblues.client;

import com.github.alexthe666.oldworldblues.CommonProxy;
import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoorFrame;
import com.github.alexthe666.oldworldblues.block.BlockVaultDoor;
import com.github.alexthe666.oldworldblues.block.BlockVaultDoorFrame;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityLocker;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityOWBStorage;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import com.github.alexthe666.oldworldblues.client.gui.GuiVATSCombat;
import com.github.alexthe666.oldworldblues.client.gui.GuiOWBStorage;
import com.github.alexthe666.oldworldblues.client.model.ModelVaultJumpsuit;
import com.github.alexthe666.oldworldblues.client.model.ModelVaultSecurityArmor;
import com.github.alexthe666.oldworldblues.client.render.entity.RenderSalesmanVillager;
import com.github.alexthe666.oldworldblues.client.render.entity.RenderSeat;
import com.github.alexthe666.oldworldblues.client.render.entity.RenderVaultTecPoster;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderLocker;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderVaultDoor;
import com.github.alexthe666.oldworldblues.client.render.item.OWBTEISR;
import com.github.alexthe666.oldworldblues.entity.EntitySalesmanVillager;
import com.github.alexthe666.oldworldblues.entity.EntitySeat;
import com.github.alexthe666.oldworldblues.entity.EntityVaultTecPoster;
import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import com.github.alexthe666.oldworldblues.event.VATSClientEvents;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import com.github.alexthe666.oldworldblues.item.ISpecialItemRender;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Random;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @SideOnly(Side.CLIENT)
    private static final OWBTEISR TEISR = new OWBTEISR();
    private static final ModelBiped VAULT_JUMPSUIT_MODEL = new ModelVaultJumpsuit(0.2F, false);
    private static final ModelBiped VAULT_JUMPSUIT_MODEL_LEGS = new ModelVaultJumpsuit(0.15F, false);
    private static final ModelBiped VAULT_JUMPSUIT_MODEL_SMALLARMS = new ModelVaultJumpsuit(0.2F, true);
    private static final ModelBiped VAULT_JUMPSUIT_MODEL_LEGS_SMALLARMS = new ModelVaultJumpsuit(0.15F, true);

    private static final ModelBiped VAULT_SECURITY_MODEL = new ModelVaultSecurityArmor(0.2F, false);
    private static final ModelBiped VAULT_SECURITY_MODEL_LEGS = new ModelVaultSecurityArmor(0.15F, false);
    private static final ModelBiped VAULT_SECURITY_MODEL_SMALLARMS = new ModelVaultSecurityArmor(0.2F, true);
    private static final ModelBiped VAULT_SECURITY_MODEL_LEGS_SMALLARMS = new ModelVaultSecurityArmor(0.15F, true);
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(OWBBlocks.VAULT_DOOR, (new StateMap.Builder()).ignore(BlockVaultDoor.FACING).build());
        ModelLoader.setCustomStateMapper(OWBBlocks.VAULT_DOOR_FRAME, (new StateMap.Builder()).ignore(BlockVaultDoorFrame.FACING).build());
        ModelLoader.setCustomStateMapper(OWBBlocks.INTERIOR_VAULT_DOOR, (new StateMap.Builder()).ignore(BlockInteriorVaultDoor.FACING).build());
        ModelLoader.setCustomStateMapper(OWBBlocks.INTERIOR_VAULT_DOOR_FRAME, (new StateMap.Builder()).ignore(BlockInteriorVaultDoorFrame.FACING).build());
        ModelLoader.setCustomModelResourceLocation(OWBItems.PIPBOY3000IV, 0, new ModelResourceLocation("oldworldblues:pipboy3000iv", "inventory"));
        ModelBakery.registerItemVariants(OWBItems.NUMBER_DECAL, new ResourceLocation("oldworldblues:number_decal_0"), new ResourceLocation("oldworldblues:number_decal_1"), new ResourceLocation("oldworldblues:number_decal_2"),
                new ResourceLocation("oldworldblues:number_decal_3"), new ResourceLocation("oldworldblues:number_decal_4"), new ResourceLocation("oldworldblues:number_decal_5"), new ResourceLocation("oldworldblues:number_decal_6"),
                new ResourceLocation("oldworldblues:number_decal_7"), new ResourceLocation("oldworldblues:number_decal_8"), new ResourceLocation("oldworldblues:number_decal_9"));

        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 0, new ModelResourceLocation("oldworldblues:number_decal_0", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 1, new ModelResourceLocation("oldworldblues:number_decal_1", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 2, new ModelResourceLocation("oldworldblues:number_decal_2", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 3, new ModelResourceLocation("oldworldblues:number_decal_3", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 4, new ModelResourceLocation("oldworldblues:number_decal_4", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 5, new ModelResourceLocation("oldworldblues:number_decal_5", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 6, new ModelResourceLocation("oldworldblues:number_decal_6", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 7, new ModelResourceLocation("oldworldblues:number_decal_7", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 8, new ModelResourceLocation("oldworldblues:number_decal_8", "inventory"));
        ModelLoader.setCustomModelResourceLocation(OWBItems.NUMBER_DECAL, 9, new ModelResourceLocation("oldworldblues:number_decal_9", "inventory"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVaultDoor.class, new RenderVaultDoor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInteriorVaultDoor.class, new RenderInteriorVaultDoor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLocker.class, new RenderLocker());
        try {
            for (Field f : OWBBlocks.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block && Item.getItemFromBlock((Block)obj) != null && !(obj instanceof ISpecialItemRender)) {
                    String name = ((Block)obj).getUnlocalizedName().substring(19);
                    System.out.println(name);
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block)obj), 0, new ModelResourceLocation("oldworldblues:" + name, "inventory"));

                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            for (Field f : OWBItems.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item && !(obj instanceof ISpecialItemRender)) {
                    String name = ((Item)obj).getUnlocalizedName().substring(19);
                    System.out.println(name);
                    ModelLoader.setCustomModelResourceLocation((Item)obj, 0, new ModelResourceLocation("oldworldblues:" + name, "inventory"));

                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void preInit(){
        MinecraftForge.EVENT_BUS.register(new VATSClientEvents());
    }

    public void init(){
        RenderingRegistry.registerEntityRenderingHandler(EntitySalesmanVillager.class, new RenderSalesmanVillager(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityVaultTecPoster.class, new RenderVaultTecPoster(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntitySeat.class, new RenderSeat(Minecraft.getMinecraft().getRenderManager()));
    }

    public void postInit(){
        VATSClientEvents.initializeVATSRenderLayer();
        OWBItems.PIPBOY3000IV.setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(OWBBlocks.VAULT_DOOR).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(OWBBlocks.INTERIOR_VAULT_DOOR).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(OWBBlocks.LOCKER_BOTTOM).setTileEntityItemStackRenderer(TEISR);
    }

    public void openVatsCombatGui(){
        Minecraft.getMinecraft().displayGuiScreen(new GuiVATSCombat());
    }

    Random random = new Random();
    public void updateClientViewPoint() {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(Minecraft.getMinecraft().player, VATSProperties.class);
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 1 + random.nextInt(2);
    }

    public Object getArmorModel(Entity entity, int index, int type) {
        int newIndex = index + type;
        if(index == 0 || index == 1){
            if(entity instanceof AbstractClientPlayer){
                if(isPlayerSlimArms((AbstractClientPlayer)entity)){
                    newIndex += 2;
                }
            }
        }
        switch (newIndex) {
            case 0:
                return VAULT_JUMPSUIT_MODEL_LEGS;
            case 1:
                return VAULT_JUMPSUIT_MODEL;
            case 2:
                return VAULT_JUMPSUIT_MODEL_LEGS_SMALLARMS;
            case 3:
                return VAULT_JUMPSUIT_MODEL_SMALLARMS;
            case 4:
                return VAULT_SECURITY_MODEL_LEGS;
            case 5:
                return VAULT_SECURITY_MODEL;
            case 6:
                return VAULT_SECURITY_MODEL_LEGS_SMALLARMS;
            case 7:
                return VAULT_SECURITY_MODEL_SMALLARMS;
        }
        return null;
    }

    public String getVaultJumpsuitTexture(Entity entity, int renderIndex, int type){
        String armor = type == 1 ? "security" : "jumpsuit";
        if(entity instanceof AbstractClientPlayer){
            if(isPlayerSlimArms((AbstractClientPlayer)entity)){
                return "oldworldblues:textures/models/armor/vault_" + armor + "_" + (renderIndex == 2 ? "1_slim.png" : "0_slim.png");
            }
        }
        return "oldworldblues:textures/models/armor/vault_" + armor + "_" + (renderIndex == 2 ? "1.png" : "0.png");
    }

    private boolean isPlayerSlimArms(AbstractClientPlayer player){
        RenderPlayer slimRender = Minecraft.getMinecraft().getRenderManager().getSkinMap().get("slim");
        RenderPlayer renderplayer = (RenderPlayer)Minecraft.getMinecraft().getRenderManager().<AbstractClientPlayer>getEntityRenderObject(player);
        return renderplayer.equals(slimRender) ;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity entity = world.getTileEntity(pos);
        if (id == GUI_OWB_STORAGE && entity instanceof TileEntityOWBStorage) {
            return new GuiOWBStorage(player.inventory, (TileEntityOWBStorage) entity);
        }
        return null;
    }
}
