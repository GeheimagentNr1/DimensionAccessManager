package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import de.geheimagentnr1.dimension_access_manager.util.DimensionHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
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
		
		return gameProfiles.map( GameProfile::getName )
			.collect( Collectors.joining( ", " ) );
	}
	
	private static Stream<GameProfile> addTargetsToList(
		DimensionAccessListCapability dimensionAccessListCapability,
		Collection<GameProfile> gameProfiles ) {
		
		return gameProfiles.stream()
			.filter( dimensionAccessListCapability::add );
	}
	
	private static Stream<GameProfile> removeTargetsFromList(
		DimensionAccessListCapability dimensionAccessListCapability,
		Collection<GameProfile> gameProfiles ) {
		
		return gameProfiles.stream()
			.filter( dimensionAccessListCapability::remove );
	}
	
	//package-private
	static void runForWhitelist(
		ServerWorld serverWorld,
		NonNullConsumer<DimensionAccessWhitelistCapability> runner ) {
		
		serverWorld.getCapability( ModCapabilities.DIMENSION_ACCESS_WHITELIST ).ifPresent( runner );
	}
	
	//package-private
	static void sendWhitelistList(
		DimensionAccessWhitelistCapability dimensionAccessWhitelistCapability,
		CommandSource source,
		DimensionType dimension ) {
		
		String gameProfiles = gameProfilesToString( dimensionAccessWhitelistCapability.getGameProfiles() );
		if( gameProfiles.isEmpty() ) {
			source.sendFeedback(
				new StringTextComponent( String.format(
					"Whitelist \"%s\" is empty.",
					DimensionHelper.dimensionTypeToName( dimension )
				) ),
				false
			);
		} else {
			source.sendFeedback(
				new StringTextComponent( String.format(
					"Whitelist \"%s\": %s",
					DimensionHelper.dimensionTypeToName( dimension ),
					gameProfiles
				) ),
				false
			);
		}
	}
	
	//package-private
	static void addTargetsToWhitelist(
		CommandSource source,
		DimensionType dimension,
		ServerWorld serverWorld,
		Collection<GameProfile> gameProfiles ) {
		
		runForWhitelist(
			serverWorld,
			dimensionAccessWhitelistCapability -> source.sendFeedback(
				new StringTextComponent( String.format(
					"Added to dimension \"%s\" whitelist: %s",
					DimensionHelper.dimensionTypeToName( dimension ),
					gameProfilesToString( addTargetsToList(
						dimensionAccessWhitelistCapability,
						gameProfiles
					) )
				)
				),
				true
			)
		);
	}
	
	//package-private
	static void removeTargetsFromWhitelist(
		CommandSource source,
		DimensionType dimension,
		ServerWorld serverWorld,
		Collection<GameProfile> gameProfiles ) {
		
		runForWhitelist(
			serverWorld,
			dimensionAccessWhitelistCapability -> source.sendFeedback(
				new StringTextComponent( String.format(
					"Removed from dimension \"%s\" whitelist: %s",
					DimensionHelper.dimensionTypeToName( dimension ),
					gameProfilesToString( removeTargetsFromList(
						dimensionAccessWhitelistCapability,
						gameProfiles
					) )
				)
				),
				true
			)
		);
	}
	
	//package-private
	static void runForBlacklist(
		ServerWorld serverWorld,
		NonNullConsumer<DimensionAccessBlacklistCapability> runner ) {
		
		serverWorld.getCapability( ModCapabilities.DIMENSION_ACCESS_BLACKLIST ).ifPresent( runner );
	}
	
	//package-private
	static void sendBlacklistList(
		DimensionAccessBlacklistCapability dimensionAccessBlacklistCapability,
		CommandSource source,
		DimensionType dimension ) {
		
		String gameProfiles = gameProfilesToString( dimensionAccessBlacklistCapability.getGameProfiles() );
		if( gameProfiles.isEmpty() ) {
			source.sendFeedback(
				new StringTextComponent( String.format(
					"Blacklist \"%s\" is empty.",
					DimensionHelper.dimensionTypeToName( dimension )
				) ),
				false
			);
		} else {
			source.sendFeedback(
				new StringTextComponent( String.format(
					"Blacklist \"%s\": %s",
					DimensionHelper.dimensionTypeToName( dimension ),
					gameProfiles
				) ),
				false
			);
		}
	}
	
	//package-private
	static void addTargetsToBlacklist(
		CommandSource source,
		DimensionType dimension,
		ServerWorld serverWorld,
		Collection<GameProfile> gameProfiles ) {
		
		runForBlacklist(
			serverWorld,
			dimensionAccessBlacklistCapability -> source.sendFeedback(
				new StringTextComponent( String.format(
					"Added to dimension \"%s\" blacklist: %s",
					DimensionHelper.dimensionTypeToName( dimension ),
					gameProfilesToString( addTargetsToList(
						dimensionAccessBlacklistCapability,
						gameProfiles
					) )
				)
				),
				true
			)
		);
	}
	
	//package-private
	static void removeTargetsFromBlacklist(
		CommandSource source,
		DimensionType dimension,
		ServerWorld serverWorld,
		Collection<GameProfile> gameProfiles ) {
		
		runForBlacklist(
			serverWorld,
			dimensionAccessBlacklistCapability -> source.sendFeedback(
				new StringTextComponent( String.format(
					"Removed from dimension \"%s\" blacklist: %s",
					DimensionHelper.dimensionTypeToName( dimension ),
					gameProfilesToString( removeTargetsFromList(
						dimensionAccessBlacklistCapability,
						gameProfiles
					) )
				)
				),
				true
			)
		);
	}
}
