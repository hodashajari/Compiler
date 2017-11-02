
package cop5556fa17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.Scanner.Token;
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
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
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
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);  
	assertEquals(KW_int, dec.type.kind);
	assertEquals("k", dec.name);
	assertNull(dec.e);
	}
	
	@Test 
	public void program_parser_noDecOrStatement_invalid() throws SyntaxException, LexicalException {
	String input = "ident1 ;";
	//String input = "ident1 ident2 [[x,y]] = !123 ;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_assignStatement_invalid() throws SyntaxException, LexicalException {
	String input = "ident1 ident2 [x,y] = !123 ;";
	//String input = "ident1 ident2 [[x,y]] = !123 ;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_assignStatement_noAssign_invalid() throws SyntaxException, LexicalException {
	//String input = "ident1 ident2  true;";
	String input = "ident1 ident2 [[x,y]]  !123 ;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_assignStatement_noExpression_invalid() throws SyntaxException, LexicalException {
	//String input = "ident1 ident2 =;";
	String input = "ident1 ident2 [[x,y]] =;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_inStatement_noRarrow_invalid() throws SyntaxException, LexicalException {
	String input = "prog k @123;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_inStatement_noSink_invalid() throws SyntaxException, LexicalException {
	String input = "prog k <-;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_outStatement_noLarrow_invalid() throws SyntaxException, LexicalException {
	//String input = "ident1 ident2  identifier;";
	String input = "ident1 ident2  SCREEN;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_outStatement_noSource_invalid() throws SyntaxException, LexicalException {
	//String input = "ident1 ident2 -> ;";
	String input = "ident1 ident2 -> ;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_sourceDeclaration_noIdentifier_invalid() throws SyntaxException, LexicalException {
	//String input = "ident1 url = \"me1333\";";
	String input = "ident1 file = @ sin[(123), true];";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_sourceDeclaration_noAssign_invalid() throws SyntaxException, LexicalException {
	//String input = "ident1 url ident2  \"me1333\";";
	String input = "ident1 file ident2  @ sin[(123), true];";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_sourceDeclaration_noSource_invalid() throws SyntaxException, LexicalException {
	//String input = "ident1 url ident2 = ;";
	String input = "ident1 file ident2 = ;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_imageDeclaration_noIdentifier_invalid() throws SyntaxException, LexicalException {
	String input = "ident image [x, y]  <- \"jugraj\";";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_imageDeclaration_noIdentifier1_invalid() throws SyntaxException, LexicalException {
	String input = "ident image <- \"jugraj\";";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_imageDeclaration_noIdentifier2_invalid() throws SyntaxException, LexicalException {
	String input = "ident image ;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_imageDeclaration__invalid() throws SyntaxException, LexicalException {
	//String input = "ident image [x y] ident2 <- \"jugraj\";";
	//String input = "ident image [x, y ident2 <- \"jugraj\";";
	//String input = "ident image [x, y ident2 \"jugraj\";";
	//String input = "ident image x, y] ident2 \"jugraj\";";
	String input = "ident image [x, y] ident2  \"jugraj\";";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_variableDeclaration_noExpression_invalid() throws SyntaxException, LexicalException {
	String input = "prog int = ;//comment\nint k = sin(c+b/2);";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_variableDeclaration_noIdentifier_invalid() throws SyntaxException, LexicalException {
	String input = "prog int = sin(c+b/2);//comment\nint k = sin(c+b/2);";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test
	public void unaryExpression_invalid3() throws SyntaxException, LexicalException {
	String input = "(!DEF_X!)";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	parser.expression();  //Parse the program
	}catch (SyntaxException e) {
	show(e);
	throw e;
	}
	}
	
	@Test
	public void expression1() throws SyntaxException, LexicalException {
	String input = "2";
	show(input);
	Scanner scanner = new Scanner(input).scan();  
	show(scanner);   
	Parser parser = new Parser(scanner);  
	Expression_IntLit ast = (Expression_IntLit) parser.expression();  //Call expression directly.  
	show(ast);
	assertEquals(2, ast.value);
	}
	
	@Test
	public void expression_valid() throws SyntaxException, LexicalException {
	String input = "+x?(true):sin[false,false]";
	show(input);
	Scanner scanner = new Scanner(input).scan();  
	show(scanner);   
	Parser parser = new Parser(scanner);  
	Expression_Conditional ast = (Expression_Conditional) parser.expression();  //Call expression directly.  
	show(ast);
	assertEquals(OP_PLUS, ast.firstToken.kind);
	Expression_Unary condition = (Expression_Unary) ast.condition;
	assertEquals(OP_PLUS, condition.op);
	Expression_PredefinedName x = (Expression_PredefinedName) condition.e;
	assertEquals("x", x.firstToken.getText());
	Expression_BooleanLit trueExp = (Expression_BooleanLit) ast.trueExpression;
	assertEquals(true, trueExp.value);
	Expression_FunctionAppWithIndexArg falseExp = (Expression_FunctionAppWithIndexArg) ast.falseExpression;
	assertEquals(KW_sin, falseExp.function);
	Index arg = falseExp.arg;
	Expression_BooleanLit e0 = (Expression_BooleanLit) arg.e0;
	assertEquals(false, e0.value);
	Expression_BooleanLit e1 = (Expression_BooleanLit) arg.e1;
	assertEquals(false, e1.value);
	}
	
	/*
	* To check simple unary expressions
	*/
	@Test
	public void unaryExpression_invalid() throws SyntaxException, LexicalException {
	String input = "+";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	parser.expression();  //Parse the program
	}catch (SyntaxException e) {
	show(e);
	throw e;
	}
	}
	
	@Test
	public void unaryExpression_invalid1() throws SyntaxException, LexicalException {
	String input = "-";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	parser.expression();  //Parse the program
	}catch (SyntaxException e) {
	show(e);
	throw e;
	}
	}
	
	@Test
	public void unaryExpression_invalid2() throws SyntaxException, LexicalException {
	String input = "!";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	parser.expression();  //Parse the program
	}catch (SyntaxException e) {
	show(e);
	throw e;
	}
	}
	
	@Test
	public void unaryExpression_valid() throws SyntaxException, LexicalException {
	String input = "+1";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression_Unary ast = (Expression_Unary) parser.expression();  //Call expression directly.  
	show(ast);
	assertEquals(OP_PLUS, ast.op);
	Expression_IntLit exp_int = (Expression_IntLit) ast.e;
	assertEquals(1, exp_int.value);
	}
	
	@Test
	public void expressionBooleanLit_valid1() throws SyntaxException, LexicalException {
	String input = "true";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression_BooleanLit ast = (Expression_BooleanLit) parser.expression();  //Call expression directly.  
	show(ast);
	assertEquals(true, ast.value);
	}
	
	@Test
	public void unaryExpression_valid2() throws SyntaxException, LexicalException {
	String input = "++x";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression_Unary ast = (Expression_Unary) parser.expression();  //Call expression directly.  
	show(ast);
	assertEquals(OP_PLUS, ast.op);
	Expression_Unary exp_un = (Expression_Unary) ast.e;
	assertEquals(OP_PLUS, exp_un.op);
	Expression_PredefinedName exp_pre = (Expression_PredefinedName) exp_un.e;
	assertEquals("x", exp_pre.firstToken.getText());
	}
	
	@Test
	public void unaryExpression_valid3() throws SyntaxException, LexicalException {
	String input = "+-+-!!true";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void unaryExpression_valid4() throws SyntaxException, LexicalException {
	String input = "!x!"; //Only !x is a valid expression and the ending ! is handled by parse()
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void unaryExpression_valid5() throws SyntaxException, LexicalException {
	String input = "identifier [exp, exp]";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void multiExpression_valid() throws SyntaxException, LexicalException {
	String input = "+1*+1/+1%+1";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void addExpression_valid() throws SyntaxException, LexicalException {
	String input = "+1*+1/+1%+1++1*+1/+1%+1-+1*+1/+1%+1";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void relExpression_valid() throws SyntaxException, LexicalException {
	String input = "+1*+1/+1%+1++1*+1/+1%+1-+1*+1/+1%+1<+1*+1/+1%+1++1*+1/+1%+1-+1*+1/+1%+1<=+1*+1/+1%+1++1*+1/+1%+1-+1*+1/+1%+1";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void eqExpression_valid() throws SyntaxException, LexicalException {
	String input = "true==false==true";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void andExpression_valid() throws SyntaxException, LexicalException {
	String input = "sin[+1,1230]&x&!!!!!false";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	
	@Test
	public void andExpression_invalid() throws SyntaxException, LexicalException {
	String input = "sin[+1,0123]";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	parser.expression();  //Parse the program
	}catch (SyntaxException e) {
	show(e);
	throw e;
	}
	}
	
	@Test
	public void orExpression_valid() throws SyntaxException, LexicalException {
	String input = "sin[+1,1230]&x&!!!!!false | 123*0/true%(!y)";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();  //Call expression directly.  
	show(ast);
	}
	

	@Test 
	public void program_variableDeclaration_valid() throws SyntaxException, LexicalException {
	String input = "prog int k = sin(c+b/2);//comment\nint k = sin(c+b/2);";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog");
	Declaration_Variable dec1 = (Declaration_Variable) ast.decsAndStatements.get(0);
	assertEquals(KW_int, dec1.firstToken.kind);
	assertEquals(KW_int, dec1.type.kind);
	assertEquals("k", dec1.name);
	Expression_FunctionAppWithExprArg exp = (Expression_FunctionAppWithExprArg) dec1.e;
	assertEquals(KW_sin, exp.firstToken.kind);
	assertEquals(KW_sin, exp.function);
	Expression_Binary exp_binary = (Expression_Binary) exp.arg;
	assertEquals("c", exp_binary.firstToken.getText());
	Expression_Ident exp_identc = (Expression_Ident) exp_binary.e0;
	assertEquals("c", exp_identc.firstToken.getText());
	assertEquals("c", exp_identc.name);
	Expression_Binary exp_binary1 = (Expression_Binary) exp_binary.e1;
	assertEquals("b", exp_binary1.firstToken.getText());
	Expression_Ident exp_identb = (Expression_Ident) exp_binary1.e0;
	assertEquals("b", exp_identb.firstToken.getText());
	assertEquals("b", exp_identb.name);
	Expression_IntLit exp_intlit = (Expression_IntLit) exp_binary1.e1;
	assertEquals(2, exp_intlit.firstToken.intVal());
	assertEquals(2, exp_intlit.value);
	
	Declaration_Variable dec2= (Declaration_Variable) ast.decsAndStatements.get(1);
	assertEquals(KW_int, dec2.firstToken.kind);
	assertEquals(KW_int, dec2.type.kind);
	assertEquals("k", dec2.name);
	Expression_FunctionAppWithExprArg expx = (Expression_FunctionAppWithExprArg) dec2.e;
	assertEquals(KW_sin, expx.firstToken.kind);
	assertEquals(KW_sin, expx.function);
	Expression_Binary exp_binaryx = (Expression_Binary) expx.arg;
	assertEquals("c", exp_binaryx.firstToken.getText());
	Expression_Ident exp_identcx = (Expression_Ident) exp_binaryx.e0;
	assertEquals("c", exp_identcx.firstToken.getText());
	assertEquals("c", exp_identcx.name);
	Expression_Binary exp_binary1x = (Expression_Binary) exp_binaryx.e1;
	assertEquals("b", exp_binary1x.firstToken.getText());
	Expression_Ident exp_identbx = (Expression_Ident) exp_binary1x.e0;
	assertEquals("b", exp_identbx.firstToken.getText());
	assertEquals("b", exp_identbx.name);
	Expression_IntLit exp_intlitx = (Expression_IntLit) exp_binary1x.e1;
	assertEquals(2, exp_intlitx.firstToken.intVal());
	assertEquals(2, exp_intlitx.value);	
	}
	
	@Test 
	public void program_variableDeclaration_invalid() throws SyntaxException, LexicalException {
	String input = "prog int k = sin(c+b/2)//comment\nint k = sin(c+b/2);";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse(); //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test 
	public void program_variableDeclaration_valid1() throws SyntaxException, LexicalException {
	String input = "identifier boolean bool;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("identifier", ast.name);
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);
	assertEquals("boolean", dec.firstToken.getText());
	assertEquals(KW_boolean, dec.type.kind);
	assertEquals("bool", dec.name);
	assertEquals(null, dec.e);
	}
	
	@Test 
	public void program_variableDeclaration_valid2() throws SyntaxException, LexicalException {
	String input = "identifier boolean bool = sin[+1,1230]&x&!!!!!false | 123*0/true%(!y);";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("identifier", ast.name);
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);
	assertEquals(KW_boolean, dec.type.kind);
	assertEquals("bool", dec.name);
	Expression_Binary exp_binary = (Expression_Binary) dec.e;
	assertEquals(OP_OR, exp_binary.op);
	assertEquals(KW_sin, exp_binary.firstToken.kind);
	Expression_Binary exp_binary1 = (Expression_Binary) exp_binary.e0;
	assertEquals(OP_AND, exp_binary1.op);
	assertEquals(KW_sin, exp_binary1.firstToken.kind);
	Expression_Binary exp_binary10 = (Expression_Binary) exp_binary1.e0;
	assertEquals(OP_AND, exp_binary10.op);
	Expression_FunctionAppWithIndexArg exp_func_index = (Expression_FunctionAppWithIndexArg) exp_binary10.e0;
	Expression_PredefinedName exp_pre_name = (Expression_PredefinedName) exp_binary10.e1;
	assertEquals(KW_x, exp_pre_name.firstToken.kind);
	Index arg = exp_func_index.arg;
	Expression_Unary index_exp_unary = (Expression_Unary) arg.e0;
	Expression_IntLit index_exp_intlit = (Expression_IntLit) arg.e1;
	assertEquals(1230, index_exp_intlit.value);
	assertEquals(OP_PLUS, index_exp_unary.op);
	Expression_IntLit index_exp_unary_exp_intlit = (Expression_IntLit) index_exp_unary.e;
	assertEquals(1, index_exp_unary_exp_intlit.value);
	Expression_Unary exp_unary = (Expression_Unary) exp_binary1.e1;
	Expression_Binary exp_binary2 = (Expression_Binary) exp_binary.e1;
	Expression_Unary x = (Expression_Unary) exp_binary2.e1;
	assertEquals(KW_y, x.e.firstToken.kind);	
	}
	
	@Test
	public void program_imageDeclaration_valid() throws SyntaxException, LexicalException {
	String input = "ident image [x, y] ident2 <- \"jugraj\";";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident", ast.name);
	Declaration_Image dec = (Declaration_Image) ast.decsAndStatements.get(0);
	assertEquals("ident2", dec.name);
	Expression_PredefinedName xsize = (Expression_PredefinedName) dec.xSize;
	assertEquals(KW_x, xsize.kind);
	Expression_PredefinedName ysize = (Expression_PredefinedName) dec.ySize;
	assertEquals(KW_y, ysize.kind);
	Source_StringLiteral source = (Source_StringLiteral) dec.source;
	assertEquals("jugraj", source.fileOrUrl);
	}
	
	@Test
	public void program_imageDeclaration_valid1() throws SyntaxException, LexicalException {
	String input = "ident1 image ident2 <- @ (123);";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident1", ast.name);
	Declaration_Image dec = (Declaration_Image) ast.decsAndStatements.get(0);
	assertEquals("image", dec.firstToken.getText());
	assertEquals("ident2", dec.name);
	Source_CommandLineParam source = (Source_CommandLineParam) dec.source;
	assertEquals(OP_AT, source.firstToken.kind);
	Expression_IntLit exp = (Expression_IntLit) source.paramNum;
	assertEquals(123, exp.value);
	}
	
	@Test
	public void program_imageDeclaration_valid2() throws SyntaxException, LexicalException {
	String input = "ident1 image ident2 <- source_ident;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident1", ast.name);
	Declaration_Image dec = (Declaration_Image) ast.decsAndStatements.get(0);
	assertEquals("ident2", dec.name);
	Source_Ident source = (Source_Ident) dec.source;
	assertEquals("source_ident", source.name);
	}
	
	@Test
	public void program_sourceSinkDeclaration_valid() throws SyntaxException, LexicalException {
	String input = "ident1 file ident2 = @ sin[(123), true];";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident1", ast.name);
	Declaration_SourceSink dec = (Declaration_SourceSink) ast.decsAndStatements.get(0);
	assertEquals("file", dec.firstToken.getText());
	assertEquals("ident2", dec.name);
	assertEquals(KW_file, dec.type);
	Source_CommandLineParam source = (Source_CommandLineParam) dec.source;
	assertEquals(OP_AT, source.firstToken.kind);
	Expression_FunctionAppWithIndexArg exp = (Expression_FunctionAppWithIndexArg) source.paramNum;
	assertEquals(KW_sin, exp.function);
	Index arg = exp.arg;
	Expression_IntLit exp1 = (Expression_IntLit) arg.e0;
	assertEquals(123, exp1.value);
	Expression_BooleanLit exp_bool = (Expression_BooleanLit) arg.e1;
	assertEquals(true, exp_bool.value);
	
	}
	
	@Test
	public void program_sourceSinkDeclaration_valid1() throws SyntaxException, LexicalException {
	String input = "ident1 url ident2 = \"me1333\";";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident1", ast.name);
	Declaration_SourceSink dec = (Declaration_SourceSink) ast.decsAndStatements.get(0);
	assertEquals(KW_url, dec.type);
	assertEquals("ident2", dec.name);
	Source_StringLiteral source = (Source_StringLiteral) dec.source;
	assertEquals("me1333", source.firstToken.getText());
	assertEquals("me1333", source.fileOrUrl);
	}
	
	
	
	@Test
	public void program_assignmentStatement_withLHSSelector_valid1() throws SyntaxException, LexicalException {
	String input = "ident1 ident2 [[x,y]] = !123 ;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident1", ast.name);
	Statement_Assign state = (Statement_Assign) ast.decsAndStatements.get(0);
	assertEquals("ident2", state.firstToken.getText());
	Expression_Unary exp = (Expression_Unary) state.e;
	assertEquals(OP_EXCL, exp.op);
	Expression_IntLit exp_int = (Expression_IntLit) exp.e;
	assertEquals(123, exp_int.value);
	
	LHS lhs = state.lhs;
	assertEquals("ident2", lhs.name);
	Index index = lhs.index;
	Expression_PredefinedName exp_pre1 = (Expression_PredefinedName) index.e0;
	assertEquals(KW_x, exp_pre1.kind);
	assertEquals(KW_x, exp_pre1.firstToken.kind);
	Expression_PredefinedName exp_pre2 = (Expression_PredefinedName) index.e1;
	assertEquals(KW_y, exp_pre2.kind);
	assertEquals(KW_y, exp_pre2.firstToken.kind);
	}
	
	@Test
	public void program_Statement_Out_valid() throws SyntaxException, LexicalException {
	String input = "ident1 ident2 -> SCREEN;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident1", ast.name);
	Statement_Out state = (Statement_Out) ast.decsAndStatements.get(0);
	assertEquals("ident2", state.name);
	Sink_SCREEN sink = (Sink_SCREEN) state.sink;
	assertEquals(KW_SCREEN, sink.kind);
	}
	
	@Test
	public void program_Statement_Out_valid1() throws SyntaxException, LexicalException {
	String input = "ident1 ident2 -> identifier;";
	show(input);
	Scanner scanner = new Scanner(input).scan();
	show(scanner);
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals("ident1", ast.name);
	Statement_Out state = (Statement_Out) ast.decsAndStatements.get(0);
	assertEquals("ident2", state.name);
	Sink_Ident sink = (Sink_Ident) state.sink;
	assertEquals("identifier", sink.name);
	}
	
	@Test
	public void program_Statement_In_valid() throws LexicalException, SyntaxException {
	String input = "prog k <- @123;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Statement_In state = (Statement_In) ast.decsAndStatements.get(0);
	assertEquals("k", state.firstToken.getText());
	assertEquals("k", state.name);
	Source_CommandLineParam source = (Source_CommandLineParam) state.source;
	assertEquals(OP_AT, source.firstToken.kind);
	Expression_IntLit exp = (Expression_IntLit) source.paramNum;
	assertEquals(123, exp.value);
	}
	
	@Test
	public void imageDeclaration_invalid() throws LexicalException, SyntaxException {
	String input = "name image [x y] identifier ;"; 
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
	// initialize it
	show(scanner); // Display the tokens
	Parser parser = new Parser(scanner); //Create a parser
	thrown.expect(SyntaxException.class);
	try {
	parser.parse();; //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	//UTSA
	@Test
	public void testprogram1() throws LexicalException, SyntaxException {
	String input = "prog int g=(a+b)/2;";
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
	assertEquals("g", dec.name);
	Expression_Binary e = (Expression_Binary) dec.e;
	Expression_Binary e0 = (Expression_Binary) e.e0;
	Expression_PredefinedName a = (Expression_PredefinedName) e0.e0;
	assertEquals(KW_a, a.kind);
	assertEquals(OP_PLUS, e0.op);
	Expression_Ident b = (Expression_Ident) e0.e1;
	assertEquals("b", b.name);
	assertEquals(OP_DIV, e.op);
	Expression_IntLit n = (Expression_IntLit) e.e1;
	assertEquals(2, n.value);
	}
	
	@Test
	public void testprogram2() throws LexicalException, SyntaxException {
	String input = "prog image [(a+b/2),(c*y+67)] k <- @(g+h/2-6);";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_Image dec = (Declaration_Image) ast.decsAndStatements.get(0);
	Expression_Binary e = (Expression_Binary) dec.xSize;
	Expression_PredefinedName x = (Expression_PredefinedName) e.e0;
	assertEquals(KW_a, x.kind);
	assertEquals(OP_PLUS, e.op);
	Expression_Binary e0 = (Expression_Binary) e.e1;
	Expression_Ident b = (Expression_Ident) e0.e0;
	assertEquals("b", b.name);
	assertEquals(OP_DIV, e0.op);
	Expression_IntLit n = (Expression_IntLit) e0.e1;
	assertEquals(2, n.value);
	Expression_Binary ey = (Expression_Binary) dec.ySize;
	Expression_Binary ey0 = (Expression_Binary) ey.e0;
	Expression_Ident c = (Expression_Ident) ey0.e0;
	assertEquals("c", c.name);
	assertEquals(OP_TIMES, ey0.op);
	Expression_PredefinedName y = (Expression_PredefinedName) ey0.e1;
	assertEquals(KW_y, y.kind);
	assertEquals(OP_PLUS, ey.op);
	Expression_IntLit n1 = (Expression_IntLit) ey.e1;
	assertEquals(67, n1.value);
	assertEquals("k", dec.name);
	Source_CommandLineParam s = (Source_CommandLineParam) dec.source;
	Expression_Binary p = (Expression_Binary) s.paramNum;
	Expression_Binary p0 = (Expression_Binary) p.e0;
	Expression_Ident g = (Expression_Ident) p0.e0;
	assertEquals("g", g.name);
	assertEquals(OP_PLUS, p0.op);
	Expression_Binary p01 = (Expression_Binary) p0.e1;
	Expression_Ident h = (Expression_Ident) p01.e0;
	assertEquals("h", h.name);
	assertEquals(OP_DIV, p01.op);
	Expression_IntLit n2 = (Expression_IntLit) p01.e1;
	assertEquals(2, n2.value);
	assertEquals(OP_MINUS, p.op);
	Expression_IntLit n3 = (Expression_IntLit) p.e1;
	assertEquals(6, n3.value);	
	}
	
	@Test
	public void testprogram3() throws LexicalException, SyntaxException {
	String input = "prog int k = polar_r(c+b/2);//comment starts here\r\n\rint k=polar_r(c+b/2);";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);  
	Declaration_Variable dec1 = (Declaration_Variable) ast.decsAndStatements.get(1);
	assertEquals(KW_int, dec.type.kind);
	assertEquals("k", dec.name);
	Expression_FunctionAppWithExprArg e = (Expression_FunctionAppWithExprArg) dec.e;
	assertEquals(KW_polar_r, e.function);
	Expression_Binary e0 = (Expression_Binary) e.arg;
	Expression_Ident c = (Expression_Ident) e0.e0;
	assertEquals("c", c.name);
	assertEquals(OP_PLUS, e0.op);
	Expression_Binary e01 = (Expression_Binary) e0.e1;
	Expression_Ident b = (Expression_Ident) e01.e0;
	assertEquals("b", b.name);
	Expression_IntLit n = (Expression_IntLit) e01.e1;
	assertEquals(2, n.value);
	assertEquals(KW_int, dec1.type.kind);
	assertEquals("k", dec1.name);
	Expression_FunctionAppWithExprArg f = (Expression_FunctionAppWithExprArg) dec1.e;
	assertEquals(KW_polar_r, f.function);
	Expression_Binary f0 = (Expression_Binary) f.arg;
	Expression_Ident c1 = (Expression_Ident) f0.e0;
	assertEquals("c", c1.name);
	assertEquals(OP_PLUS, f0.op);
	Expression_Binary f01 = (Expression_Binary) f0.e1;
	Expression_Ident b1 = (Expression_Ident) f01.e0;
	assertEquals("b", b1.name);
	Expression_IntLit n1 = (Expression_IntLit) f01.e1;
	assertEquals(2, n1.value);	
	}
	
	@Test
	public void testprogram4() throws LexicalException, SyntaxException {
	String input = "prog int k = ++x;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);  
	assertEquals(KW_int, dec.type.kind);
	assertEquals("k", dec.name);
	Expression_Unary u = (Expression_Unary) dec.e;
	assertEquals(OP_PLUS, u.op);
	Expression_Unary u1 = (Expression_Unary) u.e;
	assertEquals(OP_PLUS, u1.op);
	Expression_PredefinedName x = (Expression_PredefinedName) u1.e;
	assertEquals(KW_x, x.kind);	
	}
	
	@Test
	public void testprogram5() throws LexicalException, SyntaxException {
	String input = "prog int k \n;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);  
	assertEquals(KW_int, dec.type.kind);
	assertEquals("k", dec.name);
	assertNull(dec.e);
	}
	
	@Test
	public void testprogram6() throws LexicalException, SyntaxException {
	String input = "prog file k = @k[s+t,a+b];";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_SourceSink dec = (Declaration_SourceSink) ast.decsAndStatements.get(0);  
	assertEquals(KW_file, dec.type);
	assertEquals("k", dec.name);
	Source_CommandLineParam s = (Source_CommandLineParam) dec.source;
	Expression_PixelSelector e = (Expression_PixelSelector) s.paramNum;
	assertEquals("k", e.name);
	Index i = e.index;
	Expression_Binary e0 = (Expression_Binary) i.e0;
	Expression_Ident s1 = (Expression_Ident) e0.e0;
	assertEquals("s", s1.name);
	assertEquals(OP_PLUS, e0.op);
	Expression_Ident t = (Expression_Ident) e0.e1;
	assertEquals("t", t.name);
	Expression_Binary f = (Expression_Binary) i.e1;
	Expression_PredefinedName a = (Expression_PredefinedName) f.e0;
	assertEquals(KW_a, a.kind);
	assertEquals(OP_PLUS, f.op);
	Expression_Ident b = (Expression_Ident) f.e1;
	assertEquals("b", b.name);
	}
	
	@Test
	public void testprogram7() throws LexicalException, SyntaxException {
	String input = "prog k [[x,y]] = abs(!!sin[true,x?a:b]);";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Statement_Assign dec = (Statement_Assign) ast.decsAndStatements.get(0);
	LHS l = dec.lhs;
	assertEquals("k", l.name);
	Index i = l.index;
	Expression_PredefinedName x = (Expression_PredefinedName) i.e0;
	assertEquals(KW_x, x.kind);
	Expression_PredefinedName y = (Expression_PredefinedName) i.e1;
	assertEquals(KW_y, y.kind);
	Expression_FunctionAppWithExprArg e = (Expression_FunctionAppWithExprArg) dec.e;
	assertEquals(KW_abs, e.function);
	Expression_Unary f = (Expression_Unary) e.arg;
	assertEquals(OP_EXCL, f.op);
	Expression_Unary f1 = (Expression_Unary) f.e;
	assertEquals(OP_EXCL, f1.op);
	Expression_FunctionAppWithIndexArg f2 = (Expression_FunctionAppWithIndexArg) f1.e;
	assertEquals(KW_sin, f2.function);
	Index i2 = f2.arg;
	Expression_BooleanLit b0 = (Expression_BooleanLit) i2.e0;
	assertEquals(true, b0.value);
	Expression_Conditional b1 = (Expression_Conditional) i2.e1;
	Expression_PredefinedName x1 = (Expression_PredefinedName) b1.condition;
	assertEquals(KW_x, x1.kind);
	Expression_PredefinedName x2 = (Expression_PredefinedName) b1.trueExpression;
	assertEquals(KW_a, x2.kind);
	Expression_Ident b = (Expression_Ident) b1.falseExpression;
	assertEquals("b", b.name);
	}
	
	@Test
	public void testprogram8() throws LexicalException, SyntaxException {
	String input = "prog k -> SCREEN;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Statement_Out dec = (Statement_Out) ast.decsAndStatements.get(0);
	assertEquals("k", dec.name);
	Sink_SCREEN s = (Sink_SCREEN) dec.sink;
	assertEquals(KW_SCREEN, s.kind );
	}
	
	@Test
	public void testprogram9() throws LexicalException, SyntaxException {
	String input = "prog k <- SCREEN;"; 
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
	public void testprogram10() throws LexicalException, SyntaxException {
	String input = "prog k <- \"I am Utsa\""; 
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
	public void testprogram11() throws LexicalException, SyntaxException {
	String input = "prog k <- \"I am Utsa\";";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Statement_In dec = (Statement_In) ast.decsAndStatements.get(0);
	assertEquals("k", dec.name);
	Source_StringLiteral s = (Source_StringLiteral) dec.source;
	assertEquals("I am Utsa", s.fileOrUrl);
	}
	
	@Test
	public void testprogram12() throws LexicalException, SyntaxException {
	String input = "prog int k = ;"; 
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
	public void testprogram13() throws LexicalException, SyntaxException {
	String input = "prog k[] = a+b ;"; 
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
	public void testprogram14() throws LexicalException, SyntaxException {
	String input = "prog int k = !DEF_X;//comment\r\n\rk -> SCREEN;";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);
	assertEquals("k", dec.name);
	Expression_Unary e = (Expression_Unary) dec.e;
	assertEquals(OP_EXCL, e.op);
	Expression_PredefinedName x = (Expression_PredefinedName) e.e;
	assertEquals(KW_DEF_X, x.kind);
	Statement_Out dec1 = (Statement_Out) ast.decsAndStatements.get(1);
	assertEquals("k", dec1.name);
	Sink_SCREEN s = (Sink_SCREEN) dec1.sink;
	assertEquals(KW_SCREEN, s.kind );
	}
	
	@Test
	public void testprogram15() throws LexicalException, SyntaxException {
	String input = "prog int k = 1234|cos[(a<b),(g!=h)]&x%k==++DEF_X!=Z--!!k[a,b];";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);
	assertEquals(KW_int, dec.type.kind);
	assertEquals("k", dec.name);
	Expression_Binary e = (Expression_Binary) dec.e;
	Expression_IntLit e0 = (Expression_IntLit) e.e0;
	assertEquals(OP_OR, e.op);
	assertEquals(1234, e0.value);
	Expression_Binary e00 = (Expression_Binary) e.e1;
	Expression_FunctionAppWithIndexArg e1 = (Expression_FunctionAppWithIndexArg) e00.e0;
	assertEquals(KW_cos, e1.function);
	Index i = e1.arg;
	Expression_Binary f = (Expression_Binary) i.e0;
	Expression_PredefinedName a = (Expression_PredefinedName) f.e0;
	assertEquals(KW_a, a.kind);
	assertEquals(OP_LT, f.op);
	Expression_Ident b = (Expression_Ident) f.e1;
	assertEquals("b", b.name);
	Expression_Binary f0 = (Expression_Binary) i.e1;
	Expression_Ident g = (Expression_Ident) f0.e0;
	assertEquals("g", g.name);
	Expression_Ident h = (Expression_Ident) f0.e1;
	assertEquals("h", h.name);
	assertEquals(OP_AND, e00.op);
	Expression_Binary e000 = (Expression_Binary) e00.e1;
	Expression_Binary e0000 = (Expression_Binary) e000.e0;
	Expression_Binary e00000 = (Expression_Binary) e0000.e0;
	Expression_PredefinedName x = (Expression_PredefinedName) e00000.e0;
	assertEquals(KW_x, x.firstToken.kind);
	assertEquals(KW_x, x.kind);
	assertEquals(OP_MOD, e00000.op);
	Expression_Ident k0 = (Expression_Ident) e00000.e1;
	assertEquals("k", k0.name);
	assertEquals(OP_EQ, e0000.op);
	Expression_Unary w = (Expression_Unary) e0000.e1;
	assertEquals(OP_PLUS, w.firstToken.kind);
	assertEquals(OP_PLUS, w.op);
	Expression_Unary w0 = (Expression_Unary) w.e;
	assertEquals(OP_PLUS, w0.op);
	Expression_PredefinedName defx = (Expression_PredefinedName) w0.e;
	assertEquals(KW_DEF_X, defx.firstToken.kind);
	assertEquals(OP_NEQ, e000.op);
	Expression_Binary q = (Expression_Binary) e000.e1;
	Expression_PredefinedName Z = (Expression_PredefinedName) q.e0;
	assertEquals(KW_Z, Z.kind);
	assertEquals(OP_MINUS, q.op);
	Expression_Unary q0 = (Expression_Unary) q.e1;
	assertEquals(OP_MINUS, q0.op);
	Expression_Unary q00 = (Expression_Unary) q0.e;
	assertEquals(OP_EXCL, q00.op);
	Expression_Unary q000 = (Expression_Unary) q00.e;
	assertEquals(OP_EXCL, q000.op);
	Expression_PixelSelector v = (Expression_PixelSelector) q000.e;
	assertEquals(IDENTIFIER, v.firstToken.kind);
	assertEquals("k", v.name);
	Index i0 = v.index;
	Expression_PredefinedName a0 = (Expression_PredefinedName) i0.e0;
	assertEquals(KW_a, a0.kind);
	Expression_Ident b0 = (Expression_Ident) i0.e1;
	assertEquals("b", b.name);
	}
	
	@Test
	public void testprogram16() throws LexicalException, SyntaxException {
	String input = "prog int k = +x?(true):atan[(a),(y)];";
	show(input);
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner);
	Program ast = parser.parse();
	show(ast);
	assertEquals(ast.name, "prog"); 
	Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements.get(0);
	assertEquals(KW_int, dec.type.kind);
	assertEquals("k", dec.name);
	Expression_Conditional e = (Expression_Conditional) dec.e;
	Expression_Unary u = (Expression_Unary) e.condition;
	assertEquals(OP_PLUS, e.firstToken.kind);
	assertEquals(OP_PLUS, u.op);
	Expression_PredefinedName x = (Expression_PredefinedName) u.e;
	assertEquals(KW_x, x.kind);
	Expression_BooleanLit e0 = (Expression_BooleanLit) e.trueExpression;
	assertEquals(true, e0.value);
	Expression_FunctionAppWithIndexArg e00 = (Expression_FunctionAppWithIndexArg) e.falseExpression;
	assertEquals(KW_atan, e00.function);
	Index i = e00.arg;
	Expression_PredefinedName a = (Expression_PredefinedName) i.e0;
	assertEquals(KW_a, a.kind);
	Expression_PredefinedName b = (Expression_PredefinedName) i.e1;
	assertEquals(KW_y, b.kind);	
	}
	
	@Test
	public void testexp1() throws SyntaxException, LexicalException {
	String input = "+++c";
	show(input);
	Scanner scanner = new Scanner(input).scan();  
	show(scanner);   
	Parser parser = new Parser(scanner);  
	Expression ast = parser.expression();
	show(ast);
	Expression_Unary e = (Expression_Unary) ast;
	assertEquals(OP_PLUS, e.op );
	Expression_Unary e0 = (Expression_Unary) e.e;
	assertEquals(OP_PLUS, e0.op);
	Expression_Unary e00 = (Expression_Unary) e0.e;
	assertEquals(OP_PLUS, e00.op);
	Expression_Ident b = (Expression_Ident) e00.e;
	assertEquals("c", b.name);
	}
	
	@Test
	public void testexp2() throws LexicalException, SyntaxException {
	String input = "+x?(true):atan[false,\"false\"]"; 
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
	// initialize it
	show(scanner); // Display the tokens
	Parser parser = new Parser(scanner); //Create a parser
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.expression();; //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test
	public void testexp3() throws LexicalException, SyntaxException {
	String input = "+x?true"; 
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
	// initialize it
	show(scanner); // Display the tokens
	Parser parser = new Parser(scanner); //Create a parser
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.expression();; //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test
	public void testexp4() throws LexicalException, SyntaxException {
	String input = "(!DEF_X!)";
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
	// initialize it
	show(scanner); // Display the tokens
	Parser parser = new Parser(scanner); //Create a parser
	thrown.expect(SyntaxException.class);
	try {
	Expression ast = parser.expression();; //Parse the program, which should throw an exception
	} catch (SyntaxException e) {
	show(e);  //catch the exception and show it
	throw e;  //rethrow for Junit
	}
	}
	
	@Test
	public void testprogram17() throws LexicalException, SyntaxException {
	String input = "prog int k = 1234|cos[(a<b),(g!=h)&x%k==++DEF_X!=Z--!!k[a,b];"; 
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
	public void testprogram18() throws LexicalException, SyntaxException {
	String input = "prog int k = (!DEF_X;"; 
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
	public void testprogram19() throws LexicalException, SyntaxException {
	String input = "prog k [[x,y]]"; 
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
	public void testprogram20() throws LexicalException, SyntaxException {
	String input = "prog int k = !DEF_X;//comment\r\n\rk;"; 
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
	public void testprogram21() throws LexicalException, SyntaxException {
	//String input = "prog int k = ;";
	//String input = "prog int k;";
	String input = "prog int k = x?a:;";
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner); 
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse();
	} catch (SyntaxException e) {
	show(e); 
	throw e; 
	}
	}
	
	@Test
	public void testprogram22() throws LexicalException, SyntaxException {
	//String input = "prog file k = ;";
	//String input = "prog file  k k[s+t,a+b];";
	//String input = "prog file = k[s+t,a+b];";
	// String input = "prog file k;";
	String input = "prog file k = @k[s+,a+b];";
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner); 
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse();
	} catch (SyntaxException e) {
	show(e); 
	throw e; 
	}
	}
	
	@Test
	public void testprogram23() throws LexicalException, SyntaxException {
	//String input = "prog image [(a+b/2),(c*y+)]  <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2)] k <- @(g+h/2-6);";
	// String input = "prog image [(a+b/2),(c*y+) k <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2),(c*y+)] k <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2),(c*y+67) k <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2),(c*y+67)] k <- @(g+) ;";
	String input = "prog image [(a+b/2),(c*y+67)] k <- ;";
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner); 
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse();
	} catch (SyntaxException e) {
	show(e); 
	throw e; 
	}
	}
	
	@Test
	public void testprogram24() throws LexicalException, SyntaxException {
	String input = "prog image [(a+b/2),(c*y+)]  <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2)] k <- @(g+h/2-6);";
	// String input = "prog image [(a+b/2),(c*y+) k <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2),(c*y+)] k <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2),(c*y+67) k <- @(g+h/2-6);";
	//String input = "prog image [(a+b/2),(c*y+67)] k <- @(g+) ;";
	//String input = "prog image [(a+b/2),(c*y+67)] k <- ;";
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner); 
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse();
	} catch (SyntaxException e) {
	show(e); 
	throw e; 
	}
	}
	
	@Test
	public void testprogram25() throws LexicalException, SyntaxException {
	String input = "prog <- \"I am Utsa\";";
	//String input = "prog k \"I am Utsa\";";
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner); 
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse();
	} catch (SyntaxException e) {
	show(e); 
	throw e; 
	}
	}
	
	@Test
	public void testprogram26() throws LexicalException, SyntaxException {
	String input = "prog k -> x;";
	//String input = "prog k SCREEN;";
	show(input); // Display the input
	Scanner scanner = new Scanner(input).scan(); 
	show(scanner); 
	Parser parser = new Parser(scanner); 
	thrown.expect(SyntaxException.class);
	try {
	ASTNode ast = parser.parse();
	} catch (SyntaxException e) {
	show(e); 
	throw e; 
	}
	}
	
	//UTSA
	
	//BISWAJIT
	
	@Test
	public void testDecAndAssignStmt() throws LexicalException, SyntaxException {
	String input = "prog int k; test=x;";
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
	
	//Assignment Statement
	Statement_Assign stmt = (Statement_Assign) ast.decsAndStatements
	.get(1);  
	assertEquals(IDENTIFIER, stmt.firstToken.kind);
	assertEquals("test", stmt.firstToken.getText());
	assertEquals("test", stmt.lhs.firstToken.getText());
	assertEquals("x", stmt.e.firstToken.getText());
	assertNull(stmt.lhs.index);	
	}
	
	@Test
	public void testDecAndAssignStmt2() throws LexicalException, SyntaxException {
	String input = "prog int k; test[[x,y]]=x;";
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
	
	//Assignment Statement
	Statement_Assign stmt = (Statement_Assign) ast.decsAndStatements
	.get(1);  
	assertEquals("test", stmt.firstToken.getText());
	assertEquals("test", stmt.lhs.firstToken.getText());
	assertEquals("x", stmt.e.firstToken.getText());
	assertEquals("x", stmt.lhs.index.firstToken.getText());
	assertEquals("x", stmt.lhs.index.e0.firstToken.getText());
	assertEquals("y", stmt.lhs.index.e1.firstToken.getText());	
	}
	
	@Test
	public void testDecAndAssignStmt3() throws LexicalException, SyntaxException {
	String input = "prog int k; test[[r,A]]=x;";
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
	
	//Assignment Statement
	Statement_Assign stmt = (Statement_Assign) ast.decsAndStatements
	.get(1);  
	assertEquals("test", stmt.firstToken.getText());
	assertEquals("test", stmt.lhs.firstToken.getText());
	assertEquals("x", stmt.e.firstToken.getText());
	assertEquals("r", stmt.lhs.index.firstToken.getText());
	assertEquals("r", stmt.lhs.index.e0.firstToken.getText());
	assertEquals("A", stmt.lhs.index.e1.firstToken.getText());	
	}
	
	@Test
	public void testDecAndImageInStmt() throws LexicalException, SyntaxException {
	String input = "prog int k;testrand <- test;";
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
	
	//Assignment Statement
	Statement_In stmt = (Statement_In) ast.decsAndStatements
	.get(1);  
	assertEquals(IDENTIFIER, stmt.firstToken.kind);
	assertEquals("testrand", stmt.name);
	assertEquals("test", stmt.source.firstToken.getText());	
	}
	
	@Test
	public void testDecAndImageInStmt2() throws LexicalException, SyntaxException {
	String input = "prog int k;testrand <- \"test1\";";
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
	
	//Assignment Statement
	Statement_In stmt = (Statement_In) ast.decsAndStatements
	.get(1);  
	assertEquals(IDENTIFIER, stmt.firstToken.kind);
	assertEquals("testrand", stmt.name);
	assertEquals("test1", stmt.source.firstToken.getText());	
	}
	
	@Test
	public void testDecAndImageInStmt3() throws LexicalException, SyntaxException {
	String input = "prog int k;testrand <- @x;";
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
	
	//Assignment Statement
	Statement_In stmt = (Statement_In) ast.decsAndStatements
	.get(1);  
	assertEquals(IDENTIFIER, stmt.firstToken.kind);
	assertEquals("testrand", stmt.name);
	assertEquals("@", stmt.source.firstToken.getText());
	}
	
	@Test
	public void testDecAndImageOutStmt() throws LexicalException, SyntaxException {
	String input = "prog int k;testrand -> test;";
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
	
	//Assignment Statement
	Statement_Out stmt = (Statement_Out) ast.decsAndStatements
	.get(1);  
	assertEquals(IDENTIFIER, stmt.firstToken.kind);
	assertEquals("testrand", stmt.name);
	assertEquals("test", stmt.sink.firstToken.getText());	
	}
	
	@Test
	public void testDecAndImageOutStmt2() throws LexicalException, SyntaxException {
	String input = "prog int k;testrand -> SCREEN;";
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
	
	//Assignment Statement
	Statement_Out stmt = (Statement_Out) ast.decsAndStatements
	.get(1);  
	assertEquals(IDENTIFIER, stmt.firstToken.kind);
	assertEquals("testrand", stmt.name);
	assertEquals("SCREEN", stmt.sink.firstToken.getText());	
	assertEquals(KW_SCREEN, stmt.sink.firstToken.kind);
	}
	//BISWAJIT
}