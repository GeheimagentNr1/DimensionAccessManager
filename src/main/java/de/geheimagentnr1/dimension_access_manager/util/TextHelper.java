package de.geheimagentnr1.dimension_access_manager.util;

import net.minecraft.world.dimension.DimensionType;

import java.util.Objects;


public class TextHelper {
	
	
	public static String dimensionTypeToName( DimensionType dimension ) {
		
		return Objects.requireNonNull( dimension.getRegistryName(), "Registry Name is null" ).toString();
	}
	
	public static String getIsAccessText( boolean granted ) {
		
		return granted ? " is granted." : " is locked.";
	}
}
