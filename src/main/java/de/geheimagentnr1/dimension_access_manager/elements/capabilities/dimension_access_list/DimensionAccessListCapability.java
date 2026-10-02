package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.utils.GameProfileUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.TreeSet;


public abstract class DimensionAccessListCapability implements ValueIOSerializable {

	
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
	public void serialize( @NotNull ValueOutput output ) {
		
		ValueOutput.TypedOutputList<CompoundTag> list = output.list( "game_profiles", CompoundTag.CODEC );
		gameProfiles.forEach( gameProfile -> list.add( GameProfileUtils.writeGameProfile( new CompoundTag(), gameProfile ) ) );
	}
	
	@Override
	public void deserialize( @NotNull ValueInput input ) {
		
		gameProfiles.clear();
		input.listOrEmpty( "game_profiles", CompoundTag.CODEC ).forEach( this::addGameProfile );
	}
	
	//Format up to 1.21.5 (INBTSerializable<ListTag>), see LegacyAttachmentMigrationHandler
	public void deserializeLegacy( @NotNull ListTag nbt ) {
		
		nbt.forEach( inbt -> {
			if( inbt instanceof CompoundTag compound ) {
				addGameProfile( compound );
			}
		} );
	}
	
	private void addGameProfile( @NotNull CompoundTag compound ) {
		
		GameProfile profile = GameProfileUtils.readGameProfile( compound );
		if( profile != null ) {
			gameProfiles.add( profile );
		}
	}
	
	@NotNull
	public TreeSet<GameProfile> getGameProfiles() {
		
		return gameProfiles;
	}
}
