package de.geheimagentnr1.dimension_access_manager;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


@SuppressWarnings( "UtilityClassWithPublicConstructor" )
@Mod( DimensionAccessManager.MODID )
public class DimensionAccessManager {
	
	
	public static final String MODID = "dimension_access_manager";
	
	public DimensionAccessManager() {
		
		ModLoadingContext.get().registerConfig( ModConfig.Type.SERVER, ServerConfig.CONFIG );
	}
}
