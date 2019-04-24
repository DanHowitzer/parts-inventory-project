/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danielhowardinventorysystem.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dhoward
 */
public class OutsourcedPart extends Part {
    private final SimpleStringProperty CompanyName = new SimpleStringProperty("");
    
    public OutsourcedPart(String name, double price, int inStock, int min, int max, String companyName) {
        setName(name);
        setPrice(price);
        setInStock(inStock);
        setMin(min);
        setMax(max);
        setCompanyName(companyName);
        
    }
    
    public final void setCompanyName(String companyName) {
        CompanyName.set(companyName);
    }
    
    public final String getCompanyName(){
        return CompanyName.get();
    }    
    

}
