package de.geheimagentnr1.dimension_access_manager.handlers;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.commands.DimensionsCommand;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;


@Mod.EventBusSubscriber( modid = DimensionAccessManager.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE )
public class ForgeEventHandler {
	
	
	@SubscribeEvent
	public static void handleWorldAttachCapabilitiesEvent( AttachCapabilitiesEvent<Level> event ) {
		
		event.addCapability(
			DimensionAccessCapability.registry_name,
			new DimensionAccessCapability()
		);
		event.addCapability(
			DimensionAccessBlacklistCapability.registry_name,
			new DimensionAccessBlacklistCapability()
		);
		event.addCapability(
			DimensionAccessWhitelistCapability.registry_name,
			new DimensionAccessWhitelistCapability()
		);
	}
	
	@SubscribeEvent
	public static void handleRegisterCommandsEvent( RegisterCommandsEvent event ) {
		
		DimensionCommand.register( event.getDispatcher() );
		DimensionsCommand.register( event.getDispatcher() );
	}
	
	@SubscribeEvent
	public static void handleEntityTravelToDimensionEvent( EntityTravelToDimensionEvent event ) {
		
		Entity entity = event.getEntity();
		ServerLevel serverLevel = ServerLifecycleHooks.getCurrentServer().getLevel( event.getDimension() );
		
		if( serverLevel != null ) {
			if( entity instanceof ServerPlayer ) {
				GameProfile gameProfile = ( (ServerPlayer)entity ).getGameProfile();
				serverLevel.getCapability( ModCapabilities.DIMENSION_ACCESS ).ifPresent( dimensionAccessCapability -> {
					if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.GRANTED ) {
						serverLevel.getCapability( ModCapabilities.DIMENSION_ACCESS_BLACKLIST ).ifPresent(
							dimensionAccessBlacklistCapability -> {
								if( dimensionAccessBlacklistCapability.contains( gameProfile ) ) {
									event.setResult( Event.Result.DENY );
									event.setCanceled( true );
								}
							} );
					} else {
						serverLevel.getCapability( ModCapabilities.DIMENSION_ACCESS_WHITELIST ).ifPresent(
							dimensionAccessWhitelistCapability -> {
								if( !dimensionAccessWhitelistCapability.contains( gameProfile ) ) {
									event.setResult( Event.Result.DENY );
									event.setCanceled( true );
								}
							} );
					}
				} );
			} else {
				serverLevel.getCapability( ModCapabilities.DIMENSION_ACCESS ).ifPresent( dimensionAccessCapability -> {
					if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.LOCKED ) {
						event.setResult( Event.Result.DENY );
						event.setCanceled( true );
					}
				} );
			}
		}
	}
}
