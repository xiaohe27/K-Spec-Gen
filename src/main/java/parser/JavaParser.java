package parser;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import parser.ast_visitor.MyASTVisitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JavaParser {
 
	//use ASTParse to parse string
	public static void parse(String pgmTxt) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(pgmTxt.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		//MyASTVisitor myASTVisitor = new MyASTVisitor(cu);
        MyASTVisitor myASTVisitor = new MyASTVisitor(cu, pgmTxt);
		cu.accept(myASTVisitor);

        //check each line comment and judge whether they are LI
        cu.getCommentList().forEach(commentObj ->
        {if (commentObj instanceof Comment) {
            Comment comment = (Comment) commentObj;
            if (comment.isLineComment()) {
                int commentStartPos = comment.getStartPosition();
                //the comment with regex: //@LI some expression here
                //is considered a LI.
                String lpInvStr = pgmTxt.substring(commentStartPos, commentStartPos + comment.getLength());
                if (lpInvStr.matches("//@LI\\p{Blank}+[\\p{Print}\\p{Blank}&&[^;]]+;"))
                myASTVisitor.getAnnotationInfo().addPotentialLI(lpInvStr, commentStartPos);
            }
        }
        });

        System.out.println("Method with pre and post conditions:");
        myASTVisitor.getAnnotationInfo().printInfo();


        //we do not need to use comment visitor to get the contents of the comments.
        //we can associate the comments with their context by checking the pos.
	}
 
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
 
	//loop directory to get file list
	public static void ParseFilesInDir(String path) throws IOException{
		File dirs = new File(path);
		String dirPath = dirs.getCanonicalPath() +File.separator;
 
		File root = new File(dirPath);
		//System.out.println(rootDir.listFiles());
		File[] files = root.listFiles ( );
		String filePath = null;
 
		 for (File f : files ) {
			 filePath = f.getAbsolutePath();
			 if(f.isFile()){
				 parse(readFileToString(filePath));
			 }
		 }
	}
 
	public static void main(String[] args) throws IOException {
		ParseFilesInDir(args[0]);
	}

}