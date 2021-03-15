package de.geheimagentnr1.dimension_access_manager.util;

import net.minecraft.world.dimension.DimensionType;

import java.util.Objects;


public class DimensionHelper {
	
	
	public static String dimensionTypeToName( DimensionType dimension ) {
		
		return Objects.requireNonNull( dimension.getRegistryName() ).toString();
	}
}
