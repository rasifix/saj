grammar JSON;

@header {
package com.github.rasifix.saj;

import java.util.regex.Pattern;

}

@lexer::header {
package com.github.rasifix.saj;
}

// Optional step: Disable automatic error recovery
@members {
protected JsonContentHandler handler;

void setContentHandler(JsonContentHandler handler) {
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
	:	'-'? DIGIT+ ('.' DIGIT+)?
	;

fragment DIGIT
	:	'0'..'9'
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
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

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
	|	n=number { handler.value($n.r); }
	|	object 
	|	array 
	|	b=BOOLEAN { handler.value(Boolean.parseBoolean($b.getText())); }
	|	'null' { handler.nullValue(); }
	;

number returns [double r]
	: n=NUMBER { Pattern.matches("(0|(-?[1-9]\\d*))(\\.\\d+)?", n.getText())}?
	  exp=EXPONENT?
	  { 
	  	if ($exp == null) {
	  		$r = Double.parseDouble($n.getText());
	  	} else {
	  		$r = Double.parseDouble($n.getText() + $exp.getText()); 
	  	}
	  }	
	;

