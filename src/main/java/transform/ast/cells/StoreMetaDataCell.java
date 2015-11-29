package transform.ast.cells;

import java.util.Collection;
import java.util.Set;

/**
 * Created by xiaohe on 11/1/15.
 * StoreMetaData is a map that mapping addr to constant "LocalLocMetadata".
 */
public class StoreMetaDataCell extends Cell {
    private static final String fixed1 = "... (.Map => ?_:Map)\n";
    private static final String fixed2 = "... ";
    private Set<Integer> addresses;

    //input: a set of addresses
    //the values
    public StoreMetaDataCell(Set<Integer> addresses) {
        super(Cell.STORE_METADATA);
        this.addresses = addresses;
    }
}
