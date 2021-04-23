package de.geheimagentnr1.dimension_access_manager.handlers;

import com.mojang.authlib.GameProfile;
import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.commands.DimensionsCommand;
import de.geheimagentnr1.dimension_access_manager.elements.commands.ModArgumentTypes;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;


@Mod.EventBusSubscriber( modid = DimensionAccessManager.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE )
public class ForgeEventManager {
	
	
	@SubscribeEvent
	public static void handleWorldAttachCapabilitiesEvent( AttachCapabilitiesEvent<World> event ) {
		
		event.addCapability( DimensionAccessCapability.registry_name, new DimensionAccessCapability() );
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
	public static void handlerServerStartingEvent( FMLServerStartingEvent event ) {
		
		ModArgumentTypes.registerArgumentTypes();
		DimensionCommand.register( event.getCommandDispatcher() );
		DimensionsCommand.register( event.getCommandDispatcher() );
	}
	
	@SubscribeEvent
	public static void handleEntityTravelToDimensionEvent( EntityTravelToDimensionEvent event ) {
		
		Entity entity = event.getEntity();
		ServerWorld serverWorld = ServerLifecycleHooks.getCurrentServer().func_71218_a( event.getDimension() );
		
		if( entity instanceof ServerPlayerEntity ) {
			GameProfile gameProfile = ( (ServerPlayerEntity)entity ).getGameProfile();
			serverWorld.getCapability( ModCapabilities.DIMENSION_ACCESS ).ifPresent( dimensionAccessCapability -> {
				if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.GRANTED ) {
					serverWorld.getCapability( ModCapabilities.DIMENSION_ACCESS_BLACKLIST ).ifPresent(
						dimensionAccessBlacklistCapability -> {
							if( dimensionAccessBlacklistCapability.contains( gameProfile ) ) {
								event.setResult( Event.Result.DENY );
								event.setCanceled( true );
							}
						} );
				} else {
					serverWorld.getCapability( ModCapabilities.DIMENSION_ACCESS_WHITELIST ).ifPresent(
						dimensionAccessWhitelistCapability -> {
							if( !dimensionAccessWhitelistCapability.contains( gameProfile ) ) {
								event.setResult( Event.Result.DENY );
								event.setCanceled( true );
							}
						} );
				}
			} );
		} else {
			serverWorld.getCapability( ModCapabilities.DIMENSION_ACCESS ).ifPresent( dimensionAccessCapability -> {
				if( dimensionAccessCapability.getDimensionAccess() == DimensionAccessType.LOCKED ) {
					event.setResult( Event.Result.DENY );
					event.setCanceled( true );
				}
			} );
		}
	}
}
