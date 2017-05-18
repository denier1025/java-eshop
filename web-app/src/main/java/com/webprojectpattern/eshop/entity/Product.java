package com.webprojectpattern.eshop.entity;

import java.io.FileInputStream;
import java.io.InputStream;

public class Product {

    private int id;
    private int groupId;
    private String productName;
    private int count;
    private String color;
    private String size;
    private int price;
    private String description;
    private boolean blocked = false;
    private InputStream inputStream;
    private String base64DataString;
    private byte[] bytes;

    public Product() {
    }

    public Product(String productName, int price) {
        this.productName = productName;
        this.price = price;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getBase64DataString() {
        return base64DataString;
    }

    public void setBase64DataString(String base64DataString) {
        this.base64DataString = base64DataString;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return productName + ", price = " + price;
    }
}
