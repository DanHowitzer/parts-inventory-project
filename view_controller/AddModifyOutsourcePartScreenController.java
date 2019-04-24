package danielhowardinventorysystem.view_controller;

import danielhowardinventorysystem.model.Inventory;
import danielhowardinventorysystem.model.OutsourcedPart;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddModifyOutsourcePartScreenController {

    @FXML
    private Text title;

    @FXML
    private RadioButton radioInhouse;

    @FXML
    private ToggleGroup AddPartRadio;

    @FXML
    private RadioButton radioOutsourced;

    @FXML
    private Label companyNameLabel;

    @FXML
    private TextField idField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField invField;

    @FXML
    private TextField priceCostField;

    @FXML
    private TextField maxField;

    @FXML
    private TextField minField;

    @FXML
    private TextField companyNameField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    
    private boolean add;
    
    public void isAdd(boolean add) {
        if(add) {
            this.title.setText("Add Part");
            this.add = true;            
        }
        else {
            this.title.setText("Modify Part");
            this.add = false;
        }
    }

    @FXML
    void handleCancelButtonAction(ActionEvent event) throws IOException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
    void handleInHouseButtonAction(ActionEvent event) throws IOException {
        //loads the in house version of menu if we're not modifying
        //do nothing if we're modifying as the user can't change an outsource part to an in house part
        //tells the outsource menu whether it should be an add or a modify menu
        if(add) {
            Stage stage;
            Parent root;
            stage=(Stage) this.radioInhouse.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddModifyInHousePartScreen.fxml"));
            root = loader.load();
            AddModifyInHousePartScreenController inhousePartScreenController = loader.getController();

            //if this is an add screen, make sure outsource screen is also an add screen, otherwise it's a modify screen
            //copy any data entered into outsource screen so user doesn't lose their stuff
            inhousePartScreenController.isAdd(true);
            inhousePartScreenController.setAddScreen(this.partNameField.getText(), this.priceCostField.getText(), this.invField.getText(), this.minField.getText(), this.maxField.getText());

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();    
        }
        else {
            //if the user is modifying an inhouse product, do not allow them to switch to the modify outsource screen
            //switch the radio buttons back to their original position
            this.radioOutsourced.setSelected(true);
            this.radioInhouse.setSelected(false);
        } 
    }

    @FXML
    void handleSaveButtonAction(ActionEvent event) throws IOException {
        //when the user saves, add part to inventory 
        String partname = partNameField.getText();
        double price = Double.parseDouble(priceCostField.getText());
        int instock = Integer.parseInt(invField.getText());
        int min = Integer.parseInt(minField.getText());
        int max = Integer.parseInt(maxField.getText());
        String companyname = companyNameField.getText();
        
        //if this is an add screen, add the part
        //else update the part with the new input
        if(this.add) {
            //create an outsourced part based on user input
            OutsourcedPart part = new OutsourcedPart(partname,price,instock,min,max,companyname);
            //add part to inventory
            Inventory.addPart(part);
        }
        else {
            int partid;
            partid = Integer.parseInt(idField.getText());
            Inventory.updatePart(partid,partname,price,instock,min,max,companyname);
        }
        
        //open the main screen
        Stage stage;
        Parent root;
        stage=(Stage) saveButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); 
    }
    
    //set up fields based on outsourcepart passed by main screen
    public void modify(OutsourcedPart outsourcepart) {
        this.idField.setText(Integer.toString(outsourcepart.getPartID()));
        this.partNameField.setText(outsourcepart.getName());
        this.invField.setText(Integer.toString(outsourcepart.getInStock()));
        this.priceCostField.setText(Double.toString(outsourcepart.getPrice()));
        this.maxField.setText(Integer.toString(outsourcepart.getMax()));
        this.minField.setText(Integer.toString(outsourcepart.getMin()));
        this.companyNameField.setText(outsourcepart.getName());
        
        isAdd(false);
    }
    
    //sets up add screen if user switched from outsourced to inhouse in the middle of entering data
    public void setAddScreen(String partName, String price, String inv, String min, String max) {
        this.partNameField.setText(partName);
        this.priceCostField.setText(price);
        this.invField.setText(inv);
        this.maxField.setText(max);
        this.minField.setText(min);
    }

}
