package de.geheimagentnr1.dimension_access_manager.utils;

import net.minecraft.util.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.players.NameAndId;

import javax.annotation.Nullable;
import java.util.UUID;


public class GameProfileUtils {
	
	
	//Since 1.21.9 command arguments only provide NameAndId (id and name). The format stays the same as before
	//(Name, Id as int array); stored Properties of older versions are ignored, they were never used for access checks.
	@Nullable
	public static NameAndId readGameProfile( CompoundTag pTag ) {
		
		UUID uuid = pTag.read( "Id", UUIDUtil.CODEC ).orElse( Util.NIL_UUID );
		String name = pTag.getStringOr( "Name", "" );
		try {
			return new NameAndId( uuid, name );
		} catch( Throwable throwable ) {
			return null;
		}
	}
	
	public static CompoundTag writeGameProfile( CompoundTag pTag, NameAndId pGameProfile ) {
		
		if( !pGameProfile.name().isEmpty() ) {
			pTag.putString( "Name", pGameProfile.name() );
		}
		if( !pGameProfile.id().equals( Util.NIL_UUID ) ) {
			pTag.store( "Id", UUIDUtil.CODEC, pGameProfile.id() );
		}
		return pTag;
	}
}
