package com.github.alexthe666.oldworldblues;

import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import com.github.alexthe666.oldworldblues.event.VATSServerEvents;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import com.github.alexthe666.oldworldblues.init.OWBVillagers;
import com.github.alexthe666.oldworldblues.keybinding.OWBKeybinds;
import com.github.alexthe666.oldworldblues.message.MessageAddTargetToMap;
import com.github.alexthe666.oldworldblues.message.MessageTriggerVats;
import com.github.alexthe666.oldworldblues.message.MessageUseAP;
import com.github.alexthe666.oldworldblues.message.MessageVatsTarget;
import com.github.alexthe666.oldworldblues.structure.OWBWorldGenerator;
import net.ilexiconn.llibrary.server.config.Config;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = OldWorldBlues.MODID, dependencies = "required-after:llibrary@[" + OldWorldBlues.LLIBRARY_VERSION + ",)", version = OldWorldBlues.VERSION)
public class OldWorldBlues
{
    public static final String MODID = "oldworldblues";
    public static final String VERSION = "ALPHA";
    public static final String LLIBRARY_VERSION = "1.7.9";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    @SidedProxy(clientSide = "com.github.alexthe666.oldworldblues.client.ClientProxy", serverSide = "com.github.alexthe666.oldworldblues.CommonProxy")
    public static CommonProxy PROXY;
    @NetworkWrapper({MessageTriggerVats.class, MessageVatsTarget.class, MessageUseAP.class, MessageAddTargetToMap.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    @SuppressWarnings("deprecation")
    @Config
    public static OWBConfig CONFIG;
    public static CreativeTabs TAB;
    @Mod.Instance(MODID)
    public static OldWorldBlues INSTANCE;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        TAB = new CreativeTabs("oldworldblues") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(OWBItems.PIPBOY3000IV);
            }
        };
        NetworkRegistry.INSTANCE.registerGuiHandler(OldWorldBlues.INSTANCE, PROXY);
        PROXY.preInit();
        MinecraftForge.EVENT_BUS.register(new VATSServerEvents());
        GameRegistry.registerWorldGenerator(new OWBWorldGenerator(), 0);
        MinecraftForge.EVENT_BUS.register(PROXY);
        OWBVillagers.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        PROXY.init();
        EntityPropertiesHandler.INSTANCE.registerProperties(VATSProperties.class);
        OWBKeybinds.register();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        PROXY.postInit();

    }
}
