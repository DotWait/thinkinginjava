package com.dotwait.check;

public class LayerToFieldValue {
    private String layer;
    private Object value;

    public LayerToFieldValue() {
    }

    public LayerToFieldValue(String layer, Object value) {
        this.layer = layer;
        this.value = value;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
