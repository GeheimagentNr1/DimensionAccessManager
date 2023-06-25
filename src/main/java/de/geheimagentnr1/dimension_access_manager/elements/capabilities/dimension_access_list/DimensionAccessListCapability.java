package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.TreeSet;


public abstract class DimensionAccessListCapability implements ICapabilitySerializable<ListTag> {
	
	
	@NotNull
	private final LazyOptional<? extends DimensionAccessListCapability> holder = LazyOptional.of( () -> this );
	
	@NotNull
	private final TreeSet<GameProfile> gameProfiles = new TreeSet<>( Comparator.comparing( GameProfile::getId ) );
	
	public boolean contains( @NotNull GameProfile gameProfile ) {
		
		return gameProfiles.contains( gameProfile );
	}
	
	public boolean add( @NotNull GameProfile gameProfile ) {
		
		return gameProfiles.add( gameProfile );
	}
	
	public boolean remove( @NotNull GameProfile gameProfile ) {
		
		return gameProfiles.remove( gameProfile );
	}
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability( @NotNull Capability<T> cap, @Nullable Direction side ) {
		
		return getCapability() == cap ? holder.cast() : LazyOptional.empty();
	}
	
	@NotNull
	protected abstract Capability<? extends DimensionAccessListCapability> getCapability();
	
	@Override
	public ListTag serializeNBT() {
		
		ListTag listNBT = new ListTag();
		gameProfiles.forEach( gameProfile -> {
			CompoundTag compound = new CompoundTag();
			listNBT.add( NbtUtils.writeGameProfile( compound, gameProfile ) );
		} );
		return listNBT;
	}
	
	@Override
	public void deserializeNBT( @NotNull ListTag nbt ) {
		
		nbt.forEach( inbt -> {
			if( inbt.getId() == Tag.TAG_COMPOUND ) {
				gameProfiles.add( NbtUtils.readGameProfile( (CompoundTag)inbt ) );
			}
		} );
	}
	
	@NotNull
	public TreeSet<GameProfile> getGameProfiles() {
		
		return gameProfiles;
	}
}
