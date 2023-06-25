package de.geheimagentnr1.dimension_access_manager;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilitiesRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.elements.commands.ModArgumentTypesRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.elements.commands.ModCommandsRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.handlers.DimensionAccessHandler;
import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod( DimensionAccessManager.MODID )
public class DimensionAccessManager extends AbstractMod {
	
	
	@NotNull
	static final String MODID = "dimension_access_manager";
	
	@NotNull
	@Override
	public String getModId() {
		
		return MODID;
	}
	
	@Override
	protected void initMod() {
		
		ServerConfig serverConfig = registerConfig( ServerConfig::new );
		registerEventHandler( new ModCapabilitiesRegisterFactory( this, serverConfig ) );
		registerEventHandler( new ModArgumentTypesRegisterFactory() );
		registerEventHandler( new ModCommandsRegisterFactory( serverConfig ) );
		registerEventHandler( new DimensionAccessHandler() );
	}
}
