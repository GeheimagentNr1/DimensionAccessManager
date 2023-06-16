package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import de.geheimagentnr1.dimension_access_manager.util.ResourceLocationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.NonNullConsumer;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//package-private
class DimensionCommandPlayersHelper {
	
	
	private static String gameProfilesToString( TreeSet<GameProfile> gameProfiles ) {
		
		return gameProfilesToString( gameProfiles.stream() );
	}
	
	private static String gameProfilesToString( Stream<GameProfile> gameProfiles ) {
		
		return gameProfiles.map( GameProfile::getName ).collect( Collectors.joining( ", " ) );
	}
	
	private static Stream<GameProfile> addTargetsToList(
		DimensionAccessListCapability dimensionAccessListCapability,
		Collection<GameProfile> gameProfiles ) {
		
		return gameProfiles.stream().filter( dimensionAccessListCapability::add );
	}
	
	private static Stream<GameProfile> removeTargetsFromList(
		DimensionAccessListCapability dimensionAccessListCapability,
		Collection<GameProfile> gameProfiles ) {
		
		return gameProfiles.stream().filter( dimensionAccessListCapability::remove );
	}
	
	//package-private
	static void runForWhitelist(
		ServerLevel serverLevel,
		NonNullConsumer<DimensionAccessWhitelistCapability> runner ) {
		
		serverLevel.getCapability( ModCapabilities.DIMENSION_ACCESS_WHITELIST ).ifPresent( runner );
	}
	
	//package-private
	static void sendWhitelistList(
		DimensionAccessWhitelistCapability dimensionAccessWhitelistCapability,
		CommandSourceStack source,
		ServerLevel serverLevel ) {
		
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
		CommandSourceStack source,
		ServerLevel serverLevel,
		Collection<GameProfile> gameProfiles ) {
		
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
		CommandSourceStack source,
		ServerLevel serverLevel,
		Collection<GameProfile> gameProfiles ) {
		
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
		ServerLevel serverLevel,
		NonNullConsumer<DimensionAccessBlacklistCapability> runner ) {
		
		serverLevel.getCapability( ModCapabilities.DIMENSION_ACCESS_BLACKLIST ).ifPresent( runner );
	}
	
	//package-private
	static void sendBlacklistList(
		DimensionAccessBlacklistCapability dimensionAccessBlacklistCapability,
		CommandSourceStack source,
		ServerLevel serverLevel ) {
		
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
		CommandSourceStack source,
		ServerLevel serverLevel,
		Collection<GameProfile> gameProfiles ) {
		
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
		CommandSourceStack source,
		ServerLevel serverLevel,
		Collection<GameProfile> gameProfiles ) {
		
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
