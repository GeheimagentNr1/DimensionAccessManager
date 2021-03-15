package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;


//package-private
@FunctionalInterface
interface DimensionCommandRunner {
	
	
	//public
	void run(
		CommandContext<CommandSource> context,
		CommandSource source,
		DimensionType dimension,
		MinecraftServer server,
		ServerWorld serverWorld );
	//public
	static void run( CommandContext<CommandSource> context, DimensionCommandRunner runner ) {
		
		CommandSource source = context.getSource();
		DimensionType dimension = DimensionArgument.getDimensionArgument( context, "dimension" );
		MinecraftServer server = source.getServer();
		ServerWorld serverWorld = server.func_71218_a( dimension );
		
		runner.run( context, source, dimension, server, serverWorld );
	}
}
