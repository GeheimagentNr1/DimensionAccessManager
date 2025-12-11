package de.geheimagentnr1.dimension_access_manager.config;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;


public class ServerConfig {
	
	
	@NotNull
	private final ModConfigSpec spec;
	
	@NotNull
	private final ModConfigSpec.EnumValue<DimensionAccessType> defaultDimensionAccessType;
	
	@NotNull
	private final ModConfigSpec.IntValue dimensionCommandPermissionLevel;
	
	public ServerConfig() {
		
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		
		builder.comment( "Defines if new dimensions are GRANTED or LOCKED by default." );
		defaultDimensionAccessType = builder.defineEnum( "default_dimension_access_type", DimensionAccessType.GRANTED );
		
		builder.comment( "Defines which permission level is required to run the /dimension command" );
		dimensionCommandPermissionLevel = builder.defineInRange( "dimension_command_permission_level", 3, 0, 4 );
		
		spec = builder.build();
	}
	
	@NotNull
	public ModConfigSpec getSpec() {
		
		return spec;
	}
	
	@NotNull
	public DimensionAccessType getDefaultDimensionAccessType() {
		
		return defaultDimensionAccessType.get();
	}
	
	public void setDefaultDimensionAccessType( @NotNull DimensionAccessType _defaultDimensionAccessType ) {
		
		defaultDimensionAccessType.set( _defaultDimensionAccessType );
	}
	
	@NotNull
	public Integer getDimensionCommandPermissionLevel() {
		
		return dimensionCommandPermissionLevel.get();
	}
}
