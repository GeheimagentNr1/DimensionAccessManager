package de.geheimagentnr1.dimension_access_manager.elements.capabilities;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


public class ModAttachmentTypes {
	
	
	@NotNull
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
		DeferredRegister.create( NeoForgeRegistries.ATTACHMENT_TYPES, DimensionAccessManager.MODID );
	
	@NotNull
	public static final Supplier<AttachmentType<DimensionAccessCapability>> DIMENSION_ACCESS =
		ATTACHMENT_TYPES.register(
			DimensionAccessCapability.registry_name,
			() -> AttachmentType.serializable( DimensionAccessCapability::new ).build()
		);
	
	@NotNull
	public static final Supplier<AttachmentType<DimensionAccessBlacklistCapability>> DIMENSION_ACCESS_BLACKLIST =
		ATTACHMENT_TYPES.register(
			DimensionAccessBlacklistCapability.registry_name,
			() -> AttachmentType.serializable( DimensionAccessBlacklistCapability::new ).build()
		);
	
	@NotNull
	public static final Supplier<AttachmentType<DimensionAccessWhitelistCapability>> DIMENSION_ACCESS_WHITELIST =
		ATTACHMENT_TYPES.register(
			DimensionAccessWhitelistCapability.registry_name,
			() -> AttachmentType.serializable( DimensionAccessWhitelistCapability::new ).build()
		);
}
