package de.geheimagentnr1.dimension_access_manager.elements.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.geheimagentnr1.dimension_access_manager.config.ServerConfig;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionAccessTypeArgument;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionCommandAccessHelper;
import de.geheimagentnr1.minecraft_forge_api.elements.commands.CommandInterface;
import lombok.RequiredArgsConstructor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings( "SameReturnValue" )
@RequiredArgsConstructor
public class DimensionsCommand implements CommandInterface {
	
	
	@NotNull
	private final ServerConfig serverConfig;
	
	@NotNull
	@Override
	public LiteralArgumentBuilder<CommandSourceStack> build() {
		
		LiteralArgumentBuilder<CommandSourceStack> dimensions = Commands.literal( "dimensions" );
		dimensions.then( Commands.literal( "status" )
			.executes( this::showDimensionsStatus ) );
		dimensions.then( Commands.literal( "default" )
			.requires( source -> source.hasPermission( 3 ) )
			.then( Commands.literal( "defaultDimensionAccessType" )
				.executes( this::showDefaultDimensionAccessType )
				.then( Commands.argument( "dimensionAccessType", DimensionAccessTypeArgument.dimensionAccessType() )
					.executes( this::setDefaultDimensionAccessType ) ) ) );
		return dimensions;
	}
	
	private int showDimensionsStatus( @NotNull CommandContext<CommandSourceStack> context ) {
		
		CommandSourceStack source = context.getSource();
		MinecraftServer server = source.getServer();
		server.getAllLevels().forEach( serverLevel ->
			DimensionCommandAccessHelper.showDimensionStatus( source, serverLevel )
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int showDefaultDimensionAccessType( @NotNull CommandContext<CommandSourceStack> context ) {
		
		CommandSourceStack source = context.getSource();
		source.sendSuccess(
			() -> Component.literal( String.format(
				"The default dimension access type is %s.",
				serverConfig.getDefaultDimensionAccessType()
			) ),
			false
		);
		return Command.SINGLE_SUCCESS;
	}
	
	private int setDefaultDimensionAccessType( @NotNull CommandContext<CommandSourceStack> context ) {
		
		CommandSourceStack source = context.getSource();
		serverConfig.setDefaultDimensionAccessType(
			DimensionAccessTypeArgument.getDimensionAccessType( context, "dimensionAccessType" )
		);
		source.sendSuccess(
			() -> Component.literal( String.format(
				"The default dimension access type is now %s.",
				serverConfig.getDefaultDimensionAccessType()
			) ),
			true
		);
		return Command.SINGLE_SUCCESS;
	}
}
