package cop5556fa17;

import cop5556fa17.Scanner.Token;
import cop5556fa17.Scanner.Kind;
import cop5556fa17.TypeUtils.Type;
import cop5556fa17.AST.*;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression_Binary;
import cop5556fa17.AST.Expression_BooleanLit;
import cop5556fa17.AST.Expression_Conditional;
import cop5556fa17.AST.Expression_FunctionAppWithExprArg;
import cop5556fa17.AST.Expression_FunctionAppWithIndexArg;
import cop5556fa17.AST.Expression_Ident;
import cop5556fa17.AST.Expression_IntLit;
import cop5556fa17.AST.Expression_PixelSelector;
import cop5556fa17.AST.Expression_PredefinedName;
import cop5556fa17.AST.Expression_Unary;
import cop5556fa17.AST.Index;
import cop5556fa17.AST.LHS;
import cop5556fa17.AST.Program;
import cop5556fa17.AST.Sink_Ident;
import cop5556fa17.AST.Sink_SCREEN;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_Assign;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;

import java.util.HashMap;

public class TypeCheckVisitor implements ASTVisitor {
	

		@SuppressWarnings("serial")
		public static class SemanticException extends Exception {
			Token t;

			public SemanticException(Token t, String message) {
				super("line " + t.line + " pos " + t.pos_in_line + ": "+  message);
				this.t = t;
			}

		}		
		
		HashMap<String, Declaration> symTable = new HashMap<>();
	
	/**
	 * The program name is only used for naming the class.  It does not rule out
	 * variables with the same name.  It is returned for convenience.
	 * 
	 * @throws Exception 
	 */
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		for (ASTNode node: program.decsAndStatements) {
			node.visit(this, arg);
		}
		return program.name;
	}

	@Override
	public Object visitDeclaration_Variable(
			Declaration_Variable declaration_Variable, Object arg)
			throws Exception {
		
		
		
		//REQUIRE if (Expression !=  ε) Declaration_Variable.Type == Expression.Type
		declaration_Variable.typeName = TypeUtils.getType(declaration_Variable.type);
		
		if(declaration_Variable.e != null) {
			Expression expr = (Expression) declaration_Variable.e.visit(this, null);
			if(declaration_Variable.typeName == expr.typeName) {
				//All good
			}else {
				throw new SemanticException(declaration_Variable.firstToken, "Type mismatch: Declaration_Variable.Type == Expression.Type");
			}
		}
		
		if(symTable.containsKey(declaration_Variable.name)) {
			throw new SemanticException(declaration_Variable.firstToken, "Duplicate variable declaration");
		}else {
			symTable.put(declaration_Variable.name, declaration_Variable);
		}
		
		
		
		return declaration_Variable;
	}

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary,
			Object arg) throws Exception {
		// Expression_Binary ::= Expression0 op Expression1
		Expression e0 = (Expression) expression_Binary.e0.visit(this, null);
		Expression e1 = (Expression) expression_Binary.e1.visit(this, null);
		
		if(e0.typeName != e1.typeName) {
			throw new SemanticException(expression_Binary.firstToken, "Type mismatch");
		}
		
//		if op ∈ {EQ, NEQ} then BOOLEAN
		
		
//        else if (op ∈ {GE, GT, LT, LE} && Expression0.Type == INTEGER) then BOOLEAN
		
		
//		else if (op ∈ {AND, OR}) && 
//		(Expression0.Type == INTEGER || Expression0.Type ==BOOLEAN) 
//		then Expression0.Type
		
		
//		else if op ∈ {DIV, MINUS, MOD, PLUS, POWER, TIMES} && Expression0.Type == INTEGER
//					then INTEGER
//				else Ʇ

		Kind op = expression_Binary.op;
		
		if (op == Kind.OP_EQ   || op == Kind.OP_NEQ ) {
			expression_Binary.typeName = Type.BOOLEAN;
		} else if ((op == Kind.OP_GE   || op == Kind.OP_GT || op == Kind.OP_LT   || op == Kind.OP_LE  ) && e0.typeName == Type.INTEGER) {
			expression_Binary.typeName = Type.BOOLEAN;
		} else if ((op == Kind.OP_AND   || op == Kind.OP_OR ) && (e0.typeName == Type.INTEGER || e0.typeName == Type.BOOLEAN) ) {
			expression_Binary.typeName = e0.typeName == Type.BOOLEAN ? Type.BOOLEAN : Type.INTEGER ;
		} else if ((op == Kind.OP_DIV   || op == Kind.OP_MINUS || op == Kind.OP_MOD   || op == Kind.OP_PLUS  || op == Kind.OP_POWER || op == Kind.OP_TIMES  ) && e0.typeName == Type.INTEGER) {
			expression_Binary.typeName = Type.INTEGER;
		}else {
			throw new SemanticException(expression_Binary.firstToken, "Type mismatch");
		}
		return expression_Binary;
	}

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary,
			Object arg) throws Exception {
		// Expression_Unary ::= op Expression
		
		Expression expr = (Expression) expression_Unary.e.visit(this, arg);
		
		Kind op = expression_Unary.op;
		
		if (op == Kind.OP_EXCL   && (expr.typeName == Type.BOOLEAN || expr.typeName == Type.INTEGER ) ) {
			expression_Unary.typeName = expr.typeName;
		} else if ((op == Kind.OP_PLUS  || op == Kind.OP_MINUS ) && expr.typeName == Type.INTEGER) {
			expression_Unary.typeName = Type.INTEGER;
		}else {
			throw new SemanticException(expression_Unary.firstToken, "Type mismatch");
		}
		
		return expression_Unary;
		
	}

	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		// Index ::= Expression0 Expression1
//		REQUIRE: Expression0.Type == INTEGER &&  Expression1.Type == INTEGER
//				Index.isCartesian <= !(Expression0 == KW_r && Expression1 == KW_a)
		Expression e0 = (Expression) index.e0.visit(this, arg);
		Expression e1 = (Expression) index.e1.visit(this, arg);
		if(e0.typeName == Type.INTEGER &&  e1.typeName== Type.INTEGER) {
			//all good;
		}else {
			throw new SemanticException(index.firstToken, "Type mismatch");
		}
		boolean isCartesian = !(e0.firstToken.isKind(Kind.KW_r) && e1.firstToken.isKind(Kind.KW_a));
		index.setCartesian(isCartesian);
		return index;
	}

	@Override
	public Object visitExpression_PixelSelector(
			Expression_PixelSelector expression_PixelSelector, Object arg)
			throws Exception {
		// Expression_PixelSelector ::=   name Index
//     	name.Type <= SymbolTable.lookupType(name)
//		Expression_PixelSelector.Type <=  if name.Type == IMAGE then INTEGER 
//		                                else if Index == null then name.Type
//		                                else  Ʇ
//		          REQUIRE:  Expression_PixelSelector.Type ≠ Ʇ
		
		if(symTable.containsKey(expression_PixelSelector.name)) {
			Declaration dec = symTable.get(expression_PixelSelector.name);
			if(dec.typeName == Type.IMAGE) {
				expression_PixelSelector.typeName = Type.INTEGER;
			}else if(expression_PixelSelector.index == null) {
				expression_PixelSelector.typeName = dec.typeName;
			}else
				throw new SemanticException(expression_PixelSelector.firstToken, "Type Mismatch.");
		}else {
			throw new SemanticException(expression_PixelSelector.firstToken, "Not declared before");
		}
		
		return expression_PixelSelector;

	}

	@Override
	public Object visitExpression_Conditional(
			Expression_Conditional expression_Conditional, Object arg)
			throws Exception {
		// Expression_Conditional ::=  Expressioncondition Expressiontrue Expressionfalse
//			REQUIRE:  Expressioncondition.Type == BOOLEAN &&  
//	                Expressiontrue.Type ==Expressionfalse.Type
//	                	Expression_Conditional.Type <= Expressiontrue.Type
		
		Expression eCon = (Expression) expression_Conditional.condition.visit(this, null);
		Expression eTrue = (Expression) expression_Conditional.trueExpression.visit(this, null);
		Expression eFalse = (Expression) expression_Conditional.falseExpression.visit(this, null);
		
		
		if(eCon.typeName == Type.BOOLEAN && (eTrue.typeName == eFalse.typeName)) {
			
		}else {
			throw new SemanticException(expression_Conditional.firstToken, "Type mismatch");
		}
		
		expression_Conditional.typeName = eTrue.typeName;
		
		return expression_Conditional;

	}

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image,
			Object arg) throws Exception {
		// Declaration_Image  ::= name (  xSize ySize | ε) Source
		
		
		
		declaration_Image.typeName = Type.IMAGE;       // TypeUtils.getType(declaration_Image.firstToken);
		
		// TO DO 
		
		// REQUIRE if xSize != ε then ySize != ε && xSize.Type == INTEGER && ySize.type == INTEGER
		
		
		if(declaration_Image.xSize != null) {
			
			//xsize init and check
			declaration_Image.xSize.visit(this, null);
			if(declaration_Image.xSize.typeName != Type.INTEGER) {
				throw new SemanticException(declaration_Image.firstToken, "Type mismatch: declaration_Image.Type == Expression.Type");
			}
			
			//ysize check
			if(declaration_Image.ySize != null) {
				declaration_Image.ySize.visit(this, null);
				if(declaration_Image.ySize.typeName != Type.INTEGER) {
					throw new SemanticException(declaration_Image.firstToken, "Type mismatch: declaration_Image.Type == Expression.Type");
				}
			}else {
				throw new SemanticException(declaration_Image.firstToken, "Type mismatch: declaration_Image.Type == Expression.Type");
			}
		}
		if(declaration_Image.source != null) {
			declaration_Image.source.visit(this, null);
		}
//		{
//			throw new SemanticException(declaration_Image.firstToken, "Source not provided");
//		}
		
		if(symTable.containsKey(declaration_Image.name)) {
			throw new SemanticException(declaration_Image.firstToken, "Duplicate variable declaration");
		}else {
			symTable.put(declaration_Image.name, declaration_Image);
		}

		return declaration_Image;
	}

	@Override
	public Object visitSource_StringLiteral(
			Source_StringLiteral source_StringLiteral, Object arg)
			throws Exception {
		// Source_StringLiteral ::=  fileOrURL
		// Source_StringLIteral.Type <= if isValidURL(fileOrURL) then URL else FILE
		
		if(isValidURL(source_StringLiteral.fileOrUrl)) {
			source_StringLiteral.typeName = Type.URL;
		}else {
			source_StringLiteral.typeName = Type.FILE;
		}
		
		return source_StringLiteral;
	}

	@Override
	public Object visitSource_CommandLineParam(
			Source_CommandLineParam source_CommandLineParam, Object arg)
			throws Exception {
		// Source_CommandLineParam  ::= ExpressionparamNum
//		Source_CommandLineParam .Type <= ExpressionparamNum.Type
//				REQUIRE:  Source_CommandLineParam .Type == INTEGER
		
		Expression expr = (Expression) source_CommandLineParam.paramNum.visit(this, arg);
		
		if(expr.typeName == Type.INTEGER) {
			source_CommandLineParam.typeName = expr.typeName;
		}else {
			throw new SemanticException(source_CommandLineParam.firstToken, "Type mismatch");
		}
		
		return source_CommandLineParam;

	}

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg)
			throws Exception {
		// Source_Ident.Type <= symbolTable.lookupType(name)
        // REQUIRE:  Source_Ident.Type == FILE || Source_Ident.Type == URL
		
		if(symTable.containsKey(source_Ident.name)) {
			source_Ident.typeName = symTable.get(source_Ident.name).typeName;
			if(source_Ident.typeName == Type.FILE || source_Ident.typeName == Type.URL) {
				//all good
			}else throw new SemanticException(source_Ident.firstToken, "Type Mismatch");
		}else {
			throw new SemanticException(source_Ident.firstToken, "Not declared before");
		}
		
		return source_Ident;
	}

	@Override
	public Object visitDeclaration_SourceSink(
			Declaration_SourceSink declaration_SourceSink, Object arg)
			throws Exception {
		
		//REQUIRE Source.Type == Declaration_SourceSink.Type
		declaration_SourceSink.typeName = TypeUtils.getType(declaration_SourceSink.firstToken);
		
		Source src = (Source) declaration_SourceSink.source.visit(this, null);
		if(declaration_SourceSink.typeName != src.typeName) {
			throw new SemanticException(declaration_SourceSink.firstToken, "Type mismatch: declaration_SourceSink.Type == src.Type");
		}
		
		if(symTable.containsKey(declaration_SourceSink.name)) {
			throw new SemanticException(declaration_SourceSink.firstToken, "Duplicate variable declaration");
		}else {
			symTable.put(declaration_SourceSink.name, declaration_SourceSink);
		}
		
		
		return declaration_SourceSink;
		
	}

	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit,
			Object arg) throws Exception {
		// Expression_IntLit ::=  value
		// Expression_IntLIt.Type <= INTEGER
		
		expression_IntLit.typeName = Type.INTEGER;
		
		return expression_IntLit;
	}

	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg,
			Object arg) throws Exception {
		// Expression_FunctionAppWithExprArg ::=  function Expression
//		REQUIRE:  Expression.Type == INTEGER
//	               Expression_FunctionAppWithExprArg.Type <= INTEGER
		
		Expression expr = (Expression) expression_FunctionAppWithExprArg.arg.visit(this, null);
		if(expr.typeName != Type.INTEGER) {
			throw new SemanticException(expression_FunctionAppWithExprArg.firstToken, "Type mismatch");
		}
		
		expression_FunctionAppWithExprArg.typeName = Type.INTEGER;

		return expression_FunctionAppWithExprArg;
	}

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg,
			Object arg) throws Exception {
		// Expression_FunctionAppWithIndexArg ::=   function Index
        	// Expression_FunctionAppWithIndexArg.Type <= INTEGER

		expression_FunctionAppWithIndexArg.typeName = Type.INTEGER;

		return expression_FunctionAppWithIndexArg;
	}

	@Override
	public Object visitExpression_PredefinedName(
			Expression_PredefinedName expression_PredefinedName, Object arg)
			throws Exception {
		// Expression_PredefinedName ::=  predefNameKind
		// Expression_PredefinedName.TYPE <= INTEGER
		
		expression_PredefinedName.typeName = Type.INTEGER;
		
		return expression_PredefinedName;
	}

	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg)
			throws Exception {
//		Statement_Out.Declaration <= name.Declaration
//	               REQUIRE:  (name.Declaration != null)
		
//	              REQUIRE:   ((name.Type == INTEGER || name.Type == BOOLEAN) && Sink.Type == SCREEN)
//		                  ||  (name.Type == IMAGE && (Sink.Type ==FILE || Sink.Type == SCREEN))
		
		if(symTable.get(statement_Out.name) != null){
			Declaration dec = symTable.get(statement_Out.name);
			statement_Out.setDec(dec);
			
			statement_Out.sink.visit(this, null);
			
			if(  ((dec.typeName == Type.INTEGER || dec.typeName == Type.BOOLEAN) && statement_Out.sink.typeName == Type.SCREEN)
	                  ||  (dec.typeName == Type.IMAGE && (statement_Out.sink.typeName == Type.FILE || statement_Out.sink.typeName == Type.SCREEN))  ) {
				//All good
			}else {
				throw new SemanticException(statement_Out.firstToken, "Type mismatch");
			}
			
			
			
		}else {
			throw new SemanticException(statement_Out.firstToken, "Not declared before.");
		}
		
		
		return statement_Out;

	}

	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg)
			throws Exception {
		// Statement_In.Declaration <= name.Declaration
        // REQUIRE:  (name.Declaration != null) & (name.type == Source.type)
		
		if(symTable.get(statement_In.name) != null){
			Declaration dec = symTable.get(statement_In.name);
			statement_In.setDec(dec);
			
			statement_In.source.visit(this, arg);
//			if(dec.typeName != statement_In.source.typeName) {
//				throw new SemanticException(statement_In.firstToken, "Type mismatch");
//			}
			
		}
//			else {
//			throw new SemanticException(statement_In.firstToken, "Not declared before.");
//		}
		
		return statement_In;
	}

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign,
			Object arg) throws Exception {
		// REQUIRE:  LHS.Type == Expression.Type
		
		LHS l = (LHS) statement_Assign.lhs.visit(this, null);
		Expression expr = (Expression) statement_Assign.e.visit(this, null);
		
		if(l.typeName != expr.typeName) {
			throw new SemanticException(statement_Assign.firstToken, "Type mismatch: statement_Assign.lhs.typeName != expr.typeName ");
		}
		
		// StatementAssign.isCartesian <= LHS.isCartesian
		statement_Assign.setCartesian(statement_Assign.lhs.isCartesian());
		

		return statement_Assign;
	}

	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		// LHS ::= name Index
		// LHS.Declaration <= symbolTable.lookupDec(name)
        // LHS.Type <= LHS.Declaration.Type
        // LHS.isCarteisan <= Index.isCartesian
		
		if(symTable.containsKey(lhs.name)) {
			lhs.dec = symTable.get(lhs.name);
			lhs.typeName = lhs.dec.typeName;
			if (lhs.index != null) {
				Index ind = (Index) lhs.index.visit(this, arg);
				lhs.setCartesian(ind.isCartesian());
			}else {
				lhs.setCartesian(false);
			}
			
		}else {
			throw new SemanticException(lhs.firstToken, "Not declared before");
		}
		
		
		return lhs;
	}

	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg)
			throws Exception {
		// Sink_SCREEN ::= SCREEN
		// Sink_SCREEN.Type <= SCREEN
		
		sink_SCREEN.typeName = Type.SCREEN;
		
		return sink_SCREEN;

	}

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg)
			throws Exception {
		// Sink_Ident ::= name
//		Sink_Ident.Type <= symbolTable.lookupType(name) 
//	        REQUIRE:  Sink_Ident.Type  == FILE
		
		if(symTable.containsKey(sink_Ident.name)) {
			
			sink_Ident.typeName = symTable.get(sink_Ident.name).typeName;
			if(sink_Ident.typeName != Type.FILE) {
				throw new SemanticException(sink_Ident.firstToken, "Type mismatch.");
			}
		}else {
			throw new SemanticException(sink_Ident.firstToken, "Not declared before");
		}
		
		
		return sink_Ident;

	}

	@Override
	public Object visitExpression_BooleanLit(
			Expression_BooleanLit expression_BooleanLit, Object arg)
			throws Exception {
		// Expression_BooleanLit.Type <= BOOLEAN
		
		expression_BooleanLit.typeName = Type.BOOLEAN;

		return expression_BooleanLit;
	}

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident,
			Object arg) throws Exception {
		// Expression_Ident  ::=   name
		// Expression_Ident.Type <= symbolTable.lookupType(name)
		
		if(symTable.containsKey(expression_Ident.name)) {
			expression_Ident.typeName = symTable.get(expression_Ident.name).typeName;
		}else {
			throw new SemanticException(expression_Ident.firstToken, "Not declared before");
		}
		
		
		return expression_Ident;
	}
	
	public static boolean isValidURL(String url)
	  {
	    if (url == null) {
	      return false;
	    }
	    // Assigning the url format regular expression
	    String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
	    return url.matches(urlPattern);
	  }

}
