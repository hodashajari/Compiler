/* *
 * Scanner for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2017.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2017 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2017
  */

package cop5556fa17;


import java.util.ArrayList;
import java.util.Arrays;


public class Scanner {
	
	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {
		
		int pos;

		public LexicalException(String message, int pos) {
			super(message);
			this.pos = pos;
		}
		
		public int getPos() { return pos; }

	}
	
	public static enum State {
		START, IN_DIGIT, IN_IDENT, AFTER_EQ, AFTER_QUOTE, AFTER_SLASH,AFTER_BACKSLASH,AFTER_MULT, AFTER_MINUS, AFTER_EXCLAMATION, AFTER_GREATER, AFTER_LESS, AFTER_PIPE;
	}

	public static enum Kind {
		IDENTIFIER, INTEGER_LITERAL, BOOLEAN_LITERAL, STRING_LITERAL, 
		KW_x/* x */, KW_X/* X */, KW_y/* y */, KW_Y/* Y */, KW_r/* r */, KW_R/* R */, KW_a/* a */, 
		KW_A/* A */, KW_Z/* Z */, KW_DEF_X/* DEF_X */, KW_DEF_Y/* DEF_Y */, KW_SCREEN/* SCREEN */, 
		KW_cart_x/* cart_x */, KW_cart_y/* cart_y */, KW_polar_a/* polar_a */, KW_polar_r/* polar_r */, 
		KW_abs/* abs */, KW_sin/* sin */, KW_cos/* cos */, KW_atan/* atan */, KW_log/* log */, 
		KW_image/* image */,  KW_int/* int */, 
		KW_boolean/* boolean */, KW_url/* url */, KW_file/* file */, 
		
		OP_ASSIGN/* = */, OP_GT/* > */, OP_LT/* < */, 
		OP_EXCL/* ! */, OP_Q/* ? */, OP_COLON/* : */, OP_EQ/* == */, OP_NEQ/* != */, OP_GE/* >= */, OP_LE/* <= */, 
		OP_AND/* & */, OP_OR/* | */, OP_PLUS/* + */, OP_MINUS/* - */, OP_TIMES/* * */, OP_DIV/* / */, OP_MOD/* % */, 
		OP_POWER/* ** */, OP_AT/* @ */, OP_RARROW/* -> */, OP_LARROW/* <- */,
		
		LPAREN/* ( */, RPAREN/* ) */, 
		LSQUARE/* [ */, RSQUARE/* ] */, SEMI/* ; */, COMMA/* , */, EOF;
	}

	/** Class to represent Tokens. 
	 * 
	 * This is defined as a (non-static) inner class
	 * which means that each Token instance is associated with a specific 
	 * Scanner instance.  We use this when some token methods access the
	 * chars array in the associated Scanner.
	 * 
	 * 
	 * @author Beverly Sanders
	 *
	 */
	public class Token {
		public final Kind kind;
		public final int pos;
		public final int length;
		public final int line;
		public final int pos_in_line;

		public Token(Kind kind, int pos, int length, int line, int pos_in_line) {
			super();
			this.kind = kind;
			this.pos = pos;
			this.length = length;
			this.line = line;
			this.pos_in_line = pos_in_line;
		}

		public String getText() {
			if (kind == Kind.STRING_LITERAL) {
				return chars2String(chars, pos, length);
			}
			else return String.copyValueOf(chars, pos, length);
		}

		/**
		 * To get the text of a StringLiteral, we need to remove the
		 * enclosing " characters and convert escaped characters to
		 * the represented character.  For example the two characters \ t
		 * in the char array should be converted to a single tab character in
		 * the returned String
		 * 
		 * @param chars
		 * @param pos
		 * @param length
		 * @return
		 */
		private String chars2String(char[] chars, int pos, int length) {
			StringBuilder sb = new StringBuilder();
			for (int i = pos + 1; i < pos + length - 1; ++i) {// omit initial and final "
				char ch = chars[i];
				if (ch == '\\') { // handle escape
					i++;
					ch = chars[i];
					switch (ch) {
					case 'b':
						sb.append('\b');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'r':
						sb.append('\r'); //for completeness, line termination chars not allowed in String literals
						break;
					case 'n':
						sb.append('\n'); //for completeness, line termination chars not allowed in String literals
						break;
					case '\"':
						sb.append('\"');
						break;
					case '\'':
						sb.append('\'');
						break;
					case '\\':
						sb.append('\\');
						break;
					default:
						assert false;
						break;
					}
				} else {
					sb.append(ch);
				}
			}
			return sb.toString();
		}

		/**
		 * precondition:  This Token is an INTEGER_LITERAL
		 * 
		 * @returns the integer value represented by the token
		 */
		public int intVal() {
			assert kind == Kind.INTEGER_LITERAL;
			return Integer.valueOf(String.copyValueOf(chars, pos, length));
		}

		public String toString() {
			return "[" + kind + "," + String.copyValueOf(chars, pos, length)  + "," + pos + "," + length + "," + line + ","
					+ pos_in_line + "]";
		}

		/** 
		 * Since we overrode equals, we need to override hashCode.
		 * https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-
		 * 
		 * Both the equals and hashCode method were generated by eclipse
		 * 
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((kind == null) ? 0 : kind.hashCode());
			result = prime * result + length;
			result = prime * result + line;
			result = prime * result + pos;
			result = prime * result + pos_in_line;
			return result;
		}

		/**
		 * Override equals method to return true if other object
		 * is the same class and all fields are equal.
		 * 
		 * Overriding this creates an obligation to override hashCode.
		 * 
		 * Both hashCode and equals were generated by eclipse.
		 * 
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Token other = (Token) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (kind != other.kind)
				return false;
			if (length != other.length)
				return false;
			if (line != other.line)
				return false;
			if (pos != other.pos)
				return false;
			if (pos_in_line != other.pos_in_line)
				return false;
			return true;
		}

		/**
		 * used in equals to get the Scanner object this Token is 
		 * associated with.
		 * @return
		 */
		private Scanner getOuterType() {
			return Scanner.this;
		}
		
		public boolean isKind(Kind kind) {
			return this.kind == kind;
		}

	}

	/** 
	 * Extra character added to the end of the input characters to simplify the
	 * Scanner.  
	 */
	static final char EOFchar = 0;
	
	/**
	 * The list of tokens created by the scan method.
	 */
	final ArrayList<Token> tokens;
	
	/**
	 * An array of characters representing the input.  These are the characters
	 * from the input string plus and additional EOFchar at the end.
	 */
	final char[] chars;  



	
	/**
	 * position of the next token to be returned by a call to nextToken
	 */
	private int nextTokenPos = 0;

	Scanner(String inputString) {
		int numChars = inputString.length();
		this.chars = Arrays.copyOf(inputString.toCharArray(), numChars + 1); // input string terminated with null char
		chars[numChars] = EOFchar;
		tokens = new ArrayList<Token>();
	}


	/**
	 * Method to scan the input and create a list of Tokens.
	 * 
	 * If an error is encountered during scanning, throw a LexicalException.
	 * 
	 * @return
	 * @throws LexicalException
	 */
	public Scanner scan() throws LexicalException {
		/* TODO  Replace this with a correct and complete implementation!!! */
		int pos = 0;
		int line = 1;
		int posInLine = 1;
		int length = chars.length;
		State state = State.START;
		int startPos = 0;
		int ch;
		char cha;
		while(pos<length-1) {
			
			cha = chars[pos];
			
			switch(state) {
			
			case START : 
				
				while (pos < chars.length) {
					if (Character.isWhitespace(chars[pos])) {
						if (chars[pos] == '\n' || chars[pos] == '\r') {
							if (chars[pos] == '\r' && chars[pos+1] == '\n') {
								pos++;
							}
							
							line++;posInLine=1;pos++;
						}else {
							pos++;posInLine++;
						}
						
					} else
						break;
				}
				ch = chars[pos];
				switch(ch) {
					case ';' : {
						tokens.add(new Token(Kind.SEMI,pos,1,line,posInLine));
						startPos = pos;
						pos++; posInLine++;
					}
					break;
					
					case '+': {
						tokens.add(new Token(Kind.OP_PLUS,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case '%': {
						tokens.add(new Token(Kind.OP_MOD,pos,1,line,posInLine));
						pos++; posInLine++;
					}
						break;
					case '@': {
						tokens.add(new Token(Kind.OP_AT,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case '&': {
						tokens.add(new Token(Kind.OP_AND,pos,1,line,posInLine));
						pos++;posInLine++;
					}

						break;
					case ':': {
						tokens.add(new Token(Kind.OP_COLON,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;

					case '?': {
						tokens.add(new Token(Kind.OP_Q,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					
					case '0': {
						tokens.add(new Token(Kind.INTEGER_LITERAL,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case '(': {
						tokens.add(new Token(Kind.LPAREN,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case ')': {
						tokens.add(new Token(Kind.RPAREN,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case ',': {
						tokens.add(new Token(Kind.COMMA,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case '[': {
						tokens.add(new Token(Kind.LSQUARE,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case ']': {
						tokens.add(new Token(Kind.RSQUARE,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case '|':{
						tokens.add(new Token(Kind.OP_OR,pos,1,line,posInLine));
						pos++;posInLine++;
					}
						break;
					case '=': {
						if (pos + 1 < length-1) {
							state = State.AFTER_EQ;
							pos++; posInLine++;
						} else {
							tokens.add(new Token(Kind.OP_ASSIGN,pos,1,line,posInLine));
							state = State.START;
							pos++;posInLine++;
						}
						break;
					}
					case '*': {
						if (pos + 1 < length-1) {
							state = State.AFTER_MULT;
							pos++;posInLine++;
						} else {
							tokens.add(new Token(Kind.OP_TIMES,pos,1,line,posInLine));
							state = State.START;
							pos++;posInLine++;
						}
						break;
					}
					case '>':
						if (pos + 1 < length-1) {
							state = State.AFTER_GREATER;
							pos++;posInLine++;
						} else {
							tokens.add(new Token(Kind.OP_GT,pos,1,line,posInLine));
							state = State.START;
							pos++;posInLine++;
						}
						break;
					case '<':
						if (pos + 1 < length-1) {
							state = State.AFTER_LESS;
							pos++;posInLine++;
						} else {
							tokens.add(new Token(Kind.OP_LT,pos,1,line,posInLine));
							state = State.START;
							pos++;posInLine++;
						}
						break;
					case '!':
						if (pos + 1 < length-1) {
							state = State.AFTER_EXCLAMATION;
							pos++;posInLine++;
						} else {
							tokens.add(new Token(Kind.OP_EXCL,pos,1,line,posInLine));
							state = State.START;
							pos++;posInLine++;
						}
						break;
					case '-':
						if (pos + 1 < length-1) {
							state = State.AFTER_MINUS;
							pos++;posInLine++;
						} else {
							tokens.add(new Token(Kind.OP_MINUS,pos,1,line,posInLine));
							state = State.START;
							pos++;posInLine++;
						}
						break;
					case '/':
						if (pos + 1 < length-1) {
							state = State.AFTER_SLASH;
							pos++;posInLine++;
						} else {
							tokens.add(new Token(Kind.OP_DIV,pos,1,line,posInLine));
							state = State.START;
							pos++;posInLine++;
						}
						break;
						
					case '"':
						if (pos + 1 < length-1) {
							state = State.AFTER_QUOTE;
							pos++;posInLine++;
						} else {
							throw new LexicalException("Illegal Use of Quote/ String Literal : "+chars[pos], pos+1);
						}
						break;
					
					default: {
						if (Character.isDigit(ch)) {
							if (pos + 1 < length-1) {
								state = State.IN_DIGIT;
								pos++;
							} else {
								tokens.add(new Token(Kind.INTEGER_LITERAL, pos,1,line,posInLine));
								state = State.START;
								pos++; posInLine++; startPos = pos;
							}
						} else if (Character.isJavaIdentifierStart(ch)) {
							if (pos + 1 < length-1) {
								state = State.IN_IDENT;
								pos++;posInLine++;
							} else {
								//
								
								
								switch(ch) {
								case 'Z': {
									tokens.add(new Token(Kind.KW_Z,pos,1,line,posInLine));
								}
									break;
								case 'A': {
									tokens.add(new Token(Kind.KW_A,pos,1,line,posInLine));
								}
									break;

								case 'a': {
									tokens.add(new Token(Kind.KW_a,pos,1,line,posInLine));
								}
									break;
								case 'R': {
									tokens.add(new Token(Kind.KW_R,pos,1,line,posInLine));
								}
									break;
								case 'r': {
									tokens.add(new Token(Kind.KW_r,pos,1,line,posInLine));
								}
									break;
								case 'Y': {
									tokens.add(new Token(Kind.KW_Y,pos,1,line,posInLine));
								}
									break;
								case 'y': {
									tokens.add(new Token(Kind.KW_y,pos,1,line,posInLine));
								}
									break;
								case 'X': {
									tokens.add(new Token(Kind.KW_X,pos,1,line,posInLine));
								}
									break;
								case 'x': {
									tokens.add(new Token(Kind.KW_x,pos,1,line,posInLine));
								}
									break;
								default:
									tokens.add(new Token(Kind.IDENTIFIER, pos,1,line,posInLine));
								}
								
								
								
								state = State.START;
								pos++; posInLine++;startPos = pos;
							}
						} else if(chars[pos] == EOFchar) {
							pos++; posInLine++;
						}else {
							throw new LexicalException("Illegal char : "+chars[pos], pos);
						}
					}
					
						
				}
				break;
				
				
			case AFTER_QUOTE:
				int strLitLength = 0;
				while (pos < length-1) {
					
					if (chars[pos] == '"') {
						tokens.add(new Token(Kind.STRING_LITERAL,pos-strLitLength-1,strLitLength+2,line,posInLine-strLitLength-1));
						pos++;posInLine++;
						state = State.START;
						break;
					} else if (chars[pos] == '\\'){
						pos++;posInLine++;strLitLength++;
						switch(chars[pos]) {
						
						case 'b': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						
						case 't': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						case 'n': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						case 'f': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						case 'r': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						
						case '"': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						case '\'': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						case '\\': {
							pos++;posInLine++;strLitLength++;
						}
							break;
						default:
							throw new LexicalException("Illegal char in String Literal: ", pos-1);
						}
							
					}else if(chars[pos+1] == EOFchar) {
						throw new LexicalException("Illegal use; No end quote found in String literal: "+chars[pos], posInLine);
					}else if(chars[pos] == '\n' || chars[pos] == '\r') {
						throw new LexicalException("Illegal use found in String literal: "+chars[pos], pos);
					}else {
						pos++;posInLine++;strLitLength++;
					}
					
					
				}
				
				
			break;
				
			case IN_DIGIT:
				int digitLength = 1;
				while (pos < length-1 && Character.isDigit(chars[pos])) {
					pos++;
					digitLength++;
				}
				StringBuilder sb = new StringBuilder();
				sb.append(chars, pos-digitLength, digitLength);
				try {
					Integer.parseInt(sb.toString());
					tokens.add(new Token(Kind.INTEGER_LITERAL,pos-digitLength, digitLength,line, posInLine));
					state = State.START;
					posInLine=posInLine+digitLength;
				} catch (Exception e) {
					throw new LexicalException("Illegal char - Java Int out of range : "+chars[pos], pos-digitLength);
				}
			break;
			
			case AFTER_EQ:
				ch = chars[pos];
				if (ch == '=') {
					tokens.add(new Token(Kind.OP_EQ,pos-1,2,line,posInLine-1));
					pos++;posInLine++;
					state = State.START;
				} else {
					tokens.add(new Token(Kind.OP_ASSIGN,pos-1,1,line,posInLine-1));
					state = State.START;
				}
				break;
				
			case AFTER_GREATER: {
				ch = chars[pos];
				if (ch == '=') {
					tokens.add(new Token(Kind.OP_GE,pos-1,2,line,posInLine-1));
					pos++;posInLine++;
					state = State.START;
				} else {
					tokens.add(new Token(Kind.OP_GT,pos-1,1,line,posInLine-1));
					state = State.START;
				}
			}
			break;
			case AFTER_LESS: {
				ch = chars[pos];
				if (ch == '=') {
					tokens.add(new Token(Kind.OP_LE,pos-1,2,line,posInLine-1));
					pos++;posInLine++;
					state = State.START;
				} else if (ch == '-') {
					tokens.add(new Token(Kind.OP_LARROW,pos-1,2,line,posInLine-1));
					pos++;posInLine++;
					state = State.START;
				} else {
					tokens.add(new Token(Kind.OP_LT,pos-1,1,line,posInLine-1));
					state = State.START;
				}

			}
				break;
				
			case AFTER_EXCLAMATION: {
				ch = chars[pos];
				if (ch == '=') {
					tokens.add(new Token(Kind.OP_NEQ,pos-1,2,line,posInLine-1));
					pos++;posInLine++;
					state = State.START;
				} else {
					tokens.add(new Token(Kind.OP_EXCL,pos-1,1,line,posInLine-1));
					state = State.START;
				}
			}
				break;
				
			case AFTER_MINUS: {
				ch = chars[pos];
				if (ch == '>') {
					tokens.add(new Token(Kind.OP_RARROW,pos-1,2,line,posInLine-1));
					pos++;posInLine++;
					state = State.START;
				} else {
					tokens.add(new Token(Kind.OP_MINUS,pos-1,1,line,posInLine-1));
					state = State.START;
				}
			}
				break;
				
			case AFTER_MULT: {
				ch = chars[pos];
				if (ch == '*') {
					tokens.add(new Token(Kind.OP_POWER,pos-1,2,line,posInLine-1));
					pos++;posInLine++;
					state = State.START;
				} else {
					tokens.add(new Token(Kind.OP_TIMES,pos-1,1,line,posInLine-1));
					state = State.START;
				}
			}
				break;	
			
			case AFTER_SLASH: {
				if (pos < length && (ch = chars[pos]) == '/') {
					while (pos < length-1) {
							if (Character.isWhitespace(chars[pos])) {
								
								if (chars[pos] == '\n' || chars[pos] == '\r') {
									if (chars[pos] == '\r' && chars[pos+1] == '\n') {
										pos++;
									}
									line++;posInLine=1;
									state = State.START;
									pos++;
									break;
								}
								pos++;
							} else if (pos >= length - 1) {
								state = State.START;
								if (pos == length - 1)
									pos++;posInLine++;
								break;
							} else {pos++;posInLine++;}
					}

				} else {
					tokens.add(new Token(Kind.OP_DIV,pos-1,1,line,posInLine-1));
					state = State.START;
				}
			}
				break;
				
			case IN_IDENT : 
				// KW_int/* int */, 
				// KW_boolean/* boolean */
				
				
				StringBuilder str = new StringBuilder();
				int strLength;
				str.append(chars[pos-1]);
				while (pos < length-1 && (Character.isJavaIdentifierStart(chars[pos]) || Character.isDigit(chars[pos]))) {
					str.append(chars[pos]);
					pos++;posInLine++;
				}
				state = State.START;
				strLength = str.length();
				String parseKeyw = str.toString();
				switch (parseKeyw) {
				case "int": {
					tokens.add(new Token(Kind.KW_int,pos-strLength,strLength,line,posInLine-strLength));
					
				}
					break;
				case "boolean": {
					tokens.add(new Token(Kind.KW_boolean,pos-strLength,strLength,line,posInLine-strLength));
				}
				break;
				case "true": {
					tokens.add(new Token(Kind.BOOLEAN_LITERAL,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "false": {
					tokens.add(new Token(Kind.BOOLEAN_LITERAL,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "image": {
					tokens.add(new Token(Kind.KW_image,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "url": {
					tokens.add(new Token(Kind.KW_url,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "file": {
					tokens.add(new Token(Kind.KW_file,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "log": {
					tokens.add(new Token(Kind.KW_log,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;

				case "atan": {
					tokens.add(new Token(Kind.KW_atan,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "cos": {
					tokens.add(new Token(Kind.KW_cos,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "sin": {
					tokens.add(new Token(Kind.KW_sin,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "abs": {
					tokens.add(new Token(Kind.KW_abs,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "polar_r": {
					tokens.add(new Token(Kind.KW_polar_r,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "polar_a": {
					tokens.add(new Token(Kind.KW_polar_a,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "cart_y": {
					tokens.add(new Token(Kind.KW_cart_y,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "cart_x": {
					tokens.add(new Token(Kind.KW_cart_x,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;

				case "SCREEN": {
					tokens.add(new Token(Kind.KW_SCREEN,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "DEF_Y": {
					tokens.add(new Token(Kind.KW_DEF_Y,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "DEF_X": {
					tokens.add(new Token(Kind.KW_DEF_X,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "Z": {
					tokens.add(new Token(Kind.KW_Z,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "A": {
					tokens.add(new Token(Kind.KW_A,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;

				case "a": {
					tokens.add(new Token(Kind.KW_a,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "R": {
					tokens.add(new Token(Kind.KW_R,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "r": {
					tokens.add(new Token(Kind.KW_r,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "Y": {
					tokens.add(new Token(Kind.KW_Y,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "y": {
					tokens.add(new Token(Kind.KW_y,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "X": {
					tokens.add(new Token(Kind.KW_X,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				case "x": {
					tokens.add(new Token(Kind.KW_x,pos-strLength,strLength,line,posInLine-strLength));
				}
					break;
				
					
				default: {
					tokens.add(new Token(Kind.IDENTIFIER,pos-strLength,strLength,line,posInLine-strLength));
				}

				}

				break;
			}
			
			
		}
		
		
		
		tokens.add(new Token(Kind.EOF, pos, 0, line, posInLine));
		return this;

	}

	
	/*
	 * Skips whitespaces and increments line 
	 */
	public int skipWhiteSpace(int pos) {
		while (pos < chars.length) {
			if (Character.isWhitespace(chars[pos])) {
				if (chars[pos] == '\n') {
					//increment line value and set posInLine to zero
				}
				pos++;
			} else
				break;
		}
		return pos;
	}
	
	

	/**
	 * Returns true if the internal interator has more Tokens
	 * 
	 * @return
	 */
	public boolean hasTokens() {
		return nextTokenPos < tokens.size();
	}

	/**
	 * Returns the next Token and updates the internal iterator so that
	 * the next call to nextToken will return the next token in the list.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * @return
	 */
	public Token nextToken() {
		return tokens.get(nextTokenPos++);
	}
	
	/**
	 * Returns the next Token, but does not update the internal iterator.
	 * This means that the next call to nextToken or peek will return the
	 * same Token as returned by this methods.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * 
	 * @return next Token.
	 */
	public Token peek() {
		return tokens.get(nextTokenPos);
	}
	
	
	/**
	 * Resets the internal iterator so that the next call to peek or nextToken
	 * will return the first Token.
	 */
	public void reset() {
		nextTokenPos = 0;
	}

	/**
	 * Returns a String representation of the list of Tokens 
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Tokens:\n");
		for (int i = 0; i < tokens.size(); i++) {
			sb.append(tokens.get(i)).append('\n');
		}
		return sb.toString();
	}

}
