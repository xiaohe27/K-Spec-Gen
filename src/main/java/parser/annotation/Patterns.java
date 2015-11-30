package parser.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaohe on 10/7/15.
 */
public class Patterns {
    public static final String ClauseStr =
            "@(requires|ensures|returns)\\p{Blank}+([\\p{Print}\\p{Blank}&&[^;]]*);";
    public static final Pattern SingleClause = Pattern.compile(ClauseStr);
    public static final Pattern METHOD_CONTRACT =
            Pattern.compile("/\\*\\*\\p{Space}+((\\*\\p{Space}*" + ClauseStr + "\\p{Space}*)*)\\*/");
    public static final Pattern LI =
            Pattern.compile("//@LI\\p{Blank}+([\\p{Print}\\p{Blank}&&[^;]]+);");

    //"/\\*@\\p{Space}*(env|store)\\p{Space}*\\{([\\p{Print}\\p{Space}&&[^{}]]*)\\}\\p{Space}*@\\*/"
    private static final String rawCell = "(env|store)\\p{Space}*\\{([\\p{Print}\\p{Space}&&[^{}]]*)\\}";

    public static final Pattern CellInComment =
            Pattern.compile("/\\*@\\p{Space}*" + rawCell + "\\p{Space}*@\\*/");

    public static final Pattern RAW_CELL = Pattern.compile(rawCell);

    protected static final String REQUIRES = "requires";
    protected static final String ENSURES = "ensures";
    protected static final String RETURNS = "returns";

    public static void main(String[] args) {
        String input = "/**" +
                "\n * @requires a < b && b < c + 2;\n" +
                "\n*@requires ddd < b32 && bbb > ccc * 2;\n" +
                "\n* @ensures d == a + b + c;\n" +
                "\n*  @returns 52;*/";

        input = "/** \n" +
                " * @requires n >= 0;\n" +
                " * @ensures true;\n" +
                " * @returns (n * (n + 1)) / 2;\n" +
                " */";

        Matcher matcher = METHOD_CONTRACT.matcher(input);
        int count = 0;
        int groupSize = matcher.groupCount();
        System.out.println("Group size is " + groupSize);

        String contractStr = null;
        if (matcher.find()) {
            contractStr = matcher.group(1);
        }

        if (contractStr == null)
            return;

        matcher = Pattern.compile(ClauseStr).matcher(contractStr);

        while (matcher.find()) {

            String groupI = matcher.group(1);

            switch (groupI) {
                case REQUIRES:
                    System.out.println("@pre : " + matcher.group(2));
                    break;

                case ENSURES:
                    System.out.println("@post : " + matcher.group(2));
                    break;

                case RETURNS:
                    System.out.println("@ret : " + matcher.group(2));
                    break;

                default:
                    break;
            }
        }
    }
}
