// Copyright (c) 2014 K Team. All Rights Reserved.
/*
 * Function sum_iterative returns the sum of the first n natural numbers.
 */
class sum {

@Pre (a > 0)
public static void zz(int a) {}

private String ddd() {return "";}

private int fff() {return 0;}

@Pre(s != null)
private int ggg(String s) {return 0;}

private int aaa() {return 0;}

    public static void main(String[] args) {
        System.out.println("Sum to 5:" + sum_iterative(5));
    }

/**
* @requires n >= 0;
 * @requires someOtherPre;
* @ensures true;
* @returns (n * (n + 1)) / 2;
*/
    static int sum_iterative (int n)
/*@ rule <k> $ => return (n * (n + 1)) / 2; ...</k>
    if n >= 0 */
    {
        int s;

        s = 0;
        //@ inv s = ((old(n) - n) * (old(n) + n + 1)) / 2 /\ n >= 0
        //@LI (n >= 0)
        while (n > 0) {
            //@LI cond1;
            //@LI cond2;
            //@LI cond3;
            //sth not a LI.

            s = s + n;
            n = n - 1;

        String debug = "findMe!";
        }


//test multiple loops executed in a seq
int x = 3;
int r = 0;

	//@ LI for the second loop
        while (x > 0) {
            //@LI cond22;
            //sth not a LI too.
            //@LI cond25;
            //@LI cond27;
            r = r + x;

            //third
            while (x > 0) {
                //@LI cond11111111111111;
                //@LI cond2222222222222221;
                //@LI cond3333333333333337;
                r = r + x;
                x = x - 1;

                String debug = "findMe 3!";
            }

            x = x - 1;

        String debug = "findMe too!";
        }


x = 3;
r = 0;

	//@ LI for the fourth loop
        while (x > 0) {
            //@LI cond11;
            //@LI cond21;
            //@LI cond37;
            r = r + x;
            x = x - 1;

        String debug = "findMe 3!";
        }

        return s;
    }
}


