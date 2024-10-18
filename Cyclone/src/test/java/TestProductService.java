import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cyclone.Model.Product;
import com.cyclone.Service.ProductService;
import com.cyclone.Repository.Interface.ProductRepository;

class TestProductService {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        
        List<Product> mockProducts = Arrays.asList(
            new Product(1, "Product 1", "Description 1", 10.0, 5, null),
            new Product(2, "Product 2", "Description 2", 20.0, 10, null)
        );
        when(productRepository.getAllProducts()).thenReturn(mockProducts);

        
        List<Product> result = productService.getAllProducts();

        
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
    }

    @Test
    void testAddProduct() {
        
        Product product = new Product(1, "New Product", "Description", 15.0, 7, null);
        when(productRepository.addProduct(product)).thenReturn(true);

       
        boolean result = productService.addProduct(product);

      
        assertTrue(result);
        verify(productRepository, times(1)).addProduct(product);
    }

    @Test
    void testGetProductById() {
        
        Product mockProduct = new Product(1, "Product 1", "Description 1", 10.0, 5, null);
        when(productRepository.getProductById(1)).thenReturn(Optional.of(mockProduct));

   
        Optional<Product> result = productService.getProductById(1);

     
        assertTrue(result.isPresent());
        assertEquals("Product 1", result.get().getName());
    }

    @Test
    void testUpdateProduct() {
       
        Product product = new Product(1, "Updated Product", "New Description", 25.0, 15, null);
        when(productRepository.updateProduct(product)).thenReturn(true);

       
        boolean result = productService.updateProduct(product);

        
        assertTrue(result);
        verify(productRepository, times(1)).updateProduct(product);
    }

    @Test
    void testDeleteProduct() {
        
        when(productRepository.deleteProduct(1)).thenReturn(true);

        
        boolean result = productService.deleteProduct(1);

        
        assertTrue(result);
        verify(productRepository, times(1)).deleteProduct(1);
    }
}
