/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danielhowardinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author dhoward
 */
public class Inventory {
    public static ObservableList<Product> Products = FXCollections.observableArrayList();
    public static ObservableList<Part> AllParts = FXCollections.observableArrayList();
    
    public static FilteredList<Product> filteredProducts = new FilteredList<>(Inventory.Products, p -> true);
    public static FilteredList<Part> filteredAllParts = new FilteredList<>(Inventory.AllParts, p -> true);
    
    //Automatically generate productid and partid starting from 1 and incrementing as each product is added
    private static int nextProductID = 0;
    private static int nextPartID = 0;
    
    //Increments nextProductID by 1 so the next product will get a unique productid
    private static void incrementProductID() {
        nextProductID++;
    }
    
    //Increments nextPartID by 1 so the next part will get a unique partID
    private static void incrementPartID() {
        nextPartID++;
    }
    
    //Adds a passed product to the Products arraylist
    public static void addProduct(Product product) {
        incrementProductID();
        product.setProductID(nextProductID);
        Products.add(product);
    }
    
    //Finds and removes a product from Products based on passed ProductID
    public static boolean removeProduct(int productID) {
        for(Product product : Products) {
            if(product.getProductID() == productID) {
                Products.remove(product);
                return true;
            }
        }
        return false;
    }
    
    //Finds and returns a product based on ProductID passed
    public static Product lookupProduct(int productID) {
        for(Product product: Products) {
            if(product.getProductID() == productID) {
                return product;
            }
        }
        return null;
    }
    
    public static void updateProduct(int productID) {
        //does nothing
    }
    
    public static void updateProduct(Product updatedproduct) {
    //finds the product in the array with the same productid
    //updates it to the new product
        for(Product product: Products) {
            if(product.getProductID() == updatedproduct.getProductID()) {
                //get the index of the existing product with the same productid
                int index = Products.indexOf(product);
                //update that product to the new version that was passed
                Products.set(index, updatedproduct);
            }
        }
    }
    
    //Adds a passed Part to the AllParts arraylist
    public static void addPart(Part part) {
        incrementPartID();
        part.setPartID(nextPartID);
        AllParts.add(part);
    }
    
    //Finds and deletes a part from the AllParts arraylist using the passed partID
    public static boolean deletePart(int partID) {
        for(Part part: AllParts) {
            if(part.getPartID() == partID) {
                AllParts.remove(part);
                return true;
            }
        }
        return false;
    }
    
    //Finds and returns a Part from AllParts arraylist based on passed partID
    public static Part lookupPart(int partID) {
        for(Part part: AllParts) {
            if(part.getPartID() == partID) {
                return part;
            }
        }
        return null;
    }
    
    public static void updatePart(int partID) {
        //does nothing
    }
    
    //update inhouse part
    public static void updatePart(int partid, String partname, double price, int inv, int min, int max, int machineid) {
        for(Part part: AllParts) {
            if(part.getPartID() == partid) {
                InhousePart inhousepart = (InhousePart) part;
                inhousepart.setName(partname);
                inhousepart.setPrice(price);
                inhousepart.setInStock(inv);
                inhousepart.setMin(min);
                inhousepart.setMax(max);
                inhousepart.setMachineID(machineid);
            }
        }
    }
    
    //update outsource part
    public static void updatePart(int partid, String partname, double price, int inv, int min, int max, String companyname) {
        for(Part part: AllParts) {
            if(part.getPartID() == partid) {
                OutsourcedPart outsourcepart = (OutsourcedPart) part;
                outsourcepart.setName(partname);
                outsourcepart.setPrice(price);
                outsourcepart.setInStock(inv);
                outsourcepart.setMin(min);
                outsourcepart.setMax(max);
                outsourcepart.setCompanyName(companyname);
            }
        }
    }
}
