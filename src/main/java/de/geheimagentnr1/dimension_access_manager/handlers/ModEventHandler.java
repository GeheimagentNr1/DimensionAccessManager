package de.geheimagentnr1.dimension_access_manager.handlers;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapabilityStorage;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapabilityStorage;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapabilityStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleCommonSetupEvent( FMLCommonSetupEvent event ) {
		
		CapabilityManager.INSTANCE.register(
			DimensionAccessCapability.class,
			new DimensionAccessCapabilityStorage(),
			DimensionAccessCapability::new
		);
		CapabilityManager.INSTANCE.register(
			DimensionAccessBlacklistCapability.class,
			new DimensionAccessBlacklistCapabilityStorage(),
			DimensionAccessBlacklistCapability::new
		);
		CapabilityManager.INSTANCE.register(
			DimensionAccessWhitelistCapability.class,
			new DimensionAccessWhitelistCapabilityStorage(),
			DimensionAccessWhitelistCapability::new
		);
	}
	
	@SubscribeEvent
	public static void handleModConfigLoadingEvent( ModConfig.Loading event ) {
		
		ServerConfig.printConfig();
	}
	
	@SubscribeEvent
	public static void handleModConfigReloadingEvent( ModConfig.Reloading event ) {
		
		ServerConfig.printConfig();
	}
}
