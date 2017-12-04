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
		
		Program program=null;
		Token firstToken=t;
		Token name=t;
		ArrayList<ASTNode> decsAndStatements=new ArrayList<>();

		if (t.isKind(IDENTIFIER)) {
			consume();
			while(t.kind != EOF && (getpredictSets("Declaration").contains(t.kind) || getpredictSets("Statement").contains(t.kind))) {
				if (getpredictSets("Declaration").contains(t.kind)) {
					decsAndStatements.add(declaration());
					if (t.isKind(SEMI)) {
						consume();
					} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
					
				} else if (getpredictSets("Statement").contains(t.kind)) {
					decsAndStatements.add(statement());
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
		program =new Program(firstToken,name,decsAndStatements);
		return program;
	}

	

	private Declaration declaration() throws SyntaxException {
		// Declaration :: =  VariableDeclaration     |    ImageDeclaration   |   SourceSinkDeclaration  
		Declaration declaration=null;
		if (getpredictSets("VariableDeclaration").contains(t.kind)) {
			declaration = variableDeclaration();
		} else if (getpredictSets("ImageDeclaration").contains(t.kind)) {
			declaration = imageDeclaration();
		} else if (getpredictSets("SourceSinkDeclaration").contains(t.kind)) {
			declaration = sourceSinkDeclaration();
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return declaration;
	}

	private Declaration_SourceSink sourceSinkDeclaration() throws SyntaxException {
		// SourceSinkDeclaration ::= SourceSinkType IDENTIFIER  OP_ASSIGN  Source
		Declaration_SourceSink declaration_SourceSink=null;
        Token firstToken=t;
        Source s=null;
        Token type=null;
        Token name=null;
        
		if (getpredictSets("SourceSinkType").contains(t.kind)) {
			type=t;
			sourceSinkType();
			if (t.isKind(IDENTIFIER)) {
				name=t;
				consume();
				if (t.isKind(OP_ASSIGN)) {
					consume();
					if (getpredictSets("Source").contains(t.kind)) {
						s = source();
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
		declaration_SourceSink=new Declaration_SourceSink(firstToken,type,name,s);
        return declaration_SourceSink;
		
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

	private Declaration_Image imageDeclaration() throws SyntaxException {
		//ImageDeclaration ::=  KW_image  (LSQUARE Expression COMMA Expression RSQUARE | ε) IDENTIFIER ( OP_LARROW Source | ε ) 
		Token firstToken=t;
		Declaration_Image declaration_image=null;
		Expression xsize=null;
		Expression ysize=null;
		Source s=null;
		Token name=null;
		
		if (t.isKind(KW_image)) {
			consume();
			if (t.isKind(LSQUARE)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					xsize = expression();
					if (t.isKind(COMMA)) {
						consume();
						if (getpredictSets("Expression").contains(t.kind)) {
							ysize = expression();
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
				name=t;
				consume();
				if (t.isKind(OP_LARROW)) {
					consume();
					if (getpredictSets("Source").contains(t.kind)) {
						s = source();
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
		declaration_image=new Declaration_Image(firstToken,xsize,ysize,name,s);
        return declaration_image;
		
	}

	private Declaration_Variable variableDeclaration() throws SyntaxException {
		// VariableDeclaration  ::=  VarType IDENTIFIER  (  OP_ASSIGN  Expression  | ε )
		Declaration_Variable declarationVariable=null;
		Token firstToken=t;
		Token type=t;
		Expression expression=null;
		Token name=null;
		
		if (getpredictSets("VarType").contains(t.kind)) {
			varType();
			name=t;
			if (t.isKind(IDENTIFIER)) {
				consume();
				if (t.isKind(OP_ASSIGN)) {
					consume();
					if (getpredictSets("Expression").contains(t.kind)) {
						expression = expression();
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
		declarationVariable=new Declaration_Variable(firstToken,type,name,expression);
		return declarationVariable;
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
	Expression expression() throws SyntaxException {
		//Expression ::=  OrExpression  (OP_Q  Expression OP_COLON Expression   |    ε   )
		Token firstToken=t;
		Expression condition=null;
		Expression trueExpression=null;
		Expression falseExpression=null;
		
		if (getpredictSets("OrExpression").contains(t.kind)) {
			condition = orExpression();
			if (t.isKind(OP_Q)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					trueExpression = expression();
					if (t.isKind(OP_COLON)) {
						consume();
						if (getpredictSets("Expression").contains(t.kind)) {
							falseExpression = expression();
						}else {
							throw new SyntaxException(t,"Error while parsing program at " + t.kind);
						}
						condition = new Expression_Conditional(firstToken, condition, trueExpression, falseExpression);
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return condition;
		
	}
	
	

	public Expression orExpression() throws SyntaxException {
		// OrExpression ::= AndExpression   (  OP_OR  AndExpression)*
		Token firstToken=t;
		Expression e0=null;
		Token op=null;
		Expression e1=null;
		if (getpredictSets("AndExpression").contains(t.kind)) {
			e0 = andExpression();
			while(t.isKind(OP_OR)) {
				op = t;
				consume();
				if (getpredictSets("AndExpression").contains(t.kind)) {
					e1 = andExpression();
					
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				e0 = new Expression_Binary(firstToken,e0,op,e1);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		
		return e0;
		
	}

	public Expression andExpression() throws SyntaxException {
		// AndExpression ::= EqExpression ( OP_AND  EqExpression )*
		Token firstToken=t;
		Expression e0=null;
		Token op=null;
		Expression e1=null;
		
		if (getpredictSets("EqExpression").contains(t.kind)) {
			e0 = eqExpression();
			while(t.isKind(OP_AND)) {
				op = t;
				consume();
				if (getpredictSets("EqExpression").contains(t.kind)) {
					e1 = andExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				e0 = new Expression_Binary(firstToken,e0,op,e1);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return e0;
	}

	public Expression eqExpression() throws SyntaxException {
		// EqExpression ::= RelExpression  (  (OP_EQ | OP_NEQ )  RelExpression )*
		Token firstToken=t;
		Expression e0=null;
		Token op=null;
		Expression e1=null;
		
		if (getpredictSets("RelExpression").contains(t.kind)) {
			e0 = relExpression();
			while(t.isKind(OP_EQ) || t.isKind(OP_NEQ)) {
				op=t;
				consume();
				if (getpredictSets("RelExpression").contains(t.kind)) {
					e1 = relExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				e0 = new Expression_Binary(firstToken,e0,op,e1);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return e0;
	}

	public Expression relExpression() throws SyntaxException {
		// RelExpression ::= AddExpression (  ( OP_LT  | OP_GT |  OP_LE  | OP_GE )   AddExpression)*
		Token firstToken=t;
		Expression e0=null;
		Token op=null;
		Expression e1=null;
		
		if (getpredictSets("AddExpression").contains(t.kind)) {
			e0 = addExpression();
			while(t.isKind(OP_LT) || t.isKind(OP_GT) || t.isKind(OP_LE) || t.isKind(OP_GE)) {
				op=t;
				consume();
				if (getpredictSets("AddExpression").contains(t.kind)) {
					e1 = addExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				e0 = new Expression_Binary(firstToken,e0,op,e1);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return e0;
	}

	public Expression addExpression() throws SyntaxException {
		// AddExpression ::= MultExpression   (  (OP_PLUS | OP_MINUS ) MultExpression )*
		Token firstToken=t;
		Expression e0=null;
		Token op=null;
		Expression e1=null;
		
		if (getpredictSets("MultExpression").contains(t.kind)) {
			e0 = multExpression();
			while(t.isKind(OP_PLUS) || t.isKind(OP_MINUS)) {
				op=t;
				consume();
				if (getpredictSets("MultExpression").contains(t.kind)) {
					e1 = multExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				e0 = new Expression_Binary(firstToken,e0,op,e1);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return e0;
		
	}

	public Expression multExpression() throws SyntaxException {
		// MultExpression := UnaryExpression ( ( OP_TIMES | OP_DIV  | OP_MOD ) UnaryExpression )*
		Expression_Binary expression_binary=null;
		Token firstToken=t;
		Expression e0=null;
		Token op=null;
		Expression e1=null;
		
		if (getpredictSets("UnaryExpression").contains(t.kind)) {
			e0 = unaryExpression();
			while(t.isKind(OP_TIMES) || t.isKind(OP_DIV) || t.isKind(OP_MOD)) {
				op=t;
				consume();
				if (getpredictSets("UnaryExpression").contains(t.kind)) {
					e1 = unaryExpression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				
				e0=new Expression_Binary(firstToken,e0,op,e1);
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		return e0;
		
	}

	public Statement statement() throws SyntaxException {
		//  Statement  ::=  IDENTIFIER ( AssignmentStatement | ImageOutStatement   | ImageInStatement)
		Statement statement=null;
		Token newToken=scanner.peek();
		
//		if (t.isKind(IDENTIFIER)) {
//			consume();
		
			if (getpredictSets("AssignmentStatement").contains(newToken.kind)) {
				statement = assignmentStatement();
			} else if (getpredictSets("ImageOutStatement").contains(newToken.kind)) {
				statement = imageOutStatement();
			} else if (getpredictSets("ImageInStatement").contains(newToken.kind)) {
				statement = imageInStatement();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
			
//		} else {
//			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
//		}
		return statement;
	}

	private Statement_Assign assignmentStatement() throws SyntaxException{
		// AssignmentStatement ::= Lhs OP_ASSIGN Expression
		Statement_Assign statement_assign=null;
        Token firstToken=t;
        LHS lhs=null;
        Expression expression=null;
        
		
			lhs = lhs();
			if (t.isKind(OP_ASSIGN)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression = expression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		
		statement_assign=new Statement_Assign(firstToken,lhs,expression);
        return statement_assign;

	}

	public LHS lhs() throws SyntaxException {
		// Lhs::=  LSQUARE LhsSelector RSQUARE   | ε 
		LHS lhs=null;
        Token firstToken=t;
        Token name=t;
        Index in=null;
        
        if (t.isKind(IDENTIFIER)) {
			consume();
        } else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
	    if (getpredictSets("Lhs").contains(t.kind)) {
			if (t.isKind(LSQUARE)) {
				consume();
				if (getpredictSets("LhsSelector").contains(t.kind)) {
					in = lhsSelector();
					if (t.isKind(RSQUARE)) {
						consume();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}
	    }else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		 lhs=new LHS(firstToken,name,in);
	     return lhs;
		
	}

	public Index lhsSelector() throws SyntaxException {
		// LhsSelector ::= LSQUARE  ( XySelector  | RaSelector  )   RSQUARE
		Index i=null;
		
		if (t.isKind(LSQUARE)) {
			consume();
			if (getpredictSets("XySelector").contains(t.kind)) {
				i = xySelector();
				if (t.isKind(RSQUARE)) {
					consume();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			} else if (getpredictSets("RaSelector").contains(t.kind)) {
				i = raSelector();
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
		return i;
		
	}

	public Index raSelector() throws SyntaxException {
		// KW_r COMMA KW_A
		Index i=null;
        Token firstToken=t;
        Expression_PredefinedName e0=null;
        Expression_PredefinedName e1=null;
        
		if (t.isKind(KW_r)) {
			Token ftr=t;
			Kind k=t.kind;
			consume();
			e0=new Expression_PredefinedName(ftr,k);
			if (t.isKind(COMMA)) {
				consume();
				if (t.isKind(KW_a)) {
					Token ftA=t;
	                 k=t.kind;
					consume();
					e1=new Expression_PredefinedName(ftA,k);
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		i=new Index(firstToken,e0,e1);
        return i;
		
	}

	public Index xySelector() throws SyntaxException {
		// KW_x COMMA KW_y
		Index i=null;
        Token firstToken=t;
        Expression_PredefinedName e0=null;
        Expression_PredefinedName e1=null;
        Kind k;
        
		if (t.isKind(KW_x)) {
			Token ftx=t;
			k=t.kind;
			consume();
			e0=new Expression_PredefinedName(ftx,k);
			if (t.isKind(COMMA)) {
				consume();
				if (t.isKind(KW_y)) {
					Token fty=t;
				    k=t.kind;
					consume();
					e1=new Expression_PredefinedName(fty,k);
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		i=new Index(firstToken,e0,e1);
	     return i;
	}

	private Statement_Out imageOutStatement() throws SyntaxException{
		// ImageOutStatement ::= OP_RARROW Sink 
		Statement_Out statement_out=null;
        Token firstToken=t;
        Token name=t;
        Sink sink=null;
        
        if (t.isKind(IDENTIFIER)) {
			consume();
        } else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		if (t.isKind(OP_RARROW)) {
			consume();
			if (getpredictSets("Sink").contains(t.kind)) {
				sink = sink();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		statement_out=new Statement_Out(firstToken,name,sink);
        return statement_out;


	}
	
	private Sink sink() throws SyntaxException {
		// Sink ::= IDENTIFIER | KW_SCREEN  //ident must be file
		
		Sink sink=null;
        Token firstToken=t;
        Token name=t;
        
		if (t.isKind(IDENTIFIER)) {
			sink = new Sink_Ident(firstToken,name);
			consume();
		}else if (t.isKind(KW_SCREEN)) {
			sink = new Sink_SCREEN(firstToken);
			consume();
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return sink;
	}

	private Statement_In imageInStatement() throws SyntaxException{
		// ImageInStatement ::= OP_LARROW Source
		Statement_In statement_in=null;
        Token firstToken=t;
        Token name=t;
        Source source=null;
        if (t.isKind(IDENTIFIER)) {
			consume();
        } else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
        
		if (t.isKind(OP_LARROW)) {
			consume();
			if (getpredictSets("Source").contains(t.kind)) {
				source = source();
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		statement_in=new Statement_In(firstToken,name,source);
        return statement_in;

	}

	private Source source() throws SyntaxException {
		// STRING_LITERAL  |   OP_AT Expression |   IDENTIFIER 
		Source s=null;
		 Token firstToken=t;
		 Token name=t; 
		
		if (t.isKind(STRING_LITERAL)) {
			String fileorurl=t.getText();
			consume();
			s=new Source_StringLiteral(firstToken,fileorurl);
		} else if (t.isKind(OP_AT)) {
			Expression paramnum=null;
			consume();
			if (getpredictSets("Expression").contains(t.kind)) {
				paramnum = expression();
				s=new Source_CommandLineParam(firstToken,paramnum);
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else if (t.isKind(IDENTIFIER)) {
			consume();
			s=new Source_Ident(firstToken,name);
		}else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return s;
	}
	
	
	
	
	public Expression unaryExpressionNotPlusMinus() throws SyntaxException {
		
		// UnaryExpressionNotPlusMinus ::=  OP_EXCL  UnaryExpression  | Primary |
		//  IdentOrPixelSelectorExpression | KW_x | KW_y | KW_r | KW_a | KW_X |
		//  KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
		
		Expression expression_unary=null;
		Token firstToken=t;
		Token op=null;
		Expression expression=null;
 
		if (t.isKind(OP_EXCL)) {
			op = t;
			consume();
			if (getpredictSets("UnaryExpression").contains(t.kind)) {
				expression = unaryExpression();
				expression_unary = new Expression_Unary(firstToken,op,expression);
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else if (getpredictSets("Primary").contains(t.kind)) {
			expression_unary = primary();
		} else if (getpredictSets("IdentOrPixelSelectorExpression").contains(t.kind)) {
			expression_unary = identOrPixelSelectorExpression();
		}else {
		//  IdentOrPixelSelectorExpression | KW_x | KW_y | KW_r | KW_a | KW_X |
			// KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
			Kind kind = t.kind;
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
			
			expression_unary = new Expression_PredefinedName(firstToken,kind);
		}
		return expression_unary;
	}

	public Expression identOrPixelSelectorExpression() throws SyntaxException {
		// IdentOrPixelSelectorExpression::=  IDENTIFIER (LSQUARE Selector RSQUARE   |  ε )
		 Expression expression=null;
	     Token firstToken=t;
	     Token ident=t;
         Token name=t;
         
		if (t.isKind(IDENTIFIER)) {
			consume();
			expression=new Expression_Ident(firstToken,ident);
			if (t.isKind(LSQUARE)) {
				Index i=null;
				consume();
				if (getpredictSets("Selector").contains(t.kind)) {
					i = selector();
					if (t.isKind(RSQUARE)) {
						consume();
					} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
					expression = new Expression_PixelSelector(firstToken,name,i);
				} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
				
			}
			
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		return expression;
		
	}

	public Index selector() throws SyntaxException {
		// Expression COMMA Expression   
		Index i=null;
        Token firstToken=t;
        Expression e0=null;
        Expression e1=null;
		
		if (getpredictSets("Expression").contains(t.kind)) {
			e0 = expression();
			if (t.isKind(COMMA)) {
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					e1 = expression();
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		i=new Index(firstToken,e0,e1);
        return i;
	}

	public Expression primary() throws SyntaxException {
		// INTEGER_LITERAL | LPAREN Expression RPAREN | FunctionApplication | BOOLEAN_LITERAL
		Expression expression=null;
	     Token firstToken=t;
	     
		if (t.isKind(INTEGER_LITERAL)) {
			int val=Integer.parseInt(t.getText());
			consume();
			expression=new Expression_IntLit(firstToken,val);
		}else if (t.isKind(LPAREN)) {
			consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					expression = expression();
					if (t.isKind(RPAREN)) {
						consume();
					} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
				} else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
		} else if (getpredictSets("FunctionApplication").contains(t.kind)) {
			expression = functionApplication();
		} else if (t.isKind(BOOLEAN_LITERAL)) {
			boolean val=Boolean.parseBoolean(t.getText());
			consume();
			expression=new Expression_BooleanLit(firstToken,val);
		} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
			
		return expression;
		
	}

	public Expression functionApplication() throws SyntaxException {
		// FunctionApplication ::= FunctionName (LPAREN Expression RPAREN  |  LSQUARE Selector RSQUARE )
		Expression expression=null;
        Token firstToken=t;
        
		if (getpredictSets("FunctionName").contains(t.kind)) {
			Kind func=t.kind;
			functionName();
			
			if (t.isKind(LPAREN)) {
				Expression arg=null;
				consume();
				if (getpredictSets("Expression").contains(t.kind)) {
					arg = expression();
					if (t.isKind(RPAREN)) {
						consume();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
					expression = new Expression_FunctionAppWithExprArg(firstToken,func,arg);
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else if (t.isKind(LSQUARE)) {
				Index arg=null;
				consume();
				if (getpredictSets("Selector").contains(t.kind)) {
					arg = selector();
					if (t.isKind(RSQUARE)) {
						consume();
					}else {
						throw new SyntaxException(t,"Error while parsing program at " + t.kind);
					}
					expression = new Expression_FunctionAppWithIndexArg(firstToken,func,arg);
				}else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
				}
			}else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else {
			throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		return expression;
		
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

	public Expression unaryExpression() throws SyntaxException {
		// OP_PLUS UnaryExpression | OP_MINUS UnaryExpression    | UnaryExpressionNotPlusMinus
		Expression_Unary expression_unary=null;
		Token firstToken=t;
		Token op=null;
		Expression e=null;
		
		if (t.isKind(OP_PLUS)) {
			op = t;
			consume();
			if (getpredictSets("UnaryExpression").contains(t.kind)) {
				e = unaryExpression();
			} else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		}else if (t.isKind(OP_MINUS)) {
			op = t;
			consume();
			if (getpredictSets("UnaryExpression").contains(t.kind)) {
				e = unaryExpression();
			} else {
					throw new SyntaxException(t,"Error while parsing program at " + t.kind);
			}
		} else if (getpredictSets("UnaryExpressionNotPlusMinus").contains(t.kind)) {
			e = unaryExpressionNotPlusMinus();
			return e;
		} else {
				throw new SyntaxException(t,"Error while parsing program at " + t.kind);
		}
		
		expression_unary=new Expression_Unary(firstToken,op,e);
		return expression_unary;
		
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
