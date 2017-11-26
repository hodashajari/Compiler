package cop5556fa17;

import java.util.ArrayList;

import cop5556fa17.Scanner.Kind;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cop5556fa17.TypeUtils.Type;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression;
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
import cop5556fa17.AST.Source;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;
import cop5556fa17.AST.Statement_Assign;
//import cop5556fa17.image.ImageFrame;
//import cop5556fa17.image.ImageSupport;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * All methods and variable static.
	 */
	FieldVisitor fv;
	static String fieldName;
	static String fieldType;
	static Object initValue;
	private int slot = 1;
	

	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;

	MethodVisitor mv; // visitor of method currently under construction

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;
	


	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.name;  
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
		cw.visitSource(sourceFileName, null);
		// create main method
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		// initialize
		mv.visitCode();		
		//add label before first instruction
		Label mainStart = new Label();
		mv.visitLabel(mainStart);		
		// if GRADE, generates code to add string to log
		CodeGenUtils.genLog(GRADE, mv, "entering main");

		// visit decs and statements to add field to class
		//  and instructions to main method, respectivley
		ArrayList<ASTNode> decsAndStatements = program.decsAndStatements;
		for (ASTNode node : decsAndStatements) {
			node.visit(this, arg);
		}

		//generates code to add string to log
		CodeGenUtils.genLog(GRADE, mv, "leaving main");
		
		//adds the required (by the JVM) return statement to main
		mv.visitInsn(RETURN);
		
		//adds label at end of code
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		
		//handles parameters and local variables of main. Right now, only args
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);

		//Sets max stack size and number of local vars.
		//Because we use ClassWriter.COMPUTE_FRAMES as a parameter in the constructor,
		//asm will calculate this itself and the parameters are ignored.
		//If you have trouble with failures in this routine, it may be useful
		//to temporarily set the parameter in the ClassWriter constructor to 0.
		//The generated classfile will not be correct, but you will at least be
		//able to see what is in it.
		mv.visitMaxs(0, 0);
		
		//terminate construction of main method
		mv.visitEnd();
		
		//terminate class construction
		cw.visitEnd();

		//generate classfile as byte array and return
		return cw.toByteArray();
	}

	@Override
	public Object visitDeclaration_Variable(Declaration_Variable declaration_Variable, Object arg) throws Exception {
	
		
		fieldName = declaration_Variable.name;
		Type fieldType = declaration_Variable.typeName;
//		initValue 
		
		declaration_Variable.setSlotNum(slot++);
		
		if (fieldType.equals(Type.INTEGER)) {
			FieldVisitor fv = cw.visitField(ACC_STATIC, fieldName, "I", null, null);
			fv.visitEnd();
		} else if (fieldType.equals(Type.BOOLEAN)) {
			FieldVisitor fv = cw.visitField(ACC_STATIC, fieldName, "Z", null, null);
			fv.visitEnd();
		}
		
		if (declaration_Variable.e != null) {
			declaration_Variable.e.visit(this, arg);
			if (declaration_Variable.typeName == Type.INTEGER) {
				mv.visitFieldInsn(PUTSTATIC, className,declaration_Variable.name, "I");
			} else if (declaration_Variable.typeName == Type.BOOLEAN) {
				mv.visitFieldInsn(PUTSTATIC, className, declaration_Variable.name, "Z");
			}
		}
		
	
		
		return declaration_Variable;
		
	}
	
	

	@Override
	public Object visitExpression_Binary(Expression_Binary expression_Binary, Object arg) throws Exception {
		// TODO change to switch
		// verify with test cas3s
		
		Label trueCase = new Label();
		Label falseCase = new Label();

		if (expression_Binary.e0 != null) {
			expression_Binary.e0.visit(this, arg);
		}
			
		
		if (expression_Binary.e1 != null) {
			expression_Binary.e1.visit(this, arg);
		}
			

		if (expression_Binary.op == Kind.OP_PLUS) {
			
			// no operands
			mv.visitInsn(IADD);
			
		} else if (expression_Binary.op == Kind.OP_MINUS) {
			mv.visitInsn(ISUB);
			
			
		}  else if (expression_Binary.op == Kind.OP_TIMES) {
			mv.visitInsn(IMUL);
			
		} else if (expression_Binary.op == Kind.OP_DIV) {
			
			
			mv.visitInsn(IDIV);
		} else if (expression_Binary.op == Kind.OP_MOD) {
			
			mv.visitInsn(IREM);
			
		}else if (expression_Binary.op == Kind.OP_AND) {
			
			
			mv.visitInsn(IAND);
			
			
		} else if (expression_Binary.op == Kind.OP_OR) {
			mv.visitInsn(IOR);
			
			
		
		}  else if (expression_Binary.op == Kind.OP_EQ) {
			mv.visitJumpInsn(IF_ICMPEQ, trueCase);
			mv.visitLdcInsn(0);
			mv.visitJumpInsn(GOTO, falseCase);
			
			mv.visitLabel(trueCase);
			mv.visitLdcInsn(1);
			
			mv.visitLabel(falseCase);
			
		}else if (expression_Binary.op == Kind.OP_LT) {
			mv.visitJumpInsn(IF_ICMPLT, trueCase);
			
			mv.visitLdcInsn(0);
			mv.visitJumpInsn(GOTO, falseCase);
			
			mv.visitLabel(trueCase);
			mv.visitLdcInsn(1);
			
			mv.visitLabel(falseCase);
			
			
		} else if (expression_Binary.op == Kind.OP_NEQ) {
			mv.visitJumpInsn(IF_ICMPNE, trueCase);
	
			mv.visitLdcInsn(0);
			mv.visitJumpInsn(GOTO, falseCase);
			
			mv.visitLabel(trueCase);
			mv.visitLdcInsn(1);
			
			mv.visitLabel(falseCase);
			
			
		} else if (expression_Binary.op == Kind.OP_GE) {
			mv.visitJumpInsn(IF_ICMPGE, trueCase);
		
			mv.visitLdcInsn(0);
			mv.visitJumpInsn(GOTO, falseCase);
			mv.visitLabel(trueCase);
			mv.visitLdcInsn(1);
			
			mv.visitLabel(falseCase);
			
			
		} else if (expression_Binary.op == Kind.OP_GT ) {
			mv.visitJumpInsn(IF_ICMPGT, trueCase);
			
			mv.visitLdcInsn(0);
			mv.visitJumpInsn(GOTO, falseCase);
			
			
			mv.visitLabel(trueCase);
			mv.visitLdcInsn(1);
			
			mv.visitLabel(falseCase);
			
			
		} else if (expression_Binary.op == Kind.OP_LT) {
			mv.visitJumpInsn(IF_ICMPLE, trueCase);
		
			mv.visitLdcInsn(0);
			mv.visitJumpInsn(GOTO, falseCase);
			
			
			mv.visitLabel(trueCase);
			mv.visitLdcInsn(1);
			
			mv.visitLabel(falseCase);
			
			
		}
		CodeGenUtils.genLogTOS(GRADE, mv, expression_Binary.typeName);
		return null;
	}

	

	// generate code to leave the two values on the stack
	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg)
			throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_Conditional(Expression_Conditional expression_Conditional, Object arg)
			throws Exception {
		// TODO test cas3s
		Label trueCase = new Label();
		Label falseCase = new Label();
		
		if(expression_Conditional.condition != null) {
			expression_Conditional.condition.visit(this, arg);
		}
		
		mv.visitJumpInsn(IFEQ, falseCase);
		
		expression_Conditional.trueExpression.visit(this, arg);
		mv.visitJumpInsn(GOTO, trueCase);
		
		mv.visitLabel(falseCase);
		expression_Conditional.falseExpression.visit(this, arg);
		
		mv.visitLabel(trueCase);
		
		
		 CodeGenUtils.genLogTOS(GRADE, mv,expression_Conditional.trueExpression.typeName);
		 return null;
		
		
		
//		CodeGenUtils.genLogTOS(GRADE, mv, expression_Conditional.trueExpression.getType());
//		return null;
	}

	@Override
	public Object visitExpression_Unary(Expression_Unary expression_Unary, Object arg) throws Exception {
		// TODO switch cases
		// test cas3s
		if (expression_Unary.e != null)
			expression_Unary.e.visit(this, arg);

		if (expression_Unary.op == Kind.OP_PLUS) {
			
		} 
		else if (expression_Unary.op == Kind.OP_MINUS) {
			
			mv.visitInsn(INEG);
		} 
		else if (expression_Unary.op == Kind.OP_EXCL) {
			
			if (expression_Unary.e.typeName == Type.INTEGER) {
				
				
				mv.visitLdcInsn(INTEGER.MAX_VALUE);
				mv.visitInsn(IXOR);
				
				
			} else if (expression_Unary.e.typeName == Type.BOOLEAN) {
				Label booleanTrue = new Label();
				Label booleanFalse = new Label();

				mv.visitJumpInsn(IFEQ, booleanFalse);
				mv.visitLdcInsn(0);
				mv.visitJumpInsn(GOTO, booleanTrue);

				mv.visitLabel(booleanFalse);
				mv.visitLdcInsn(1);

				mv.visitLabel(booleanTrue);
			}
		}

		CodeGenUtils.genLogTOS(GRADE, mv, expression_Unary.typeName);
		return null;
	}

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}
	
  
	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	

	@Override
	public Object visitSource_CommandLineParam(Source_CommandLineParam source_CommandLineParam, Object arg)
			throws Exception {
		
//		int k = (int) source_CommandLineParam.paramNum.visit(this, arg);
		
		mv.visitVarInsn(ALOAD, 0);
		
		
//		mv.visitLdcInsn(this.args[k]);
		
		source_CommandLineParam.paramNum.visit(this, arg);
		mv.visitInsn(AALOAD);
		//parse based on type.
		
//		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
//		mv.visitFieldInsn(PUTSTATIC, className, paramDec.getIdent().getText(), "I");
		
		
		return null;
	}

	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}


	@Override
	public Object visitDeclaration_SourceSink(Declaration_SourceSink declaration_SourceSink, Object arg)
			throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}
	


	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception {
		mv.visitLdcInsn(expression_IntLit.value);
		CodeGenUtils.genLogTOS(GRADE, mv, Type.INTEGER);
		//CodeGenUtils.genLog(GRADE, mv, expression_IntLit.toString());
		return null;
	}

	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg)
			throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	/** For Integers and booleans, the only "sink"is the screen, so generate code to print to console.
	 * For Images, load the Image onto the stack and visit the Sink which will generate the code to handle the image.
	 */
	
	@Override
	public Object visitStatement_Out(Statement_Out statement_Out, Object arg) throws Exception {
		// only INTEGER and BOOLEAN
		
		
//		statement_Out.sink.visit(this, arg);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		if (statement_Out.getDec().typeName == Type.INTEGER) {
			 mv.visitFieldInsn(GETSTATIC, className,statement_Out.name, "I");
			 
//			 CodeGenUtils.genPrintTOS(GRADE, mv, Type.INTEGER);
			 CodeGenUtils.genLogTOS(GRADE, mv, Type.INTEGER);
			 mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println","(I)V", false); 
//			 
			
		} else if (statement_Out.getDec().typeName == Type.BOOLEAN) {
			
			mv.visitFieldInsn(GETSTATIC, className, statement_Out.name, "Z");
			CodeGenUtils.genLogTOS(GRADE, mv, Type.BOOLEAN);
			 mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println","(Z)V",false);
//			 CodeGenUtils.genPrintTOS(GRADE, mv, Type.BOOLEAN);
		}
		
		
		// TODO HW6 remaining cases
		return null;
	}

	/**
	 * Visit source to load rhs, which will be a String, onto the stack
	 * 
	 *  In HW5, you only need to handle INTEGER and BOOLEAN
	 *  Use java.lang.Integer.parseInt or java.lang.Boolean.parseBoolean 
	 *  to convert String to actual type. 
	 *  
	 *  TODO HW6 remaining types
	 */
	@Override
	public Object visitStatement_In(Statement_In statement_In, Object arg) throws Exception {
		// test cas3s
		
		statement_In.source.visit(this, arg);
		if (statement_In.getDec().typeName == Type.INTEGER) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);

			mv.visitFieldInsn(PUTSTATIC, className, statement_In.name, "I");
		}

		else if (statement_In.getDec().typeName == Type.BOOLEAN) {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);

			mv.visitFieldInsn(PUTSTATIC, className, statement_In.name, "Z");
		}

		return null;
	}

	
	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */
//	@Override
//	public Object visitStatement_Transform(Statement_Assign statement_Assign, Object arg) throws Exception {
//		//TODO  (see comment)
//		throw new UnsupportedOperationException();
//	}

	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */
	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		
		
		if (lhs.typeName == Type.INTEGER) {
//			mv.visitVarInsn(ISTORE, lhs.dec.getSlotNum());
			CodeGenUtils.genPrintTOS(GRADE, mv, Type.INTEGER);
			mv.visitFieldInsn(PUTSTATIC, className,lhs.name, "I");
		} else if (lhs.dec.typeName == Type.BOOLEAN) {
			mv.visitFieldInsn(PUTSTATIC, className, lhs.name, "Z");
//			mv.visitVarInsn(ISTORE, lhs.dec.getSlotNum());
		}
		
		return null;
	}
	

	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg) throws Exception {
		//TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg) throws Exception {
		//TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception {

		mv.visitLdcInsn(expression_BooleanLit.value);

		CodeGenUtils.genLogTOS(GRADE, mv, Type.BOOLEAN);
		return null;
	}

	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident,
			Object arg) throws Exception {
		//TODO - verify with test cas3s
		String fieldtype = "";

		if (expression_Ident.typeName == Type.BOOLEAN)
			fieldtype = "Z";
		else
			fieldtype = "I";

		mv.visitFieldInsn(GETSTATIC, className, expression_Ident.name, fieldtype);
		// mv.visitLdcInsn(expression_Ident.name);
		CodeGenUtils.genLogTOS(GRADE, mv, expression_Ident.typeName);
		return null;
		
//		CodeGenUtils.genLogTOS(GRADE, mv, expression_Ident.getType());
//		return null;
	}

	@Override
	public Object visitStatement_Assign(Statement_Assign statement_Assign, Object arg) throws Exception {
		
		
		statement_Assign.e.visit(this, arg);
//		CodeGenUtils.genPrint(DEVEL, mv, "\nassignment: " + statement_Assign.lhs.name+ "=");
//		CodeGenUtils.genPrintTOS(GRADE, mv, statement_Assign.e.typeName);
		statement_Assign.lhs.visit(this, arg);
		return null;
	}

}
