package com.es.phoneshop.model.product.sort;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SortFieldTest {

    @Test
    public void getValidEnum() {
        SortField descriptionField = SortField.getEnum("description");
        SortField priceField = SortField.getEnum("price");

        assertNotNull(descriptionField);
        assertNotNull(priceField);
    }

    @Test
    public void getInvalidEnum() {
        SortField field1 = SortField.getEnum(null);
        SortField field2 = SortField.getEnum("");
        SortField field3 = SortField.getEnum("something");

        assertNull(field1);
        assertNull(field2);
        assertNull(field3);
    }

}