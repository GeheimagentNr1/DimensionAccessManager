package de.geheimagentnr1.dimension_access_manager.handlers;

import de.geheimagentnr1.dimension_access_manager.config.ModConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.DimensionManageCommand;
import de.geheimagentnr1.dimension_access_manager.elements.commands.DimensionsStatusCommand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.FORGE )
public class ForgeRegistryEventManager {
	
	
	@SubscribeEvent
	public static void handlerServerStartEvent( FMLServerStartingEvent event ) {
		
		ModConfig.initConfig( event.getServer() );
		ModConfig.load();
		DimensionManageCommand.register( event.getCommandDispatcher() );
		DimensionsStatusCommand.register( event.getCommandDispatcher() );
	}
}
