package transform.ast.cells;

import java.util.Collection;

/**
 * Created by xiaohe on 11/1/15.
 * StoreMetaData is a map that mapping addr to constant "LocalLocMetadata".
 */
public class StoreMetaDataCell extends Cell {
    private static final String fixed1 = "... (.Map => ?_:Map)\n";
    private static final String fixed2 = "... ";
    private Collection<String> addresses;

    //input: a set of addresses
    //the values
    public StoreMetaDataCell(Collection<String> addresses) {
        super(Cell.STORE_METADATA);
        this.addresses = addresses;
    }
}
