package com.example.merakissan.Models;

public class Product {
    private String ProductTitle , ProductCategory , ProductType , ProductDescription , ImageUri , CreatedBy;
    private int ProductPrice;

    public Product() {

    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public String getProductType() {
        return ProductType;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public int getProductPrice() {
        return ProductPrice;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }
}
