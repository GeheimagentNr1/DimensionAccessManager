package de.geheimagentnr1.dimension_access_manager.elements.commands.dimension;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.geheimagentnr1.dimension_access_manager.elements.capabilities.dimension_access.DimensionAccessType;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class DimensionAccessTypeArgument implements ArgumentType<DimensionAccessType> {
	
	
	@NotNull
	public static final String registry_name = "dimension_access_type";
	
	@NotNull
	private final String[] values;
	
	@NotNull
	public static DimensionAccessTypeArgument dimensionAccessType() {
		
		return new DimensionAccessTypeArgument();
	}
	
	@SuppressWarnings( "SameParameterValue" )
	@NotNull
	public static <S> DimensionAccessType getDimensionAccessType(
		@NotNull CommandContext<S> context,
		@NotNull String name ) {
		
		return context.getArgument( name, DimensionAccessType.class );
	}
	
	private DimensionAccessTypeArgument() {
		
		values = Arrays.stream( DimensionAccessType.values() )
			.map( Enum::name )
			.toArray( String[]::new );
	}
	
	@NotNull
	@Override
	public DimensionAccessType parse( @NotNull StringReader reader ) {
		
		return DimensionAccessType.valueOf( reader.readUnquotedString() );
	}
	
	@NotNull
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(
		@NotNull CommandContext<S> context,
		@NotNull SuggestionsBuilder builder ) {
		
		return SharedSuggestionProvider.suggest( values, builder );
	}
	
	@NotNull
	@Override
	public Collection<String> getExamples() {
		
		return Arrays.stream( values ).collect( Collectors.toList() );
	}
}
