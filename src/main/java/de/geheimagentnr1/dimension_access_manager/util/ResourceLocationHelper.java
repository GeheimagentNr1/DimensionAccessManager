package de.geheimagentnr1.dimension_access_manager.util;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;


public class ResourceLocationHelper {
	
	
	public static ResourceLocation build( String registry_name ) {
		
		return new ResourceLocation( DimensionAccessManager.MODID, registry_name );
	}
	
	public static String serverWorldToName( ServerWorld serverWorld ) {
		
		return Objects.requireNonNull( serverWorld.func_234923_W_().func_240901_a_() ).toString();
	}
}
