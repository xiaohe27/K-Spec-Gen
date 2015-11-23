package transform.utils;

/**
 * Created by hx312 on 21/11/2015.
 */
public class ConstraintGen {
    /**
     * The constraint expressed by [min, max] where min and max are integers.
     *
     * @param var
     * @param min
     * @param max
     * @return
     */
    private static String genIntRangeConstraint(String var, long min, long max) {
        return var + " >=Int " + min + " andBool " + var + " <=Int " + max + " ";
    }

    /**
     * The constraint expressed by [min, max] where min and max are floating numbers.
     *
     * @param var
     * @param min
     * @param max
     * @return
     */
    private static String genFloatRangeConstraint(String var, double min, double max) {
        return var + " >=Float " + min + " andBool " + var + " <=Float " + max + " ";
    }

    /**
     * Generate the range constraint for the var of the given type.
     *
     * @param type    The type of the variable.
     * @param varName The name of the variable.
     * @return The constraint expression describing the range of the values that the var can take.
     */
    public static String genRangeConstraint4Type(String type, String varName) {
        switch (type) {
            case "byte":
                return genIntRangeConstraint(varName, Byte.MIN_VALUE, Byte.MAX_VALUE);

            case "short":
                return genIntRangeConstraint(varName, Short.MIN_VALUE, Short.MAX_VALUE);

            case "int":
                return genIntRangeConstraint(varName, Integer.MIN_VALUE, Integer.MAX_VALUE);

            case "long":
                return genIntRangeConstraint(varName, Long.MIN_VALUE, Long.MAX_VALUE);

            case "char":
                return genIntRangeConstraint(varName, Character.MIN_VALUE, Character.MAX_VALUE);

            case "float":
                return genFloatRangeConstraint(varName, Float.MIN_VALUE, Float.MAX_VALUE);

            case "double":
                return genFloatRangeConstraint(varName, Double.MIN_VALUE, Double.MAX_VALUE);

            default:
                return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(genRangeConstraint4Type("int", "X"));
    }
}
