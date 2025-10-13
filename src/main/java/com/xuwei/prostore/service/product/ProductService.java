package com.xuwei.prostore.service.product;

import com.xuwei.prostore.dto.ProductDto;
import com.xuwei.prostore.model.Product;
import com.xuwei.prostore.request.AddProductRequest;
import com.xuwei.prostore.request.ProductUpdateRequest;

import java.util.List;

public interface ProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String category, String name);
    Long countProductsByBrandAndName(String brand, String name);


    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
