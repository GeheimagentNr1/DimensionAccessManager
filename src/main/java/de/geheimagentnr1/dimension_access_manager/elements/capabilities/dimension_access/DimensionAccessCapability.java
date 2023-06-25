package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilitiesRegisterFactory;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


@RequiredArgsConstructor
public class DimensionAccessCapability implements ICapabilitySerializable<IntTag> {
	
	
	@NotNull
	public static final String registry_name = "dimension_access";
	
	@NotNull
	private final LazyOptional<DimensionAccessCapability> holder = LazyOptional.of( () -> this );
	
	@NotNull
	private final ServerConfig serverConfig;
	
	@NotNull
	private DimensionAccessType dimensionAccess;
	
	public DimensionAccessCapability( @NotNull final ServerConfig _serverConfig ) {
		
		serverConfig = _serverConfig;
		dimensionAccess = serverConfig.getDefaultDimensionAccessType();
	}
	
	@NotNull
	public DimensionAccessType getDimensionAccess() {
		
		return dimensionAccess;
	}
	
	public void setDimensionAccess( @NotNull DimensionAccessType _dimensionAccess ) {
		
		dimensionAccess = _dimensionAccess;
	}
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability( @NotNull Capability<T> cap, @Nullable Direction side ) {
		
		return ModCapabilitiesRegisterFactory.DIMENSION_ACCESS.orEmpty( cap, holder );
	}
	
	@NotNull
	@Override
	public IntTag serializeNBT() {
		
		return IntTag.valueOf( dimensionAccess.ordinal() );
	}
	
	@Override
	public void deserializeNBT( @NotNull IntTag nbt ) {
		
		DimensionAccessType[] dimensionAccessTypes = DimensionAccessType.values();
		int value = nbt.getAsInt();
		if( value >= 0 && value < dimensionAccessTypes.length ) {
			dimensionAccess = dimensionAccessTypes[value];
		} else {
			dimensionAccess = DimensionAccessType.GRANTED;
		}
	}
}
