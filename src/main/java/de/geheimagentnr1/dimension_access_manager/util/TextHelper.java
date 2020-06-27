package de.geheimagentnr1.dimension_access_manager.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;


public class TextHelper {
	
	
	public static String dimensionTypeToName( ServerWorld dimension ) {
		
		return dimensionTypeToName( dimension.func_234923_W_() );
	}
	
	public static String dimensionTypeToName( RegistryKey<World> registryKey ) {
		
		return registryKey.func_240901_a_().toString();
	}
	
	public static String getIsAccessText( boolean granted ) {
		
		return granted ? " is granted." : " is locked.";
	}
}
