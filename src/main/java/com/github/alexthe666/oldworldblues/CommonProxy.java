package com.github.alexthe666.oldworldblues;

import com.github.alexthe666.oldworldblues.block.BlockGenericSlab;
import com.github.alexthe666.oldworldblues.block.BlockVaultDoor;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityOWBStorage;
import com.github.alexthe666.oldworldblues.entity.EntitySalesmanVillager;
import com.github.alexthe666.oldworldblues.entity.EntitySeat;
import com.github.alexthe666.oldworldblues.entity.EntityVaultTecPoster;
import com.github.alexthe666.oldworldblues.init.*;
import com.github.alexthe666.oldworldblues.inventory.ContainerOWBStorage;
import com.github.alexthe666.oldworldblues.item.ItemBlockVaultDoor;
import com.github.alexthe666.oldworldblues.recipe.RecipeNumberDecal;
import com.github.alexthe666.oldworldblues.recipe.RecipeVaultDoorNumber;
import com.github.alexthe666.oldworldblues.recipe.RecipeVaultJumpsuitNumber;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class CommonProxy implements IGuiHandler {
    public static final int GUI_OWB_STORAGE = 1;

    public void preInit() {

    }

    public void init() {
    }

    public void postInit() {
        ActionPointMappings.INSTANCE.registerActionType(new ItemStack(Items.BOW), ActionPointMappings.ActionType.BOW);
        ActionPointMappings.INSTANCE.registerAP(new ItemStack(Items.BOW), 40);
    }


    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IRecipe recipeNumberDecal = new RecipeNumberDecal().setRegistryName(new ResourceLocation("oldworldblues:number_decal_recipe"));
        IRecipe recipeVaultDoorNumber = new RecipeVaultDoorNumber().setRegistryName(new ResourceLocation("oldworldblues:vault_door_number_recipe"));
        IRecipe recipeVaultJumpsuitNumber = new RecipeVaultJumpsuitNumber().setRegistryName(new ResourceLocation("oldworldblues:vault_jumpsuit_number"));
        event.getRegistry().registerAll(recipeNumberDecal, recipeVaultDoorNumber, recipeVaultJumpsuitNumber);
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
       event.getRegistry().registerAll(OWBWorld.WASTELAND_PLAINS_BIOME, OWBWorld.WASTELAND_DESERT_BIOME, OWBWorld.WASTELAND_RIVER_BIOME);
    }

    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                OWBSounds.ENTER_VATS,
                OWBSounds.LEAVE_VATS,
                OWBSounds.VATS_SELECT_TARGET,
                OWBSounds.VATS_DESELECT_TARGET,
                OWBSounds.VATS_CHANGE_TARGET,
                OWBSounds.VATS_CRITICAL,
                OWBSounds.VATS_CRITICAL_BAR_FULL
        );
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        registerSpawnable(EntityEntryBuilder.<EntitySalesmanVillager>create(), event, EntitySalesmanVillager.class, "salesman_villager", 1, 0XB69222, 0X933313);
        registerUnspawnable(EntityEntryBuilder.<EntityVaultTecPoster>create(), event, EntityVaultTecPoster.class, "vault_tec_poster", 2);
        registerUnspawnable(EntityEntryBuilder.<EntityVaultTecPoster>create(), event, EntitySeat.class, "owb_seat", 3);

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : OWBItems.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item) {
                    event.getRegistry().register((Item) obj);
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        event.getRegistry().register(item);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : OWBBlocks.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : OWBBlocks.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof BlockVaultDoor) {
                    ItemBlockVaultDoor itemBlock = new ItemBlockVaultDoor((Block) obj);
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof BlockGenericSlab) {
                    ItemBlock itemBlock = ((BlockGenericSlab)obj).getItemBlock();
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block) {
                    ItemBlock itemBlock = new ItemBlock((Block) obj);
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        ItemBlock itemBlock = new ItemBlock(block);
                        itemBlock.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static void registerSpawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int mainColor, int subColor) {
        builder.entity(entityClass);
        builder.id(new ResourceLocation(OldWorldBlues.MODID, name), id);
        builder.name(name);
        builder.egg(mainColor, subColor);
        builder.tracker(64, 1, true);
        event.getRegistry().register(builder.build());
    }

    public static void registerUnspawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id) {
        builder.entity(entityClass);
        builder.id(new ResourceLocation(OldWorldBlues.MODID, name), id);
        builder.name(name);
        builder.tracker(64, 1, true);
        event.getRegistry().register(builder.build());
    }

    public void openVatsCombatGui() {

    }

    public void updateClientViewPoint() {

    }

    public Object getArmorModel(Entity entity, int index, int type) {
        return null;
    }

    public String getVaultJumpsuitTexture(Entity entity, int renderIndex, int type) {
        return "";
    }


    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity entity = world.getTileEntity(pos);
        if (id == GUI_OWB_STORAGE && entity instanceof TileEntityOWBStorage) {
            return new ContainerOWBStorage(player.inventory, (TileEntityOWBStorage) entity, player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }


}