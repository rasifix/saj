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
