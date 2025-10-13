package com.xuwei.prostore.service.product.impl;

import com.xuwei.prostore.dto.ImageDto;
import com.xuwei.prostore.dto.ProductDto;
import com.xuwei.prostore.exception.ProductNotFoundException;
import com.xuwei.prostore.exception.ResourceNotFoundException;
import com.xuwei.prostore.model.Category;
import com.xuwei.prostore.model.Image;
import com.xuwei.prostore.model.Product;
import com.xuwei.prostore.repository.CategoryRepository;
import com.xuwei.prostore.repository.ImageRepository;
import com.xuwei.prostore.repository.ProductRepository;
import com.xuwei.prostore.request.AddProductRequest;
import com.xuwei.prostore.request.ProductUpdateRequest;
import com.xuwei.prostore.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest product) {
        //check if the category is existed in DB
        //if yes, get the category
        //if not, create a new one as a new one
        //then set it as the new product category
        Category category =
                Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
                        .orElseGet(() -> {
                            Category newCategory =
                                    new Category(product.getCategory().getName());
                            return categoryRepository.save(newCategory);
                        });
        product.setCategory(category);
        return productRepository.save(mapToProduct(product,
                category));

    }

    private Product mapToProduct(AddProductRequest request,
                                 Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found"));
    }


    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ProductNotFoundException("Product not found");
        });
    }



    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct,
                                          ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category =
                categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand,
                                                   String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand,
                                            String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
