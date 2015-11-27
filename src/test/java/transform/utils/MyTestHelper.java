package transform.utils;

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
            "(" + _methodArgs + ")" + "{}" +"}";

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
}
