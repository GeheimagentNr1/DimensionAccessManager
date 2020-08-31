package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.geheimagentnr1.dimension_access_manager.config.DimensionListType;
import de.geheimagentnr1.dimension_access_manager.config.MainConfig;
import de.geheimagentnr1.dimension_access_manager.util.TextHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;


@SuppressWarnings( "SameReturnValue" )
public class DimensionManageCommand {
	
	
	public static void register( CommandDispatcher<CommandSource> dispatcher ) {
		
		LiteralArgumentBuilder<CommandSource> dimension = Commands.literal( "dimension" );
		dimension.then( Commands.literal( "status" )
			.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
				.executes( DimensionManageCommand::showDimensionStatus ) ) );
		LiteralArgumentBuilder<CommandSource> manage = dimension.requires( source -> source.hasPermissionLevel( 3 ) );
		manage.then( Commands.literal( "grant" )
			.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
				.executes( DimensionManageCommand::grantDimension ) ) );
		manage.then( Commands.literal( "lock" )
			.then( Commands.argument( "dimension", DimensionArgument.getDimension() )
				.executes( DimensionManageCommand::lockDimension ) ) );
		manage.then( Commands.literal( "list_type" )
			.executes( DimensionManageCommand::showDimensionListType )
			.then( Commands.argument( "list_type", DimensionListTypeArgument.dimensionListType() )
				.then( Commands.argument( "invert_list", BoolArgumentType.bool() )
					.executes( DimensionManageCommand::changeDimensionListType ) ) ) );
		dispatcher.register( dimension );
	}
	
	private static int showDimensionStatus( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		DimensionType dimension = DimensionArgument.getDimensionArgument( context, "dimension" );
		source.sendFeedback( new StringTextComponent( "Access of " )
			.appendText( TextHelper.dimensionTypeToName( dimension ) )
			.appendText( TextHelper.getIsAccessText( MainConfig.isAllowedDimision( dimension ) ) ), false );
		return Command.SINGLE_SUCCESS;
	}
	
	private static int grantDimension( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		DimensionType dimension = DimensionArgument.getDimensionArgument( context, "dimension" );
		MainConfig.grantDimensionAccess( dimension );
		source.sendFeedback( new StringTextComponent( "Access of " )
				.appendText( TextHelper.dimensionTypeToName( dimension ) ).appendText( " is now granted." ),
			true );
		return Command.SINGLE_SUCCESS;
	}
	
	private static int lockDimension( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		DimensionType dimension = DimensionArgument.getDimensionArgument( context, "dimension" );
		MainConfig.lockDimensionAccess( dimension );
		source.sendFeedback( new StringTextComponent( "Access of " )
			.appendText( TextHelper.dimensionTypeToName( dimension ) ).appendText( " is now locked." ), true );
		return Command.SINGLE_SUCCESS;
	}
	
	private static int showDimensionListType( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		
		source.sendFeedback( new StringTextComponent( "Dimension List Type: " )
			.appendText( MainConfig.getDimensionListType().name() ), false );
		return Command.SINGLE_SUCCESS;
	}
	
	private static int changeDimensionListType( CommandContext<CommandSource> context ) {
		
		CommandSource source = context.getSource();
		DimensionListType dimensionListType = DimensionListTypeArgument.getDimensionListType( context, "list_type" );
		boolean revert = BoolArgumentType.getBool( context, "invert_list" );
		
		MainConfig.setDimensionListType( dimensionListType );
		if( revert ) {
			MainConfig.invertDimensions();
		}
		source.sendFeedback( new StringTextComponent( "Dimension List Type set to: " )
			.appendText( MainConfig.getDimensionListType().name() ), false );
		DimensionType.getAll().forEach( dimensionType ->
			source.sendFeedback( new StringTextComponent( TextHelper.dimensionTypeToName( dimensionType ) )
					.appendText( TextHelper.getIsAccessText( MainConfig.isAllowedDimision( dimensionType ) ) ),
				false ) );
		return Command.SINGLE_SUCCESS;
	}
}
