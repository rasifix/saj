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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.github.rasifix.saj.internal.JSONLexer;
import com.github.rasifix.saj.internal.JSONParser;

/**
 * 
 * 
 * @author rasifix
 */
public class JsonReader {
	
	private final JsonContentHandler handler;

	public JsonReader(JsonContentHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("handler cannot be null");
		}
		this.handler = handler;
	}
	
	/**
	 * Parse a String containing JSON.
	 * 
	 * @param json the JSON expression
	 * @throws IOException if reading fails
	 */
	public void parseJson(String json) throws IOException {
		parseJson(new StringReader(json));
	}
	
	/**
	 * Parse JSON from the given {@link InputStream}. The encoding
	 * is detected according to RFC4627 section 3.
	 * 
	 * @param inputStream the input stream from which to read
	 * @throws IOException if reading fails
	 */
	public void parseJson(InputStream inputStream) throws IOException {
		final BufferedInputStream stream = new BufferedInputStream(inputStream);
		final String encoding = detectEncoding(stream);
		final Reader reader = new InputStreamReader(stream, encoding);
		parseJson(reader);
	}
	
	/**
	 * Parse JSON from the given reader. The reader will not be closed by this
	 * method.
	 * 
	 * @param reader the Reader from which to parse JSON
	 * @throws IOException if reading from the Reader fails
	 */
	public void parseJson(Reader reader) throws IOException {
        JSONLexer lexer = new JSONLexer(new ANTLRReaderStream(reader));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        JSONParser g = new JSONParser(tokens);
        g.setContentHandler(handler);
        
        try {
            g.json();
        } catch (RecognitionException e) {
            throw new RuntimeException(e);
        }
	}

	private String detectEncoding(InputStream stream) throws IOException {
		stream.mark(4);

		byte[] buf = new byte[4];
		int len = stream.read(buf);

		// xx xx xx xx UTF-8
		String encoding = "UTF-8";
		if (len == 4) {
			if (buf[0] == 0 && buf[1] == 0 && buf[2] == 0) {
				// 00 00 00 xx UTF-32BE
				encoding = "UTF-32BE";
			} else if (buf[0] == 0 && buf[1] == 0 && buf[2] == 0) {
				// 00 xx 00 xx UTF-16BE
				encoding = "UTF-16BE";
			} else if (buf[1] == 0 && buf[2] == 0 && buf[3] == 0) {
				// xx 00 00 00 UTF-32LE
				encoding = "UTF-32LE";
			} else if (buf[1] == 0 && buf[1] == 0 && buf[3] == 0) {
				// xx 00 xx 00 UTF-16LE
				encoding = "UTF-16LE";
			}
		}

		stream.reset();

		return encoding;
	}

}
