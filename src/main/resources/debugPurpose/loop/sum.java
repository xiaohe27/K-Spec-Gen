// Copyright (c) 2014 K Team. All Rights Reserved.
/*
 * Function sum_iterative returns the sum of the first n natural numbers.
 */
class sum {
    public static void main(String[] args) {
        System.out.println("Sum to 5:" + sum_iterative(5));
    }

    /**
     * @requires n >= 0;
     * @returns (n * (n + 1)) / 2;
     */
    static int sum_iterative(int n) {
        int s;

        s = 0;
        //@ inv s = ((old(n) - n) * (old(n) + n + 1)) / 2 /\ n >= 0
        while (n > 0) {
            s = s + n;
            n = n - 1;
        }

        return s;
    }
}


