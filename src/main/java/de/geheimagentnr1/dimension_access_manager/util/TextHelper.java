package de.geheimagentnr1.dimension_access_manager.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;


public class TextHelper {
	
	
	public static String dimensionTypeToName( DimensionType dimension ) {
		
		ResourceLocation registry_name = dimension.getRegistryName();
		if( registry_name == null ) {
			throw new NullPointerException( "Registry Name is null" );
		}
		return registry_name.toString();
	}
	
	public static String getIsAccessText( boolean granted ) {
		
		return granted ? " is granted." : " is locked.";
	}
}
