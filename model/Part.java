/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danielhowardinventorysystem.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dhoward
 */
abstract public class Part {
    private final SimpleIntegerProperty PartID = new SimpleIntegerProperty(0);
    private final SimpleStringProperty Name = new SimpleStringProperty("");
    private final SimpleDoubleProperty Price = new SimpleDoubleProperty(0.0);
    private final SimpleIntegerProperty InStock = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty Min = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty Max = new SimpleIntegerProperty(0);
    
    public final void setName(String name) {
        Name.set(name);
    }
    
    public final String getName() {
        return Name.get();
    }
    
    public final void setPrice(double price) {
        Price.set(price);
    }
    
    public final double getPrice() {
        return Price.get();
    }
    
    public final void setInStock(int inStock) {
        InStock.set(inStock);
    }
    
    public final int getInStock() {
        return InStock.get();
    }
    
    public final void setMin(int min) {
        Min.set(min);
    }
    
    public final int getMin() {
        return Min.get();
    }
    
    public final void setMax(int max) {
        Max.set(max);
    }
    
    public final int getMax() {
        return Max.get();
    }
    
    public final void setPartID(int partID) {
        PartID.set(partID);
    }
    
    public final int getPartID(){
        return PartID.get();
    }
    
}
