package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilitiesRegisterFactory;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;


public class DimensionAccessBlacklistCapability extends DimensionAccessListCapability {
	
	
	@NotNull
	public static final String registry_name = "dimension_access_blacklist";
	
	@NotNull
	@Override
	protected Capability<DimensionAccessBlacklistCapability> getCapability() {
		
		return ModCapabilitiesRegisterFactory.DIMENSION_ACCESS_BLACKLIST;
	}
}
