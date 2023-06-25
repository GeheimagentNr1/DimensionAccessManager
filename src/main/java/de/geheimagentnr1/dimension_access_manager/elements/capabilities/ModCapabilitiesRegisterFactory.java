package de.geheimagentnr1.dimension_access_manager.elements.capabilities;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.minecraft_forge_api.elements.capabilities.CapabilitiesRegisterFactory;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryEntry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ModCapabilitiesRegisterFactory extends CapabilitiesRegisterFactory {
	
	
	@NotNull
	public static final Capability<DimensionAccessCapability> DIMENSION_ACCESS =
		CapabilityManager.get( new CapabilityToken<>() {
		
		} );
	
	@NotNull
	public static final Capability<DimensionAccessBlacklistCapability> DIMENSION_ACCESS_BLACKLIST =
		CapabilityManager.get( new CapabilityToken<>() {
		
		} );
	
	@NotNull
	public static final Capability<DimensionAccessWhitelistCapability> DIMENSION_ACCESS_WHITELIST =
		CapabilityManager.get( new CapabilityToken<>() {
		
		} );
	
	@NotNull
	private final ServerConfig serverConfig;
	
	public ModCapabilitiesRegisterFactory( @NotNull AbstractMod abstractMod, @NotNull ServerConfig _serverConfig ) {
		
		super( abstractMod );
		serverConfig = _serverConfig;
	}
	
	@NotNull
	@Override
	protected List<Class<?>> capabilityClasses() {
		
		return List.of(
			DimensionAccessCapability.class,
			DimensionAccessBlacklistCapability.class,
			DimensionAccessWhitelistCapability.class
		);
	}
	
	@NotNull
	@Override
	protected List<RegistryEntry<ICapabilityProvider>> capabilities() {
		
		return List.of(
			RegistryEntry.create(
				DimensionAccessCapability.registry_name,
				new DimensionAccessCapability( serverConfig )
			),
			RegistryEntry.create(
				DimensionAccessBlacklistCapability.registry_name,
				new DimensionAccessBlacklistCapability()
			),
			RegistryEntry.create(
				DimensionAccessWhitelistCapability.registry_name,
				new DimensionAccessWhitelistCapability()
			)
		);
	}
}
