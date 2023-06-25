package de.geheimagentnr1.dimension_access_manager.config;

import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.minecraft_forge_api.config.AbstractConfig;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.NotNull;


public class ServerConfig extends AbstractConfig {
	
	
	@NotNull
	private static final String DEFAULT_DIMENSION_ACCESS_TYPE_KEY = "default_dimension_access_type";
	
	public ServerConfig( @NotNull AbstractMod _abstractMod ) {
		
		super( _abstractMod );
	}
	
	@NotNull
	@Override
	public ModConfig.Type type() {
		
		return ModConfig.Type.SERVER;
	}
	
	@Override
	public boolean isEarlyLoad() {
		
		return false;
	}
	
	@Override
	protected void registerConfigValues() {
		
		registerConfigValue(
			"Defines if new dimensions are GRANTED or LOCKED by default.",
			DEFAULT_DIMENSION_ACCESS_TYPE_KEY,
			( builder, path ) -> builder.defineEnum( path, DimensionAccessType.GRANTED )
		);
	}
	
	@NotNull
	public DimensionAccessType getDefaultDimensionAccessType() {
		
		return getValue( DimensionAccessType.class, DEFAULT_DIMENSION_ACCESS_TYPE_KEY );
	}
	
	public void setDefaultDimensionAccessType( @NotNull DimensionAccessType _defaultDimensionAccessType ) {
		
		setValue( DimensionAccessType.class, DEFAULT_DIMENSION_ACCESS_TYPE_KEY, _defaultDimensionAccessType );
	}
}
