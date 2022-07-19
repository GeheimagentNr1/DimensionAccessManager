package de.geheimagentnr1.dimension_access_manager;

import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionAccessTypeArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


@SuppressWarnings( "UtilityClassWithPublicConstructor" )
@Mod( DimensionAccessManager.MODID )
public class DimensionAccessManager {
	
	
	public static final String MODID = "dimension_access_manager";
	
	private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(
		Registry.COMMAND_ARGUMENT_TYPE_REGISTRY,
		MODID
	);
	
	private static final RegistryObject<SingletonArgumentInfo<DimensionAccessTypeArgument>>
		DIMENSION_ACCESS_TYPE_COMMAND_ARGUMENT_TYPE =
		COMMAND_ARGUMENT_TYPES.register(
			DimensionAccessTypeArgument.registry_name,
			() -> ArgumentTypeInfos.registerByClass(
				DimensionAccessTypeArgument.class,
				SingletonArgumentInfo.contextFree( DimensionAccessTypeArgument::dimensionAccessType )
			)
		);
	
	public DimensionAccessManager() {
		
		ModLoadingContext.get().registerConfig( ModConfig.Type.SERVER, ServerConfig.CONFIG );
		COMMAND_ARGUMENT_TYPES.register( FMLJavaModLoadingContext.get().getModEventBus());
	}
}
