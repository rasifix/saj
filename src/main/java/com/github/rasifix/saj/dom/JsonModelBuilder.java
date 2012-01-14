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

import java.util.LinkedList;

import com.github.rasifix.saj.JsonContentHandler;

public class JsonModelBuilder implements JsonContentHandler {

    private LinkedList<Object> valueStack = new LinkedList<Object>();
    
    public Object getResult(){
        if (valueStack == null || valueStack.size() == 0) {
            return null;
        }
        return valueStack.peek();
    }
    
    @Override
    public void endArray() {
        trackBack();
    }

    @Override
    public void endObject() {
        trackBack();
    }

    @Override
    public void endMember() {
        Object value = valueStack.pop();
        String key = (String) valueStack.pop();
        JsonObject parent = (JsonObject) valueStack.peek();
        parent.put(key, value);
    }

    private void trackBack(){
        if (valueStack.size() > 1){
            Object value = valueStack.pop();
            Object prev = valueStack.peek();
            if (prev instanceof String){
                valueStack.push(value);
            }
        }
    }
    
    private void consumeValue(Object value){
        if (valueStack.isEmpty()) {
            valueStack.push(value);
            
        } else {
            Object prev = valueStack.peek();
            if (prev instanceof JsonArray) {
            	JsonArray array = (JsonArray) prev;
                array.add(value);
            } else{
                valueStack.push(value);
            }
        }
    }
    
    @Override
    public void value(boolean value) {
        consumeValue(value);
    }
    
    @Override
    public void value(double value) {
        consumeValue(value);
    }
    
    @Override
    public void value(int value) {
        consumeValue(value);
    }
    
    @Override
    public void value(String value) {
        consumeValue(value);
    }
    
    @Override
    public void nullValue() {
        consumeValue(null);    	
    }

    public void startArray() {
        JsonArray array = new JsonArray();
        consumeValue(array);
        valueStack.push(array);
    }

    public void startObject() {
    	JsonObject object = new JsonObject();
        consumeValue(object);
        valueStack.push(object);
    }

    public void startMember(String key) {
        valueStack.push(key);
    }
    
}
