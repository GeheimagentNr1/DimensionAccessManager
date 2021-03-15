package de.geheimagentnr1.dimension_access_manager.util;

public enum NBTType {
	INT( 3 ),
	LIST( 9 ),
	COMPOUND( 10 );
	
	private final int id;
	
	//private
	NBTType( int _id ) {
		
		id = _id;
	}
	
	public int getId() {
		
		return id;
	}
}
