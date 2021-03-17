package de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.dimension_access_whitelist;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.ModCapabilities;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access_list.DimensionAccessListCapability;
import de.geheimagentnr1.dimension_access_manager.util.ResourceLocationHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;


public class DimensionAccessWhitelistCapability extends DimensionAccessListCapability {
	
	
	public static final ResourceLocation registry_name = ResourceLocationHelper.build( "dimension_access_whitelist" );
	
	@Override
	protected Capability<DimensionAccessWhitelistCapability> getCapability() {
		
		return ModCapabilities.DIMENSION_ACCESS_WHITELIST;
	}
}
