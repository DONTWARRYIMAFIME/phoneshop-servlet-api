package com.es.phoneshop.model.product.sort;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SortOrderTest {

    @Test
    public void getValidEnum() {
        SortOrder ascLowerCase = SortOrder.getEnum("asc");
        SortOrder ascUpperCase = SortOrder.getEnum("ASC");
        SortOrder asc = SortOrder.getEnum("AsC");
        SortOrder desc = SortOrder.getEnum("desc");

        assertNotNull(ascLowerCase);
        assertNotNull(ascUpperCase);
        assertNotNull(asc);
        assertNotNull(desc);
    }

    @Test
    public void getInvalidEnum() {
        SortOrder order1 = SortOrder.getEnum(null);
        SortOrder order2 = SortOrder.getEnum("");
        SortOrder order3 = SortOrder.getEnum("something");

        assertNull(order1);
        assertNull(order2);
        assertNull(order3);
    }

}