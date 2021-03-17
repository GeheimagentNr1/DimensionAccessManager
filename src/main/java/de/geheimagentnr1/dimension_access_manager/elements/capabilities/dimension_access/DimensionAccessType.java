package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import java.util.Locale;


public enum DimensionAccessType {
	GRANTED,
	LOCKED;
	
	public String getLowerCase() {
		
		return name().toLowerCase( Locale.ENGLISH );
	}
}
