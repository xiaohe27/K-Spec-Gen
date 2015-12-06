package parser;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import parser.annotation.AnnotationInfo;
import parser.annotation.Patterns;
import parser.ast_visitor.MyASTVisitor;
import transform.ast.KSpec;
import transform.ast.cells.Cell;
import transform.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.regex.Matcher;

public class JavaParser {

    //use ASTParse to parse string
    public static String parse(File file) throws IOException {
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
        String[] classpath = FileUtils.getClassPaths();
        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
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
                int commentStartPos = comment.getStartPosition();
                String commentStr = pgmTxt.substring(commentStartPos,
                        commentStartPos + comment.getLength()).trim();

                if (comment.isLineComment()) {
                    //the comment with regex: //@LI some expression here
                    //is considered a LI.
                    Matcher LI_Matcher = Patterns.LI.matcher(commentStr);
                    if (LI_Matcher.find())
                        myASTVisitor.getAnnotationInfo().addPotentialLI(LI_Matcher.group(1), commentStartPos);
                } else if (comment.isBlockComment()) {
                    //the comment with regex:
                    // "/\\*@\\p{Space}*(env|store)\\p{Space}*\\{([\\p{Print}\\p{Space}&&[^{}]]*)\\}@\\*/"
                    Matcher Cell_Matcher = Patterns.RAW_CELL.matcher(commentStr);
                    if (Cell_Matcher.find()) {
                        switch (Cell_Matcher.group(1)) {
                            case Cell.ENV:
                                myASTVisitor.getAnnotationInfo()
                                        .addEnvCellInfo(Cell_Matcher.group(2), commentStartPos);
                                break;

                            case Cell.STORE:
                                myASTVisitor.getAnnotationInfo()
                                        .addStoreCellInfo(Cell_Matcher.group(2), commentStartPos);
                                break;

                            case Cell.OBJ_STORE:
                                myASTVisitor.getAnnotationInfo()
                                        .addObjStoreCellInfo(Cell_Matcher.group(2),
                                                commentStartPos);
                                break;
                        }
                    }
                }
            }
        });

        //we do not need to use comment visitor to get the contents of the comments.
        //we can associate the comments with their context by checking the pos.

        AnnotationInfo annotationInfo = myASTVisitor.getAnnotationInfo();

//        System.out.println("Method with pre and post conditions:");
//        annotationInfo.printInfo();

        KSpec kSpec = new KSpec(unitName + "-K-Spec", annotationInfo);
//        System.out.println(kSpec.toString());
        return kSpec.toString();
    }

}