package de.geheimagentnr1.dimension_access_manager.elements.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.geheimagentnr1.dimension_access_manager.elements.commands.dimension.DimensionCommandAccessHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;


@SuppressWarnings( "SameReturnValue" )
public class DimensionsStatusCommand {
	
	
	public static void register( CommandDispatcher<CommandSource> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSource> granted_dimensions = Commands.literal( "dimensions_status" );
		granted_dimensions.executes( DimensionsStatusCommand::showDimensionsStatus );
		dispatcher.register( granted_dimensions );
	}
	
	private static int showDimensionsStatus( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		MinecraftServer server = source.getServer();
		DimensionType.getAll().forEach( dimension -> {
			ServerWorld serverWorld = server.func_71218_a( dimension );
			DimensionCommandAccessHelper.showDimensionStatus( source, dimension, serverWorld );
		} );
		return Command.SINGLE_SUCCESS;
	}
}
