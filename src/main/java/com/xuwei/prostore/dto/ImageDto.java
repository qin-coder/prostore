package com.xuwei.prostore.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageDto {
    private Long id;
    private String fileName;
    private String downloadUrl;

}