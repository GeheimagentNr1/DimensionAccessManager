package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list;

import de.geheimagentnr1.dimension_access_manager.util.NBTType;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;


@SuppressWarnings( "AbstractClassWithoutAbstractMethods" )
public abstract class DimensionAccessListCapabilityStorage<T extends DimensionAccessListCapability>
	implements Capability.IStorage<T> {
	
	
	@Nullable
	@Override
	public INBT writeNBT( Capability<T> capability, T instance, Direction side ) {
		
		return instance.serializeNBT();
	}
	
	@Override
	public void readNBT( Capability<T> capability, T instance, Direction side, INBT nbt ) {
		
		if( nbt.getId() == NBTType.LIST.getId() ) {
			instance.deserializeNBT( (ListNBT)nbt );
		}
	}
}
