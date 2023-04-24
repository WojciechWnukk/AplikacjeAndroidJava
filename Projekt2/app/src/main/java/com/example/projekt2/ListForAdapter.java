package com.example.projekt2;

public class ListForAdapter {
    private String model;
    private String producer;

    public ListForAdapter(String model, String producer) {
        this.model = model;
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }
}
