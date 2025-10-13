package com.xuwei.prostore.service.image.impl;

import com.xuwei.prostore.dto.ImageDto;
import com.xuwei.prostore.exception.ResourceNotFoundException;
import com.xuwei.prostore.model.Image;
import com.xuwei.prostore.model.Product;
import com.xuwei.prostore.repository.ImageRepository;
import com.xuwei.prostore.service.image.ImageService;
import com.xuwei.prostore.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Image not found")
        );
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("Image not found");
        });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files,
                                     Long productId) {
        Product product = productService.getProductById(productId);
        List<Image> imagesToSave = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                imagesToSave.add(image);
            } catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to process file:" +
                        " " + file.getOriginalFilename(), e);
            }
        }
        List<Image> savedImages =
                imageRepository.saveAll(imagesToSave);

        savedImages.forEach(img -> {
            String downloadUrl =
                    "/api/v1/images/image/download/" + img.getId();
            img.setDownloadUrl(downloadUrl);
        });

        imageRepository.saveAll(savedImages);

        return savedImages.stream()
                .map(img -> new ImageDto(
                        img.getId(),
                        img.getFileName(),
                        img.getDownloadUrl()
                ))
                .toList();
    }


    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
