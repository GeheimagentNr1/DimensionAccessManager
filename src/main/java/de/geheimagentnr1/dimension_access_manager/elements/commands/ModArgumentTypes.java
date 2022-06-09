package de.geheimagentnr1.dimension_access_manager.elements.commands;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionAccessTypeArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;


public class ModArgumentTypes {
	
	
	public static void registerArgumentTypes() {
		
		Registry.register(
			Registry.COMMAND_ARGUMENT_TYPE,
			DimensionAccessManager.MODID + ":" + DimensionAccessTypeArgument.registry_name,
			ArgumentTypeInfos.registerByClass(
				DimensionAccessTypeArgument.class,
				SingletonArgumentInfo.contextFree( DimensionAccessTypeArgument::new )
			)
		);
	}
}
