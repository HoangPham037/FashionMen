package com.android45.fashionmen.Medel;

public class GioHang {
    int productID;
    String name;
    long price;
    String imgSp;
    int amount;

    public GioHang() {
    }

    public GioHang(int productID, String name, long price, String imgSp, int amount) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.imgSp = imgSp;
        this.amount = amount;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImgSp() {
        return imgSp;
    }

    public void setImgSp(String imgSp) {
        this.imgSp = imgSp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
