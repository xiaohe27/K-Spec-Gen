package transform.ast;

import parser.annotation.AnnotationInfo;
import transform.utils.Utils;

import java.util.ArrayList;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KSpec extends KASTNode {
    private ArrayList<KReqClause> reqClauses = new ArrayList<>();
    private ArrayList<KModule> modules = new ArrayList<>();

    public KSpec(String name, AnnotationInfo annotationInfo) {
        super(name);
        reqClauses.add(new KReqClause());
        annotationInfo.getMethodsInfoStream().forEach(
                methodInfo -> {
                    KModule kModule = new KModule(methodInfo);
                    modules.add(kModule);
                }
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        reqClauses.forEach(reqClause -> sb.append(reqClause.toString() + Utils.NEW_LINE));
        modules.forEach(module -> sb.append(module.toString() + Utils.NEW_LINE));
        return sb.toString();
    }
}
