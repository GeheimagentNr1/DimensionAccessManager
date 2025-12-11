package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;


public enum DimensionAccessType implements StringRepresentable {
	GRANTED,
	LOCKED;
	
	@NotNull
	@Override
	public String getSerializedName() {
		
		return name().toLowerCase();
	}
}
