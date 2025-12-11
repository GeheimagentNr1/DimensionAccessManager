package de.geheimagentnr1.dimension_access_manager.elements.commands;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionCommand;
import lombok.RequiredArgsConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;


@RequiredArgsConstructor
public class ModCommandsRegisterFactory {
	
	
	@NotNull
	private final ServerConfig serverConfig;
	
	@SubscribeEvent
	public void onRegisterCommands( @NotNull RegisterCommandsEvent event ) {
		
		event.getDispatcher().register( new DimensionCommand( serverConfig ).build() );
		event.getDispatcher().register( new DimensionsCommand( serverConfig ).build() );
	}
}
