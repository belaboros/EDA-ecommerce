package org.msffp.order.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.msffp.order.model.Product;
import org.msffp.order.model.ProductParsingException;


/**
 * See examples
 * - https://medium.com/javarevisited/junit-5-assertions-5d360545e3a
 */
public class ProductTest {


    @Test
    public void fromValidJson1Test() {
        assertDoesNotThrow( () -> {
            String jsonProductMsg = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"id\"},{\"type\":\"string\",\"optional\":true,\"field\":\"name\"},{\"type\":\"boolean\",\"optional\":true,\"field\":\"active\"},{\"type\":\"int32\",\"optional\":true,\"field\":\"version\"},{\"type\":\"double\",\"optional\":true,\"field\":\"unitPrice\"}],\"optional\":true,\"name\":\"payload\"},\"payload\":{\"id\":25,\"name\":\"tomato\",\"active\":true,\"version\":0,\"unitPrice\":8.6}}";
            Product product = Product.fromJson(jsonProductMsg);
            assertEquals(product.getId(), 25);
            assertEquals(product.getName(), "tomato");
            assertTrue(product.isActive());
            assertEquals(product.getUnitPrice(), 8.6);
            assertEquals(product.getVersion(), 0);
        });
    } 

    @Test
    public void fromValidJson2Test() {
        assertDoesNotThrow( () -> {
            String jsonProductMsg = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"id\"},{\"type\":\"string\",\"optional\":true,\"field\":\"name\"},{\"type\":\"boolean\",\"optional\":true,\"field\":\"active\"},{\"type\":\"int32\",\"optional\":true,\"field\":\"version\"},{\"type\":\"double\",\"optional\":true,\"field\":\"unitPrice\"}],\"optional\":true,\"name\":\"payload\"},\"payload\":{\"id\":27,\"name\":\"peanut\",\"active\":false,\"version\":3,\"unitPrice\":7.5}}";
            Product product = Product.fromJson(jsonProductMsg);
            assertEquals(product.getId(), 27);
            assertEquals(product.getName(), "peanut");
            assertFalse(product.isActive());
            assertEquals(product.getUnitPrice(), 7.5);
            assertEquals(product.getVersion(), 3);
        });
    }
    
    @Test
    public void fromInvalidJson1EmptyJsonTest() {
        assertThrows( ProductParsingException.class, () -> {
            String jsonProductMsg = "";
            Product product = Product.fromJson(jsonProductMsg);
            assertEquals(product.getId(), 27);
            assertEquals(product.getName(), "peanut");
            assertFalse(product.isActive());
            assertEquals(product.getUnitPrice(), 7.5);
            assertEquals(product.getVersion(), 3);
        });
    }     
    
    @Test
    public void fromInvalidJson2MissingIDTest() {
        assertThrows( ProductParsingException.class, () -> {
            String jsonProductMsg = "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"id\"},{\"type\":\"string\",\"optional\":true,\"field\":\"name\"},{\"type\":\"boolean\",\"optional\":true,\"field\":\"active\"},{\"type\":\"int32\",\"optional\":true,\"field\":\"version\"},{\"type\":\"double\",\"optional\":true,\"field\":\"unitPrice\"}],\"optional\":true,\"name\":\"payload\"},\"payload\":{\"name\":\"chips\",\"active\":true,\"version\":0,\"unitPrice\":16.6}}";
            Product product = Product.fromJson(jsonProductMsg);
            //assertEquals(product.getId(), 27);
            assertEquals(product.getName(), "chips");
            assertTrue(product.isActive());
            assertEquals(product.getUnitPrice(), 16.6);
            assertEquals(product.getVersion(), 0);
        });
    }  
}

