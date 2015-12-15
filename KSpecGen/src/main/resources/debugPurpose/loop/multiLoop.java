class multiLoop {
    public static void main(String[] args) {
        System.out.println("Sum to 5:" + sum_iterative(5));
    }

    /**
     * @requires n > 100;
     */
    static int loop(int n) {
        int s;

        s = 0;
        while (n > 0)
        //@LOOP_PROP out;
        {
            int t;
            t = 2;
            while (n > 7)
            //@LOOP_PROP inner1;
            {
                int z;
                z = 11;
            }

            while (n < 9)
            //@LOOP_PROP inner2;
            {
                if (n > 5) {
                    s = t + n;
                    t = n * 2;
                } else
                    s = s - 1;

            }
        }

        return s;
    }
}


