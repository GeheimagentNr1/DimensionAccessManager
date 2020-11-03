package de.geheimagentnr1.dimension_access_manager.elements.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.geheimagentnr1.dimension_access_manager.config.MainConfig;
import de.geheimagentnr1.dimension_access_manager.util.TextHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;


public class DimensionsStatusCommand {
	
	
	public static void register( CommandDispatcher<CommandSource> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSource> granted_dimensions = Commands.literal( "dimensions_status" );
		granted_dimensions.executes( context -> {
			CommandSource source = context.getSource();
			ServerLifecycleHooks.getCurrentServer().getWorlds().forEach( dimension ->
				source.sendFeedback( new StringTextComponent( TextHelper.dimensionTypeToName( dimension ) )
						.appendString( TextHelper.getIsAccessText( MainConfig.isAllowedDimision( dimension ) ) ),
					false ) );
			return Command.SINGLE_SUCCESS;
		} );
		dispatcher.register( granted_dimensions );
	}
}
