package de.geheimagentnr1.dimension_access_manager.config;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


@SuppressWarnings( "SynchronizationOnStaticField" )
public class MainConfig {
	
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final String mod_name = "Dimension Access Manager";
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec CONFIG;
	
	private static final ForgeConfigSpec.ConfigValue<List<String>> DIMENSIONS;
	
	private static final ForgeConfigSpec.EnumValue<DimensionListType> DIMENSION_LIST_TYPE;
	
	private static final TreeSet<DimensionType> dimensions = new TreeSet<>(
		Comparator.comparingInt( DimensionType::getId ) );
	
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
	
	public static void checkAndPrintConfig() {
		
		checkCorrectAndReadDimensions();
		LOGGER.info( "Loading \"{}\" Config", mod_name );
		LOGGER.info( "{} = {}", DIMENSIONS.getPath(), DIMENSIONS.get() );
		LOGGER.info( "{} = {}", DIMENSION_LIST_TYPE.getPath(), DIMENSION_LIST_TYPE.get() );
		LOGGER.info( "\"{}\" Config loaded", mod_name );
	}
	
	private static void checkCorrectAndReadDimensions() {
		
		ArrayList<String> read_dimensions = new ArrayList<>( DIMENSIONS.get() );
		
		synchronized( dimensions ) {
			dimensions.clear();
			for( String read_dimension : read_dimensions ) {
				ResourceLocation registry_name = ResourceLocation.tryCreate( read_dimension );
				if( registry_name != null ) {
					DimensionType dimension = DimensionType.byName( registry_name );
					if( dimension == null ) {
						LOGGER.warn( "Removed unknown dimension: {}", read_dimension );
					} else {
						dimensions.add( dimension );
					}
				} else {
					LOGGER.warn( "Removed invalid dimension registry name {}", read_dimension );
				}
			}
			if( DIMENSIONS.get().size() != dimensions.size() ) {
				DIMENSIONS.set( dimensionsToRegistryNameList() );
			}
		}
	}
	
	private static ArrayList<String> dimensionsToRegistryNameList() {
		
		ArrayList<String> registryNames = new ArrayList<>();
		
		synchronized( dimensions ) {
			for( DimensionType dimension : dimensions ) {
				registryNames.add( Objects.requireNonNull( dimension.getRegistryName() ).toString() );
			}
		}
		return registryNames;
	}
	
	public static void invertDimensions() {
		
		ArrayList<String> newDimensionRegistryNames = new ArrayList<>();
		
		synchronized( dimensions ) {
			for( DimensionType dimensionType : DimensionType.getAll() ) {
				if( !dimensions.contains( dimensionType ) ) {
					newDimensionRegistryNames.add(
						Objects.requireNonNull( dimensionType.getRegistryName() ).toString() );
				}
			}
		}
		newDimensionRegistryNames.sort( String::compareTo );
		DIMENSIONS.set( newDimensionRegistryNames );
		checkAndPrintConfig();
	}
	
	public static boolean isAllowedDimision( DimensionType dimension ) {
		
		boolean isInList;
		
		synchronized( dimensions ) {
			isInList = dimensions.contains( dimension );
		}
		if( getDimensionListType() == DimensionListType.GRANT_LIST ) {
			return isInList;
		} else {
			return !isInList;
		}
	}
	
	public static void grantDimensionAccess( DimensionType dimension ) {
		
		synchronized( dimensions ) {
			if( getDimensionListType() == DimensionListType.LOCK_LIST ) {
				dimensions.remove( dimension );
			} else {
				dimensions.add( dimension );
			}
			DIMENSIONS.set( dimensionsToRegistryNameList() );
		}
	}
	
	public static void lockDimensionAccess( DimensionType dimension ) {
		
		synchronized( dimensions ) {
			if( getDimensionListType() == DimensionListType.GRANT_LIST ) {
				dimensions.remove( dimension );
			} else {
				dimensions.add( dimension );
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
