package parser.annotation;

/**
 * Created by xiaohe on 10/6/15.
 */
public class MethodInfo {
    private String methName;
    private String preAndPostCond;

    public MethodInfo(String methName, String preAndPostCond) {
        this.methName = methName;
        this.preAndPostCond = preAndPostCond;
    }

    public String toString() {
        String ret = "Method " + methName + " 's pre and post condition is \n";
        ret += preAndPostCond;

        return ret;
    }
}
