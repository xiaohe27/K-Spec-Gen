package transform.ast;

import parser.annotation.AnnotationInfo;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KSpec extends KASTNode {
    private ArrayList<KReqClause> reqClauses;
    private ArrayList<KModule> modules;

    public KSpec(String name, AnnotationInfo annotationInfo) {
        super(name);
        annotationInfo.getAllMethodsInfo().forEach(
                methodInfo -> {
                    KModule kModule = new KModule(methodInfo);
                    modules.add(kModule);
                }
        );
    }


    @Override
    public String toString() {
        return null;
    }
}
