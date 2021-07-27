package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.util.ResourceLocationHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class DimensionAccessCapability implements ICapabilitySerializable<IntTag> {
	
	
	public static final ResourceLocation registry_name = ResourceLocationHelper.build( "dimension_access" );
	
	private final LazyOptional<DimensionAccessCapability> holder = LazyOptional.of( () -> this );
	
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
		
		return ModCapabilities.DIMENSION_ACCESS.orEmpty( cap, holder );
	}
	
	@Override
	public IntTag serializeNBT() {
		
		return IntTag.valueOf( dimensionAccess.ordinal() );
	}
	
	@Override
	public void deserializeNBT( IntTag nbt ) {
		
		DimensionAccessType[] dimensionAccessTypes = DimensionAccessType.values();
		int value = nbt.getAsInt();
		if( value >= 0 && value < dimensionAccessTypes.length ) {
			dimensionAccess = dimensionAccessTypes[value];
		} else {
			dimensionAccess = DimensionAccessType.GRANTED;
		}
	}
}
