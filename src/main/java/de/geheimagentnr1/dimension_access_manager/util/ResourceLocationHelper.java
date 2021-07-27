package de.geheimagentnr1.dimension_access_manager.util;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.Objects;


public class ResourceLocationHelper {
	
	
	public static ResourceLocation build( String registry_name ) {
		
		return new ResourceLocation( DimensionAccessManager.MODID, registry_name );
	}
	
	public static String serverLevelToName( ServerLevel serverLevel ) {
		
		return Objects.requireNonNull( serverLevel.dimension().location() ).toString();
	}
}
