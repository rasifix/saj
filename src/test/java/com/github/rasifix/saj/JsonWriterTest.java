package com.github.rasifix.saj;

import static junit.framework.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;

public class JsonWriterTest {
	
	private StringWriter result = new StringWriter();
	private JsonWriter writer = new JsonWriter(result);
	
	private void startObject() {
		writer.startObject();
	}
	
	private void endObject() {
		writer.endObject();
	}
	
	private void startArray() {
		writer.startArray();
	}
	
	private void endArray() {
		writer.endArray();
	}
	
	private void startMember(String name) {
		writer.startMember(name);
	}
	
	private void endMember() {
		writer.endMember();
	}
	
	private void member(String name, int value) {
		writer.member(name, value);
	}
	
	private void member(String name, boolean value) {
		writer.member(name, value);
	}
	
	private void member(String name, double value) {
		writer.member(name, value);
	}
	
	private void member(String name, String value) {
		writer.member(name, value);
	}
	
	private void value(int value) {
		writer.value(value);
	}
	
	private void value(boolean value) {
		writer.value(value);
	}
	
	private void value(double value) {
		writer.value(value);
	}
	
	private void value(String value) {
		writer.value(value);
	}
	
	private void assertResult(String expected) {
		writer.close();
		assertEquals(expected, result.toString());
	}

	@Test
	public void testEmptyObject() throws Exception {
		startObject();
		endObject();
		
		assertResult("{}");
	}
	
	@Test
	public void testObjectWithSingleMember() throws Exception {
		startObject();
		member("number", 5);
		endObject();
		
		assertResult("{\"number\":5}");
	}
	
	@Test
	public void testObjectWithMultipleMembers() throws Exception {
		startObject();
		member("number", 5);
		member("boolean", false);
		member("double", 6.4);
		member("string", "HELLO");
		endObject();
		
		assertResult("{\"number\":5,\"boolean\":false,\"double\":6.4,\"string\":\"HELLO\"}");
	}
	
	@Test
	public void testEmptyArray() throws Exception {
		startArray();
		endArray();
		
		assertResult("[]");
	}
	
	@Test
	public void testArrayWithSingleItem() throws Exception {
		startArray();
		value(5);
		endArray();
		
		assertResult("[5]");
	}
	
	@Test
	public void testArrayWithMultipleItems() throws Exception {
		startArray();
		value(5);
		value(false);
		value(6.4);
		value("HELLO");
		endArray();
		
		assertResult("[5,false,6.4,\"HELLO\"]");
	}
	
	@Test
	public void testObjectWithArrayMember() throws Exception {
		startObject();
		startMember("array");
		startArray();
		value(6);
		value(4);
		endArray();
		endMember();
		startMember("other");
		startArray();
		value(3);
		value(7);
		endArray();
		endMember();
		endObject();
		
		assertResult("{\"array\":[6,4],\"other\":[3,7]}");
	}
	
	@Test
	public void testArrayWithObjectMember() throws Exception {
		startArray();
		startObject();
		member("a", 1);
		member("b", 2);
		endObject();
		startObject();
		member("a", 1);
		member("b", 2);
		endObject();
		endArray();
		
		assertResult("[{\"a\":1,\"b\":2},{\"a\":1,\"b\":2}]");
	}
	
}
