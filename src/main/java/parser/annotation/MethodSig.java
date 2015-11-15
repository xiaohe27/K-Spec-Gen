package parser.annotation;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import java.util.ArrayList;

/**
 * Created by hx312 on 14/11/2015.
 */
public class MethodSig {
    private String packageName;
    private String className;
    private String methodName;
    private ArrayList<SingleVariableDeclaration> formalParams;
    private String retType;

    public MethodSig(String packageName, String className, String methodName, ArrayList<SingleVariableDeclaration> formalParams, String retType) {
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.formalParams = formalParams;
        this.retType = retType;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getQualifiedClsName() {
        if (packageName == null) {
            return className;
        } else {
            return packageName + "." + className;
        }
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public ArrayList<SingleVariableDeclaration> getFormalParams() {
        return formalParams;
    }

    public String getRetType() {
        return retType;
    }
}
