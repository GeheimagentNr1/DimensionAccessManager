package de.geheimagentnr1.dimension_access_manager.handlers;

import de.geheimagentnr1.dimension_access_manager.config.ModConfig;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.FORGE )
public class DimensionChangeHandler {
	
	
	@SubscribeEvent
	public static void handleTravelToDimension( EntityTravelToDimensionEvent event ) {
		
		if( !ModConfig.isAllowedDimision( event.getDimension() ) ) {
			event.setResult( Event.Result.DENY );
			event.setCanceled( true );
		}
	}
}
