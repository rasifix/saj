package com.github.rasifix.saj;

import static org.easymock.EasyMock.createStrictMock;

import java.io.IOException;
import java.io.StringReader;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class JsonReaderTest {

	private JsonContentHandler handler;
	
	@Before
	public void beforeTest() {
		handler = createStrictMock(JsonContentHandler.class);
	}
	
	private void replay() {
		EasyMock.replay(handler);
	}
	
	private void verify() {
		EasyMock.verify(handler);
	}
	
	private void parse(String json) throws IOException {
		replay();
		JsonReader reader = new JsonReader(handler);
		reader.parseJson(new StringReader(json));
		verify();
	}
	
	@Test
	public void testParseDoubles() throws Exception {
		value(1.0e+6);
		parse("1.0e+6");
	}

	@Test
	public void testParseArray() throws Exception {
		startArray();
		value(true);
		value("foobar");
		value(2.0);
		nullValue();
		endArray();
		
		parse("[true,\"foobar\",2.0,null]");
	}
	
	@Test
	public void testParseObject() throws Exception {
		startObject();
		startMember("foo");
		value(1.0);
		endMember();
		endObject();
		
		parse("{ \"foo\" : 1 }");
	}
	
	@Test
	public void testNestedStructures() throws Exception {
		startObject();
			startMember("a");
				startArray();
					value(1.0);
					value(2.0);
					startArray();
						value(3.0);
						value(4.0);
						startObject();
							startMember("x");
								nullValue();
							endMember();
						endObject();
					endArray();
					startObject();
						startMember("b");
							startObject();
								startMember("c");
									value(1.0);
								endMember();
								startMember("d");
									startArray();
										nullValue();
										value(true);
									endArray();
								endMember();
							endObject();
						endMember();
					endObject();
				endArray();
			endMember();
		endObject();
		
		parse("{\"a\":[1,2,[3,4,{\"x\":null}],{\"b\":{\"c\":1,\"d\":[null,true]}}]}");
	}
	
	private void startObject() {
		handler.startObject();
	}
	
	private void endObject() {
		handler.endObject();
	}
	
	private void startArray() {
		handler.startArray();
	}
	
	private void endArray() {
		handler.endArray();
	}
	
	private void startMember(String name) {
		handler.startMember(name);
	}
	
	private void endMember() {
		handler.endMember();
	}
	
	private void nullValue() {
		handler.nullValue();
	}
	
	private void value(boolean value) {
		handler.value(value);
	}
	
	private void value(double value) {
		handler.value(value);
	}
	
	private void value(int value) {
		handler.value(value);
	}
	
	private void value(String value) {
		handler.value(value);
	}
	
}
