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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * JsonContentHandler that builds Maps and Lists for JSON objects and JSON arrays.
 *  
 * @author rasifix
 */
public class SimpleContentHandler implements JsonContentHandler {

	private Object result;
	
	private State state;
	
	public Object getResult() {
		return result;
	}
	
	@Override
	public void startObject() {
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		state = new ObjectState(state, obj);
	}

	@Override
	public void startMember(String name) {
		state = new MemberState(state, name);
	}

	@Override
	public void endMember() {
		MemberState memberState = (MemberState) state;
		String name = memberState.getName();
		Object value = memberState.getValue();
		this.state = state.getParent();
		this.state.member(name, value);
	}

	@Override
	public void endObject() {
		Object value = state.getValue();
		this.state = state.getParent();
		
		if (this.state == null) {
			this.result = value;
		} else {
			this.state.value(value);
		}
	}

	@Override
	public void startArray() {
		this.state = new ArrayState(state);
	}

	@Override
	public void endArray() {
		Object value = state.getValue();
		this.state = state.getParent();
		
		if (this.state == null) {
			result = value;
		} else {
			this.state.value(value);
		}
	}

	@Override
	public void value(String value) {
		this.state.value(value);
	}

	@Override
	public void value(double number) {
		this.state.value(number);
	}

	@Override
	public void value(int number) {
		this.state.value(number);
	}

	@Override
	public void value(boolean bool) {
		this.state.value(bool);
	}

	@Override
	public void nullValue() {
		this.state.value(null);
	}
	
	private static interface State {
		
		State getParent();
		
		Object getValue();

		void value(Object value);

		void member(String name, Object value);
		
	}
	
	private static class ObjectState implements State {
		
		private State parent;
		
		private Map<String, Object> obj;
		
		ObjectState(State parent, Map<String, Object> obj) {
			this.parent = parent;
			this.obj = obj;
		}
		
		@Override
		public State getParent() {
			return parent;
		}
		
		@Override
		public Object getValue() {
			return obj;
		}
		
		@Override
		public void member(String name, Object value) {
			obj.put(name, value);
		}

		@Override
		public void value(Object value) {
			throw new IllegalStateException("no value allowed directly inside of object");
		}

	}
	
	private static class ArrayState implements State {
		
		private final State parent;
		
		private final List<Object> content;
		
		ArrayState(State parent) {
			this.parent = parent;
			this.content = new LinkedList<Object>();
		}
		
		@Override
		public State getParent() {
			return parent;
		}
		
		@Override
		public Object getValue() {
			return content;
		}
		
		@Override
		public void member(String name, Object value) {
			throw new IllegalStateException("members not allowed in array");
		}
		
		@Override
		public void value(Object value) {
			this.content.add(value);
		}
		
	}
	
	private static class MemberState implements State {
		
		private final State parent;
		private final String name;
		private Object value;
		
		MemberState(State parent, String name) {
			this.parent = parent;
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public Object getValue() {
			return value;
		}
		
		@Override
		public State getParent() {
			return parent;
		}

		@Override
		public void member(String name, Object value) {
			throw new IllegalStateException("member not allowed within member");
		}

		@Override
		public void value(Object value) {
			this.value = value;
		}
		
	}

}
