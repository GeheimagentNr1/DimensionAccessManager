package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.utils.GameProfileUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.TreeSet;


public abstract class DimensionAccessListCapability implements INBTSerializable<ListTag> {

	
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
	
	@Override
	public ListTag serializeNBT( HolderLookup.Provider provider ) {
		
		ListTag listNBT = new ListTag();
		gameProfiles.forEach( gameProfile -> {
			CompoundTag compound = new CompoundTag();
			listNBT.add( GameProfileUtils.writeGameProfile( compound, gameProfile ) );
		} );
		return listNBT;
	}
	
	@Override
	public void deserializeNBT( HolderLookup.Provider provider, ListTag nbt ) {
		
		nbt.forEach( inbt -> {
			if( inbt.getId() == Tag.TAG_COMPOUND ) {
				GameProfile profile = GameProfileUtils.readGameProfile( (CompoundTag)inbt );
				if( profile != null ) {
					gameProfiles.add( profile );
				}
			}
		} );
	}
	
	@NotNull
	public TreeSet<GameProfile> getGameProfiles() {
		
		return gameProfiles;
	}
}
