package de.geheimagentnr1.dimension_access_manager.elements.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionAccessTypeArgument;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionCommandAccessHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;


@SuppressWarnings( "SameReturnValue" )
public class DimensionsCommand {
	
	
	public static void register( CommandDispatcher<CommandSource> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSource> dimensions = Commands.literal( "dimensions" );
		dimensions.then( Commands.literal( "status" )
			.executes( DimensionsCommand::showDimensionsStatus ) );
		LiteralArgumentBuilder<CommandSource> manageDimensions = dimensions
			.requires( source -> source.hasPermission( 3 ) );
		manageDimensions.then( Commands.literal( "default" )
			.then( Commands.literal( "defaultDimensionAccessType" )
				.executes( DimensionsCommand::showDefaultDimensionAccessType )
				.then( Commands.argument( "dimensionAccessType", DimensionAccessTypeArgument.dimensionAccessType() )
					.executes( DimensionsCommand::setDefaultDimensionAccessType ) ) ) );
		dispatcher.register( dimensions );
	}
	
	private static int showDimensionsStatus( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		MinecraftServer server = source.getServer();
		server.getAllLevels().forEach( serverWorld ->
			DimensionCommandAccessHelper.showDimensionStatus( source, serverWorld )
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int showDefaultDimensionAccessType( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		source.sendSuccess(
			new StringTextComponent( String.format(
				"The default dimension access type is %s.",
				ServerConfig.getDefaultDimensionAccessType()
			) ),
			false
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int setDefaultDimensionAccessType( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		ServerConfig.setDefaultDimensionAccessType(
			DimensionAccessTypeArgument.getDimensionAccessType( context, "dimensionAccessType" )
		);
		source.sendSuccess(
			new StringTextComponent( String.format(
				"The default dimension access type is now %s.",
				ServerConfig.getDefaultDimensionAccessType()
			) ),
			true
		);
		return Command.SINGLE_SUCCESS;
	}
}
