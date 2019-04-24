/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danielhowardinventorysystem.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dhoward
 */
public class Product {
    public ObservableList<Part> AssociatedParts = FXCollections.observableArrayList();
    private SimpleIntegerProperty ProductID = new SimpleIntegerProperty(0);
    private SimpleStringProperty Name = new SimpleStringProperty("");
    private SimpleDoubleProperty Price = new SimpleDoubleProperty(0.0);
    private SimpleIntegerProperty InStock = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty Min = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty Max = new SimpleIntegerProperty(0);
    
    public Product(String productName, double price, int inStock, int min, int max) {
        this.setName(productName);
        this.setPrice(price);
        this.setInStock(inStock);
        this.setMin(min);
        this.setMax(max);
    }
    
    public Product() {
        this.setProductID(0);
        this.setName("");
        this.setPrice(0.0);
        this.setInStock(0);
        this.setMin(0);
        this.setMax(0);
    }
    
    public void setName(String name) {
        Name.set(name);
    }
    
    public String getName() {
        return Name.get();
    }
    
    public void setPrice(double price) {
        Price.set(price);
    }
    
    public double getPrice() {
        return Price.get();
    }
    
    public void setInStock(int inStock) {
        InStock.set(inStock);
    }
    
    public int getInStock() {
        return InStock.get();
    }
    
    public void setMin(int min) {
        Min.set(min);
    }
    
    public int getMin() {
        return Min.get();
    }
    
    public void setMax(int max) {
        Max.set(max);
    }
    
    public int getMax() {
        return Max.get();
    }
    
    public void addAssociatedPart(Part part) {
        this.AssociatedParts.add(part);
    }
    
    public boolean removeAssociatedPart(int partID) {
        for(Part part : AssociatedParts) {
            if(part.getPartID() == partID) {
                AssociatedParts.remove(part);
                return true;
            }
        }
        return false;
    }
    
    public Part lookupAssociatedPart(int partID) {
        for(Part part : AssociatedParts) {
            if(part.getPartID() == partID) {
                return part;
            }
        }
        return null;
    }
    
    //returns true if there are associated part entries
    public boolean hasAssociatedParts() {
        System.err.println(AssociatedParts.isEmpty());
        if(AssociatedParts.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public void setProductID(int productID) {
        ProductID.set(productID);
    }
    
    public int getProductID() {
        return ProductID.get();
    }
   
/*  //alternative remove code in case we can't remove via the index of the Part object
    public void removeAssociatedPart(int partID) {
        AssociatedParts.removeIf((Part part) -> part.getPartID() == partID);
}*/
    
    
    
}
