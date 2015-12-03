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
        //@LI out;
        {
            int t;
            t = 2;
            while (n > 7)
            //@LI inner1;
            {
                int z;
                z = 11;
            }

            while (n < 9)
            //@LI inner2;
            {
                s = t + n;
            }
        }

        return s;
    }
}


