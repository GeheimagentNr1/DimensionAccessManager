package de.geheimagentnr1.dimension_access_manager.elements.commands;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionListTypeArgument;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;


public class ModArgumentTypes {
	
	
	public static void registerArgumentTypes() {
		
		ArgumentTypes.register( DimensionAccessManager.MODID + ":" + DimensionListTypeArgument.registry_name,
			DimensionListTypeArgument.class,
			new ArgumentSerializer<>( DimensionListTypeArgument::dimensionListType ) );
	}
}
