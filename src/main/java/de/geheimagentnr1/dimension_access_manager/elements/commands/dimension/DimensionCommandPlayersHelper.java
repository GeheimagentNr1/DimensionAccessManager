package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilitiesRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import de.geheimagentnr1.minecraft_forge_api.util.ResourceLocationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.NonNullConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//package-private
class DimensionCommandPlayersHelper {
	
	
	@NotNull
	private static String gameProfilesToString( @NotNull TreeSet<GameProfile> gameProfiles ) {
		
		return gameProfilesToString( gameProfiles.stream() );
	}
	
	@NotNull
	private static String gameProfilesToString( @NotNull Stream<GameProfile> gameProfiles ) {
		
		return gameProfiles.map( GameProfile::getName ).collect( Collectors.joining( ", " ) );
	}
	
	@NotNull
	private static Stream<GameProfile> addTargetsToList(
		@NotNull DimensionAccessListCapability dimensionAccessListCapability,
		@NotNull Collection<GameProfile> gameProfiles ) {
		
		return gameProfiles.stream().filter( dimensionAccessListCapability::add );
	}
	
	@NotNull
	private static Stream<GameProfile> removeTargetsFromList(
		@NotNull DimensionAccessListCapability dimensionAccessListCapability,
		@NotNull Collection<GameProfile> gameProfiles ) {
		
		return gameProfiles.stream().filter( dimensionAccessListCapability::remove );
	}
	
	//package-private
	static void runForWhitelist(
		@NotNull ServerLevel serverLevel,
		@NotNull NonNullConsumer<DimensionAccessWhitelistCapability> runner ) {
		
		serverLevel.getCapability( ModCapabilitiesRegisterFactory.DIMENSION_ACCESS_WHITELIST ).ifPresent( runner );
	}
	
	//package-private
	static void sendWhitelistList(
		@NotNull DimensionAccessWhitelistCapability dimensionAccessWhitelistCapability,
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel ) {
		
		String gameProfiles = gameProfilesToString( dimensionAccessWhitelistCapability.getGameProfiles() );
		if( gameProfiles.isEmpty() ) {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"Whitelist \"%s\" is empty.",
					ResourceLocationHelper.serverLevelToName( serverLevel )
				) ),
				false
			);
		} else {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"Whitelist \"%s\": %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					gameProfiles
				) ),
				false
			);
		}
	}
	
	//package-private
	static void addTargetsToWhitelist(
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel,
		@NotNull Collection<GameProfile> gameProfiles ) {
		
		runForWhitelist(
			serverLevel,
			dimensionAccessWhitelistCapability -> source.sendSuccess(
				() -> Component.literal( String.format(
					"Added to dimension \"%s\" whitelist: %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					gameProfilesToString( addTargetsToList( dimensionAccessWhitelistCapability, gameProfiles ) )
				) ),
				true
			)
		);
	}
	
	//package-private
	static void removeTargetsFromWhitelist(
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel,
		@NotNull Collection<GameProfile> gameProfiles ) {
		
		runForWhitelist(
			serverLevel,
			dimensionAccessWhitelistCapability -> source.sendSuccess(
				() -> Component.literal( String.format(
					"Removed from dimension \"%s\" whitelist: %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					gameProfilesToString( removeTargetsFromList( dimensionAccessWhitelistCapability, gameProfiles ) )
				) ),
				true
			)
		);
	}
	
	//package-private
	static void runForBlacklist(
		@NotNull ServerLevel serverLevel,
		@NotNull NonNullConsumer<DimensionAccessBlacklistCapability> runner ) {
		
		serverLevel.getCapability( ModCapabilitiesRegisterFactory.DIMENSION_ACCESS_BLACKLIST ).ifPresent( runner );
	}
	
	//package-private
	static void sendBlacklistList(
		@NotNull DimensionAccessBlacklistCapability dimensionAccessBlacklistCapability,
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel ) {
		
		String gameProfiles = gameProfilesToString( dimensionAccessBlacklistCapability.getGameProfiles() );
		if( gameProfiles.isEmpty() ) {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"Blacklist \"%s\" is empty.",
					ResourceLocationHelper.serverLevelToName( serverLevel )
				) ),
				false
			);
		} else {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"Blacklist \"%s\": %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					gameProfiles
				) ),
				false
			);
		}
	}
	
	//package-private
	static void addTargetsToBlacklist(
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel,
		@NotNull Collection<GameProfile> gameProfiles ) {
		
		runForBlacklist(
			serverLevel,
			dimensionAccessBlacklistCapability -> source.sendSuccess(
				() -> Component.literal( String.format(
					"Added to dimension \"%s\" blacklist: %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					gameProfilesToString( addTargetsToList( dimensionAccessBlacklistCapability, gameProfiles ) )
				) ),
				true
			)
		);
	}
	
	//package-private
	static void removeTargetsFromBlacklist(
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel,
		@NotNull Collection<GameProfile> gameProfiles ) {
		
		runForBlacklist(
			serverLevel,
			dimensionAccessBlacklistCapability -> source.sendSuccess(
				() -> Component.literal( String.format(
					"Removed from dimension \"%s\" blacklist: %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					gameProfilesToString( removeTargetsFromList( dimensionAccessBlacklistCapability, gameProfiles ) )
				) ),
				true
			)
		);
	}
}
