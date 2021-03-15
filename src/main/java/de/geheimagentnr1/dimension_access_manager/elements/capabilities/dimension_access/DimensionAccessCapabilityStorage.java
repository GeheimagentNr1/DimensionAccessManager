package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access;

import de.geheimagentnr1.dimension_access_manager.util.NBTType;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;


public class DimensionAccessCapabilityStorage implements Capability.IStorage<DimensionAccessCapability> {
	
	
	@Nullable
	@Override
	public INBT writeNBT(
		Capability<DimensionAccessCapability> capability,
		DimensionAccessCapability instance,
		Direction side ) {
		
		return instance.serializeNBT();
	}
	
	@Override
	public void readNBT(
		Capability<DimensionAccessCapability> capability,
		DimensionAccessCapability instance,
		Direction side,
		INBT nbt ) {
		
		if( nbt.getId() == NBTType.INT.getId() ) {
			instance.deserializeNBT( (IntNBT)nbt );
		}
	}
}
