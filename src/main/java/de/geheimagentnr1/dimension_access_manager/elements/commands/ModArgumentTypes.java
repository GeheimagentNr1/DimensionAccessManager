package de.geheimagentnr1.dimension_access_manager.elements.commands;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionAccessTypeArgument;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;


public class ModArgumentTypes {
	
	
	public static void registerArgumentTypes() {
		
		ArgumentTypes.register(
			DimensionAccessManager.MODID + ":" + DimensionAccessTypeArgument.registry_name,
			DimensionAccessTypeArgument.class,
			new EmptyArgumentSerializer<>( DimensionAccessTypeArgument::dimensionAccessType )
		);
	}
}
