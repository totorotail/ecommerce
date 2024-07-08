package com.zerobase.order.application;

import com.zerobase.order.domain.model.Product;
import com.zerobase.order.domain.product.AddProductCartForm;
import com.zerobase.order.domain.product.AddProductForm;
import com.zerobase.order.domain.product.AddProductItemForm;
import com.zerobase.order.domain.redis.Cart;
import com.zerobase.order.domain.repository.ProductRepository;
import com.zerobase.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CartApplicationTest {

    @Autowired
    CartApplication cartApplication;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    void addTestModify() {
        Long customerId = 100L;
        cartApplication.clearCart(customerId);

        Product p = addProduct();

        Product result = productRepository.findWithProductItemsById(p.getId()).get();

        assertNotNull(result);

        assertEquals(result.getName(), "나이키 에어포스");
        assertEquals(result.getDescription(), "신발");
        assertEquals(result.getProductItems().size(), 3);

        AddProductCartForm addProductCartForm = makeForm(result);

        Cart cart = cartApplication.addCart(customerId, addProductCartForm);

        assertEquals(cart.getMessages().size(), 0);

        cart = cartApplication.getCart(customerId);
        assertEquals(cart.getMessages().size(), 1);

    }

    AddProductCartForm makeForm(Product p) {
        AddProductCartForm.ProductItem productItem = AddProductCartForm.ProductItem.builder()
                .id(p.getProductItems().get(0).getId())
                .name(p.getProductItems().get(0).getName())
                .count(5)
                .price(20000)
                .build();
        return AddProductCartForm.builder()
                .id(p.getId())
                .sellerId(p.getSellerId())
                .name(p.getName())
                .description(p.getDescription())
                .items(List.of(productItem))
                .build();
    }

    Product addProduct() {
        Long sellerId = 1L;
        AddProductForm form = makeProductForm("나이키 에어포스", "신발", 3);
        return productService.addProduct(sellerId, form);
    }

    private static AddProductForm makeProductForm(String name, String description, int itemCount) {
        List<AddProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            itemForms.add(makeProductItemForm(null, name + i));
        }
        return AddProductForm.builder()
                .name(name)
                .description(description)
                .items(itemForms)
                .build();
    }

    private static AddProductItemForm makeProductItemForm(Long productId, String name) {
        return AddProductItemForm.builder()
                .productId(productId)
                .name(name)
                .price(10000)
                .count(10)
                .build();
    }
}