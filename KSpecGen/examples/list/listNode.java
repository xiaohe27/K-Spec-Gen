// Copyright (c) 2015 K Team. All Rights Reserved.
public class listNode {
    int val;
    listNode next;

    listNode(int val) {
        this.val = val;
        this.next = null;
    }

    public static void main(String[] args) {
        listNode node1 = new listNode(1);
        listNode node2 = new listNode(2);
        listNode node3 = new listNode(3);
        node1.next = node2;
        reverse(node1);
    }

    /**
     * @objectStore {
     * (list(lp1)(A:List) => list(?lp2)(rev(A)))
     * }
     * @returns ?lp2;
     */
    static listNode reverse(listNode x) {
        listNode p;

        p = null;
        while (x != null)
        /*@env {
        x |-> 1,
        p |-> 2
        }@*/
        /*@store{
        1 |-> (lp1 => null),
        2 |-> (lp2 => ?lp2)
        }@*/
        /*@objectStore{
        list(lp1)(B:List) list(lp2)(C:List) => list(?lp2)(?A:List)
        }@*/ {
            listNode y;

            y = x.next;
            x.next = p;
            p = x;
            x = y;
        }

        return p;
    }
}

