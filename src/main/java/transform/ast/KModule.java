package transform.ast;

import parser.annotation.MethodInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KModule extends KASTNode {
    private ArrayList<KImport> imports;

    private ArrayList<KRule> rules;

    public KModule(MethodInfo methodInfo) {
        super(methodInfo.getMethodName());

    }

    @Override
    public String toString() {
        return null;
    }
}
