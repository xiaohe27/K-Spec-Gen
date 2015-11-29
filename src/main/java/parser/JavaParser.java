package parser;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import parser.annotation.AnnotationInfo;
import parser.ast_visitor.MyASTVisitor;
import transform.ast.KSpec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaParser {

    //use ASTParse to parse string
    public static void parse(File file) throws IOException {
        String unitName = file.getName();
        final String pgmTxt = new String(Files.readAllBytes(file.toPath()));

        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setResolveBindings(true);//we need the type info of the vars.
        parser.setSource(pgmTxt.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setBindingsRecovery(true);

        //******maybe used or not***************
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        ////////////////////////////////////////
        parser.setUnitName(unitName);
        String[] sources = new String[]{file.getParentFile().getAbsolutePath()};
        String[] classpath = getClassPaths();
        parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        /////////////////////////////////////////


        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        if (cu.getAST().hasBindingsRecovery()) {
            System.out.println("Binding activated!");
        } else {
            System.out.println("Binding disabled!");
        }

        //MyASTVisitor myASTVisitor = new MyASTVisitor(cu);
        MyASTVisitor myASTVisitor = new MyASTVisitor(cu, pgmTxt);
        cu.accept(myASTVisitor);

        //check each line comment and judge whether they are LI
        cu.getCommentList().forEach(commentObj ->
        {
            if (commentObj instanceof Comment) {
                Comment comment = (Comment) commentObj;
                if (comment.isLineComment()) {
                    int commentStartPos = comment.getStartPosition();
                    //the comment with regex: //@LI some expression here
                    //is considered a LI.
                    String lpInvStr = pgmTxt.substring(commentStartPos, commentStartPos + comment.getLength());
                    Pattern pattern = Pattern.compile("//@LI\\p{Blank}+([\\p{Print}\\p{Blank}&&[^;]]+);");
                    Matcher matcher = pattern.matcher(lpInvStr);
                    if (matcher.find())
                        myASTVisitor.getAnnotationInfo().addPotentialLI(matcher.group(1), commentStartPos);
                }
            }
        });

        //we do not need to use comment visitor to get the contents of the comments.
        //we can associate the comments with their context by checking the pos.

        AnnotationInfo annotationInfo = myASTVisitor.getAnnotationInfo();

//        System.out.println("Method with pre and post conditions:");
//        annotationInfo.printInfo();
        KSpec kSpec = new KSpec("MY-K-Spec", annotationInfo);

        System.out.println(kSpec.toString());
    }

    //loop directory to get file list
    public static void ParseFilesInDir(String path) throws IOException {
        File file = new File(path);

        if (file.isFile()) {
            parse(file);
            return;
        }

        String dirPath = file.getCanonicalPath() + File.separator;

        File root = new File(dirPath);
        //System.out.println(rootDir.listFiles());
        File[] files = root.listFiles();
        String filePath = null;

        for (File f : files) {
            filePath = f.getAbsolutePath();
            if (f.isFile()) {
                parse(f);
            }
        }
    }

    public static String[] getClassPaths() {
        String pathSeparator = System.getProperty("path.separator");
        String classPath = System.getProperty("java.class.path");
        String[] classPaths = classPath.split(pathSeparator);
        return classPaths;
    }

    public static void main(String[] args) throws IOException {
        ParseFilesInDir(args[0]);
    }
}