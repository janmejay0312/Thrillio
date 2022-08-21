package com.janmejay.thrillio.constants;

public enum UserType {

	  USER(0),
	  EDITOR(1),
	  CHIEF_EDITOR(2);
	 
	private UserType(int value) {
		 this.value=value;
	 }
	private int value; 
	public int getValue() {
		return value;
	}
}
