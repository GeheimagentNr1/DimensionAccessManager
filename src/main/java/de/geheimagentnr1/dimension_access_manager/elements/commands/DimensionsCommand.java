package de.geheimagentnr1.dimension_access_manager.elements.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionAccessTypeArgument;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionCommandAccessHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;


@SuppressWarnings( "SameReturnValue" )
public class DimensionsCommand {
	
	
	public static void register( CommandDispatcher<CommandSourceStack> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSourceStack> dimensions = Commands.literal( "dimensions" );
		dimensions.then( Commands.literal( "status" )
			.executes( DimensionsCommand::showDimensionsStatus ) );
		LiteralArgumentBuilder<CommandSourceStack> opManageDimensions = Commands.literal( "dimensions" )
			.requires( source -> source.hasPermission( 3 ) );
		opManageDimensions.then( Commands.literal( "default" )
			.then( Commands.literal( "defaultDimensionAccessType" )
				.executes( DimensionsCommand::showDefaultDimensionAccessType )
				.then( Commands.argument( "dimensionAccessType", DimensionAccessTypeArgument.dimensionAccessType() )
					.executes( DimensionsCommand::setDefaultDimensionAccessType ) ) ) );
		dispatcher.register( dimensions );
		dispatcher.register( opManageDimensions );
	}
	
	private static int showDimensionsStatus( CommandContext<CommandSourceStack> context ) {
		
		CommandSourceStack source = context.getSource();
		MinecraftServer server = source.getServer();
		server.getAllLevels().forEach( serverLevel ->
			DimensionCommandAccessHelper.showDimensionStatus( source, serverLevel )
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int showDefaultDimensionAccessType( CommandContext<CommandSourceStack> context ) {
		
		CommandSourceStack source = context.getSource();
		source.sendSuccess(
			new TextComponent( String.format(
				"The default dimension access type is %s.",
				ServerConfig.getDefaultDimensionAccessType()
			) ),
			false
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int setDefaultDimensionAccessType( CommandContext<CommandSourceStack> context ) {
		
		CommandSourceStack source = context.getSource();
		ServerConfig.setDefaultDimensionAccessType(
			DimensionAccessTypeArgument.getDimensionAccessType( context, "dimensionAccessType" )
		);
		source.sendSuccess(
			new TextComponent( String.format(
				"The default dimension access type is now %s.",
				ServerConfig.getDefaultDimensionAccessType()
			) ),
			true
		);
		return Command.SINGLE_SUCCESS;
	}
}
