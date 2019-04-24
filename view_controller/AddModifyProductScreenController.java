package danielhowardinventorysystem.view_controller;

import danielhowardinventorysystem.model.Inventory;
import danielhowardinventorysystem.model.Part;
import danielhowardinventorysystem.model.Product;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddModifyProductScreenController {

    @FXML
    private Text title;

    @FXML
    private TextField idField;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField invField;

    @FXML
    private TextField priceCostField;

    @FXML
    private TextField maxField;

    @FXML
    private TextField minField;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Part> addTable;

    @FXML
    private TableColumn<Part, Integer> addTablePartID;

    @FXML
    private TableColumn<Part, String> addTablePartname;

    @FXML
    private TableColumn<Part, Integer> addTableInventoryLevel;

    @FXML
    private TableColumn<Part, Double> addTablePricePerUnit;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Part> deleteTable;

    @FXML
    private TableColumn<Part, Integer> deleteTablePartID;

    @FXML
    private TableColumn<Part, String> deleteTablePartName;

    @FXML
    private TableColumn<Part, Integer> deleteTableInventoryLevel;

    @FXML
    private TableColumn<Part, Double> deleteTablePricePerUnit;

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    
    private boolean add;
    

    //Instantiate a product to manipulate for adding / modifying
    private Product modifiedProduct;
    
    @FXML
    public void initialize() {
        if(add) {
            modifiedProduct = new Product("",0.0,0,0,0);
        }
        //sets up AllParts table for the user to select from
        addTablePartID.setCellValueFactory(new PropertyValueFactory<>("PartID"));
        addTablePartname.setCellValueFactory(new PropertyValueFactory<>("Name"));
        addTableInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("InStock"));
        addTablePricePerUnit.setCellValueFactory(new PropertyValueFactory<>("Price"));
        addTable.setItems(Inventory.AllParts);
        
        //if there is nothing in the search field, unfilter it
        //that way if the user clears the search field, all parts are again displayed
        this.searchField.textProperty().addListener((Observable, oldValue, newValue) -> {
            if(newValue == null || newValue.isEmpty()){
                Inventory.filteredAllParts.setPredicate(part -> true);
            }
            else {
                //do nothing
            }
        });
        
    }

    
    //tells the module whether we're adding or modifying a product
    public void isAdd(boolean add) {
        if(add) {
            this.title.setText("Add Product");
            this.add = true;
            modifiedProduct = new Product();
            
            //set up part add table
            deleteTablePartID.setCellValueFactory(new PropertyValueFactory<>("PartID"));
            deleteTablePartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            deleteTableInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("InStock"));
            deleteTablePricePerUnit.setCellValueFactory(new PropertyValueFactory<>("Price"));
            deleteTable.setItems(modifiedProduct.AssociatedParts);   
        }
        else {
            this.title.setText("Modify Product");
            this.add = false;
        }
    }

    @FXML
    void handleAddButtonAction(ActionEvent event) {
        //add selected part to the modifiedproduct associated part list
        Part selectedPart = addTable.getSelectionModel().getSelectedItem();
        modifiedProduct.addAssociatedPart(selectedPart);
    }

    @FXML
    void handleCancelButtonAction(ActionEvent event) throws IOException {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Cancel Confirmation");
            alert.setContentText("Are you sure you want to cancel out of this creen?");
            
            //ask user to confirm whether they want to delete
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                //opens main screen

                Stage stage;
                Parent root;
                stage=(Stage) cancelButton.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                //do nothing
            }

    }

    @FXML
    void handleDeleteButtonAction(ActionEvent event) {
        //pass partid to modifiedpart to remove the associated part selected
        Part selectedPart = deleteTable.getSelectionModel().getSelectedItem();
        
        if(selectedPart != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this part?");
            
            //ask user to confirm whether they want to delete
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                modifiedProduct.removeAssociatedPart(selectedPart.getPartID()); 
            } else {
                //do nothing
            }
  
        }
        else {
            //do nothing
        }
    }

    @FXML
    void handleSaveButtonAction(ActionEvent event) throws IOException {
        
        //check for errors before adding new part
        //start building error message string
        StringBuilder errorMessage = new StringBuilder();
        //boolean stores wheter we receive an error during check
        boolean error = false;
        
        //copy and parse fields filled in by user
        String name = productNameField.getText();
        
        int inv;
        
        try {
            inv = Integer.parseInt(invField.getText());
        }
        catch(NumberFormatException e) {
            errorMessage.append("Product must have a valid inv value.\n");
            inv = 0;
            error = true;
        }
        
        int max;
        
        try {
            max = Integer.parseInt(maxField.getText());
        }
        catch(NumberFormatException e) {
            errorMessage.append("Invalid max value.\n");
            max = 0;
            error = true;
        }
        
        int min;
         
        try {
            min = Integer.parseInt(minField.getText());
        }
        catch(NumberFormatException e) {
            errorMessage.append("Invalid min value.\n");
            min = 0;
            error = true;
        }

        double price;
        
        //ensure price is filled in with a valid entry
        try{
            price = Double.parseDouble(priceCostField.getText());
        }
        catch(NumberFormatException e) {
            errorMessage.append("Product must have a valid price.\n");
            price = 0.0;
            error = true;
        }
        
        double costOfParts = 0.0;
        
        
        
        //check to see if there is at least one associated part
        //add up cost of parts if there are any
        if(modifiedProduct.AssociatedParts.isEmpty()) {
            errorMessage.append("Product must have at least one associated part.\n");
            error = true;
        }
        else {
            for(Part part: modifiedProduct.AssociatedParts) {
                costOfParts += part.getPrice();
            }
        }
        
        //check for errors
        //min must be less than or equal to max and max must be greater than or equal to min
        if(min > max) {
            errorMessage.append("Min value cannot be greater than Max value.\n");
            error = true;
        }
        else if(max < min) {
            errorMessage.append("Max value cannot be less than Min value.\n");
            error = true;
        }
        
        //inv must be less than or equal to max
        //inv must be greater than or equal to min
        if(inv > max) {
            errorMessage.append("Inv value cannot be greater than Max value.\n");
            error = true;
        }
        else if(inv < min) {
            errorMessage.append("Inv value cannot be less than Min value.\n");
            error = true;
        }
        
        //name must not be empty
        if(name.isEmpty()) {
            errorMessage.append("The product must have a name.\n");
            error = true;
        }
        
        //price must be greater than or equal to cost of all associated parts
        if(price < costOfParts) {
            errorMessage.append("Price of product must be equal or greater than the total cost of associated parts.\n");
            error = true;
        }
        
        //if there are any errors, display an error dialog box and do not save product
        if(error) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Errors in user input");
            alert.setContentText("Errors must be corrected before product can be saved.");

            Label label = new Label("Please correct the following errors:");

            TextArea textArea = new TextArea(errorMessage.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }
        //if there are no errors, continue saving the product
        else {
            modifiedProduct.setName(productNameField.getText());
            modifiedProduct.setInStock(Integer.parseInt(invField.getText()));
            modifiedProduct.setPrice(Double.parseDouble(priceCostField.getText()));
            modifiedProduct.setMax(Integer.parseInt(maxField.getText()));
            modifiedProduct.setMin(Integer.parseInt(minField.getText()));

            //if we're on the add product screen, add the product
            if(add) {
                Inventory.addProduct(modifiedProduct);
            }
            else {
                Inventory.updateProduct(modifiedProduct);
                System.err.println("Saving modified product min " + modifiedProduct.getMin());
            }

            //opens main screen
            Stage stage;
            Parent root;
            stage=(Stage) saveButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
        }
    }

    @FXML
    void handleSearchButtonAction(ActionEvent event) {
         //implement search button by filtering list based on user input
        //if a user inputs an int, then search via partID
        //if a user inputs a string, search the part name
        String newValue = this.searchField.getText();
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
        sortedAllParts.comparatorProperty().bind(this.addTable.comparatorProperty());
        
        //add sorted data to table view
        addTable.setItems(sortedAllParts);
    }
    
    @FXML
    public void modify(Product productToModify) {
        //set up local copy of product to modify
        modifiedProduct = productToModify;
        
        //set up screen for modifying
        isAdd(false);
        
        //set up product fields for user modification
        this.idField.setText(Integer.toString(modifiedProduct.getProductID()));
        this.productNameField.setText(modifiedProduct.getName());
        this.invField.setText(Integer.toString(modifiedProduct.getInStock()));
        this.priceCostField.setText(Double.toString(modifiedProduct.getPrice()));
        this.maxField.setText(Integer.toString(modifiedProduct.getMax()));
        this.minField.setText(Integer.toString(modifiedProduct.getMin()));
        
        deleteTablePartID.setCellValueFactory(new PropertyValueFactory<>("PartID"));
        deleteTablePartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        deleteTableInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("InStock"));
        deleteTablePricePerUnit.setCellValueFactory(new PropertyValueFactory<>("Price"));
        deleteTable.setItems(modifiedProduct.AssociatedParts);         
    }
    
    

}
