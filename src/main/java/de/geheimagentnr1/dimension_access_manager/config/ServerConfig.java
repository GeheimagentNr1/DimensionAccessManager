package de.geheimagentnr1.dimension_access_manager.config;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ServerConfig {
	
	
	private static final Logger LOGGER = LogManager.getLogger( ServerConfig.class );
	
	private static final String MOD_NAME = ModLoadingContext.get().getActiveContainer().getModInfo().getDisplayName();
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec CONFIG;
	
	private static final ForgeConfigSpec.EnumValue<DimensionAccessType> DEFAULT_DIMENSION_ACCESS_TYPE;
	
	private static final ForgeConfigSpec.IntValue DIMENSION_COMMAND_PERMISSION_LEVEL;
	
	static {
		
		DEFAULT_DIMENSION_ACCESS_TYPE = BUILDER
			.comment( "Defines if new dimensions are GRANTED or LOCKED by default." )
			.defineEnum( "default_dimension_access_type", DimensionAccessType.GRANTED );
		DIMENSION_COMMAND_PERMISSION_LEVEL = BUILDER
			.comment( "Defines which permission level is required to run the /dimension command" )
			.defineInRange( "dimension_command_permmsion_level", 3, 0, 4 );
		CONFIG = BUILDER.build();
	}
	
	public static void printConfig() {
		
		LOGGER.info( "Loading \"{}\" Server Config", MOD_NAME );
		LOGGER.info( "{} = {}", DEFAULT_DIMENSION_ACCESS_TYPE.getPath(), DEFAULT_DIMENSION_ACCESS_TYPE.get() );
		LOGGER.info( "{} = {}", DIMENSION_COMMAND_PERMISSION_LEVEL.getPath(), DIMENSION_COMMAND_PERMISSION_LEVEL.get() );
		LOGGER.info( "\"{}\" Server Config loaded", MOD_NAME );
	}
	
	public static DimensionAccessType getDefaultDimensionAccessType() {
		
		return DEFAULT_DIMENSION_ACCESS_TYPE.get();
	}
	
	public static void setDefaultDimensionAccessType( DimensionAccessType _defaultDimensionAccessType ) {
		
		DEFAULT_DIMENSION_ACCESS_TYPE.set( _defaultDimensionAccessType );
	}
	
	public static Integer getDimensionCommandPermissionLevel() {
		
		return DIMENSION_COMMAND_PERMISSION_LEVEL.get();
	}
}
