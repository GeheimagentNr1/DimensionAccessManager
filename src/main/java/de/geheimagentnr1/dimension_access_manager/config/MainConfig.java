package de.geheimagentnr1.dimension_access_manager.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.util.TextHelper;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;


public class MainConfig {
	
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String mod_name = "Dimension Access Manager";
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	@SuppressWarnings( "StaticNonFinalField" )
	public static ForgeConfigSpec CONFIG;
	
	private static final HashMap<DimensionType, ForgeConfigSpec.BooleanValue> DIMENSION_ACCESSES = new HashMap<>();
	
	
	public static void initConfig() {
		
		BUILDER.comment( "Option for every dimension if the access for the dimension is granted or not:" );
		BUILDER.push( "dimensions" );
		DimensionType.getAll().forEach( dimensionType -> DIMENSION_ACCESSES.put( dimensionType,
			BUILDER.define( TextHelper.dimensionTypeToName( dimensionType ), true ) ) );
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
	
	public static void printConfig() {
		
		CommentedFileConfig configData = CommentedFileConfig.builder( FMLPaths.CONFIGDIR.get().resolve(
			DimensionAccessManager.MODID + ".toml" ) ).sync().autosave().writingMode( WritingMode.REPLACE ).build();
		
		LOGGER.info( "Loading \"{}\" Config", mod_name );
		configData.load();
		CONFIG.setConfig( configData );
		DIMENSION_ACCESSES.forEach( ( dimension, access ) ->
			LOGGER.info( "{} = {}", TextHelper.dimensionTypeToName( dimension ), access.get() ) );
		LOGGER.info( "\"{}\" Config loaded", mod_name );
	}
	
	public static HashMap<DimensionType, ForgeConfigSpec.BooleanValue> getAccessMap() {
		
		return DIMENSION_ACCESSES;
	}
	
	public static void setAccess( DimensionType dimension, boolean granted ) {
		
		ForgeConfigSpec.BooleanValue spec = DIMENSION_ACCESSES.get( dimension );
		if( spec != null ) {
			spec.set( granted );
		}
	}
	
	public static boolean isAllowedDimision( DimensionType dimension ) {
		
		ForgeConfigSpec.BooleanValue spec = DIMENSION_ACCESSES.get( dimension );
		if( spec == null ) {
			return true;
		} else {
			return spec.get();
		}
	}
}
