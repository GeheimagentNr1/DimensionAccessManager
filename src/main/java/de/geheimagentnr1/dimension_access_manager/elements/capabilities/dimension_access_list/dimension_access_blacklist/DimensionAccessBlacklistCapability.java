package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_blacklist;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.dimension_access_manager.util.ResourceLocationBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;


public class DimensionAccessBlacklistCapability extends DimensionAccessListCapability {
	
	
	public static final ResourceLocation registry_name = ResourceLocationBuilder.build( "dimension_access_blacklist" );
	
	@Override
	protected Capability<DimensionAccessBlacklistCapability> getCapability() {
		
		return ModCapabilities.DIMENSION_ACCESS_BLACKLIST;
	}
}
