package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;


//package-private
@FunctionalInterface
interface DimensionCommandRunner {
	
	
	//public
	void run(
		@NotNull CommandContext<CommandSourceStack> context,
		@NotNull CommandSourceStack source,
		@NotNull ServerLevel serverLevel );
	
	//public
	static void run( @NotNull CommandContext<CommandSourceStack> context, @NotNull DimensionCommandRunner runner )
		throws CommandSyntaxException {
		
		CommandSourceStack source = context.getSource();
		ServerLevel serverLevel = DimensionArgument.getDimension( context, "dimension" );
		
		runner.run( context, source, serverLevel );
	}
}
