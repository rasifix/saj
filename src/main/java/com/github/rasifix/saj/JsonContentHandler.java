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

import java.util.List;
import java.util.Map;

/**
 * Interface for callback of {@link JsonReader}. The parser invokes the methods
 * defined in this interface. It is completely up to the application to decide
 * what to do with these callbacks. A simple implementation is provided
 * (see {@link SimpleContentHandler}) that maps JSON objects to a {@link Map}
 * and JSON arrays to {@link List}.
 * 
 * @author rasifix
 */
public interface JsonContentHandler {

	/**
	 * Marks the start of a JSON object. i.e. this method is invoked
	 * when the opening curly bracket is encountered.
	 */
	void startObject();
	
	/**
	 * Invoked directly inside of a JSON object to mark the start of
	 * member. This method call will be followed by any other JSON
	 * value method, such as another object, array, or simple value types.
	 * 
	 * @param name the name of the member
	 */
	void startMember(String name);
	
	/**
	 * Invoked to demarcate the end of a member. Between the call
	 * to {@link #startMember(String)} and this method, there must
	 * be exaclty one JSON value. 
	 */
	void endMember();
	
	/**
	 * Marks the end of a JSON object. i.e. this method is invoked
	 * when the closing curly bracket is encountered.
	 */
	void endObject();
	
	/**
	 * Marks the start of a JSON array. i.e. this method is invoked
	 * when the opening square bracket is encountered.
	 */
	void startArray();
	
	/**
	 * Marks the end of a JSON array. i.e. this method is invoked
	 * when the closing square bracket is encountered.
	 */
	void endArray();
	
	void value(String value);
	
	void value(double number);
	
	void value(boolean bool);
	
	void nullValue();

}
