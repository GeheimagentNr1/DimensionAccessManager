package de.geheimagentnr1.dimension_access_manager.config;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ServerConfig {
	
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String MOD_NAME = ModLoadingContext.get().getActiveContainer().getModInfo().getDisplayName();
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec CONFIG;
	
	private static final ForgeConfigSpec.EnumValue<DimensionAccessType> DEFAULT_DIMENSION_ACCESS_TYPE;
	
	static {
		
		DEFAULT_DIMENSION_ACCESS_TYPE = BUILDER
			.comment( "Defines if new dimensions are GRANTED or LOCKED by default." )
			.defineEnum( "default_dimension_access_type", DimensionAccessType.GRANTED );
		CONFIG = BUILDER.build();
	}
	
	public static void printConfig() {
		
		LOGGER.info( "Loading \"{}\" Config", MOD_NAME );
		LOGGER.info( "{} = {}", DEFAULT_DIMENSION_ACCESS_TYPE.getPath(), DEFAULT_DIMENSION_ACCESS_TYPE.get() );
		LOGGER.info( "\"{}\" Config loaded", MOD_NAME );
	}
	
	public static DimensionAccessType getDefaultDimensionAccessType() {
		
		return DEFAULT_DIMENSION_ACCESS_TYPE.get();
	}
	
	public static void setDefaultDimensionAccessType( DimensionAccessType _defaultDimensionAccessType ) {
		
		DEFAULT_DIMENSION_ACCESS_TYPE.set( _defaultDimensionAccessType );
	}
}
