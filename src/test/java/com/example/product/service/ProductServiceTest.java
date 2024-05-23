package com.example.product.service;

import com.example.product.dto.ProductRequest;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        productService.productRepository = productRepository;
        productService.modelMapper = modelMapper;
    }

    @Test
    void testSaveProduct() {
        ProductRequest request = new ProductRequest("Product 1", "Description 1", BigDecimal.valueOf(100.0));
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0));

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(request);

        assertThat(savedProduct.getName()).isEqualTo(request.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(request.getDescription());
        assertThat(savedProduct.getPrice()).isEqualTo(request.getPrice());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0));
        Product product2 = new Product(2L, "Product 2", "Description 2", BigDecimal.valueOf(160.0));

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSize(2);
        assertThat(products.get(0).getName()).isEqualTo("Product 1");
        assertThat(products.get(1).getName()).isEqualTo("Product 2");

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> foundProduct = productService.getProductById(1L);
        assertThat(foundProduct.isPresent()).isTrue();
        assertThat(foundProduct.get().getName()).isEqualTo("Product 1");

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0));
        ProductRequest productDetails = new ProductRequest("Updated Product", "Updated Description", BigDecimal.valueOf(150.0));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(1L, productDetails);

        assertThat(updatedProduct.getName()).isEqualTo(productDetails.getName());
        assertThat(updatedProduct.getDescription()).isEqualTo(productDetails.getDescription());
        assertThat(updatedProduct.getPrice()).isEqualTo(productDetails.getPrice());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        ProductRequest productDetails = new ProductRequest("Updated Product", "Updated Description", BigDecimal.valueOf(150.0));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1L, productDetails);
        });
        assertThat(exception.getMessage()).isEqualTo("Product not found");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));
        productService.deleteProduct(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }
}
