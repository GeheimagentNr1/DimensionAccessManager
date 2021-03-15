package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.util.NBTType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.TreeSet;


public abstract class DimensionAccessListCapability implements ICapabilitySerializable<ListNBT> {
	
	
	private final LazyOptional<? extends DimensionAccessListCapability> capability = LazyOptional.of( () -> this );
	
	private final TreeSet<GameProfile> gameProfiles = new TreeSet<>( Comparator.comparing( GameProfile::getId ) );
	
	public boolean contains( GameProfile gameProfile ) {
		
		return gameProfiles.contains( gameProfile );
	}
	
	public boolean add( GameProfile gameProfile ) {
		
		return gameProfiles.add( gameProfile );
	}
	
	public boolean remove( GameProfile gameProfile ) {
		
		return gameProfiles.remove( gameProfile );
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {
		
		if( cap == getCapability() ) {
			return capability.cast();
		}
		return LazyOptional.empty();
	}
	
	protected abstract Capability<? extends DimensionAccessListCapability> getCapability();
	
	@Override
	public ListNBT serializeNBT() {
		
		ListNBT listNBT = new ListNBT();
		gameProfiles.forEach( gameProfile -> {
			CompoundNBT compound = new CompoundNBT();
			listNBT.add( NBTUtil.writeGameProfile( compound, gameProfile ) );
		} );
		return listNBT;
	}
	
	@Override
	public void deserializeNBT( ListNBT nbt ) {
		
		nbt.forEach( inbt -> {
			if( inbt.getId() == NBTType.COMPOUND.getId() ) {
				gameProfiles.add( NBTUtil.readGameProfile( (CompoundNBT)inbt ) );
			}
		} );
	}
	
	public TreeSet<GameProfile> getGameProfiles() {
		
		return gameProfiles;
	}
}
