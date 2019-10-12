package edivad.waterresources.proxy;

import com.google.common.util.concurrent.ListenableFuture;

import edivad.waterresources.Main;
import edivad.waterresources.ModBlocks;
import edivad.waterresources.ModItems;
import edivad.waterresources.ModLiquids;
import edivad.waterresources.network.Messages;
import edivad.waterresources.worldgen.OreGenerator;
import edivad.waterresources.worldgen.WorldTickHandler;
import edivad.waterresources.worldgen.structures.WorldGenCustomStructures;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e)
	{
		Messages.registerMessages(Main.MODID);
		MinecraftForge.EVENT_BUS.register(EventHandler.instance);
		GameRegistry.registerWorldGenerator(OreGenerator.instance, 5);
		MinecraftForge.EVENT_BUS.register(OreGenerator.instance);
		GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);

		ModLiquids.init();
	}

	public void init(FMLInitializationEvent e)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(WorldTickHandler.instance);
	}

	public void postInit(FMLPostInitializationEvent e)
	{

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		ModBlocks.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ModItems.register(event.getRegistry());
	}

	public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule)
	{
		throw new IllegalStateException("This should only be called from client side");
	}

	public EntityPlayer getClientPlayer()
	{
		throw new IllegalStateException("This should only be called from client side");
	}

	@SubscribeEvent
	public void OnConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(Main.MODID))
			ConfigManager.sync(Main.MODID, Config.Type.INSTANCE);
	}
}
