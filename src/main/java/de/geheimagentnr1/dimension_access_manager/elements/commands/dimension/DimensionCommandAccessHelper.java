package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.dimension_access_manager.util.ResourceLocationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.NonNullConsumer;


public class DimensionCommandAccessHelper {
	
	
	//package-private
	static void runForAccess( ServerLevel serverLevel, NonNullConsumer<DimensionAccessCapability> runner ) {
		
		serverLevel.getCapability( ModCapabilities.DIMENSION_ACCESS ).ifPresent( runner );
	}
	
	public static void showDimensionStatus( CommandSourceStack source, ServerLevel serverLevel ) {
		
		runForAccess( serverLevel, dimensionAccessCapability -> {
			if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.GRANTED ) {
				DimensionCommandPlayersHelper.runForBlacklist(
					serverLevel,
					dimensionAccessBlacklistCapability -> sendDimensionStatusFeedback(
						source,
						serverLevel,
						DimensionAccessType.GRANTED,
						isListed( source, dimensionAccessBlacklistCapability )
					)
				);
			} else {
				DimensionCommandPlayersHelper.runForWhitelist(
					serverLevel,
					dimensionAccessWhitelistCapability -> sendDimensionStatusFeedback(
						source,
						serverLevel,
						DimensionAccessType.LOCKED,
						isListed( source, dimensionAccessWhitelistCapability )
					)
				);
			}
		} );
	}
	
	private static boolean isListed(
		CommandSourceStack source,
		DimensionAccessListCapability dimensionAccessListCapability ) {
		
		Entity entity = source.getEntity();
		if( entity instanceof ServerPlayer ) {
			return dimensionAccessListCapability.contains( ( (ServerPlayer)entity ).getGameProfile() );
		}
		return false;
	}
	
	private static void sendDimensionStatusFeedback(
		CommandSourceStack source,
		ServerLevel serverLevel,
		DimensionAccessType dimensionStatus,
		boolean isListed ) {
		
		if( isListed ) {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"\"%s\": Access is %s. For you %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					dimensionStatus.getLowerCase(),
					( dimensionStatus == DimensionAccessType.GRANTED
						? DimensionAccessType.LOCKED
						: DimensionAccessType.GRANTED ).getLowerCase()
				) ),
				false
			);
		} else {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"\"%s\": Access is %s.",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					dimensionStatus.getLowerCase()
				) ),
				false
			);
		}
	}
	
	//package-private
	static void sendDimensionAccessChangedFeedback(
		CommandSourceStack source,
		ServerLevel serverLevel,
		DimensionAccessCapability dimensionAccessCapability ) {
		
		source.sendSuccess(
			() -> Component.literal( String.format(
				"%s is now %s.",
				ResourceLocationHelper.serverLevelToName( serverLevel ),
				dimensionAccessCapability.getDimensionAccess().getLowerCase()
			) ),
			true
		);
	}
}
