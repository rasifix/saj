package com.github.rasifix.saj;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class JsonReader {
	
	private final JsonContentHandler handler;

	public JsonReader(JsonContentHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("handler cannot be null");
		}
		this.handler = handler;
	}
	
	public void parseJson(String json) throws IOException {
		parseJson(new StringReader(json));
	}
	
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
	
}
