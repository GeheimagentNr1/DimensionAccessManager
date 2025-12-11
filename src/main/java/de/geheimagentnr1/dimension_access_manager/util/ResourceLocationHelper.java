package de.geheimagentnr1.dimension_access_manager.util;

import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;


public class ResourceLocationHelper {
	
	
	@NotNull
	public static String serverLevelToName( @NotNull ServerLevel serverLevel ) {
		
		return serverLevel.dimension().location().toString();
	}
}
