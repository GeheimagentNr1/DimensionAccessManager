package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.minecraft_forge_api.elements.commands.CommandInterface;
import lombok.RequiredArgsConstructor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;


@SuppressWarnings( "SameReturnValue" )
@RequiredArgsConstructor
public class DimensionCommand implements CommandInterface {
	
	
	@NotNull
	private final ServerConfig serverConfig;
	
	@NotNull
	@Override
	public LiteralArgumentBuilder<CommandSourceStack> build() {
		
		Predicate<CommandSourceStack> permissionChecker = source ->
			source.hasPermission( serverConfig.getDimensionCommandPermissionLevel() );
		LiteralArgumentBuilder<CommandSourceStack> dimension = Commands.literal( "dimension" );
		dimension.then( Commands.argument( "dimension", DimensionArgument.dimension() )
			.then( Commands.literal( "access" )
				.then( Commands.literal( "status" )
					.executes( this::showDimensionStatus ) )
				.then( Commands.literal( "grant" )
					.requires( permissionChecker )
					.executes( this::grantDimension ) )
				.then( Commands.literal( "lock" )
					.requires( permissionChecker )
					.executes( this::lockDimension ) ) )
			.then( Commands.literal( "players" )
				.requires( permissionChecker )
				.then( Commands.literal( "list" )
					.executes( this::showLists ) )
				.then( Commands.literal( "whitelist" )
					.then( Commands.literal( "list" )
						.executes( this::showWhitelist ) )
					.then( Commands.literal( "add" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( this::addTargetsToWhitelist ) ) )
					.then( Commands.literal( "remove" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( this::removeTargetsFromWhitelist ) ) ) )
				.then( Commands.literal( "blacklist" )
					.then( Commands.literal( "list" )
						.executes( this::showBlacklist ) )
					.then( Commands.literal( "add" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( this::addTargetsToBlacklist ) ) )
					.then( Commands.literal( "remove" )
						.then( Commands.argument( "targets", GameProfileArgument.gameProfile() )
							.executes( this::removeTargetsFromBlacklist ) ) ) ) ) );
		return dimension;
	}
	
	private int showDimensionStatus( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> DimensionCommandAccessHelper.showDimensionStatus(
				source,
				serverLevel
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int grantDimension( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> DimensionCommandAccessHelper.runForAccess(
				serverLevel,
				dimensionAccessCapability -> {
					dimensionAccessCapability.setDimensionAccess( DimensionAccessType.GRANTED );
					DimensionCommandAccessHelper.sendDimensionAccessChangedFeedback(
						source,
						serverLevel,
						dimensionAccessCapability
					);
					saveChanges( serverLevel );
				}
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int lockDimension( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> DimensionCommandAccessHelper.runForAccess(
				serverLevel,
				dimensionAccessCapability -> {
					dimensionAccessCapability.setDimensionAccess( DimensionAccessType.LOCKED );
					DimensionCommandAccessHelper.sendDimensionAccessChangedFeedback(
						source,
						serverLevel,
						dimensionAccessCapability
					);
					saveChanges( serverLevel );
				}
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int showLists( @NotNull CommandContext<CommandSourceStack> commandContext ) throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> {
				DimensionCommandPlayersHelper.runForWhitelist(
					serverLevel,
					dimensionAccessWhitelistCapability -> DimensionCommandPlayersHelper.sendWhitelistList(
						dimensionAccessWhitelistCapability,
						source,
						serverLevel
					)
				);
				DimensionCommandPlayersHelper.runForBlacklist(
					serverLevel,
					dimensionAccessBlacklistCapability -> DimensionCommandPlayersHelper.sendBlacklistList(
						dimensionAccessBlacklistCapability,
						source,
						serverLevel
					)
				);
			}
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int showWhitelist( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> DimensionCommandPlayersHelper.runForWhitelist(
				serverLevel,
				dimensionAccessWhitelistCapability -> DimensionCommandPlayersHelper.sendWhitelistList(
					dimensionAccessWhitelistCapability,
					source,
					serverLevel
				)
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int addTargetsToWhitelist( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromBlacklist( source, serverLevel, gameProfiles );
				DimensionCommandPlayersHelper.addTargetsToWhitelist( source, serverLevel, gameProfiles );
				saveChanges( serverLevel );
			}
		);
		return gameProfiles.size();
	}
	
	private int removeTargetsFromWhitelist( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromWhitelist( source, serverLevel, gameProfiles );
				saveChanges( serverLevel );
			}
		);
		return gameProfiles.size();
	}
	
	private int showBlacklist( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> DimensionCommandPlayersHelper.runForBlacklist(
				serverLevel,
				dimensionAccessBlacklistCapability -> DimensionCommandPlayersHelper.sendBlacklistList(
					dimensionAccessBlacklistCapability,
					source,
					serverLevel
				)
			)
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int addTargetsToBlacklist( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromWhitelist( source, serverLevel, gameProfiles );
				DimensionCommandPlayersHelper.addTargetsToBlacklist( source, serverLevel, gameProfiles );
				saveChanges( serverLevel );
			}
		);
		return gameProfiles.size();
	}
	
	private int removeTargetsFromBlacklist( @NotNull CommandContext<CommandSourceStack> commandContext )
		throws CommandSyntaxException {
		
		Collection<GameProfile> gameProfiles = GameProfileArgument.getGameProfiles( commandContext, "targets" );
		DimensionCommandRunner.run(
			commandContext,
			( context, source, serverLevel ) -> {
				DimensionCommandPlayersHelper.removeTargetsFromBlacklist( source, serverLevel, gameProfiles );
				saveChanges( serverLevel );
			}
		);
		return gameProfiles.size();
	}
	
	private void saveChanges( @NotNull ServerLevel serverLevel ) {
		
		serverLevel.save( null, false, false );
	}
}
