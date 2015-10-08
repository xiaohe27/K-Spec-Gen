package parser.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaohe on 10/7/15.
 */
public class Patterns {
    private static final String REQUIRE = "requires";
    private static final String ENSURE = "ensures";
    private static final String LI = "loop_invariant";

    private static final String clause = "([\\p{Print}\\p{Blank}&&[^;]]*)";

    private static final String preCondPattern = "(" + REQUIRE + ")\\p{Blank}+" + clause + ";";

    private static final String postCondPattern = "(" + ENSURE + ")\\p{Blank}+" + clause + ";";

    //
    public static final Pattern PRE_COND_PATTERN = Pattern.compile(preCondPattern);

    public static final Pattern POST_COND_PATTERN = Pattern.compile(postCondPattern);

    public static final String CLAUSE =
            "(requires|ensures)\\p{Blank}+([\\p{Print}\\p{Blank}&&[^;]]*);";

    public static final Pattern METHOD_CONTRACT =
            Pattern.compile("/\\*\\*@\\p{Space}+((" + CLAUSE + "\\p{Space}*)*)@\\*/");

    public static void main(String[] args) {
        String input = "/**@" +
                "\nrequires a < b && b < c + 2;\n" +
                "\nrequires ddd < b32 && bbb > ccc * 2;\n" +
                "\nensures d == a + b + c;\n@*/";

        Matcher matcher = METHOD_CONTRACT.matcher(input);
        int count = 0;
        int groupSize = matcher.groupCount();
        System.out.println("Group size is " + groupSize);

        String contractStr = null;
        if(matcher.find()) {
            contractStr = matcher.group(1);
        }

        System.out.println("Contract is:\n" + contractStr);

        matcher = Pattern.compile(CLAUSE).matcher(contractStr);

        while (matcher.find()) {
            System.out.println("No. " + (count++) + " clause: " + matcher.group(0));
            String groupI = matcher.group(1);

            switch (groupI) {
                case REQUIRE:
                    System.out.println("@pre : " + matcher.group(2));
                    break;

                case ENSURE:
                    System.out.println("@post : " + matcher.group(2));
                    break;

                default:
                    break;
            }
        }
    }
}
