package de.geheimagentnr1.dimension_access_manager.handlers;

import de.geheimagentnr1.dimension_access_manager.elements.commands.DimensionsStatusCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionManageCommand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.FORGE )
public class ForgeRegistryEventManager {
	
	
	@SubscribeEvent
	public static void handlerRegisterCommandsEvent( RegisterCommandsEvent event ) {
		
		DimensionManageCommand.register( event.getDispatcher() );
		DimensionsStatusCommand.register( event.getDispatcher() );
	}
}
