package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collection;


@SuppressWarnings( "SameReturnValue" )
public class DimensionCommand {
	
	
	public static void register( CommandDispatcher<CommandSource> dispatcher ) {
		
		//TODO: default und dimensions_status zu dimensions status uns dimensions default
		//TODO: dimension nach ganz vorne bei den dimension command
		LiteralArgumentBuilder<CommandSource> dimension = Commands.literal( "dimension" );
		dimension.then( Commands.literal( "access" )
			.then( Commands.literal( "status" )
				.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
					.executes( DimensionCommand::showDimensionStatus ) ) ) );
		
		LiteralArgumentBuilder<CommandSource> manageDimension = dimension.requires(
			source -> source.hasPermissionLevel( 3 )
		);
		manageDimension.then( Commands.literal( "access" )
			.then( Commands.literal( "grant" )
				.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
					.executes( DimensionCommand::grantDimension ) ) )
			.then( Commands.literal( "lock" )
				.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
					.executes( DimensionCommand::lockDimension ) ) ) );
		manageDimension.then( Commands.literal( "default" )
			.then( Commands.literal( "defaultDimensionAccessType" )
				.executes( DimensionCommand::showDefaultDimensionAccessType )
				.then( Commands.argument( "dimensionAccessType", DimensionAccessTypeArgument.dimensionAccessType() )
					.executes( DimensionCommand::setDefaultDimensionAccessType ) ) ) );
		manageDimension.then( Commands.literal( "players" )
			.then( Commands.literal( "list" )
				.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
					.executes( DimensionCommand::showLists ) ) )
			.then( Commands.literal( "whitelist" )
				.then( Commands.literal( "list" )
					.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
						.executes( DimensionCommand::showWhitelist ) ) )
				.then( Commands.literal( "add" )
					.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::addTargetsToWhitelist ) ) ) )
				.then( Commands.literal( "remove" )
					.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::removeTargetsFromWhitelist ) ) ) ) )
			.then( Commands.literal( "blacklist" )
				.then( Commands.literal( "list" )
					.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
						.executes( DimensionCommand::showBlacklist ) ) )
				.then( Commands.literal( "add" )
					.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::addTargetsToBlacklist ) ) ) )
				.then( Commands.literal( "remove" )
					.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::removeTargetsFromBlacklist ) ) ) ) ) );
		dispatcher.register( dimension );
	}
	
	private static int showDimensionStatus( CommandContext<CommandSource> commandContext ) {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) ->
				DimensionCommandAccessHelper.showDimensionStatus( source, dimension, serverWorld )
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int grantDimension( CommandContext<CommandSource> commandContext ) {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) ->
				DimensionCommandAccessHelper.runForAccess(
					serverWorld,
					dimensionAccessCapability -> {
						dimensionAccessCapability.setDimensionAccess( DimensionAccessType.GRANTED );
						DimensionCommandAccessHelper.sendDimensionAccessChangedFeedback(
							source,
							dimension,
							dimensionAccessCapability
						);
						server.save( false, true, true );
					}
				)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int lockDimension( CommandContext<CommandSource> commandContext ) {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) ->
				DimensionCommandAccessHelper.runForAccess(
					serverWorld,
					dimensionAccessCapability -> {
						dimensionAccessCapability.setDimensionAccess( DimensionAccessType.LOCKED );
						DimensionCommandAccessHelper.sendDimensionAccessChangedFeedback(
							source,
							dimension,
							dimensionAccessCapability
						);
						server.save( false, true, true );
					}
				)
		);
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
	
	private static int showLists( CommandContext<CommandSource> commandContext ) {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.runForWhitelist(
					serverWorld,
					dimensionAccessWhitelistCapability ->
						DimensionCommandPlayersHelper.sendWhitelistList(
							dimensionAccessWhitelistCapability,
							source,
							dimension
						)
				);
				DimensionCommandPlayersHelper.runForBlacklist(
					serverWorld,
					dimensionAccessBlacklistCapability ->
						DimensionCommandPlayersHelper.sendBlacklistList(
							dimensionAccessBlacklistCapability,
							source,
							dimension
						)
				);
			}
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int showWhitelist( CommandContext<CommandSource> commandContext ) {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) ->
				DimensionCommandPlayersHelper.runForWhitelist(
					serverWorld,
					dimensionAccessWhitelistCapability ->
						DimensionCommandPlayersHelper.sendWhitelistList(
							dimensionAccessWhitelistCapability,
							source,
							dimension
						)
				)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int addTargetsToWhitelist( CommandContext<CommandSource> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromBlacklist(
					source,
					dimension,
					serverWorld,
					gameProfiles
				);
				DimensionCommandPlayersHelper.addTargetsToWhitelist( source, dimension, serverWorld, gameProfiles );
				server.save( false, true, true );
			}
		);
		return gameProfiles.size();
	}
	
	private static int removeTargetsFromWhitelist( CommandContext<CommandSource> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromWhitelist(
					source,
					dimension,
					serverWorld,
					gameProfiles
				);
				server.save( false, true, true );
			}
		);
		return gameProfiles.size();
	}
	
	private static int showBlacklist( CommandContext<CommandSource> commandContext ) {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) ->
				DimensionCommandPlayersHelper.runForBlacklist(
					serverWorld,
					dimensionAccessBlacklistCapability ->
						DimensionCommandPlayersHelper.sendBlacklistList(
							dimensionAccessBlacklistCapability,
							source,
							dimension
						)
				)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int addTargetsToBlacklist( CommandContext<CommandSource> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromWhitelist(
					source,
					dimension,
					serverWorld,
					gameProfiles
				);
				DimensionCommandPlayersHelper.addTargetsToBlacklist( source, dimension, serverWorld, gameProfiles );
				server.save( false, true, true );
			}
		);
		return gameProfiles.size();
	}
	
	private static int removeTargetsFromBlacklist( CommandContext<CommandSource> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, dimension, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromBlacklist(
					source,
					dimension,
					serverWorld,
					gameProfiles
				);
				server.save( false, true, true );
			}
		);
		return gameProfiles.size();
	}
}
