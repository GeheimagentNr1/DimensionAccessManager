package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;


public class DimensionAccessCapability implements ValueIOSerializable {
	
	
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
	public void serialize( @NotNull ValueOutput output ) {
		
		output.putInt( "dimension_access", dimensionAccess.ordinal() );
	}
	
	@Override
	public void deserialize( @NotNull ValueInput input ) {
		
		deserializeLegacy( input.getIntOr( "dimension_access", DimensionAccessType.GRANTED.ordinal() ) );
	}
	
	//Format up to 1.21.5 (INBTSerializable<IntTag>), see LegacyAttachmentMigrationHandler
	public void deserializeLegacy( int value ) {
		
		DimensionAccessType[] dimensionAccessTypes = DimensionAccessType.values();
		if( value >= 0 && value < dimensionAccessTypes.length ) {
			dimensionAccess = dimensionAccessTypes[value];
		} else {
			dimensionAccess = DimensionAccessType.GRANTED;
		}
	}
}
