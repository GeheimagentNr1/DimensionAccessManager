package de.geheimagentnr1.dimension_access_manager.elements.commands;

import de.geheimagentnr1.dimension_access_manager.DimensionAccessManager;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionAccessTypeArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;


public class ModArgumentTypesRegisterFactory {
	
	
	@NotNull
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES =
		DeferredRegister.create( Registries.COMMAND_ARGUMENT_TYPE, DimensionAccessManager.MODID );
	
	static {
		ARGUMENT_TYPES.register(
			DimensionAccessTypeArgument.registry_name,
			() -> ArgumentTypeInfos.registerByClass(
				DimensionAccessTypeArgument.class,
				SingletonArgumentInfo.contextFree( DimensionAccessTypeArgument::dimensionAccessType )
			)
		);
	}
	
	@SubscribeEvent
	public void onRegister( @NotNull RegisterEvent event ) {
		
		// Registration is handled by DeferredRegister
	}
}
