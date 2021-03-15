package de.geheimagentnr1.dimension_access_manager.elements.capabilities;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist.DimensionAccessBlacklistCapability;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist.DimensionAccessWhitelistCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;


@SuppressWarnings( "StaticNonFinalField" )
public class ModCapabilities {
	
	
	@CapabilityInject( DimensionAccessCapability.class )
	public static Capability<DimensionAccessCapability> DIMENSION_ACCESS;
	
	@CapabilityInject( DimensionAccessBlacklistCapability.class )
	public static Capability<DimensionAccessBlacklistCapability> DIMENSION_ACCESS_BLACKLIST;
	
	@CapabilityInject( DimensionAccessWhitelistCapability.class )
	public static Capability<DimensionAccessWhitelistCapability> DIMENSION_ACCESS_WHITELIST;
}
