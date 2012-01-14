grammar JSON;

@header {
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
package com.github.rasifix.saj.internal;

import java.util.regex.Pattern;
import com.github.rasifix.saj.JsonContentHandler;
}

@lexer::header {
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
package com.github.rasifix.saj.internal;
}

// Optional step: Disable automatic error recovery
@members {
protected JsonContentHandler handler;

public void setContentHandler(JsonContentHandler handler) {
	if (handler == null) {
		throw new IllegalArgumentException("handler cannot be null");
	}
	this.handler = handler;
}

private static String unquote(String quotedString) {
	return quotedString.substring(1, quotedString.length() - 1);
}

protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException {
	throw new MismatchedTokenException(ttype, input);
}
public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
	throw e;
}
}
// Alter code generation so catch-clauses get replace with
// this action.
@rulecatch {
catch (RecognitionException e) {
	throw e;
}
} 

NUMBER
	:	'-'? ('0' | '1'..'9' '0'..'9'*) ('.' '0'..'9'+)? (('e'|'E')('+'|'-')? '0'..'9'+)?
	;
	
BOOLEAN
	:	'true'
	|	'false'
	;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

json
	:	value EOF
	;

object 
	:	'{' { handler.startObject(); }
	    members? 
	    '}' { handler.endObject(); }
	;

members
	:	pair
		(',' pair)*
	;

pair 
	:	m=STRING ':' { handler.startMember(unquote($m.text)); }
	    value
	    { handler.endMember(); }
	;

array
	:	'[' { handler.startArray(); }
		elements? 
		']' { handler.endArray(); }
	;

elements
	:	value (',' value)*
	;

value
	:	str=STRING 
		{
			handler.value(unquote($str.getText())); 
		}
	|	number
	|	object 
	|	array 
	|	b=BOOLEAN { handler.value(Boolean.parseBoolean($b.getText())); }
	|	'null' { handler.nullValue(); }
	;

number
	: n=NUMBER 
	  { 
	  	String number = $n.getText();
  		handler.value(Double.parseDouble($n.getText())); 
	  }	
	;

