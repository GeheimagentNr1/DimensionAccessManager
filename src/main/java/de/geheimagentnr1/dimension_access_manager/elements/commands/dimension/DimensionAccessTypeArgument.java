package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import net.minecraft.command.ISuggestionProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class DimensionAccessTypeArgument implements ArgumentType<DimensionAccessType> {
	
	
	public static final String registry_name = "dimension_access_type";
	
	private final String[] values;
	
	public static DimensionAccessTypeArgument dimensionAccessType() {
		
		return new DimensionAccessTypeArgument();
	}
	
	//package-private
	@SuppressWarnings( "SameParameterValue" )
	public static <S> DimensionAccessType getDimensionAccessType( CommandContext<S> context, String name ) {
		
		return context.getArgument( name, DimensionAccessType.class );
	}
	
	private DimensionAccessTypeArgument() {
		
		values = Arrays.stream( DimensionAccessType.values() )
			.map( Enum::name )
			.toArray( String[]::new );
	}
	
	@Override
	public DimensionAccessType parse( StringReader reader ) {
		
		return DimensionAccessType.valueOf( reader.readUnquotedString() );
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(
		CommandContext<S> context,
		SuggestionsBuilder builder ) {
		
		return ISuggestionProvider.suggest( values, builder );
	}
	
	@Override
	public Collection<String> getExamples() {
		
		return Arrays.stream( values ).collect( Collectors.toList() );
	}
}
