package de.geheimagentnr1.dimension_access_manager.elements.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.geheimagentnr1.dimension_access_manager.config.ModConfig;
import de.geheimagentnr1.dimension_access_manager.util.TextHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;


public class DimensionManageCommand {
	
	
	public static void register( CommandDispatcher<CommandSource> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSource> dimension = Commands.literal( "dimension" );
		dimension.then( Commands.literal( "status" )
			.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
				.executes( manageCommand() ) ) );
		LiteralArgumentBuilder<CommandSource> manage = dimension.requires( source -> source.hasPermissionLevel( 3 ) );
		manage.then( Commands.literal( "grant" )
			.then( Commands.argument( "dimension", DimensionArgument.getDimension() ).executes( grantCommand() ) ) );
		manage.then( Commands.literal( "lock" )
			.then( Commands.argument( "dimension", DimensionArgument.getDimension() ).executes( lockCommand() ) ) );
		dispatcher.register( dimension );
	}
	
	private static Command<CommandSource> manageCommand() {
		
		return context -> {
			RegistryKey<World> dimension = DimensionArgument.getDimensionArgument( context, "dimension" )
				.func_234923_W_();
			context.getSource().sendFeedback( new StringTextComponent( "Access of " )
				.func_240702_b_( TextHelper.dimensionTypeToName( dimension ) )
				.func_240702_b_( TextHelper.getIsAccessText( ModConfig.isAllowedDimision( dimension ) ) ), false );
			return 0;
		};
	}
	
	private static Command<CommandSource> grantCommand() {
		
		return context -> {
			RegistryKey<World> dimension = DimensionArgument.getDimensionArgument( context, "dimension" )
				.func_234923_W_();
			ModConfig.setAccess( dimension, true );
			context.getSource().sendFeedback( new StringTextComponent( "Access of " )
					.func_240702_b_( TextHelper.dimensionTypeToName( dimension ) ).func_240702_b_( " is now granted" +
						"." ),
				true );
			return 0;
		};
	}
	
	private static Command<CommandSource> lockCommand() {
		
		return context -> {
			RegistryKey<World> dimension = DimensionArgument.getDimensionArgument( context, "dimension" )
				.func_234923_W_();
			ModConfig.setAccess( dimension, false );
			context.getSource().sendFeedback( new StringTextComponent( "Access of " )
					.func_240702_b_( TextHelper.dimensionTypeToName( dimension ) ).func_240702_b_( " is now locked." ),
				true );
			return 0;
		};
	}
}
