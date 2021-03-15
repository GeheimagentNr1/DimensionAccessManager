package de.geheimagentnr1.dimension_access_manager.util;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import net.minecraft.util.ResourceLocation;


public class ResourceLocationBuilder {
	
	
	public static ResourceLocation build( String registry_name ) {
		
		return new ResourceLocation( DimensionAccessManager.MODID, registry_name );
	}
}
