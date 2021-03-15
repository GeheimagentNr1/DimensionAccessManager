package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.util.ResourceLocationBuilder;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class DimensionAccessCapability implements ICapabilitySerializable<IntNBT> {
	
	
	public static final ResourceLocation registry_name = ResourceLocationBuilder.build( "dimension_access" );
	
	private final LazyOptional<DimensionAccessCapability> capability = LazyOptional.of( () -> this );
	
	private DimensionAccessType dimensionAccess = ServerConfig.getDefaultDimensionAccessType();
	
	public DimensionAccessType getDimensionAccess() {
		
		return dimensionAccess;
	}
	
	public void setDimensionAccess( DimensionAccessType _dimensionAccess ) {
		
		dimensionAccess = _dimensionAccess;
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {
		
		if( cap == ModCapabilities.DIMENSION_ACCESS ) {
			return capability.cast();
		}
		return LazyOptional.empty();
	}
	
	@Override
	public IntNBT serializeNBT() {
		
		return IntNBT.valueOf( dimensionAccess.ordinal() );
	}
	
	@Override
	public void deserializeNBT( IntNBT nbt ) {
		
		DimensionAccessType[] dimensionAccessTypes = DimensionAccessType.values();
		int value = nbt.getInt();
		if( value >= 0 && value < dimensionAccessTypes.length ) {
			dimensionAccess = dimensionAccessTypes[value];
		} else {
			dimensionAccess = DimensionAccessType.GRANTED;
		}
	}
}
