package com.ph41626.and103_lab5.Model;

import java.io.Serializable;

public class Fruit implements Serializable {
    private String _id;
    private String name;
    private int quantity;
    private int price;
    private int status;
    private String thumbnail;
    private String description;
    private String id_distributor;

    public Fruit() {
    }

    public Fruit(String _id, String name, int quantity, int price, int status, String thumbnail, String description, String id_distributor) {
        this._id = _id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.thumbnail = thumbnail;
        this.description = description;
        this.id_distributor = id_distributor;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId_distributor() {
        return id_distributor;
    }

    public void setId_distributor(String id_distributor) {
        this.id_distributor = id_distributor;
    }
}
