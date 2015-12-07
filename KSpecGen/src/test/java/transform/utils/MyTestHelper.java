package transform.utils;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by hx312 on 26/11/2015.
 */
public class MyTestHelper {
    public static final String _className = "@CLASSNAME";
    public static final String defaultClsName = "Test";
    public static final String _methName = "@METHODNAME";
    public static final String defaultMethodName = "test";
    public static final String _retType = "@RET_TYPE";
    public static final String defaultRetType = "void";

    public static final String _methodArgs = "@METHOD_ARGS";
    public static final String defaultMethodArgs = "";

    public static final String codeTemp = "class " + _className + "  {" +
            _retType + " " + _methName +
            "(" + _methodArgs + ")" + "{}" + "}";

    public static final String allButArgsDefaultCode = codeTemp.
            replaceAll(_className, defaultClsName).
            replaceAll(_methName, defaultMethodName).
            replaceAll(_retType, defaultRetType);

    /**
     * Generate a java class from code template codeTemp, all holes use default but method args
     * with the given string.
     */
    public static String getCodeWithMethodArgs(String methodArgs) {
        return allButArgsDefaultCode.replaceAll(_methodArgs, methodArgs);
    }

    public static WhileStatement extractWhileNdFromJavaFile(File file) throws IOException {
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
        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
        /////////////////////////////////////////

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

//        if (cu.getAST().hasBindingsRecovery()) {
//            System.out.println("Binding activated!");
//        } else {
//            System.out.println("Binding disabled!");
//        }

        WhileStatement whileStatement = null;
        CUVisitor cuVisitor = new CUVisitor();
        cu.accept(cuVisitor);
        return cuVisitor.getWhileNd();
    }

    public static String[] getClassPaths() {
        String pathSeparator = System.getProperty("path.separator");
        String classPath = System.getProperty("java.class.path");
        String[] classPaths = classPath.split(pathSeparator);
        return classPaths;
    }

    private static class CUVisitor extends ASTVisitor {
        WhileStatement whileNd;

        public boolean visit(WhileStatement whileStatement) {
            this.whileNd = whileStatement;
            return false;
        }

        public WhileStatement getWhileNd() {
            return this.whileNd;
        }
    }
}
