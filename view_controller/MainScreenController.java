package danielhowardinventorysystem.view_controller;

import danielhowardinventorysystem.model.InhousePart;
import danielhowardinventorysystem.model.Inventory;
import danielhowardinventorysystem.model.OutsourcedPart;
import danielhowardinventorysystem.model.Part;
import danielhowardinventorysystem.model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainScreenController implements Initializable{

    
    @FXML
    private Text title;
    
    @FXML
    private Button exitButton;

    @FXML
    private Button partsSearchButton;

    @FXML
    private TextField partsSearchBar;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partsTablePartID;

    @FXML
    private TableColumn<Part, String> partsTablePartName;

    @FXML
    private TableColumn<Part, Integer> partsTableInventoryLevel;

    @FXML
    private TableColumn<Part, Double> partsTablePrice;

    @FXML
    private Button partsAddButton;

    @FXML
    private Button partsModifyButton;

    @FXML
    private Button partsDeleteButton;

    @FXML
    private Button productsSearchButton;

    @FXML
    private TextField productsSearchBar;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> productsTableProductID;

    @FXML
    private TableColumn<Product, String> productsTableProductName;

    @FXML
    private TableColumn<Product, Integer> productsTableInventoryLevel;

    @FXML
    private TableColumn<Product, Double> productsTableCost;

    @FXML
    private Button productsAddButton;

    @FXML
    private Button productsModifyButton;

    @FXML
    private Button productsDeleteButton;
    
    static boolean productsEntered;
    static boolean partsEntered;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.err.println("initializing main screen");
        
        //add sample parts
        if(!partsEntered) {
            Inventory.addPart(new InhousePart("Maple Icing",0.5,100,1,1,1));
            Inventory.addPart(new InhousePart("Bacon",1,20,1,1,2));
            Inventory.addPart(new InhousePart("Crushed Cookies",.25,100,1,1,3));
            partsEntered = true;
        }
        
        //add sample products
        //french cruller has no associated parts so can be deleted
        //others have associated parts so cannot be deleted
        if(!productsEntered) {
            Inventory.addProduct(new Product("French Cruller",1.99,1,0,12));
            Inventory.addProduct(new Product("Maple Bar",1.49,5,0,12));
            Inventory.addProduct(new Product("Bacon Maple Bar",2.49,10,0,12));
            
            Inventory.lookupProduct(2).addAssociatedPart(Inventory.lookupPart(1));
            Inventory.lookupProduct(3).addAssociatedPart(Inventory.lookupPart(1));
            Inventory.lookupProduct(3).addAssociatedPart(Inventory.lookupPart(2));
            
            productsEntered=true;
        }

        
        //set up parts table view
        partsTablePartID.setCellValueFactory(new PropertyValueFactory<>("PartID"));
        partsTablePartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        partsTableInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("InStock"));
        partsTablePrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        partsTable.setItems(Inventory.filteredAllParts);
      
        //if there is nothing in the search field, unfilter it
        //that way if the user clears the search field, all parts are again displayed
        this.partsSearchBar.textProperty().addListener((Observable, oldValue, newValue) -> {
            if(newValue == null || newValue.isEmpty()){
                Inventory.filteredAllParts.setPredicate(part -> true);
            }
            else {
                //do nothing
            }
        });
        
        //set up products table view
        productsTableProductID.setCellValueFactory(new PropertyValueFactory<>("ProductID"));
        productsTableProductName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        productsTableInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("InStock"));
        productsTableCost.setCellValueFactory(new PropertyValueFactory<>("Price"));
        productsTable.setItems(Inventory.Products);
        
        //if there is nothing in the search field, unfilter it
        //that way if the user clears the search field, all parts are again displayed
        this.productsSearchBar.textProperty().addListener((Observable, oldValue, newValue) -> {
            if(newValue == null || newValue.isEmpty()){
                Inventory.filteredProducts.setPredicate(product -> true);
            }
            else {
                //do nothing
            }
        });

    }


    @FXML
    void handleAddPartsButtonAction(ActionEvent event) throws IOException {
        //opens add / modify in-house parts screen with title corresponding to the user clicking "Add"
        Stage stage;
        Parent root;
        stage=(Stage) partsAddButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddModifyInHousePartScreen.fxml"));
        root = loader.load();
        AddModifyInHousePartScreenController partScreenController = loader.getController();
        //sets the title of the next screen to "Add Part"
        partScreenController.isAdd(true);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); 
    }

    @FXML
    void handleAddProductsButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage=(Stage) this.productsAddButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddModifyProductScreen.fxml"));
        root = loader.load();
        AddModifyProductScreenController controller = loader.getController();
        controller.isAdd(true);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleDeletePartsButtonAction(ActionEvent event) {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        //see if a part is selected, if so delete it
        if(part != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this part?");
            
            //ask user to confirm whether they want to delete
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Inventory.deletePart(part.getPartID());
            } else {
                //do nothing
            }

        }
        else {
            //do nothing
        }
    }

    @FXML
    void handleDeleteProductsButtonAction(ActionEvent event) {
        //pass productid of selected product to Inventory to delete product
        Product product = productsTable.getSelectionModel().getSelectedItem();
        //if no part is selected, do nothing
        if(product != null && !product.hasAssociatedParts()){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this product?");
            
            //ask user to confirm whether they want to delete
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Inventory.removeProduct(product.getProductID());
            } else {
                //do nothing
            }

        }
        else if(product != null && product.hasAssociatedParts()) {
            //show error if there is an associated part attached to this product
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Delete Product Error");
            alert.setContentText("Products with associated parts cannot be deleted!");
            alert.showAndWait();
        }
        else {
            //do nothing
        }

    }

    @FXML
    void handleExitButtonAction(ActionEvent event) {
        //Exits from program by closing the stage
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleModifyPartsButtonAction(ActionEvent event) throws IOException {
        //opens add / modify in-house parts screen with title corresponding to the user clicking "Modify"
        //if the part is outsourced, open the outsource part screen, otherwise open the in house part screen
        //load part information into new screen
        Part part = partsTable.getSelectionModel().getSelectedItem();
        if(part != null && part instanceof InhousePart) {
            //set part object as inhousepart
            InhousePart inhousepart = (InhousePart) part;       
            
            Stage stage;
            Parent root;
            stage=(Stage) partsModifyButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddModifyInHousePartScreen.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); 
            AddModifyInHousePartScreenController partScreenController = loader.getController();
            //send inhousepart to inhouse screen
            partScreenController.modify(inhousepart);
            
        }
        else if(part != null && part instanceof OutsourcedPart) {
            //set part object as inhousepart
            OutsourcedPart outsourcedpart = (OutsourcedPart) part;       
            
            Stage stage;
            Parent root;
            stage=(Stage) partsModifyButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddModifyOutsourcePartScreen.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); 
            AddModifyOutsourcePartScreenController partScreenController = loader.getController();
            //send inhousepart to inhouse screen
            partScreenController.modify(outsourcedpart);        
        }
        else {
            //TODO: display message to user saying they need to select a part in order to modify
        }

    }

    @FXML
    void handleModifyProductsButtonAction(ActionEvent event) throws IOException {
        Product product = productsTable.getSelectionModel().getSelectedItem();
        
        //if no product is selected, do nothing
        //otherwise pass this product to the modify product screen
        if(product != null) {
            Stage stage;
            Parent root;
            stage=(Stage) productsModifyButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddModifyProductScreen.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); 
            AddModifyProductScreenController controller = loader.getController();
            //pass selected product to next screen
            controller.modify(product);
        }
        else {
            //do nothing
        }
    }

    @FXML
    void handlePartsSearchButtonAction(ActionEvent event) {
        //implement search button by filtering list based on user input
        //if a user inputs an int, then search via partID
        //if a user inputs a string, search the part name
        String newValue = this.partsSearchBar.getText();
        Inventory.filteredAllParts.setPredicate(part -> {
            //if search field is blank, unfilter list
            if(newValue == null || newValue.isEmpty()){
                return true;
            }

            //test whether input is an int, if so, filter by productID
            try{
                int productid = Integer.parseInt(newValue);
                if(part.getPartID() == productid) {
                    return true;
                }
            }
            catch(NumberFormatException e) {

            }

            //put user input into lowercase
            String lowerCaseFilter = newValue.toLowerCase();

            //match user input to part names
            if(part.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });

        //wrap filtered list in sorted list
        SortedList<Part> sortedAllParts = new SortedList<>(Inventory.filteredAllParts);
        
        //bind sorted list to table view
        sortedAllParts.comparatorProperty().bind(partsTable.comparatorProperty());
        
        //add sorted data to table view
        partsTable.setItems(sortedAllParts);
    }

    @FXML
    void handleSearchProductsButtonAction(ActionEvent event) {
        //implement search button by filtering list based on user input
        //if a user inputs an int, then search via partID
        //if a user inputs a string, search the part name
        String newValue = this.productsSearchBar.getText();
        

        
        Inventory.filteredProducts.setPredicate(product -> {
            //if search field is blank, unfilter list
            if(newValue == null || newValue.isEmpty()){
                return true;
            }

            //test whether input is an int, if so, filter by productID
            try{
                int productid = Integer.parseInt(newValue);
                if(product.getProductID() == productid) {
                    return true;
                }
            }
            catch(NumberFormatException e) {

            }

            //put user input into lowercase
            String lowerCaseFilter = newValue.toLowerCase();

            //match user input to part names
            if(product.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });

        //wrap filtered list in sorted list
        SortedList<Product> sortedProducts = new SortedList<>(Inventory.filteredProducts);
        
        //bind sorted list to table view
        sortedProducts.comparatorProperty().bind(productsTable.comparatorProperty());
        
        //add sorted data to table view
        productsTable.setItems(sortedProducts);
    }

}
