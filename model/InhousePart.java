/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danielhowardinventorysystem.model;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author dhoward
 */
public class InhousePart extends Part {
    private final SimpleIntegerProperty MachineID = new SimpleIntegerProperty(0);
    
    
    public InhousePart(String name, double price, int inStock, int min, int max, int machineID) {
        setName(name);
        setPrice(price);
        setInStock(inStock);
        setMin(min);
        setMax(max);
        setMachineID(machineID);
    }
    
    public final void setMachineID(int machineID) {
        MachineID.set(machineID);
    }
    
    public final int getMachineID(){
        return MachineID.get();
    }
}
