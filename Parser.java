package cop5556fa17;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;

import static cop5556fa17.Scanner.Kind.*;

import cop5556fa17.AST.*;

public class Parser {

	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;

		public SyntaxException(Token t, String message) {
			super(message);
			this.t = t;
		}

	}


	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * Main method called by compiler to parser input.
	 * Checks for EOF
	 * 
	 * @throws SyntaxException
	 */
	public Program parse() throws SyntaxException {
		Program p=null;
		p = program();
		matchEOF();
		return p;
	}
	

	/**
	 * Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	 * 
	 * Program is start symbol of our grammar.
	 * @return 
	 * 
	 * @throws SyntaxException
	 */
	Program program() throws SyntaxException {
		// IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   

		if (t.isKind(IDENTIFIER)) {
			consume();
			while(t.kind != EOF && (getpredictSets("Declaration").contains(t.kind) || getpredictSets("Statement").contains(t.kind))) {
				if (getpredictSets("Declaration").contains(t.kind)) {
					declaration();
					if (t.isKind(SEMI)) {
						consume();
					} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
					
				} else if (getpredictSets("Statement").contains(t.kind)) {
					statement();
					if (t.isKind(SEMI)) {
						consume();
					} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				} else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			
			
		} else {
			throw new SyntaxException(t,"Error while parsing program. Doesn't start with IDENTIFIER");
		}
		
	}

	

	void declaration() throws SyntaxException {
		// Declaration :: =  VariableDeclaration     |    ImageDeclaration   |   SourceSinkDeclaration  
		if (getpredictSets("VariableDeclaration").contains(t.kind)) {
			variableDeclaration();
		} else if (getpredictSets("ImageDeclaration").contains(t.kind)) {
			imageDeclaration();
		} else if (getpredictSets("SourceSinkDeclaration").contains(t.kind)) {
			sourceSinkDeclaration();
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void sourceSinkDeclaration() throws SyntaxException {
		// SourceSinkDeclaration ::= SourceSinkType IDENTIFIER  OP_ASSIGN  Source
		if (getpredictSets("SourceSinkType").contains(t.kind)) {
			sourceSinkType();
			if (t.isKind(IDENTIFIER)) {
				consume();
				if (t.isKind(OP_ASSIGN)) {
					consume();
					if (getpredictSets("Source").contains(t.kind)) {
						source();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void sourceSinkType() throws SyntaxException {
		// SourceSinkType := KW_url | KW_file
		if (t.isKind(KW_url)) {
			consume();
		}else if (t.isKind(KW_file)) {
			consume();
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void imageDeclaration() throws SyntaxException {
		//ImageDeclaration ::=  KW_image  (LSQUARE Expression COMMA Expression RSQUARE | ε) IDENTIFIER ( OP_LARROW Source | ε )   
		if (t.isKind(KW_image)) {
			consume();
			if (t.isKind(LSQUARE)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression();
					if (t.isKind(COMMA)) {
						consume();
						if (getpredictSets("Expression").contains(t.kind)) {
							expression();
							if (t.isKind(RSQUARE)) {
								consume();
							}else {
								throw new SyntaxException(t,"Error while parsing program at " + t.kind);
							}
						}else {
							throw new SyntaxException(t,"Error while parsing program at " + t.kind);
						}
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			if (t.isKind(IDENTIFIER)) {
				consume();
				if (t.isKind(OP_LARROW)) {
					consume();
					if (getpredictSets("Source").contains(t.kind)) {
						source();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}	
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void variableDeclaration() throws SyntaxException {
		// VariableDeclaration  ::=  VarType IDENTIFIER  (  OP_ASSIGN  Expression  | ε )
		if (getpredictSets("VarType").contains(t.kind)) {
			varType();
			if (t.isKind(IDENTIFIER)) {
				consume();
				if (t.isKind(OP_ASSIGN)) {
					consume();
					if (getpredictSets("Expression").contains(t.kind)) {
						expression();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void varType() throws SyntaxException {
		// VarType ::= KW_int | KW_boolean
		if (t.isKind(KW_int)) {
			consume();
		}else if (t.isKind(KW_boolean)) {
			consume();
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	/**
	 * Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression    | OrExpression
	 * 
	 * Our test cases may invoke this routine directly to support incremental development.
	 * 
	 * @throws SyntaxException
	 */
	void expression() throws SyntaxException {
		//Expression ::=  OrExpression  (OP_Q  Expression OP_COLON Expression   |    ε   )
		if (getpredictSets("OrExpression").contains(t.kind)) {
			orExpression();
			if (t.isKind(OP_Q)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression();
					if (t.isKind(OP_COLON)) {
						consume();
						if (getpredictSets("Expression").contains(t.kind)) {
							expression();
						}else {
							throw new SyntaxException(t,"Error while parsing program at " + t.kind);
						}
					}
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}
	
	

	void orExpression() throws SyntaxException {
		// OrExpression ::= AndExpression   (  OP_OR  AndExpression)*
		if (getpredictSets("AndExpression").contains(t.kind)) {
			andExpression();
			while(t.isKind(OP_OR)) {
				consume();
				if (getpredictSets("AndExpression").contains(t.kind)) {
					andExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		
		
		
	}

	private void andExpression() throws SyntaxException {
		// AndExpression ::= EqExpression ( OP_AND  EqExpression )*
		if (getpredictSets("EqExpression").contains(t.kind)) {
			eqExpression();
			while(t.isKind(OP_AND)) {
				consume();
				if (getpredictSets("EqExpression").contains(t.kind)) {
					andExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
	}

	void eqExpression() throws SyntaxException {
		// EqExpression ::= RelExpression  (  (OP_EQ | OP_NEQ )  RelExpression )*

		if (getpredictSets("RelExpression").contains(t.kind)) {
			relExpression();
			while(t.isKind(OP_EQ) || t.isKind(OP_NEQ)) {
				consume();
				if (getpredictSets("RelExpression").contains(t.kind)) {
					relExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void relExpression() throws SyntaxException {
		// RelExpression ::= AddExpression (  ( OP_LT  | OP_GT |  OP_LE  | OP_GE )   AddExpression)*
		if (getpredictSets("AddExpression").contains(t.kind)) {
			addExpression();
			while(t.isKind(OP_LT) || t.isKind(OP_GT) || t.isKind(OP_LE) || t.isKind(OP_GE)) {
				consume();
				if (getpredictSets("AddExpression").contains(t.kind)) {
					addExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
	}

	void addExpression() throws SyntaxException {
		// AddExpression ::= MultExpression   (  (OP_PLUS | OP_MINUS ) MultExpression )*
		if (getpredictSets("MultExpression").contains(t.kind)) {
			multExpression();
			while(t.isKind(OP_PLUS) || t.isKind(OP_MINUS)) {
				consume();
				if (getpredictSets("MultExpression").contains(t.kind)) {
					multExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
	}

	void multExpression() throws SyntaxException {
		// MultExpression := UnaryExpression ( ( OP_TIMES | OP_DIV  | OP_MOD ) UnaryExpression )*
		if (getpredictSets("UnaryExpression").contains(t.kind)) {
			unaryExpression();
			while(t.isKind(OP_TIMES) || t.isKind(OP_DIV) || t.isKind(OP_MOD)) {
				consume();
				if (getpredictSets("UnaryExpression").contains(t.kind)) {
					unaryExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void statement() throws SyntaxException {
		//  Statement  ::=  IDENTIFIER ( AssignmentStatement | ImageOutStatement   | ImageInStatement)
		
		if (t.isKind(IDENTIFIER)) {
			consume();
			if (getpredictSets("AssignmentStatement").contains(t.kind)) {
				assignmentStatement();
			} else if (getpredictSets("ImageOutStatement").contains(t.kind)) {
				imageOutStatement();
			} else if (getpredictSets("ImageInStatement").contains(t.kind)) {
				imageInStatement();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void assignmentStatement() throws SyntaxException{
		// AssignmentStatement ::= Lhs OP_ASSIGN Expression
		if (getpredictSets("Lhs").contains(t.kind)) {
			lhs();
			if (t.isKind(OP_ASSIGN)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}

	}

	void lhs() throws SyntaxException {
		// Lhs::=  LSQUARE LhsSelector RSQUARE   | ε 
		if (t.isKind(LSQUARE)) {
			consume();
			if (getpredictSets("LhsSelector").contains(t.kind)) {
				lhsSelector();
				if (t.isKind(RSQUARE)) {
					consume();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}
		
	}

	void lhsSelector() throws SyntaxException {
		// LhsSelector ::= LSQUARE  ( XySelector  | RaSelector  )   RSQUARE
		if (t.isKind(LSQUARE)) {
			consume();
			if (getpredictSets("XySelector").contains(t.kind)) {
				xySelector();
				if (t.isKind(RSQUARE)) {
					consume();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			} else if (getpredictSets("RaSelector").contains(t.kind)) {
				raSelector();
				if (t.isKind(RSQUARE)) {
					consume();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void raSelector() throws SyntaxException {
		// KW_r COMMA KW_A
		if (t.isKind(KW_r)) {
			consume();
			if (t.isKind(COMMA)) {
				consume();
				if (t.isKind(KW_A)) {
					consume();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void xySelector() throws SyntaxException {
		// KW_x COMMA KW_y
		if (t.isKind(KW_x)) {
			consume();
			if (t.isKind(COMMA)) {
				consume();
				if (t.isKind(KW_y)) {
					consume();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void imageOutStatement() throws SyntaxException{
		// ImageOutStatement ::= OP_RARROW Sink 
		
		if (t.isKind(OP_RARROW)) {
			consume();
			if (getpredictSets("Sink").contains(t.kind)) {
				sink();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}


	}
	
	void sink() throws SyntaxException {
		// Sink ::= IDENTIFIER | KW_SCREEN  //ident must be file
		if (t.isKind(IDENTIFIER)) {
			consume();
		}else if (t.isKind(KW_SCREEN)) {
			consume();
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void imageInStatement() throws SyntaxException{
		// ImageInStatement ::= OP_LARROW Source
		if (t.isKind(OP_LARROW)) {
			consume();
			if (getpredictSets("Source").contains(t.kind)) {
				source();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}

	}

	void source() throws SyntaxException {
		// STRING_LITERAL  |   OP_AT Expression |   IDENTIFIER 
		if (t.isKind(STRING_LITERAL)) {
			consume();
		} else if (t.isKind(OP_AT)) {
			consume();
			if (getpredictSets("Expression").contains(t.kind)) {
				expression();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else if (t.isKind(IDENTIFIER)) {
			consume();
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
	}
	
	
	
	
	void unaryExpressionNotPlusMinus() throws SyntaxException {
		
		// UnaryExpressionNotPlusMinus ::=  OP_EXCL  UnaryExpression  | Primary |
		//  IdentOrPixelSelectorExpression | KW_x | KW_y | KW_r | KW_a | KW_X |
		//  KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
 
		if (t.isKind(OP_EXCL)) {
			consume();
			if (getpredictSets("UnaryExpression").contains(t.kind)) {
				unaryExpression();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else if (getpredictSets("Primary").contains(t.kind)) {
			primary();
		} else if (getpredictSets("IdentOrPixelSelectorExpression").contains(t.kind)) {
			identOrPixelSelectorExpression();
		}else {
		//  IdentOrPixelSelectorExpression | KW_x | KW_y | KW_r | KW_a | KW_X |
			// KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
			switch (t.kind) {
			case KW_x: {
				consume();
			}
				break;
			case KW_y: {
				consume();
			}
				break;
			case KW_r: {
				consume();
			}
				break;
			case KW_a: {
				consume();
			}
				break;
			case KW_X: {
				consume();
			}
				break;
			case KW_Y: {
				consume();
			}
				break;
			case KW_Z: {
				consume();
			}
				break;
			case KW_A: {
				consume();
			}
				break;
			case KW_R: {
				consume();
			}
				break;
			case KW_DEF_X: {
				consume();
			}
				break;
			case KW_DEF_Y: {
				consume();
			}
				break;
			default:
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}
	}

	void identOrPixelSelectorExpression() throws SyntaxException {
		// IdentOrPixelSelectorExpression::=  IDENTIFIER (LSQUARE Selector RSQUARE   |  ε )
		if (t.isKind(IDENTIFIER)) {
			consume();
			if (t.isKind(LSQUARE)) {
				consume();
				if (getpredictSets("Selector").contains(t.kind)) {
					selector();
					if (t.isKind(RSQUARE)) {
						consume();
					} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		
	}

	void selector() throws SyntaxException {
		// Expression COMMA Expression   
		if (getpredictSets("Expression").contains(t.kind)) {
			expression();
			if (t.isKind(COMMA)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
	}

	void primary() throws SyntaxException {
		// INTEGER_LITERAL | LPAREN Expression RPAREN | FunctionApplication | BOOLEAN_LITERAL
		if (t.isKind(INTEGER_LITERAL)) {
			consume();
		}else if (t.isKind(LPAREN)) {
			consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression();
					if (t.isKind(RPAREN)) {
						consume();
					} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
		} else if (getpredictSets("FunctionApplication").contains(t.kind)) {
				functionApplication();
		} else if (t.isKind(BOOLEAN_LITERAL)) {
			consume();
		} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
			
		
		
	}

	void functionApplication() throws SyntaxException {
		// FunctionApplication ::= FunctionName (LPAREN Expression RPAREN  |  LSQUARE Selector RSQUARE )
		
		if (getpredictSets("FunctionName").contains(t.kind)) {
			functionName();
			if (t.isKind(LPAREN)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression();
					if (t.isKind(RPAREN)) {
						consume();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else if (t.isKind(LSQUARE)) {
				consume();
				if (getpredictSets("Selector").contains(t.kind)) {
					selector();
					if (t.isKind(RSQUARE)) {
						consume();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void functionName() throws SyntaxException {
		// FunctionName ::= KW_sin | KW_cos | KW_atan | KW_abs 
		// | KW_cart_x | KW_cart_y | KW_polar_a | KW_polar_r
		switch (t.kind) {
		case KW_sin: {
			consume();
		}
			break;
		case KW_cos: {
			consume();
		}
			break;
		case KW_atan: {
			consume();
		}
			break;
		case KW_abs: {
			consume();
		}
			break;
		case KW_cart_x: {
			consume();
		}
			break;
		case KW_cart_y: {
			consume();
		}
			break;
		case KW_polar_a: {
			consume();
		}
			break;
		case KW_polar_r: {
			consume();
		}
			break;
		default:
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	void unaryExpression() throws SyntaxException {
		// OP_PLUS UnaryExpression | OP_MINUS UnaryExpression    | UnaryExpressionNotPlusMinus
		if (t.isKind(OP_PLUS)) {
			consume();
			if (getpredictSets("UnaryExpression").contains(t.kind)) {
				unaryExpression();
			} else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else if (t.isKind(OP_MINUS)) {
			consume();
			if (getpredictSets("UnaryExpression").contains(t.kind)) {
				unaryExpression();
			} else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else if (getpredictSets("UnaryExpressionNotPlusMinus").contains(t.kind)) {
			unaryExpressionNotPlusMinus();
		} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
	}

	/**
	 * Only for check at end of program. Does not "consume" EOF so no attempt to get
	 * nonexistent next Token.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind == EOF) {
			return t;
		}
		String message =  "Expected EOL at " + t.line + ":" + t.pos_in_line;
		throw new SyntaxException(t, message);
	}
	
	
	
	
	
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

	private ArrayList<Kind> getpredictSets(String element) {
		ArrayList<Kind> predictSets = new ArrayList<Kind>();
		return getpredictSets(element, predictSets);
	}
	

	private ArrayList<Kind> getpredictSets(String element, ArrayList<Kind> predictSets) {
		switch (element) {
		case "Declaration":
			predictSets = getpredictSets("VariableDeclaration", predictSets);
			predictSets = getpredictSets("ImageDeclaration", predictSets);
			predictSets = getpredictSets("SourceSinkDeclaration", predictSets);
			break;
		case "VariableDeclaration":
			predictSets = getpredictSets("VarType", predictSets);
			break;
		case "VarType":
			predictSets.add(Kind.KW_int);
			predictSets.add(Kind.KW_boolean);
			break;
		case "SourceSinkDeclaration":
			predictSets = getpredictSets("SourceSinkType", predictSets);
			break;
		case "Source":
			predictSets.add(Kind.STRING_LITERAL);
			predictSets.add(Kind.OP_AT);
			predictSets.add(Kind.IDENTIFIER);
			break;
		case "SourceSinkType":
			predictSets.add(Kind.KW_url);
			predictSets.add(Kind.KW_file);
			break;
		case "ImageDeclaration":
			predictSets.add(Kind.KW_image);
			break;
		case "Statement":
			predictSets.add(Kind.IDENTIFIER);
			break;
		case "ImageOutStatement":
			predictSets.add(Kind.OP_RARROW);
			break;
		case "Sink":
			predictSets.add(Kind.IDENTIFIER);
			predictSets.add(Kind.KW_SCREEN);
			break;
		case "ImageInStatement":
			predictSets.add(Kind.OP_LARROW);
			break;
		case "AssignmentStatement":
			predictSets = getpredictSets("Lhs", predictSets);
			break;
		case "Expression":
			predictSets = getpredictSets("OrExpression", predictSets);
			break;
		case "OrExpression":
			predictSets = getpredictSets("AndExpression", predictSets);
			break;
		case "AndExpression":
			predictSets = getpredictSets("EqExpression", predictSets);
			break;
		case "EqExpression":
			predictSets = getpredictSets("RelExpression", predictSets);
			break;
		case "RelExpression":
			predictSets = getpredictSets("AddExpression", predictSets);
			break;
		case "AddExpression":
			predictSets = getpredictSets("MultExpression", predictSets);
			break;
		case "MultExpression":
			predictSets = getpredictSets("UnaryExpression", predictSets);
			break;
		case "UnaryExpression":
			predictSets.add(Kind.OP_PLUS);
			predictSets.add(Kind.OP_MINUS);
			predictSets = getpredictSets("UnaryExpressionNotPlusMinus", predictSets);
			break;
		case "UnaryExpressionNotPlusMinus":
			predictSets.add(Kind.OP_EXCL);
			predictSets = getpredictSets("Primary", predictSets);
			predictSets = getpredictSets("IdentOrPixelSelectorExpression", predictSets);
			predictSets.add(Kind.KW_x);
			predictSets.add(Kind.KW_y);
			predictSets.add(Kind.KW_r);
			predictSets.add(Kind.KW_a);
			predictSets.add(Kind.KW_X);
			predictSets.add(Kind.KW_Y);
			predictSets.add(Kind.KW_Z);
			predictSets.add(Kind.KW_A);
			predictSets.add(Kind.KW_R);
			predictSets.add(Kind.KW_DEF_X);
			predictSets.add(Kind.KW_DEF_Y);
			break;
		case "Primary":
			predictSets.add(Kind.INTEGER_LITERAL);
			predictSets.add(Kind.LPAREN);
			predictSets = getpredictSets("FunctionName", predictSets);
			predictSets.add(Kind.BOOLEAN_LITERAL);
			break;
		case "IdentOrPixelSelectorExpression":
			predictSets.add(Kind.IDENTIFIER);
			break;
		case "Lhs":
			predictSets.add(Kind.LSQUARE);
			predictSets.add(Kind.OP_ASSIGN);
			break;
		case "FunctionApplication":
			predictSets = getpredictSets("FunctionName", predictSets);
			break;
		case "FunctionName":
			predictSets.add(Kind.KW_sin);
			predictSets.add(Kind.KW_cos);
			predictSets.add(Kind.KW_atan);
			predictSets.add(Kind.KW_abs);
			predictSets.add(Kind.KW_cart_x);
			predictSets.add(Kind.KW_cart_y);
			predictSets.add(Kind.KW_polar_a);
			predictSets.add(Kind.KW_polar_r);
			break;
		case "LhsSelector":
			predictSets.add(Kind.LSQUARE);
			break;
		case "XySelector":
			predictSets.add(Kind.KW_x);
			break;
		case "RaSelector":
			predictSets.add(Kind.KW_r);
			break;
		case "Selector":
			predictSets = getpredictSets("Expression", predictSets);
			break;
		default:
			return null;
		}

		Set<Kind> hs = new HashSet<>();
		hs.addAll(predictSets);
		predictSets.clear();
		predictSets.addAll(hs);
		return predictSets;

	}
	
	
}
