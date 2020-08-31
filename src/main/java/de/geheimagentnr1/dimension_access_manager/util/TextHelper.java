package de.geheimagentnr1.dimension_access_manager.util;

import net.minecraft.world.server.ServerWorld;


public class TextHelper {
	
	
	public static String dimensionTypeToName( ServerWorld dimension ) {
		
		return dimension.func_234923_W_().func_240901_a_().toString();
	}
	
	public static String getIsAccessText( boolean granted ) {
		
		return granted ? " is granted." : " is locked.";
	}
}
