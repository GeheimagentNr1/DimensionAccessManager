package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;


public class DimensionAccessCapability implements INBTSerializable<IntTag> {
	
	
	@NotNull
	public static final String registry_name = "dimension_access";
	
	@NotNull
	private DimensionAccessType dimensionAccess = DimensionAccessType.GRANTED;
	
	@NotNull
	public DimensionAccessType getDimensionAccess() {
		
		return dimensionAccess;
	}
	
	public void setDimensionAccess( @NotNull DimensionAccessType _dimensionAccess ) {
		
		dimensionAccess = _dimensionAccess;
	}
	
	@Override
	public IntTag serializeNBT( HolderLookup.Provider provider ) {
		
		return IntTag.valueOf( dimensionAccess.ordinal() );
	}
	
	@Override
	public void deserializeNBT( HolderLookup.Provider provider, IntTag nbt ) {
		
		DimensionAccessType[] dimensionAccessTypes = DimensionAccessType.values();
		int value = nbt.getAsInt();
		if( value >= 0 && value < dimensionAccessTypes.length ) {
			dimensionAccess = dimensionAccessTypes[value];
		} else {
			dimensionAccess = DimensionAccessType.GRANTED;
		}
	}
}
