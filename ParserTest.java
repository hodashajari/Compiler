package cop5556fa17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.AST.*;

import cop5556fa17.Parser.SyntaxException;

import static cop5556fa17.Scanner.Kind.*;

public class ParserTest {

	// set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}

	/**
	 * Simple test case with an empty program. This test expects an exception
	 * because all legal programs must have at least an identifier
	 * 
	 * @throws LexicalException
	 * @throws SyntaxException
	 */
	@Test
	public void testEmpty() throws LexicalException, SyntaxException {
		String input = ""; // The input is the empty string. Parsing should fail
		show(input); // Display the input
		Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
														// initialize it
		show(scanner); // Display the tokens
		Parser parser = new Parser(scanner); //Create a parser
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast = parser.parse();; //Parse the program, which should throw an exception
		} catch (SyntaxException e) {
			show(e);  //catch the exception and show it
			throw e;  //rethrow for Junit
		}
	}
	
	@Test
	public void testNameOnlyExp() throws LexicalException, SyntaxException {
		String input = "prog k [[x,y]]";  //Legal program with only a name
		show(input);            //display input
		Scanner scanner = new Scanner(input).scan();   //Create scanner and create token list
		show(scanner);    //display the tokens
		Parser parser = new Parser(scanner);   //create parser
		Program ast = parser.parse();          //parse program and get AST
		show(ast);                             //Display the AST
//		assertEquals(ast.name, "prog");        //Check the name field in the Program object
//		assertTrue(ast.decsAndStatements.isEmpty());   //Check the decsAndStatements list in the Program object.  It should be empty.
	}

	@Test
	public void testNameOnly() throws LexicalException, SyntaxException {
		String input = "prog";  //Legal program with only a name
		show(input);            //display input
		Scanner scanner = new Scanner(input).scan();   //Create scanner and create token list
		show(scanner);    //display the tokens
		Parser parser = new Parser(scanner);   //create parser
		Program ast = parser.parse();          //parse program and get AST
		show(ast);                             //Display the AST
		assertEquals(ast.name, "prog");        //Check the name field in the Program object
		assertTrue(ast.decsAndStatements.isEmpty());   //Check the decsAndStatements list in the Program object.  It should be empty.
	}

	@Test
	public void testDec1() throws LexicalException, SyntaxException {
		String input = "prog int k;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "prog"); 
		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
				.get(0);  
		assertEquals(KW_int, dec.type.kind);
		assertEquals("k", dec.name);
		assertNull(dec.e);
	}
	
	@Test
	public void testVarDec2() throws LexicalException, SyntaxException {
		String input = "prog int k = ( num1 > num2 ) ? num3 : num4;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "prog"); 
		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
				.get(0);  
		assertEquals(KW_int, dec.type.kind);
		assertEquals("k", dec.name);
		//assertNull(dec.e);
	}
	
	@Test
	public void testImageDec1() throws LexicalException, SyntaxException {
		String input = "prog image nikita;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testSourceSinkDec1() throws LexicalException, SyntaxException {
		String input = "prog url nikita = \"saxena\";";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}

	@Test
	public void testImageOutStmnt1() throws LexicalException, SyntaxException {
		String input = "prog nikita -> SCREEN;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testImageInStmnt1() throws LexicalException, SyntaxException {
		String input = "prog nikita <- @num1 + num2;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testAssgnStmnt1() throws LexicalException, SyntaxException {
		String input = "prog nikita[[x,y]] = num1 + num2;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	@Test
	public void testAssgnStmnt2() throws LexicalException, SyntaxException {
		String input = "prog nikita = num1 + num2;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
//		assertEquals(ast.name, "prog"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
//		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
//				.get(0);  
//		assertEquals(KW_int, dec.type.kind);
//		assertEquals("k", dec.name);
//		//assertNull(dec.e);
	}
	
	
	@Test
	public void testExp1() throws LexicalException, SyntaxException {
		String input = "a: b: c";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		ASTNode expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(), "Expression_PredefinedName [name=KW_a]"); 
	}
	
	@Test
	public void testExp2() throws LexicalException, SyntaxException {
		String input = "num1 + num2";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst.firstToken);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Ident [name=num1], op=OP_PLUS, e1=Expression_Ident [name=num2]]");
	}
	
	@Test
	public void testExp3() throws LexicalException, SyntaxException {
		String input = "a = (c > d) ? e : f";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(), "Expression_PredefinedName [name=KW_a]");
	}
	
	@Test
	public void testProRandom1() throws LexicalException, SyntaxException {
		String input = "lo int j = sin [x+y,a+b] ;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=lo, decsAndStatements=[Declaration_Variable [type=[KW_int,int,3,3,1,4], name=j, e=Expression_FunctionAppWithIndexArg [function=KW_sin, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]]]]");
	}
	
	@Test
	public void testExp4() throws LexicalException, SyntaxException {
		String input = "p-+q";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Ident [name=p], op=OP_MINUS, e1=Expression_Unary [op=OP_PLUS, e=Expression_Ident [name=q]]]");
	}
	
	@Test
	public void testProRandom2() throws LexicalException, SyntaxException {
		String input = "hello boolean i = cos [x+y,a+b] ; hal = 5+--++++9 ; aks <- \"aka\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=hello, decsAndStatements=[Declaration_Variable [type=[KW_boolean,boolean,6,7,1,7], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Statement_Assign [lhs=name [name=hal, index=null], e=Expression_Binary [e0=Expression_IntLit [value=5], op=OP_PLUS, e1=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_IntLit [value=9]]]]]]]]], Statement_In [name=aks, source=Source_StringLiteral [fileOrUrl=aka]]]]");
	}
	
	@Test
	public void testProRandom3() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN;_abc <- _abc;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_Ident [name=_abc]]]]");
		}
	
	@Test
	public void testProRandom4() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _abc <- \"_abcnik\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_StringLiteral [fileOrUrl=_abcnik]]]]");
	}
	
	@Test
	public void testProRandom5() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _abc <- @ true;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_CommandLineParam [paramNum=Expression_BooleanLit [value=true]]]]]");
	}
	
	@Test
	public void testProRandom6() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _abc <- @ false;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_In [name=_abc, source=Source_CommandLineParam [paramNum=Expression_BooleanLit [value=false]]]]]");
	}
	
	@Test
	public void testProRandom7() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _pqr [[x,y]] = a;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_Assign [lhs=name [name=_pqr, index=Index [e0=Expression_PredefinedName [name=KW_x], e1=Expression_PredefinedName [name=KW_y]]], e=Expression_PredefinedName [name=KW_a]]]]");
	}
	
	@Test
	public void testProRandom8() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN; _pqr [[x,y]] = a;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]], Statement_Assign [lhs=name [name=_pqr, index=Index [e0=Expression_PredefinedName [name=KW_x], e1=Expression_PredefinedName [name=KW_y]]], e=Expression_PredefinedName [name=KW_a]]]]");
	}
	
	@Test
	public void testProRandom9() throws LexicalException, SyntaxException {
		String input = "prog k <- \"Nikita Saxena\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=prog, decsAndStatements=[Statement_In [name=k, source=Source_StringLiteral [fileOrUrl=Nikita Saxena]]]]");
	}
	
	@Test
	public void testProRandom10() throws LexicalException, SyntaxException {
		String input = "nik image _abc;_abc -> SCREEN;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=nik, decsAndStatements=[Declaration_Image [xSize=null, ySize=null, name=_abc, source=null], Statement_Out [name=_abc, sink=Sink_SCREEN [kind=KW_SCREEN]]]]");
	}
	
	@Test
	public void testExp5() throws LexicalException, SyntaxException {
		String input = "a+b < d-c";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]], op=OP_LT, e1=Expression_Binary [e0=Expression_Ident [name=d], op=OP_MINUS, e1=Expression_Ident [name=c]]]");
	}
	
	@Test
	public void testExp6() throws LexicalException, SyntaxException {
		String input = "when?a?b?c:d:e:f";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Conditional [condition=Expression_Ident [name=when], trueExpression=Expression_Conditional [condition=Expression_PredefinedName [name=KW_a], trueExpression=Expression_Conditional [condition=Expression_Ident [name=b], trueExpression=Expression_Ident [name=c], falseExpression=Expression_Ident [name=d]], falseExpression=Expression_Ident [name=e]], falseExpression=Expression_Ident [name=f]]");
	}
	
	@Test
	public void testExp7() throws LexicalException, SyntaxException {
		String input = "cos(a+b)]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_FunctionAppWithExprArg [function=KW_cos, arg=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]");
	}
	
	@Test
	public void testExp8() throws LexicalException, SyntaxException {
		String input = "+-!DEF_X?+-!DEF_Y:nikita [(a*b),c*d]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Conditional [condition=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_EXCL, e=Expression_PredefinedName [name=KW_DEF_X]]]], trueExpression=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_EXCL, e=Expression_PredefinedName [name=KW_DEF_Y]]]], falseExpression=Expression_PixelSelector [name=nikita, index=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_TIMES, e1=Expression_Ident [name=b]], e1=Expression_Binary [e0=Expression_Ident [name=c], op=OP_TIMES, e1=Expression_Ident [name=d]]]]]");
	}
	
	@Test
	public void testExpRan() throws LexicalException, SyntaxException {
		String input = "+x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y & t+s | f+g ? +x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y & t+s | f+g:+x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y & t+s | f+g";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Conditional [condition=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Ident [name=t], op=OP_PLUS, e1=Expression_Ident [name=s]]], op=OP_OR, e1=Expression_Binary [e0=Expression_Ident [name=f], op=OP_PLUS, e1=Expression_Ident [name=g]]], trueExpression=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Ident [name=t], op=OP_PLUS, e1=Expression_Ident [name=s]]], op=OP_OR, e1=Expression_Binary [e0=Expression_Ident [name=f], op=OP_PLUS, e1=Expression_Ident [name=g]]], falseExpression=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Ident [name=t], op=OP_PLUS, e1=Expression_Ident [name=s]]], op=OP_OR, e1=Expression_Binary [e0=Expression_Ident [name=f], op=OP_PLUS, e1=Expression_Ident [name=g]]]]");
	}
	
	@Test
	public void testExp9() throws LexicalException, SyntaxException {
		String input = "++++x";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]]]]]");
	}
	
	@Test
	public void testExp10() throws LexicalException, SyntaxException {
		String input = "sin(a+b)";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_FunctionAppWithExprArg [function=KW_sin, arg=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]");
	}
	
	@Test
	public void testExp11() throws LexicalException, SyntaxException {
		String input = "yu[m,n]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_PixelSelector [name=yu, index=Index [e0=Expression_Ident [name=m], e1=Expression_Ident [name=n]]]");
	}
	
	@Test
	public void testProRandom11() throws LexicalException, SyntaxException {
		String input = "hello boolean i = cos [x+y,a+b] ; hal = 5+--++++9 ; niks <- \"nik\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=hello, decsAndStatements=[Declaration_Variable [type=[KW_boolean,boolean,6,7,1,7], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], " +
						"op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Statement_Assign [lhs=name [name=hal, index=null], e=Expression_Binary [e0=Expression_IntLit [value=5], op=OP_PLUS, e1=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_MINUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_Unary [op=OP_PLUS, e=Expression_IntLit [value=9]]]]]]]]], Statement_In [name=niks, source=Source_StringLiteral [fileOrUrl=nik]]]]");
	}

	@Test
	public void testExpRanAbc() throws LexicalException, SyntaxException {
		String input = "x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y & x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y & x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y & x / y - x * y >= x / y - x * y != x / y - x * y >= x / y - x * y";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]]], op=OP_AND, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]], op=OP_NEQ, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]], op=OP_GE, e1=Expression_Binary [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_DIV, e1=Expression_PredefinedName [name=KW_y]], op=OP_MINUS, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]]]]]]");
	}
	
	@Test
	public void testProRandom12() throws LexicalException, SyntaxException {
		String input = "ident1 url ident2 = \"me1333\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=ident1, decsAndStatements=[Declaration_SourceSink [type=KW_url, name=ident2, source=Source_StringLiteral [fileOrUrl=me1333]]]]");
	}
	
	@Test
	public void testProRandom13() throws LexicalException, SyntaxException {
		String input = "idents url abcd = \"this is SSD string literal\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=idents, decsAndStatements=[Declaration_SourceSink [type=KW_url, name=abcd, source=Source_StringLiteral [fileOrUrl=this is SSD string literal]]]]");
	}
	
	@Test
	public void testProRandom14() throws LexicalException, SyntaxException {
		String input = "hello boolean i = cos [x+y,a+b] ; boolean i = cos [x+y,a+b] ; niks <- \"nik\";";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=hello, decsAndStatements=[Declaration_Variable [type=[KW_boolean,boolean,6,7,1,7], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Declaration_Variable [type=[KW_boolean,boolean,34,7,1,35], name=i, e=Expression_FunctionAppWithIndexArg [function=KW_cos, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]], Statement_In [name=niks, source=Source_StringLiteral [fileOrUrl=nik]]]]");
	}
	
	@Test
	public void testProRandom15() throws LexicalException, SyntaxException {
		String input = "lo int j = sin [x+y,a+b] ;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=lo, decsAndStatements=[Declaration_Variable [type=[KW_int,int,3,3,1,4], name=j, e=Expression_FunctionAppWithIndexArg [function=KW_sin, arg=Index [e0=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]], e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_a], op=OP_PLUS, e1=Expression_Ident [name=b]]]]]]]");
	}
	
	@Test
	public void testExp12() throws LexicalException, SyntaxException {
		String input = "_habdhdb_$ int ident = 4*3;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Ident [name=_habdhdb_$]");
	}
	
	@Test
	public void testExp13() throws LexicalException, SyntaxException {
		String input = "p-+q";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Ident [name=p], op=OP_MINUS, e1=Expression_Unary [op=OP_PLUS, e=Expression_Ident [name=q]]]");
	}
	
	@Test
	public void testProRandom16() throws LexicalException, SyntaxException {
		String input = "abc int def;";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.toString(),
				"Program [name=abc, decsAndStatements=[Declaration_Variable [type=[KW_int,int,4,3,1,5], name=def, e=null]]]");
	}

	@Test
	public void testExp14() throws LexicalException, SyntaxException {
		String input = "+x*y/t%a+g-u>2<8>=9<=12==0!=9 & x+y";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]], op=OP_GT, e1=Expression_IntLit [value=2]], op=OP_LT, e1=Expression_IntLit [value=8]], op=OP_GE, e1=Expression_IntLit [value=9]], op=OP_LE, e1=Expression_IntLit [value=12]], op=OP_EQ, e1=Expression_IntLit [value=0]], op=OP_NEQ, e1=Expression_IntLit [value=9]], op=OP_AND, e1=Expression_Binary [e0=Expression_PredefinedName [name=KW_x], op=OP_PLUS, e1=Expression_PredefinedName [name=KW_y]]]");
	}
	
	@Test
	public void testExp15() throws LexicalException, SyntaxException {
		String input = "+x*y/t%a+g-u";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		Expression expAst = parser.expression();
		show(expAst);
		assertEquals(expAst.toString(),
				"Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Binary [e0=Expression_Unary [op=OP_PLUS, e=Expression_PredefinedName [name=KW_x]], op=OP_TIMES, e1=Expression_PredefinedName [name=KW_y]], op=OP_DIV, e1=Expression_Ident [name=t]], op=OP_MOD, e1=Expression_PredefinedName [name=KW_a]], op=OP_PLUS, e1=Expression_Ident [name=g]], op=OP_MINUS, e1=Expression_Ident [name=u]]");
	}
	
	@Test
	public void testEmpty1() throws LexicalException, SyntaxException {
		String input = ""; // The input is the empty string. Parsing should fail
		show(input); // Display the input
		Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
														// initialize it
		show(scanner); // Display the tokens
		Parser parser = new Parser(scanner); //Create a parser
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast = parser.parse();; //Parse the program, which should throw an exception
		} catch (SyntaxException e) {
			show(e);  //catch the exception and show it
			throw e;  //rethrow for Junit
		}
	}


	@Test
	public void testNameOnly1() throws LexicalException, SyntaxException {
		String input = "prog";  //Legal program with only a name
		show(input);            //display input
		Scanner scanner = new Scanner(input).scan();   //Create scanner and create token list
		show(scanner);    //display the tokens
		Parser parser = new Parser(scanner);   //create parser
		Program ast = parser.parse();          //parse program and get AST
		show(ast);                             //Display the AST
		assertEquals(ast.name, "prog");        //Check the name field in the Program object
		assertTrue(ast.decsAndStatements.isEmpty());   //Check the decsAndStatements list in the Program object.  It should be empty.
	}

	@Test
	public void testDec11() throws LexicalException, SyntaxException {
		String input = "prog int k;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "prog"); 
		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
				.get(0);  
		assertEquals(KW_int, dec.type.kind);
		assertEquals("k", dec.name);
		assertNull(dec.e);
	}
	
	@Test
	public void testDec3() throws LexicalException, SyntaxException {
		String input = "program int prad = true | false;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "program"); 
		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
				.get(0);  
		assertEquals(KW_int, dec.type.kind);
		assertEquals("prad", dec.name);
		Expression_Binary e = (Expression_Binary)dec.e;
		Expression_BooleanLit e0 = (Expression_BooleanLit)e.e0;
		assertEquals(true, e0.value);
		assertEquals(OP_OR, e.op);
		Expression_BooleanLit e1 = (Expression_BooleanLit)e.e1;
		assertEquals(false, e1.value);
	}

	
	@Test
	public void testDec4() throws LexicalException, SyntaxException {
		String input = "program image [ abc>=cde , jkll>aeiou] ace;"
						+"image [ abc>=cde , jkll>aeiou] ace <- \"abced\";"
				        +"url pradosa = \"source\";"
						+"file pradosa = @(atan(!p));"
				        +"boolean  _$prad = ggjhj&(!hghg);";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "program"); 
//		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Image dec = (Declaration_Image) ast.decsAndStatements
				.get(0);  
		Expression e0 = (Expression)dec.xSize;
		Expression_Binary eb= (Expression_Binary) e0;
		Expression_Ident ei0 = (Expression_Ident) eb.e0;
		Expression_Ident ei1 = (Expression_Ident) eb.e1;
		assertEquals("abc", ei0.name);
		assertEquals(OP_GE, eb.op);
		assertEquals("cde", ei1.name);
		Expression e1 = (Expression)dec.ySize;
		Expression_Binary eb0= (Expression_Binary) e1;
		Expression_Ident ei2 = (Expression_Ident) eb0.e0;
		Expression_Ident ei3 = (Expression_Ident) eb0.e1;
		assertEquals("jkll", ei2.name);
		assertEquals(OP_GT, eb0.op);
		assertEquals("aeiou", ei3.name);
		assertEquals("ace", dec.name);
		assertNull(dec.source);
		Declaration_Image dec1 = (Declaration_Image) ast.decsAndStatements
				.get(1);
		
		Expression e2 = (Expression)dec.xSize;
		Expression_Binary eb2= (Expression_Binary) e2;
		Expression_Ident ei4 = (Expression_Ident) eb2.e0;
		Expression_Ident ei5 = (Expression_Ident) eb2.e1;
		assertEquals("abc", ei4.name);
		assertEquals(OP_GE, eb.op);
		assertEquals("cde", ei5.name);
		Expression e3 = (Expression)dec.ySize;
		Expression_Binary eb3= (Expression_Binary) e3;
		Expression_Ident ei6 = (Expression_Ident) eb3.e0;
		Expression_Ident ei7 = (Expression_Ident) eb3.e1;
		assertEquals("jkll", ei6.name);
		assertEquals(OP_GT, eb3.op);
		assertEquals("aeiou", ei7.name);
		assertEquals("ace", dec1.name);
		Source_StringLiteral sl= (Source_StringLiteral) dec1.source;
		assertEquals("abced", sl.fileOrUrl);
		Declaration_SourceSink dec2 = (Declaration_SourceSink) ast.decsAndStatements
				.get(2);
		assertEquals("pradosa", dec2.name);
		assertEquals(KW_url, dec2.type);
		Source_StringLiteral s2= (Source_StringLiteral) dec2.source;
		assertEquals("source", s2.fileOrUrl);
		Declaration_SourceSink dec3 = (Declaration_SourceSink) ast.decsAndStatements
				.get(3);
		assertEquals("pradosa", dec3.name);
		assertEquals(KW_file, dec3.type);
		Source_CommandLineParam s3= (Source_CommandLineParam) dec3.source;
		Expression_FunctionAppWithExprArg ef= (Expression_FunctionAppWithExprArg) s3.paramNum;
		assertEquals(KW_atan, ef.function);
		Expression_Unary eu= (Expression_Unary) ef.arg;
		assertEquals(OP_EXCL, eu.op);
		Expression_Ident ei= (Expression_Ident) eu.e;
		assertEquals("p", ei.name);
		Declaration_Variable dec4 = (Declaration_Variable) ast.decsAndStatements
				.get(4);
		assertEquals("_$prad", dec4.name);
		Expression_Binary eb4 = (Expression_Binary)dec4.e;
		Expression_Ident eid0 = (Expression_Ident)eb4.e0;
		Expression_Unary eid1 = (Expression_Unary)eb4.e1;
		assertEquals("ggjhj",eid0.name);
		assertEquals(OP_AND, eb4.op);
		assertEquals(OP_EXCL, eid1.op);
		Expression_Ident  eid2= (Expression_Ident)eid1.e;
		assertEquals("hghg", eid2.name);
	}
	@Test
	public void exp1() throws SyntaxException, LexicalException {
	String input = "Z-old";
	Expression e = (new Parser(new Scanner(input).scan())).expression();
	show(e);
	assertEquals(Expression_Binary.class, e.getClass());
	Expression_Binary ebin = (Expression_Binary)e;
	assertEquals(Expression_PredefinedName.class, ebin.e0.getClass());
	assertEquals(KW_Z, ((Expression_PredefinedName)(ebin.e0)).kind);
	assertEquals(Expression_Ident.class, ebin.e1.getClass());
	assertEquals("old", ((Expression_Ident)(ebin.e1)).name);
	assertEquals(OP_MINUS, ebin.op);
	}
	
	
	@Test
	public void exp2() throws SyntaxException, LexicalException {
	String input = "2";
	Expression e = (new Parser(new Scanner(input).scan())).expression();
	show(e);
	assertEquals(Expression_IntLit.class, e.getClass());
	Expression_IntLit e0 = (Expression_IntLit)e;
	assertEquals(2, e0.value);
	}

	
	@Test
	public void exp3() throws SyntaxException, LexicalException {
	String input = "pradosa int b";
	Expression e = (new Parser(new Scanner(input).scan())).expression();
	show(e);
	assertEquals(Expression_Ident.class, e.getClass());
	Expression_Ident e0 = (Expression_Ident)e;
	assertEquals("pradosa", e0.name);
	}

	@Test
	public void exp4() throws SyntaxException, LexicalException {
	String input = "pradosa int b;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "pradosa"); 
//	//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
			.get(0);  
	assertEquals(KW_int, dec.type.kind);
	assertEquals("b", dec.name);
	assertNull(dec.e);
	
	}

	
	@Test
	public void exp5() throws SyntaxException, LexicalException {
	String input = "shobhit image pratham;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "shobhit"); 
//	//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
	Declaration_Image dec = (Declaration_Image) ast.decsAndStatements
			.get(0);  
	assertNull(dec.xSize);
	assertNull(dec.ySize);
	assertEquals("pratham", dec.name);
	assertNull(dec.source);
//	
	}

	
	@Test
	public void exp6() throws SyntaxException, LexicalException {
	String input = "prog image [m+p,b+y] jh;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
//	//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
	Declaration_Image dec = (Declaration_Image) ast.decsAndStatements.get(0);  
	Expression_Binary ex = (Expression_Binary) dec.xSize;
	Expression_Ident eix0 = (Expression_Ident) ex.e0;
	assertEquals("m", eix0.name);
	assertEquals(OP_PLUS, ex.op);
	Expression_Ident eix1 = (Expression_Ident) ex.e1;
	assertEquals("p", eix1.name);
	Expression_Binary ey = (Expression_Binary) dec.ySize;
	Expression_Ident eiy0 = (Expression_Ident) ey.e0;
	assertEquals("b", eiy0.name);
	assertEquals(OP_PLUS, ey.op);
	Expression_PredefinedName eiy1 = (Expression_PredefinedName) ey.e1;
	assertEquals(KW_y, eiy1.kind);
	assertEquals("jh", dec.name);
	assertNull(dec.source);
//	
	}
	
	
	
	@Test
	public void testcase1() throws SyntaxException, LexicalException {
		String input = "2";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	@Test
	public void testcase2() throws SyntaxException, LexicalException {
		String input = "a bcd";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase3() throws SyntaxException, LexicalException {
		String input = "cart_x cart_y";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase4() throws SyntaxException, LexicalException {
		String input = "prog int 2";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase5() throws SyntaxException, LexicalException {
		String input = "prog image[filepng,png] imageName <- imagepng"; //Error as there is not semicolon for ending the statement
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		//Parser.program();
		thrown.expect(SyntaxException.class);
		try {
			parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase6() throws SyntaxException, LexicalException {
		String input = "imageDeclaration image[\"abcd\"] "; //Should fail for image[""]
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase7() throws SyntaxException, LexicalException {
		String input = "prog image[filepng,png] imageName <- imagepng; \n boolean ab=true;"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();
		show(ast);
		assertEquals("prog",ast.name);
		// First Declaration statement
		Declaration_Image dec = (Declaration_Image) ast.decsAndStatements.get(0);  
		assertEquals(KW_image, dec.firstToken.kind);
		assertEquals("imageName", dec.name);
		Expression_Ident ei=(Expression_Ident)dec.xSize;
		assertEquals("filepng",ei.name);
		ei=(Expression_Ident)dec.ySize;
		assertEquals("png",ei.name);
		Source_Ident s=(Source_Ident) dec.source;
	    assertEquals("imagepng",s.name);
		// Second Declaration statement
	    Declaration_Variable dec2 = (Declaration_Variable) ast.decsAndStatements.get(1);  
		assertEquals("ab", dec2.name);
		assertEquals(KW_boolean, dec2.firstToken.kind);
		Expression_BooleanLit ebi=(Expression_BooleanLit)dec2.e;
		assertEquals(true,ebi.value);		
	}
	
	@Test
	public void testcase8() throws SyntaxException, LexicalException {
		String input = "prog image[filepng,jpg] imageName;"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();
		show(ast);
		assertEquals("prog",ast.name);
		Declaration_Image dec1 = (Declaration_Image) ast.decsAndStatements.get(0); 
		assertEquals(dec1.name,"imageName");
		Expression_Ident exi=(Expression_Ident)dec1.xSize;
		Expression_Ident eyi=(Expression_Ident)dec1.ySize;
		assertEquals(exi.name,"filepng");
		assertEquals(eyi.name,"jpg");
		assertNull(dec1.source);
	}
	
	@Test
	public void testcase10() throws SyntaxException, LexicalException {
		String input = "prog @expr k=12;"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();  //Parse the program
		show(ast);
		assertEquals(ast.name,"prog");
		assertEquals(ast.decsAndStatements.size(),0);
	}
	
	@Test
	public void testcase10parse() throws SyntaxException, LexicalException {
		String input = "prog @expr k=12;"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast=parser.parse();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase11() throws SyntaxException, LexicalException {
		String input = "prog \"abcded\" boolean a=true;"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();  //Parse the program
		show(ast);
		assertEquals(ast.name,"prog");
		assertEquals(ast.decsAndStatements.size(),0);
	}
	
	@Test
	public void testcase11_parse() throws SyntaxException, LexicalException {
		String input = "prog \"abcded\" boolean a=true;"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast=parser.parse();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	

	@Test
	public void testcase12() throws SyntaxException, LexicalException {
		String input = "isBoolean boolean ab=true; boolean cd==true; abcd=true ? return true: return false;"; //Should fail for ==
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			Program ast=parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase13() throws SyntaxException, LexicalException {
		String input = "isBoolean boolean ab=true; boolean cd==true; abcd=true ? return true: return false;"; //Should fail for =
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			Program ast=parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		} 
	}
	
	@Test
	public void testcase14() throws SyntaxException, LexicalException {
		String input = "isUrl url filepng=\"abcd\"; \n @expr=12; url awesome=@expr; \n url filepng=abcdefg"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();  //Parse the program
		show(ast);
		assertEquals(ast.name,"isUrl");
		assertEquals(ast.decsAndStatements.size(),1);
		Declaration_SourceSink dss=(Declaration_SourceSink)ast.decsAndStatements.get(0);
		assertEquals(dss.name,"filepng");
		assertEquals(dss.type,KW_url);
		Source_StringLiteral s=(Source_StringLiteral)dss.source;
		assertEquals(s.fileOrUrl,"abcd");
	}
	
	@Test
	public void testcase14_parse() throws SyntaxException, LexicalException {
		String input = "isUrl url filepng=\"abcd\"; \n @expr=12; url awesome=@expr; \n url filepng=abcdefg"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast=parser.parse();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}  
	}
	
	@Test
	public void testcase15() throws SyntaxException, LexicalException {
		String input = "isUrl url filepng=\"abcd\" \n @expr=12; url awesome=@expr; \n url filepng=abcdefg"; //Should fail for ; in line one
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			Program ast=parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}  
	}
	
	@Test
	public void testcase16() throws SyntaxException, LexicalException {
		String input = "isFile file filepng=\"abcd\"; \n @expr=12; url filepng=@expr; \n url filepng=abcdefg"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();  //Parse the program
		show(ast);
		assertEquals(ast.name,"isFile");
		assertEquals(ast.decsAndStatements.size(),1);
		assertEquals(ast.firstToken.kind,IDENTIFIER);
		
		// Declaration Statements
		Declaration_SourceSink ds=(Declaration_SourceSink)ast.decsAndStatements.get(0);
		assertEquals(ds.type,KW_file);
		assertEquals(ds.name,"filepng");
		Source_StringLiteral s=(Source_StringLiteral)ds.source;
		assertEquals(s.fileOrUrl,"abcd");
		//assertEquals(ast.)
	}
	
	@Test
	public void testcase16_parse() throws SyntaxException, LexicalException {
		String input = "isFile file filepng=\"abcd\"; \n @expr=12; url filepng=@expr; \n url filepng=abcdefg"; 
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast=parser.parse();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}  
	}
	
	@Test
	public void testcase17() throws SyntaxException, LexicalException {
		String input =  "isFile file filepng=\"abcd\" \n @expr=12; url filepng=@expr; \n url filepng=abcdefg";  //Should fail for ; in line one
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			Program ast=parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}  
	}
	
	@Test
	public void testcase18() throws SyntaxException, LexicalException {
		String input =  "isurl url urlname;";  //Should fail for url as url can only be initalised
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			Program ast=parser.program();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}  
	}
	
	@Test
	public void testcase19() throws SyntaxException, LexicalException {
		String input =  "declaration int xyz;\n boolean zya;\n image imagename;";  
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();  //Parse the program
		show(ast);
		assertEquals(ast.name,"declaration");
		assertEquals(ast.firstToken.kind,IDENTIFIER);
		
		//Declaration statements start
		Declaration_Variable dv1=(Declaration_Variable)ast.decsAndStatements.get(0);
		assertEquals(dv1.name,"xyz");
		assertEquals(dv1.type.kind,KW_int);
		assertNull(dv1.e);
		
		Declaration_Variable dv2=(Declaration_Variable)ast.decsAndStatements.get(1);
		assertEquals(dv2.name,"zya");
		assertEquals(dv2.type.kind,KW_boolean);
		assertNull(dv2.e);
		
		Declaration_Image dv3=(Declaration_Image)ast.decsAndStatements.get(2);	
		assertEquals(dv3.name,"imagename");
		assertNull(dv3.source);
		assertNull(dv3.xSize);
		assertNull(dv3.ySize);
		
		//Declaration statement end
	}
	
	@Test
	public void testcase20() throws SyntaxException, LexicalException {
		String input =  "imageProgram image imageName;"
				+ "\n imageName->abcdpng; "
				+ "\n imageName -> SCREEN; "
				+ "\n imageName <- \"awesome\";"
				+ "\n imageName <- @express; \n"
				+ "\n imageName <- abcdpng;";  // Image related Test cases
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast=parser.program();  //Parse the program
		show(ast);
		assertEquals(ast.name,"imageProgram");
		
		//Declaration statement start
		Declaration_Image dv1=(Declaration_Image)ast.decsAndStatements.get(0);
		assertEquals(dv1.name,"imageName");
		assertNull(dv1.xSize);
		assertNull(dv1.ySize);
		assertNull(dv1.source);
		
		Statement_Out dv2=(Statement_Out)ast.decsAndStatements.get(1);
		assertEquals(dv2.name,"imageName");
		Sink_Ident si2=(Sink_Ident)dv2.sink;
		assertEquals(si2.name,"abcdpng");
	}

	


}
