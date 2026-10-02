package de.geheimagentnr1.dimension_access_manager.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;


public class GameProfileUtils {
	
	
	@Nullable
	public static GameProfile readGameProfile( CompoundTag pTag ) {
		
		UUID uuid = pTag.read( "Id", UUIDUtil.CODEC ).orElse( Util.NIL_UUID );
		String s = pTag.getStringOr( "Name", "" );
		
		try {
			GameProfile gameProfile = new GameProfile( uuid, s );
			pTag.getCompound( "Properties" ).ifPresent( properties -> {
				for( String key : properties.keySet() ) {
					ListTag values = properties.getListOrEmpty( key );
					
					for( int i = 0; i < values.size(); ++i ) {
						CompoundTag valueTag = values.getCompoundOrEmpty( i );
						String value = valueTag.getStringOr( "Value", "" );
						Optional<String> signature = valueTag.getString( "Signature" );
						if( signature.isPresent() ) {
							gameProfile.getProperties().put( key, new Property( key, value, signature.get() ) );
						} else {
							gameProfile.getProperties().put( key, new Property( key, value ) );
						}
					}
				}
			} );
			return gameProfile;
		} catch( Throwable throwable ) {
			return null;
		}
	}
	
	public static CompoundTag writeGameProfile( CompoundTag pTag, GameProfile pGameProfile ) {
		
		if( !pGameProfile.getName().isEmpty() ) {
			pTag.putString( "Name", pGameProfile.getName() );
		}
		if( !pGameProfile.getId().equals( Util.NIL_UUID ) ) {
			pTag.store( "Id", UUIDUtil.CODEC, pGameProfile.getId() );
		}
		if( !pGameProfile.getProperties().isEmpty() ) {
			CompoundTag properties = new CompoundTag();
			for( String key : pGameProfile.getProperties().keySet() ) {
				ListTag values = new ListTag();
				for( Property property : pGameProfile.getProperties().get( key ) ) {
					CompoundTag valueTag = new CompoundTag();
					valueTag.putString( "Value", property.value() );
					String signature = property.signature();
					if( signature != null ) {
						valueTag.putString( "Signature", signature );
					}
					values.add( valueTag );
				}
				properties.put( key, values );
			}
			pTag.put( "Properties", properties );
		}
		return pTag;
	}
}
