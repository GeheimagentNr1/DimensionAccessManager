package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.GameProfileArgument;

import java.util.Collection;
import java.util.function.Predicate;


@SuppressWarnings( "SameReturnValue" )
public class DimensionCommand {
	
	
	private static final Predicate<CommandSourceStack> PERMISSION_CHECKER = source -> source.hasPermission( 3 );
	
	public static void register( CommandDispatcher<CommandSourceStack> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSourceStack> dimension = Commands.literal( "dimension" );
		dimension.then( Commands.argument( "dimension", DimensionArgument.dimension() )
			.then( Commands.literal( "access" )
				.then( Commands.literal( "status" )
					.executes( DimensionCommand::showDimensionStatus ) )
				.then( Commands.literal( "grant" )
					.requires( PERMISSION_CHECKER )
					.executes( DimensionCommand::grantDimension ) )
				.then( Commands.literal( "lock" )
					.requires( PERMISSION_CHECKER )
					.executes( DimensionCommand::lockDimension ) ) )
			.then( Commands.literal( "players" )
				.requires( PERMISSION_CHECKER )
				.then( Commands.literal( "list" )
					.executes( DimensionCommand::showLists ) )
				.then( Commands.literal( "whitelist" )
					.then( Commands.literal( "list" )
						.executes( DimensionCommand::showWhitelist ) )
					.then( Commands.literal( "add" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::addTargetsToWhitelist ) ) )
					.then( Commands.literal( "remove" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::removeTargetsFromWhitelist ) ) ) )
				.then( Commands.literal( "blacklist" )
					.then( Commands.literal( "list" )
						.executes( DimensionCommand::showBlacklist ) )
					.then( Commands.literal( "add" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::addTargetsToBlacklist ) ) )
					.then( Commands.literal( "remove" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( DimensionCommand::removeTargetsFromBlacklist ) ) ) ) ) );
		dispatcher.register( dimension );
	}
	
	private static int showDimensionStatus( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> DimensionCommandAccessHelper.showDimensionStatus(
				source,
				serverWorld
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int grantDimension( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> DimensionCommandAccessHelper.runForAccess(
				serverWorld,
				dimensionAccessCapability -> {
					dimensionAccessCapability.setDimensionAccess( DimensionAccessType.GRANTED );
					DimensionCommandAccessHelper.sendDimensionAccessChangedFeedback(
						source,
						serverWorld,
						dimensionAccessCapability
					);
					server.saveAllChunks( false, true, true );
				}
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int lockDimension( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> DimensionCommandAccessHelper.runForAccess(
				serverWorld,
				dimensionAccessCapability -> {
					dimensionAccessCapability.setDimensionAccess( DimensionAccessType.LOCKED );
					DimensionCommandAccessHelper.sendDimensionAccessChangedFeedback(
						source,
						serverWorld,
						dimensionAccessCapability
					);
					server.saveAllChunks( false, true, true );
				}
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int showLists( CommandContext<CommandSourceStack> commandContext ) throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.runForWhitelist(
					serverWorld,
					dimensionAccessWhitelistCapability -> DimensionCommandPlayersHelper.sendWhitelistList(
						dimensionAccessWhitelistCapability,
						source,
						serverWorld
					)
				);
				DimensionCommandPlayersHelper.runForBlacklist(
					serverWorld,
					dimensionAccessBlacklistCapability -> DimensionCommandPlayersHelper.sendBlacklistList(
						dimensionAccessBlacklistCapability,
						source,
						serverWorld
					)
				);
			}
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int showWhitelist( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> DimensionCommandPlayersHelper.runForWhitelist(
				serverWorld,
				dimensionAccessWhitelistCapability -> DimensionCommandPlayersHelper.sendWhitelistList(
					dimensionAccessWhitelistCapability,
					source,
					serverWorld
				)
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int addTargetsToWhitelist( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromBlacklist( source, serverWorld, gameProfiles );
				DimensionCommandPlayersHelper.addTargetsToWhitelist( source, serverWorld, gameProfiles );
				server.saveAllChunks( false, true, true );
			}
		);
		return gameProfiles.size();
	}
	
	private static int removeTargetsFromWhitelist( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromWhitelist( source, serverWorld, gameProfiles );
				server.saveAllChunks( false, true, true );
			}
		);
		return gameProfiles.size();
	}
	
	private static int showBlacklist( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> DimensionCommandPlayersHelper.runForBlacklist(
				serverWorld,
				dimensionAccessBlacklistCapability -> DimensionCommandPlayersHelper.sendBlacklistList(
					dimensionAccessBlacklistCapability,
					source,
					serverWorld
				)
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int addTargetsToBlacklist( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromWhitelist( source, serverWorld, gameProfiles );
				DimensionCommandPlayersHelper.addTargetsToBlacklist( source, serverWorld, gameProfiles );
				server.saveAllChunks( false, true, true );
			}
		);
		return gameProfiles.size();
	}
	
	private static int removeTargetsFromBlacklist( CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, server, serverWorld ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromBlacklist( source, serverWorld, gameProfiles );
				server.saveAllChunks( false, true, true );
			}
		);
		return gameProfiles.size();
	}
}
