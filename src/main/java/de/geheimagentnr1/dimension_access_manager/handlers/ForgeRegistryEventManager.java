package de.geheimagentnr1.dimension_access_manager.handlers;

import de.geheimagentnr1.dimension_access_manager.config.MainConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.DimensionsStatusCommand;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionManageCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.FORGE )
public class ForgeRegistryEventManager {
	
	
	@SubscribeEvent
	public static void handlerServerStartEvent( FMLServerStartingEvent event ) {
		
		MainConfig.checkAndPrintConfig();
	}
	
	@SubscribeEvent
	public static void handlerRegisterCommandsEvent( RegisterCommandsEvent event ) {
		
		DimensionManageCommand.register( event.getDispatcher() );
		DimensionsStatusCommand.register( event.getDispatcher() );
	}
}
