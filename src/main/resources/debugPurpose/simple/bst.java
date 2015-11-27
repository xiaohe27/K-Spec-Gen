public class bst {
    int value;
    bst left;
    bst right;


    public static void main(String[] args) {
        bst node1 = new bst(0);
        bst node2 = new bst(0);
        bst node3 = new bst(0);
        node1.left = node2;
        node1.right = node3;
    }


    /**
     * @requires bst(t);
     */
    static boolean find(int v, bst t) {
        if (t == null)
            return false;
        else if (v == t.value)
            return true;
        else if (v < t.value)
            return find(v, t.left);
        else
            return find(v, t.right);
    }

}
