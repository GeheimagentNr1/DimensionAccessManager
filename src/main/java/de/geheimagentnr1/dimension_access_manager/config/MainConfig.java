package de.geheimagentnr1.dimension_access_manager.config;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;


@SuppressWarnings( "SynchronizationOnStaticField" )
public class MainConfig {
	
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String mod_name = "Dimension Access Manager";
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec CONFIG;
	
	private static final ForgeConfigSpec.ConfigValue<List<String>> DIMENSIONS;
	
	private static final ForgeConfigSpec.EnumValue<DimensionListType> DIMENSION_LIST_TYPE;
	
	private static final TreeSet<RegistryKey<World>> dimensions = new TreeSet<>();
	
	static {
		
		DIMENSIONS = BUILDER.comment( "If dimension_list_type is set to LOCK_LIST, the list is the list of " +
			"dimensions, which are locked" + System.lineSeparator() +
			"If dimension_list_type is set to GRANT_LIST, the list is the list of dimensions, which are granted." )
			.define( "dimensions", Collections.emptyList(), o -> {
				if( o instanceof List<?> ) {
					List<?> list = (List<?>)o;
					return list.isEmpty() || list.get( 0 ) instanceof String;
				}
				return false;
			} );
		DIMENSION_LIST_TYPE = BUILDER.comment( "If dimension_list_type is set to LOCK_LIST, the dimension list is" +
			" the list of dimensions in which are locked." + System.lineSeparator() +
			"If dimension_list_type is set to GRANT_LIST, the dimension list is the list of dimensions which are " +
			"granted." )
			.defineEnum( "dimension_list_type", DimensionListType.LOCK_LIST );
		CONFIG = BUILDER.build();
	}
	
	private static void printConfig() {
		
		LOGGER.info( "{} = {}", DIMENSIONS.getPath(), DIMENSIONS.get() );
		LOGGER.info( "{} = {}", DIMENSION_LIST_TYPE.getPath(), DIMENSION_LIST_TYPE.get() );
	}
	
	public static void printLoadedConfig() {
		
		LOGGER.info( "Loading \"{}\" Config", mod_name );
		printConfig();
		LOGGER.info( "\"{}\" Config loaded", mod_name );
	}
	
	public static void checkAndPrintConfig() {
		
		if( checkCorrectAndReadDimensions() ) {
			LOGGER.info( "\"{}\" Config corrected", mod_name );
			printConfig();
		}
	}
	
	private static boolean checkCorrectAndReadDimensions() {
		
		ArrayList<String> read_dimensions = new ArrayList<>( DIMENSIONS.get() );
		
		synchronized( dimensions ) {
			dimensions.clear();
			for( String read_dimension : read_dimensions ) {
				ResourceLocation registry_name = ResourceLocation.tryCreate( read_dimension );
				if( registry_name != null ) {
					RegistryKey<World> registrykey = RegistryKey.getOrCreateKey( Registry.WORLD_KEY,
						registry_name );
					ServerWorld serverworld = ServerLifecycleHooks.getCurrentServer().getWorld( registrykey );
					if( serverworld == null ) {
						LOGGER.warn( "Removed unknown dimension: {}", read_dimension );
					} else {
						dimensions.add( registrykey );
					}
				} else {
					LOGGER.warn( "Removed invalid dimension registry name {}", read_dimension );
				}
			}
			if( DIMENSIONS.get().size() != dimensions.size() ) {
				DIMENSIONS.set( dimensionsToRegistryNameList() );
				return true;
			}
			return false;
		}
	}
	
	private static ArrayList<String> dimensionsToRegistryNameList() {
		
		ArrayList<String> registryNames = new ArrayList<>();
		
		synchronized( dimensions ) {
			for( RegistryKey<World> dimension : dimensions ) {
				registryNames.add( dimension.getLocation().toString() );
			}
		}
		return registryNames;
	}
	
	public static void invertDimensions() {
		
		ArrayList<String> newDimensionRegistryNames = new ArrayList<>();
		
		synchronized( dimensions ) {
			for( ServerWorld serverWorld : ServerLifecycleHooks.getCurrentServer().getWorlds() ) {
				RegistryKey<World> registryKey = serverWorld.getDimensionKey();
				if( !dimensions.contains( registryKey ) ) {
					newDimensionRegistryNames.add( registryKey.getLocation().toString() );
				}
			}
		}
		newDimensionRegistryNames.sort( String::compareTo );
		DIMENSIONS.set( newDimensionRegistryNames );
		checkAndPrintConfig();
	}
	
	public static boolean isAllowedDimision( RegistryKey<World> dimension ) {
		
		synchronized( dimensions ) {
			boolean isInList = dimensions.contains( dimension );
			if( getDimensionListType() == DimensionListType.GRANT_LIST ) {
				return isInList;
			} else {
				return !isInList;
			}
		}
	}
	
	public static boolean isAllowedDimision( ServerWorld dimension ) {
		
		return isAllowedDimision( dimension.getDimensionKey() );
	}
	
	public static void grantDimensionAccess( ServerWorld dimension ) {
		
		RegistryKey<World> dimensionKey = dimension.getDimensionKey();
		synchronized( dimensions ) {
			if( getDimensionListType() == DimensionListType.LOCK_LIST ) {
				dimensions.remove( dimensionKey );
			} else {
				dimensions.add( dimensionKey );
			}
			DIMENSIONS.set( dimensionsToRegistryNameList() );
		}
	}
	
	public static void lockDimensionAccess( ServerWorld dimension ) {
		
		RegistryKey<World> dimensionKey = dimension.getDimensionKey();
		synchronized( dimensions ) {
			if( getDimensionListType() == DimensionListType.GRANT_LIST ) {
				dimensions.remove( dimensionKey );
			} else {
				dimensions.add( dimensionKey );
			}
			DIMENSIONS.set( dimensionsToRegistryNameList() );
		}
	}
	
	public static DimensionListType getDimensionListType() {
		
		return DIMENSION_LIST_TYPE.get();
	}
	
	public static void setDimensionListType( DimensionListType dimensionListType ) {
		
		DIMENSION_LIST_TYPE.set( dimensionListType );
	}
}
