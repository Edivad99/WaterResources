package edivad.waterresources;

import org.apache.logging.log4j.Logger;

import edivad.waterresources.proxy.CommonProxy;
import edivad.waterresources.tabs.WaterResourcesTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.MODVERSION, acceptedMinecraftVersions = Main.MCVERSION, dependencies = "required-after:forge@[14.23.5.2847,);required-after:thermalfoundation@[2.6.3,);required-after:thermalexpansion@[5.5.4,)", updateJSON = Main.UPDATE_URL, useMetadata = true)
public class Main {

	public static final String MODID = "waterresources";
	public static final String MODNAME = "Water Resources";
	public static final String MCVERSION = "1.12.2";
	public static final String MODVERSION = "1.0.0";
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/Edivad99/mod-version-controll/master/waterresources_update.json";
	public static final String CLIENT_PROXY_CLASS = "edivad.waterresources.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "edivad.waterresources.proxy.ServerProxy";

	@SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static final CreativeTabs waterResourcesTab = new WaterResourcesTab("waterresourcestab");

	public Main()
	{
		FluidRegistry.enableUniversalBucket();
	}

	@Mod.Instance
	public static Main instance;

	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		proxy.postInit(e);
	}
}
