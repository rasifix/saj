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
package com.github.rasifix.saj.dom;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.github.rasifix.saj.JsonContentHandler;

public class JsonArray extends AbstractList<Object> {

	private final List<Object> values = new ArrayList<Object>();
	
	public JsonObject getObject(int index) {
		return (JsonObject) get(index);
	}
	
	public JsonArray getArray(int index) {
		return (JsonArray) get(index);
	}
	
	public boolean getBoolean(int index) {
		return (Boolean) get(index);
	}

	public String getString(int index) {
		return (String) values.get(index);
	}

	public double getInt(int index) {
		return ((Number) values.get(index)).intValue();
	}

	public double getDouble(int index) {
		return ((Number) values.get(index)).doubleValue();
	}
	
	public void streamTo(JsonContentHandler handler) {
		handler.startArray();
		for (Object content : this) {
			if (content instanceof JsonObject) {
				((JsonObject) content).streamTo(handler);
			} else if (content instanceof JsonArray) {
				((JsonArray) content).streamTo(handler);
			} else if (content instanceof String) {
				handler.value((String) content);
			} else if (content instanceof Double) {
				handler.value((Double) content);
			} else if (content instanceof Integer) {
				handler.value((Integer) content);
			} else if (content instanceof Boolean) {
				handler.value((Boolean) content);
			} else if (content == null) {
				handler.nullValue();
			} else {
				throw new RuntimeException("unsupported type " + content.getClass());
			}
		}
		handler.endArray();
	}
	
	@Override
	public Object get(int index) {
		return values.get(index);
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public void add(int idx, Object e) {
		values.add(idx, e);
	}
	
}
