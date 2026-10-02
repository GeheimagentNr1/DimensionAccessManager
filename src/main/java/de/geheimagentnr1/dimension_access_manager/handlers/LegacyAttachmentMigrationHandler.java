package de.geheimagentnr1.dimension_access_manager.handlers;

import com.mojang.logging.LogUtils;
import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModAttachmentTypes;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.NoSuchFileException;


//Up to 1.21.5 the level attachments were stored as INBTSerializable<IntTag> and INBTSerializable<ListTag>. Since
//1.21.6 NeoForge only reads compounds (ValueIOSerializable), so the old access settings, whitelists and blacklists
//would be lost. When a level is loaded, the old values are read once from the saved attachment file. The level
//attachments are loaded before LevelEvent.Load and are saved in the new format afterwards, so this only happens once.
public class LegacyAttachmentMigrationHandler {


	@NotNull
	private static final Logger LOGGER = LogUtils.getLogger();

	@NotNull
	private static final String ATTACHMENTS_FILE_NAME = "neoforge_data_attachments";

	@SubscribeEvent
	public void handleLevelLoadEvent( @NotNull LevelEvent.Load event ) {

		if( !( event.getLevel() instanceof ServerLevel level ) ) {
			return;
		}
		CompoundTag attachments;
		try {
			attachments = level.getDataStorage().readTagFromDisk( ATTACHMENTS_FILE_NAME, null, 0 )
				.getCompoundOrEmpty( "data" );
		} catch( NoSuchFileException exception ) {
			return;
		} catch( IOException exception ) {
			LOGGER.error( "Failed to read the level attachments of {}", level.dimension(), exception );
			return;
		}
		if( attachments.get( key( DimensionAccessCapability.registry_name ) ) instanceof NumericTag nbt ) {
			level.getData( ModAttachmentTypes.DIMENSION_ACCESS ).deserializeLegacy( nbt.intValue() );
			LOGGER.info( "Migrated {} of {} to the new save format", key( DimensionAccessCapability.registry_name ), level.dimension() );
		}
		if( attachments.get( key( DimensionAccessWhitelistCapability.registry_name ) ) instanceof ListTag nbt ) {
			level.getData( ModAttachmentTypes.DIMENSION_ACCESS_WHITELIST ).deserializeLegacy( nbt );
			LOGGER.info( "Migrated {} {} entries of {} to the new save format", nbt.size(), key( DimensionAccessWhitelistCapability.registry_name ), level.dimension() );
		}
		if( attachments.get( key( DimensionAccessBlacklistCapability.registry_name ) ) instanceof ListTag nbt ) {
			level.getData( ModAttachmentTypes.DIMENSION_ACCESS_BLACKLIST ).deserializeLegacy( nbt );
			LOGGER.info( "Migrated {} {} entries of {} to the new save format", nbt.size(), key( DimensionAccessBlacklistCapability.registry_name ), level.dimension() );
		}
	}

	@NotNull
	private static String key( @NotNull String name ) {

		return DimensionAccessManager.MODID + ":" + name;
	}
}
