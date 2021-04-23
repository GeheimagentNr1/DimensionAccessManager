package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;


//package-private
@FunctionalInterface
interface DimensionCommandRunner {
	
	
	//public
	void run(
		CommandContext<CommandSource> context,
		CommandSource source,
		MinecraftServer server,
		ServerWorld serverWorld );
	
	//public
	static void run( CommandContext<CommandSource> context, DimensionCommandRunner runner )
		throws CommandSyntaxException {
		
		CommandSource source = context.getSource();
		MinecraftServer server = source.getServer();
		ServerWorld serverWorld = DimensionArgument.getDimensionArgument( context, "dimension" );
		
		runner.run( context, source, server, serverWorld );
	}
}
