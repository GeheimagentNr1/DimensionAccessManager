package de.geheimagentnr1.dimension_access_manager.handlers;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModAttachmentTypes;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;


public class DimensionAccessHandler {
	
	
	@SubscribeEvent
	public void handleEntityTravelToDimensionEvent( @NotNull EntityTravelToDimensionEvent event ) {
		
		Entity entity = event.getEntity();
		ServerLevel serverLevel = ServerLifecycleHooks.getCurrentServer().getLevel( event.getDimension() );
		
		if( serverLevel != null ) {
			DimensionAccessCapability dimensionAccessCapability = serverLevel.getData( ModAttachmentTypes.DIMENSION_ACCESS );
			
			if( entity instanceof ServerPlayer serverPlayer ) {
				GameProfile gameProfile = serverPlayer.getGameProfile();
				
				if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.GRANTED ) {
					DimensionAccessBlacklistCapability blacklistCapability = 
						serverLevel.getData( ModAttachmentTypes.DIMENSION_ACCESS_BLACKLIST );
					if( blacklistCapability.contains( gameProfile ) ) {
						event.setCanceled( true );
					}
				} else {
					DimensionAccessWhitelistCapability whitelistCapability = 
						serverLevel.getData( ModAttachmentTypes.DIMENSION_ACCESS_WHITELIST );
					if( !whitelistCapability.contains( gameProfile ) ) {
						event.setCanceled( true );
					}
				}
			} else {
				if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.LOCKED ) {
					event.setCanceled( true );
				}
			}
		}
	}
}
