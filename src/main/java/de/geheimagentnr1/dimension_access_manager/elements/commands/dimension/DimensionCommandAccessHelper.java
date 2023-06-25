package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilitiesRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.minecraft_forge_api.util.ResourceLocationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.NonNullConsumer;
import org.jetbrains.annotations.NotNull;


public class DimensionCommandAccessHelper {
	
	
	//package-private
	static void runForAccess(
		@NotNull ServerLevel serverLevel,
		@NotNull NonNullConsumer<DimensionAccessCapability> runner ) {
		
		serverLevel.getCapability( ModCapabilitiesRegisterFactory.DIMENSION_ACCESS ).ifPresent( runner );
	}
	
	public static void showDimensionStatus( @NotNull CommandSourceStack source, @NotNull ServerLevel serverLevel ) {
		
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
		@NotNull CommandSourceStack source,
		@NotNull DimensionAccessListCapability dimensionAccessListCapability ) {
		
		Entity entity = source.getEntity();
		if( entity instanceof ServerPlayer ) {
			return dimensionAccessListCapability.contains( ( (ServerPlayer)entity ).getGameProfile() );
		}
		return false;
	}
	
	private static void sendDimensionStatusFeedback(
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel,
		@NotNull DimensionAccessType dimensionStatus,
		boolean isListed ) {
		
		if( isListed ) {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"\"%s\": Access is %s. For you %s",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					dimensionStatus.getSerializedName(),
					( dimensionStatus == DimensionAccessType.GRANTED
						? DimensionAccessType.LOCKED
						: DimensionAccessType.GRANTED ).getSerializedName()
				) ),
				false
			);
		} else {
			source.sendSuccess(
				() -> Component.literal( String.format(
					"\"%s\": Access is %s.",
					ResourceLocationHelper.serverLevelToName( serverLevel ),
					dimensionStatus.getSerializedName()
				) ),
				false
			);
		}
	}
	
	//package-private
	static void sendDimensionAccessChangedFeedback(
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel,
		@NotNull DimensionAccessCapability dimensionAccessCapability ) {
		
		source.sendSuccess(
			() -> Component.literal( String.format(
				"%s is now %s.",
				ResourceLocationHelper.serverLevelToName( serverLevel ),
				dimensionAccessCapability.getDimensionAccess().getSerializedName()
			) ),
			true
		);
	}
}
