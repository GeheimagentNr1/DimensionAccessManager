package de.geheimagentnr1.dimension_access_manager.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.util.TextHelper;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;


public class ModConfig {
	
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String mod_name = "Dimension Access Manager";
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	private static ForgeConfigSpec CONFIG;
	
	private static final HashMap<RegistryKey<World>, ForgeConfigSpec.BooleanValue> DIMENSION_ACCESSES =
		new HashMap<>();
	
	
	public static void initConfig( MinecraftServer server ) {
		
		BUILDER.comment( "Option for every dimension if the access for the dimension is granted or not:" );
		BUILDER.push( "dimensions" );
		server.getWorlds().forEach( serverWorld -> DIMENSION_ACCESSES.put( serverWorld.func_234923_W_(),
			BUILDER.define( TextHelper.dimensionTypeToName( serverWorld ), true ) ) );
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
	
	public static void load() {
		
		CommentedFileConfig configData = CommentedFileConfig.builder( FMLPaths.CONFIGDIR.get().resolve(
			DimensionAccessManager.MODID + ".toml" ) ).sync().autosave().writingMode( WritingMode.REPLACE ).build();
		
		LOGGER.info( "Loading \"{}\" Config", mod_name );
		configData.load();
		CONFIG.setConfig( configData );
		DIMENSION_ACCESSES.forEach( ( dimension, access ) ->
			LOGGER.info( "{} = {}", TextHelper.dimensionTypeToName( dimension ), access.get() ) );
		LOGGER.info( "\"{}\" Config loaded", mod_name );
	}
	
	public static HashMap<RegistryKey<World>, ForgeConfigSpec.BooleanValue> getAccessMap() {
		
		return DIMENSION_ACCESSES;
	}
	
	public static void setAccess( RegistryKey<World> dimension, boolean granted ) {
		
		DIMENSION_ACCESSES.get( dimension ).set( granted );
	}
	
	public static boolean isAllowedDimision( RegistryKey<World> dimension ) {
		
		return DIMENSION_ACCESSES.get( dimension ).get();
	}
}
