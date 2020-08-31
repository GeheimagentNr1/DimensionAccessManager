package de.geheimagentnr1.dimension_access_manager;

import de.geheimagentnr1.dimension_access_manager.config.MainConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.ModArgumentTypes;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


@SuppressWarnings( { "unused", "UtilityClassWithPublicConstructor" } )
@Mod( DimensionAccessManager.MODID )
public class DimensionAccessManager {
	
	
	public static final String MODID = "dimension_access_manager";
	
	public DimensionAccessManager() {
		
		ModArgumentTypes.registerArgumentTypes();
		ModLoadingContext.get().registerConfig( ModConfig.Type.COMMON, MainConfig.CONFIG, MODID + ".toml" );
	}
}
