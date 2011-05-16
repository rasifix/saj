package com.github.rasifix.saj;

public interface JsonContentHandler {

	void startObject();
	
	void startMember(String name);
	
	void endMember();
	
	void endObject();
	
	void startArray();
	
	void endArray();
	
	void value(String value);
	
	void value(double number);
	
	void value(boolean bool);
	
	void nullValue();

}
