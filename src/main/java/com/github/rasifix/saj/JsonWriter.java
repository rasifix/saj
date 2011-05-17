/*
 * Copyright 2011 Simon Raess
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rasifix.saj;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

/**
 * JsonWriter writes JSON to a given {@link Writer}. It is a {@link JsonContentHandler}
 * thus allowing streaming of JSON through a pipeline as is known from SAX.
 * 
 * @author rasifix
 */
public class JsonWriter implements JsonContentHandler {
	
	private static final String COLON = ":";

	private static final String QUOTE = "\"";
	
	private static final String COMMA = ",";
	
	private static final String NL = "\n";
	
	private static final String ARR_START = "[";
	
	private static final String ARR_END = "]";
	
	private static final String OBJ_START = "{";
	
	private static final String OBJ_END = "}";
	
	private static final String INDENT = "\t";
	
	private final BufferedWriter writer;
	
	private final LinkedList<Element> elementStack = new LinkedList<Element>();

	private Element current;
	
	private StringBuilder indent = new StringBuilder();

	private boolean prettyPrint;
	
	public JsonWriter(Writer writer) {
		if (writer == null) {
			throw new IllegalArgumentException("writer cannot be null");
		}
		
		this.writer = new BufferedWriter(writer, 1024);
		this.current = new InitialElement();
		this.elementStack.push(current);
	}
	
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}
	
	private void pushElement(Element element) {
		current.notifyChild();
		elementStack.push(element);
		current = element;
	}
	
	private void popElement() {
		elementStack.pop();
		current = elementStack.peek();
	}
	
	public void startObject()  {
		try {
			current.startObject();
			pushElement(new ObjectElement());
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void member(String name, int value) {
		startMember(name);
		value(value);
		endMember();
	}
	
	public void member(String name, long value) {
		startMember(name);
		value(value);
		endMember();
	}
	
	public void member(String name, double value) {
		startMember(name);
		value(value);
		endMember();
	}
	
	public void member(String name, String value) {
		startMember(name);
		value(value);
		endMember();
	}
	
	public void member(String name, boolean value) {
		startMember(name);
		value(value);
		endMember();
	}
	
	public void startMember(String name) {
		try {
			current.member(name);
			pushElement(new MemberElement());
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void endMember() {
		popElement();
	}
	
	public void endObject() {
		try {
			current.endObject();
			popElement();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void startArray() {
		try {
			current.startArray();
			pushElement(new ArrayElement());
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void endArray() {
		try {
			current.endArray();
			popElement();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void nullValue() {
		try {
			current.nullValue();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void value(int value) {
		try {
			current.value(value);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void value(long value) {
		try {
			current.value(value);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void value(double value) {
		try {
			current.value(value);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void value(String value) {
		try {
			current.value(value);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	public void value(boolean value) {
		try {
			current.value(value);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
    private static String escape(String string) {
        char         b;
        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        String       t;

        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
                if (b == '<') {
                    sb.append('\\');
                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
                sb.append("\\r");
                break;
            default:
                if (c < ' ' || (c >= '\u0080' && c < '\u00a0') ||
                               (c >= '\u2000' && c < '\u2100')) {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        
        return sb.toString();
    }

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
	
	protected void indent() {
		indent.append(INDENT);
	}
	
	protected void outdent() {
		indent.setLength(indent.length() - INDENT.length());
	}
	
	private abstract class Element {
		
		protected abstract boolean needsComma();

		protected void notifyChild() {
			// ignored
		}
		
		private void writePendingComma() throws IOException {
			if (needsComma()) writer.write(COMMA);
		}

		protected void writeIndent() throws IOException {
			if (prettyPrint) {
				writer.write(NL);
				writer.write(indent.toString());
			}
		}

		protected void member(String name) throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(QUOTE);
			writer.write(name);
			writer.write(QUOTE);
			writer.write(COLON);
		}
		
		protected void nullValue() throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write("null");
			notifyChild();
		}
		
		protected void value(int value) throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(Integer.toString(value));
			notifyChild();
		}
		
		protected void value(long value) throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(Long.toString(value));
			notifyChild();
		}
		
		protected void value(double value) throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(Double.toString(value));
			notifyChild();
		}
		
		protected void value(boolean value) throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(Boolean.toString(value));
			notifyChild();
		}
		
		protected void value(String value) throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(QUOTE);
			writer.write(escape(value));
			writer.write(QUOTE);
			notifyChild();
		}
		
		protected void startObject() throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(OBJ_START);
			indent();
		}
		
		protected void endObject() throws IOException {
			outdent();
			writeIndent();
			writer.write(OBJ_END);
		}
		
		protected void startArray() throws IOException {
			writePendingComma();
			writeIndent();
			
			writer.write(ARR_START);
			indent();
		}
		
		protected void endArray() throws IOException {
			outdent();
			writeIndent();
			writer.write(ARR_END);
		}
		
	}
	
	private class ObjectElement extends Element {
		
		private int members;

		protected boolean needsComma() {
			return members > 0;
		}
		
		@Override
		protected void startObject() throws IOException {
			throw new IllegalStateException("object not supported in current state (object)");
		}
		
		@Override
		protected void startArray() throws IOException {
			throw new IllegalStateException("array not supported in current state (object)");
		}
		
		@Override
		protected void endArray() throws IOException {
			throw new IllegalStateException("array not supported in current state (object)");
		}

		@Override
		protected void value(boolean value) throws IOException {
			throw new IllegalStateException("value not supported in current state (object)");
		}
		
		@Override
		protected void value(double value) throws IOException {
			throw new IllegalStateException("value not supported in current state (object)");
		}
		
		@Override
		protected void value(int value) throws IOException {
			throw new IllegalStateException("value not supported in current state (object)");
		}
		
		@Override
		protected void value(String value) throws IOException {
			throw new IllegalStateException("value not supported in current state (object)");
		}
		
		@Override
		protected void notifyChild() {
			members += 1;
		}
		
		@Override
		public String toString() {
			return "object";
		}
		
	}
	
	private class ArrayElement extends Element {
		
		private int items;

		protected boolean needsComma() {
			return items > 0;
		}
		
		@Override
		protected void notifyChild() {
			items += 1;
		}
		
		@Override
		protected void endObject() throws IOException {
			throw new IllegalStateException("illegal nesting - endObject() in array");
		}
		
		@Override
		protected void member(String name) throws IOException {
			throw new IllegalStateException("illegal nesting - startMember() in array");
		}
		
		@Override
		public String toString() {
			return "array";
		}
		
	}
	
	private class MemberElement extends Element {

		protected boolean needsComma() {
			return false;
		}
		
		@Override
		public String toString() {
			return "member";
		}

		@Override
		protected void startArray() throws IOException {
			writer.write(ARR_START);
			indent();
		}
		
		@Override
		protected void startObject() throws IOException {
			writer.write(OBJ_START);
			indent();
		}

		protected void value(int value) throws IOException {
			writer.write(Integer.toString(value));
			notifyChild();
		}
		
		protected void value(double value) throws IOException {
			writer.write(Double.toString(value));
			notifyChild();
		}
		
		protected void value(boolean value) throws IOException {
			writer.write(Boolean.toString(value));
			notifyChild();
		}
		
		protected void value(String value) throws IOException {
			writer.write(QUOTE);
			writer.write(escape(value));
			writer.write(QUOTE);
			notifyChild();
		}
		
	}
	
	private class InitialElement extends Element {
		
		protected boolean needsComma() {
			return false;
		}
		
		protected void writeIndent() throws IOException {
			// initial element, no indent...
		}
		
		@Override
		public String toString() {
			return "initial";
		}
		
	}
	
}
