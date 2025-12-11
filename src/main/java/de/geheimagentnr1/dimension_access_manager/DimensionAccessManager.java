package de.geheimagentnr1.dimension_access_manager;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModAttachmentTypes;
import de.geheimagentnr1.dimension_access_manager.elements.commands.ModArgumentTypesRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.elements.commands.ModCommandsRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.handlers.DimensionAccessHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;


@Mod( DimensionAccessManager.MODID )
public class DimensionAccessManager {
	
	
	@NotNull
	public static final String MODID = "dimension_access_manager";
	
	public DimensionAccessManager( @NotNull IEventBus modEventBus, @NotNull ModContainer modContainer ) {
		
		ServerConfig serverConfig = new ServerConfig();
		modContainer.registerConfig( ModConfig.Type.SERVER, serverConfig.getSpec() );
		
		// Register DeferredRegisters
		ModAttachmentTypes.ATTACHMENT_TYPES.register( modEventBus );
		ModArgumentTypesRegisterFactory.ARGUMENT_TYPES.register( modEventBus );
		
		// Register event handlers on NeoForge event bus
		ModCommandsRegisterFactory commandsRegisterFactory = new ModCommandsRegisterFactory( serverConfig );
		NeoForge.EVENT_BUS.register( commandsRegisterFactory );
		
		DimensionAccessHandler dimensionAccessHandler = new DimensionAccessHandler();
		NeoForge.EVENT_BUS.register( dimensionAccessHandler );
	}
}
