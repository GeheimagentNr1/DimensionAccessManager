package de.geheimagentnr1.dimension_access_manager.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import javax.annotation.Nullable;
import java.util.UUID;


public class GameProfileUtils {
	
	
	@Nullable
	public static GameProfile readGameProfile( CompoundTag pTag ) {
		
		UUID uuid = pTag.hasUUID( "Id" ) ? pTag.getUUID( "Id" ) : Util.NIL_UUID;
		String s = pTag.getString( "Name" );
		
		try {
			GameProfile gameProfile = new GameProfile( uuid, s );
			if( pTag.contains( "Properties", 10 ) ) {
				CompoundTag properties = pTag.getCompound( "Properties" );
				
				for( String key : properties.getAllKeys() ) {
					ListTag values = properties.getList( key, 10 );
					
					for( int i = 0; i < values.size(); ++i ) {
						CompoundTag valueTag = values.getCompound( i );
						String value = valueTag.getString( "Value" );
						if( valueTag.contains( "Signature", 8 ) ) {
							gameProfile.getProperties().put(
								key,
								new Property( key, value, valueTag.getString( "Signature" ) )
							);
						} else {
							gameProfile.getProperties().put( key, new Property( key, value ) );
						}
					}
				}
			}
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
			pTag.putUUID( "Id", pGameProfile.getId() );
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
