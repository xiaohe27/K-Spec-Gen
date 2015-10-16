package transform.ast;

/**
 * Created by hx312 on 13/10/2015.
 */
public class KCondition {
    public static enum ConditionKind {PreCond, PostCond}

    private ConditionKind kind;

    private CondExpression expression;
}
