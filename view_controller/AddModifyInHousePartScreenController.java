package danielhowardinventorysystem.view_controller;

import danielhowardinventorysystem.model.InhousePart;
import danielhowardinventorysystem.model.Inventory;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddModifyInHousePartScreenController {

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
    private TextField machineIDField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    
    private boolean add;
    
    //if add = true then the save button will add a new product
    //if add is false then save button will update a product
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
    void handleOutsourcedButtonAction(ActionEvent event) throws IOException {
        //loads the outsource version of menu if we're not modifying
        //do nothing if we're modifying as the user can't change an inhouse part to an outsource part
        //tells the outsource menu whether it should be an add or a modify menu
        if(add) {
            Stage stage;
            Parent root;
            stage=(Stage) this.radioOutsourced.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddModifyOutsourcePartScreen.fxml"));
            root = loader.load();
            AddModifyOutsourcePartScreenController outsourcePartScreenController = loader.getController();

            //if this is an add screen, make sure outsource screen is also an add screen, otherwise it's a modify screen
            //copy any data entered into outsource screen so user doesn't lose their stuff
            outsourcePartScreenController.isAdd(true);
            outsourcePartScreenController.setAddScreen(this.partNameField.getText(), this.priceCostField.getText(), this.invField.getText(), this.minField.getText(), this.maxField.getText());

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();    
        }
        else {
            //if the user is modifying an inhouse product, do not allow them to switch to the modify outsource screen
            //switch the radio buttons back to their original position
            this.radioOutsourced.setSelected(false);
            this.radioInhouse.setSelected(true);
        }
    }

    @FXML
    void handleSaveButtonAction(ActionEvent event) throws IOException {
        
        //check for errors before adding new part
        //start building error message string
        StringBuilder errorMessage = new StringBuilder();
        //boolean stores wheter we receive an error during check
        boolean error = false;
        
        int inv;
        
        try {
            inv = Integer.parseInt(invField.getText());
        }
        catch(NumberFormatException e) {
            errorMessage.append("Part must have a valid inv value.\n");
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
        
        int machineid;

        try {
            machineid = Integer.parseInt(machineIDField.getText());
        }
        catch(NumberFormatException e) {
            errorMessage.append("Invalid machineID value.\n");
            machineid = 0;
            error = true;
        }        
        
        //when the user saves, add part to inventory 
        String partname = partNameField.getText();
        
        
        double price;
        
        //ensure price is filled in with a valid entry
        try{
            price = Double.parseDouble(priceCostField.getText());
        }
        catch(NumberFormatException e) {
            errorMessage.append("Part must have a valid price.\n");
            price = 0.0;
            error = true;
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
        if(partname.isEmpty()) {
            errorMessage.append("The part must have a name.\n");
            error = true;
        }

       if(error) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Errors in user input");
            alert.setContentText("Errors must be corrected before part can be saved.");

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
       else {
            //if this is an add screen, add the part
            //else update the part with the new input
            if(this.add) {
            //create in house part based on user input
            InhousePart part = new InhousePart(partname,price,inv,min,max,machineid);
            //add part to inventory
            Inventory.addPart(part);
            }
            else {
                int partid;
                partid = Integer.parseInt(idField.getText());
                Inventory.updatePart(partid,partname,price,inv,min,max,machineid);
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
    }
    
    //set up fields based on inhousepart passed by main screen
    public void modify(InhousePart inhousepart) {
        this.idField.setText(Integer.toString(inhousepart.getPartID()));
        this.partNameField.setText(inhousepart.getName());
        this.invField.setText(Integer.toString(inhousepart.getInStock()));
        this.priceCostField.setText(Double.toString(inhousepart.getPrice()));
        this.maxField.setText(Integer.toString(inhousepart.getMax()));
        this.minField.setText(Integer.toString(inhousepart.getMin()));
        this.machineIDField.setText(Integer.toString(inhousepart.getMachineID()));
        
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
