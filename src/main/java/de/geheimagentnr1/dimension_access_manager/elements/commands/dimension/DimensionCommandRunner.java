package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.server.level.ServerLevel;


//package-private
@FunctionalInterface
interface DimensionCommandRunner {
	
	
	//public
	void run(
		CommandContext<CommandSourceStack> context,
		CommandSourceStack source,
		ServerLevel serverLevel );
	
	//public
	static void run( CommandContext<CommandSourceStack> context, DimensionCommandRunner runner )
		throws CommandSyntaxException {
		
		CommandSourceStack source = context.getSource();
		ServerLevel serverLevel = DimensionArgument.getDimension( context, "dimension" );
		
		runner.run( context, source, serverLevel );
	}
}
