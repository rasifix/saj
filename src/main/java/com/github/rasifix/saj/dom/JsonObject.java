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

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.github.rasifix.saj.JsonContentHandler;

public class JsonObject extends AbstractMap<String, Object> {
	
	private final Map<String, Object> members = new LinkedHashMap<String, Object>();

	public Object get(String name) {
		return members.get(name);
	}
	
	public JsonArray getArray(String name) {
		return (JsonArray) get(name);
	}
	
	public JsonObject getObject(String name) {
		return (JsonObject) get(name);
	}
	
	public boolean getBoolean(String name) {
		return (Boolean) get(name);
	}
	
	public Number getNumber(String name) {
		return (Number) get(name);
	}
	
	public int getInt(String name) {
		return ((Number) get(name)).intValue();
	}
	
	public double getDouble(String name) {
		return ((Number) get(name)).doubleValue();
	}
	
	public String getString(String name) {
		return (String) get(name);
	}
	
	public void streamTo(JsonContentHandler handler) {
		handler.startObject();
		for (Entry<String, Object> entry : entrySet()) {
			handler.startMember(entry.getKey());
			if (entry.getValue() instanceof JsonObject) {
				((JsonObject) entry.getValue()).streamTo(handler);
			} else if (entry.getValue() instanceof JsonArray) {
				((JsonArray) entry.getValue()).streamTo(handler);
			} else if (entry.getValue() instanceof Boolean) {
				handler.value((Boolean) entry.getValue());
			} else if (entry.getValue() instanceof Integer) {
				handler.value((Integer) entry.getValue());
			} else if (entry.getValue() instanceof String) {
				handler.value((String) entry.getValue());
			} else if (entry.getValue() instanceof Double) {
				handler.value((Double) entry.getValue());
			} else if (entry.getValue() == null) {
				handler.nullValue();
			} else {
				throw new RuntimeException("UUUUPS " + entry.getValue().getClass());
			}
			handler.endMember();
		}
		handler.endObject();
	}
	
	@Override
	public Object put(String key, Object value) {
		return members.put(key, value);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return members.entrySet();
	}
	
}
