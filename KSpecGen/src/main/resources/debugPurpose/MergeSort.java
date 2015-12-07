class MergeSort {
    public static void main(String[] args) {
        //Some hard coded array.
        int[] a = {-2, 22, 37, 0, 1, -2, -5, 95, 82, 64};
        System.out.println("Before sorting: \n");
        for (int i : a)
            System.out.print(" " + i + " ");

        a = ms(a);

        System.out.println("\n\nAfter sorting: \n");
        for (int i : a)
            System.out.print(" " + i + " ");
    }

    /**
     * Split phase.
     *
     * @param a
     * @return
     */
    public static int[] ms(int[] a) {
        int size = a.length;
        if (size > 1) {
            int half = size / 2;
            int[] b = new int[half];
            int[] c = new int[size - half];
            //split the array to two halves
            System.arraycopy(a, 0, b, 0, half);
            System.arraycopy(a, half, c, 0, size - half);
            b = ms(b);
            c = ms(c);
            a = merge(a, b, c);
        }
        return a;
    }

    /**
     * The merge phase.
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int[] merge(int[] a, int[] b, int[] c) {
        int p = b.length, q = c.length;
        int i = 0, j = 0, k = 0;
        while (i < p && j < q)
        //@ 0 <= i <= p
        //@ 0 <= j <= q
        {
            if (b[i] < c[j]) {
                a[k] = b[i];
                i++;
            } else {
                a[k] = c[j];
                j++;
            }

            k++;
        }
        if (i == p) System.arraycopy(c, j, a, k, q - j);
        if (j == q) System.arraycopy(b, i, a, k, p - i);
        return a;

    }
}
