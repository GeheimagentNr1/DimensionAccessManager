package de.geheimagentnr1.dimension_access_manager.handlers;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilitiesRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.minecraft_forge_api.events.ForgeEventHandlerInterface;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;


@RequiredArgsConstructor
public class DimensionAccessHandler implements ForgeEventHandlerInterface {
	
	
	@SubscribeEvent
	@Override
	public void handleEntityTravelToDimensionEvent( @NotNull EntityTravelToDimensionEvent event ) {
		
		Entity entity = event.getEntity();
		ServerLevel serverLevel = ServerLifecycleHooks.getCurrentServer().getLevel( event.getDimension() );
		
		if( serverLevel != null ) {
			if( entity instanceof ServerPlayer ) {
				GameProfile gameProfile = ( (ServerPlayer)entity ).getGameProfile();
				serverLevel.getCapability( ModCapabilitiesRegisterFactory.DIMENSION_ACCESS ).ifPresent(
					dimensionAccessCapability -> {
						if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.GRANTED ) {
							serverLevel.getCapability( ModCapabilitiesRegisterFactory.DIMENSION_ACCESS_BLACKLIST )
								.ifPresent(
									dimensionAccessBlacklistCapability -> {
										if( dimensionAccessBlacklistCapability.contains( gameProfile ) ) {
											event.setResult( Event.Result.DENY );
											event.setCanceled( true );
										}
									} );
						} else {
							serverLevel.getCapability( ModCapabilitiesRegisterFactory.DIMENSION_ACCESS_WHITELIST )
								.ifPresent(
									dimensionAccessWhitelistCapability -> {
										if( !dimensionAccessWhitelistCapability.contains( gameProfile ) ) {
											event.setResult( Event.Result.DENY );
											event.setCanceled( true );
										}
									} );
						}
					} );
			} else {
				serverLevel.getCapability( ModCapabilitiesRegisterFactory.DIMENSION_ACCESS ).ifPresent(
					dimensionAccessCapability -> {
						if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.LOCKED ) {
							event.setResult( Event.Result.DENY );
							event.setCanceled( true );
						}
					} );
			}
		}
	}
}
