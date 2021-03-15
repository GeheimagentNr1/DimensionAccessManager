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
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;


@SuppressWarnings( "SameReturnValue" )
public class DimensionsCommand {
	
	
	public static void register( CommandDispatcher<CommandSource> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSource> dimensions = Commands.literal( "dimensions" );
		dimensions.then( Commands.literal( "status" )
			.executes( DimensionsCommand::showDimensionsStatus ) );
		LiteralArgumentBuilder<CommandSource> manageDimensions = dimensions.requires(
			source -> source.hasPermissionLevel( 3 )
		);
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
		DimensionType.getAll().forEach( dimension -> {
			ServerWorld serverWorld = server.func_71218_a( dimension );
			DimensionCommandAccessHelper.showDimensionStatus( source, dimension, serverWorld );
		} );
		return Command.SINGLE_SUCCESS;
	}
	
	private static int showDefaultDimensionAccessType( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		source.sendFeedback(
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
		source.sendFeedback(
			new StringTextComponent( String.format(
				"The default dimension access type is now %s.",
				ServerConfig.getDefaultDimensionAccessType()
			) ),
			true
		);
		return Command.SINGLE_SUCCESS;
	}
}
