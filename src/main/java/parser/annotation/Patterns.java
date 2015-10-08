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

    public static final Pattern METHOD_CONTRACT =
            Pattern.compile("/\\*\\*@\\p{Space}+(((requires)|(ensures))\\p{Blank}+([\\p{Print}\\p{Blank}&&[^;]]*);\\p{Space}*)*@\\*/");

    public static void main(String[] args) {
        String input = "/**@" +
                "\nrequires a < b && b < c + 2;\n" +
                "\nrequires ddd < b32 && bbb > ccc * 2;\n" +
                "\nensures d == a + b + c;\n@*/";

        Matcher matcher = METHOD_CONTRACT.matcher(input);
        int count = 1;
        int groupSize = matcher.groupCount();
        System.out.println("Group size is " + groupSize);

        matcher.find();
        while(count <= groupSize) {
            String groupI = matcher.group(count++);
            System.out.println("group " + count + ": " + groupI);
            switch (groupI) {
                case REQUIRE :
                    System.out.println("@pre : " + matcher.group(count++));
                    break;

                case ENSURE :
                    System.out.println("@post : " + matcher.group(count++));
                    break;

                default:
                    break;
            }
        }
    }
}
