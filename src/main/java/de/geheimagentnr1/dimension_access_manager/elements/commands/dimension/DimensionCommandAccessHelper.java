package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.dimension_access_manager.util.DimensionHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.NonNullConsumer;


public class DimensionCommandAccessHelper {
	
	
	
	//package-private
	static void runForAccess(
		ServerWorld serverWorld,
		NonNullConsumer<DimensionAccessCapability> runner ) {
		
		serverWorld.getCapability( ModCapabilities.DIMENSION_ACCESS ).ifPresent( runner );
	}
	
	public static void showDimensionStatus( CommandSource source, DimensionType dimension, ServerWorld serverWorld ) {
		
		runForAccess( serverWorld, dimensionAccessCapability -> {
			if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.GRANTED ) {
				DimensionCommandPlayersHelper.runForBlacklist(
					serverWorld,
					dimensionAccessBlacklistCapability ->
						sendDimensionStatusFeedback(
							source,
							dimension,
							DimensionAccessType.GRANTED,
							isListed( source, dimensionAccessBlacklistCapability )
						)
				);
			} else {
				DimensionCommandPlayersHelper.runForWhitelist(
					serverWorld,
					dimensionAccessWhitelistCapability ->
						sendDimensionStatusFeedback(
							source,
							dimension,
							DimensionAccessType.LOCKED,
							isListed( source, dimensionAccessWhitelistCapability )
						)
				);
			}
		} );
	}
	
	private static boolean isListed(
		CommandSource source,
		DimensionAccessListCapability dimensionAccessListCapability ) {
		
		Entity entity = source.getEntity();
		if( entity instanceof ServerPlayerEntity ) {
			return dimensionAccessListCapability.contains( ( (ServerPlayerEntity)entity ).getGameProfile() );
		}
		return false;
	}
	
	private static void sendDimensionStatusFeedback(
		CommandSource source,
		DimensionType dimension,
		DimensionAccessType dimensionStatus,
		boolean isListed ) {
		
		if( isListed ) {
			source.sendFeedback(
				new StringTextComponent( String.format(
					"\"%s\": Access is %s. For you %s",
					DimensionHelper.dimensionTypeToName( dimension ),
					dimensionStatus.getLowerCase(),
					( dimensionStatus == DimensionAccessType.GRANTED
						? DimensionAccessType.LOCKED
						: DimensionAccessType.GRANTED ).getLowerCase()
				) ),
				false
			);
		} else {
			source.sendFeedback(
				new StringTextComponent( String.format(
					"\"%s\": Access is %s.",
					DimensionHelper.dimensionTypeToName( dimension ),
					dimensionStatus.getLowerCase()
				) ),
				false
			);
		}
	}
	
	//package-private
	static void sendDimensionAccessChangedFeedback(
		CommandSource source,
		DimensionType dimension,
		DimensionAccessCapability dimensionAccessCapability ) {
		
		source.sendFeedback(
			new StringTextComponent( String.format(
				"%s is now %s.",
				DimensionHelper.dimensionTypeToName( dimension ),
				dimensionAccessCapability.getDimensionAccess().getLowerCase()
			) ),
			true
		);
	}
}
