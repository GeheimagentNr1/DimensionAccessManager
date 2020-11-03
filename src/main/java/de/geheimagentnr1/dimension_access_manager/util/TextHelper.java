package de.geheimagentnr1.dimension_access_manager.util;

import net.minecraft.world.server.ServerWorld;


public class TextHelper {
	
	
	public static String dimensionTypeToName( ServerWorld dimension ) {
		
		return dimension.getDimensionKey().getLocation().toString();
	}
	
	public static String getIsAccessText( boolean granted ) {
		
		return granted ? " is granted." : " is locked.";
	}
}
