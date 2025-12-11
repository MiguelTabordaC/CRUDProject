
/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoCRUD.ui;

import java.awt.Desktop.Action;

import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.lang.Exception;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import proyectoCRUD.logic.CustomerRESTClient;
import proyectoCRUD.model.Customer;
/**
 *
 * @author david
 */
public class SignUpController {
    int f;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfSurName;
    @FXML
    private TextField tfMidInit;
    @FXML
    private TextField tfState;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfAdress;
    @FXML
    private TextField tfZip;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassw;
    @FXML
    private PasswordField pfConfPassw;
    @FXML
    private Button bbutton;
    @FXML
    private Hyperlink hlhyperlink;
    @FXML
    private Label errorLabelName;
    @FXML
    private Label errorLabelSurName;
    @FXML
    private Label errorLabelState;
    @FXML
    private Label errorLabelStreet;
    @FXML
    private Label errorLabelCity;
    @FXML
    private Label errorLabelEmail;
    @FXML
    private Label errorLabelPhone;
    @FXML
    private Label errorLabelPassw;
    @FXML
    private Label errorLabelConfPassw;
    //Logger for notifying errors
    private static final Logger LOGGER=Logger.getLogger("projectinterfaces.ui");

    private Stage stage;

    //Variables used for validating the textField
    boolean fieldNameValid = false;
    boolean fieldSurNameValid = false;
    boolean fieldMIValid = false;
    boolean fieldStateValid = false;
    boolean fieldCityValid = false;
    boolean fieldAdressValid = false;
    boolean fieldZIPValid = false;
    boolean fieldPhoneValid = false;
    boolean fieldEmailValid = false;
    boolean fieldPasswValid = false;
    boolean fieldConfPasswValid = false;

    public void init(Stage stage, Parent root) {
        
        //Logger saying the window has been initialized
        LOGGER.info("Initializing window");
        
        //Initializing the window
        Scene scene = new Scene(root);
        stage.setScene(scene);

        this.stage=stage;
        stage.setTitle("Sign Up");
        
        

        stage.setResizable(false);
        bbutton.setDisable(true);
        
        
        //On click button handler
        bbutton.setOnAction(this::handleBtRegistrarOnAction);

        hlhyperlink.setOnAction(this::handleHyperlinkOnAction);

        //All the change focus handlers for textFields
        tfName.focusedProperty().addListener(this::handleNameFocusChange);
        tfSurName.focusedProperty().addListener(this::handleSurNameFocusChange);
        tfMidInit.focusedProperty().addListener(this::handleMIFocusChange);
        tfState.focusedProperty().addListener(this::handleStateFocusChange);
        tfCity.focusedProperty().addListener(this::handleCityFocusChange);
        tfAdress.focusedProperty().addListener(this::handleAdressFocusChange);
        tfZip.focusedProperty().addListener(this::handleZIPFocusChange);
        tfPhone.focusedProperty().addListener(this::handlePhoneFocusChange);
        tfEmail.focusedProperty().addListener(this::handleEmailFocusChange);
        pfPassw.focusedProperty().addListener(this::handlePasswFocusChange);
        pfConfPassw.textProperty().addListener(this::handleConfirmPasswTextChange);

       

        
                
        stage.show();
        
    }
    /**
     * Handler for the register button 
     * @param onclick 
     */
    private void handleBtRegistrarOnAction(ActionEvent event){
        try{
            //Validation for textfield name
            if(tfName.getText().isEmpty() || tfName.getText().length()>30 || !tfName.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                throw new Exception("Name field not valid");
            }
            //Validation for textField surname
            if(tfSurName.getText().isEmpty() || tfSurName.getText().length()>30 || !tfSurName.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                throw new Exception("Surname field not valid");
            }
            //Validation for textField middle initial
            if(tfMidInit.getText().isEmpty() || tfMidInit.getText().length()>1 || !tfMidInit.getText().matches("[A-Z]+")){
                throw new Exception("Middle initial field not valid");
            }
            //Validation for textField State
            if(tfState.getText().isEmpty() || tfState.getText().length()>30 || !tfState.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                throw new Exception("State field not valid");
            }
            //Validation for textfield city
            if(tfCity.getText().isEmpty() || tfCity.getText().length()>30 || !tfCity.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                throw new Exception("City field not valid");
            }
            //Validation for textfield adress
            if(tfAdress.getText().isEmpty() || tfAdress.getText().length()>30 || !tfAdress.getText().matches("[0-9a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                throw new Exception("City field not valid");
            }
            //Validation for textfield zip
            if(tfZip.getText().isEmpty() || tfZip.getText().length()>5 || tfZip.getText().length()<5 || !tfZip.getText().matches("[0-9]+")){
                throw new Exception("Zip field not valid");
            }
            //Validation for textfield phone
            if(!tfPhone.getText().matches("[0-9 +]+") || tfPhone.getText().length()>15 || tfPhone.getText().isEmpty()){
                throw new Exception("Phone field not valid");
            }
            //Validation for textfield email
            if(!tfEmail.getText().matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || tfEmail.getText().isEmpty() || tfEmail.getText().length()>100){
                throw new Exception("Email field not valid");
            }
            //Validation for passwordfield password
            if(pfPassw.getText().isEmpty() || !pfPassw.getText().matches("^(?=.*[A-Z])(?=.*\\d).{5,30}$")){
                throw new Exception("Password field not valid");
            }
            //Validation for passwordfield confirm password
            if(pfConfPassw.getText().isEmpty() || !pfConfPassw.getText().matches("^(?=.*[A-Z])(?=.*\\d).{5,30}$") || !pfConfPassw.getText().equals(pfPassw.getText())){
                throw new Exception("Password field not valid");
            }
            //Setting all the values to the object customer
            Customer customer = new Customer();
            customer.setFirstName(tfName.getText());
            customer.setLastName(tfSurName.getText());
            customer.setMiddleInitial(tfMidInit.getText()+".");
            customer.setState(tfState.getText());
            customer.setCity(tfCity.getText());
            customer.setStreet(tfAdress.getText());
            customer.setZip(Integer.parseInt(tfZip.getText()));
            customer.setPhone(Long.parseLong(tfPhone.getText()));
            customer.setEmail(tfEmail.getText());
            customer.setPassword(pfConfPassw.getText());
            //adding the customer to the database
            CustomerRESTClient client = new CustomerRESTClient();
            client.create_XML(customer);
            client.close();
            //Alert showing it went through
            new Alert(AlertType.INFORMATION,"Succesfully registered!").showAndWait();

            LOGGER.info("Customer created successfuly");
            
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("ProyectoSignIn.fxml"));
            Parent root = loader.load();
            
            ProjectInterfacesController controller = loader.getController();
            controller.init(this.stage,root);
        }
        //Catch server error 500
        catch(InternalServerErrorException e){
            new Alert(AlertType.INFORMATION,"Internal server error,\n"+"please wait or contact your service provider").showAndWait();
            LOGGER.warning("Internal server error");
            

        }
        //Catch same email being used
        catch(ForbiddenException e){
            new Alert(AlertType.INFORMATION,"Introduced Email already exist, use another").showAndWait();

            LOGGER.info("Email introduced already exists");

        }
        //Catch any error on validation
        catch(Exception e){
            new Alert(AlertType.INFORMATION,e.getMessage()).showAndWait();

            LOGGER.info(e.getMessage());
        }
        

    }
    /**
     * Method for validating the textField name
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleNameFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
        if(oldValue){
            
                String text = tfName.getText();
                boolean valid = text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
                //Checks if its empty
                if(text.isEmpty()){
                    throw new Exception ("Name field empty");
                }
                //Checks the length of the text introduced
                if(text.length()>30){
                    throw new Exception("Name text too long");
                }
                //Check if it has invalid characters
                if (!valid){
                    throw new Exception("Name format not valid");
                }
                //Validates the field
                tfName.setStyle("-fx-border-color:green");
                errorLabelName.setText("");
                fieldNameValid= true;
                validateAllFields();
            }
        
        }
        //Catches the error and writes it in a label
        catch (Exception e){
            tfName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelName.setText(e.getMessage());
            errorLabelName.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
    }
    
    /**
     * Method for validating the textfield surname
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleSurNameFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue){
                String text = tfSurName.getText();
                boolean valid = text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");

                //Checks if its empty

                if(text.isEmpty()){
                    throw new Exception ("Surname field empty");
                }
                //Validates the length of the text
                if(text.length()>30){
                    throw new Exception("Surname text too long");
                }
                //validates if it has the correct format
                if (!valid){
                    throw new Exception("Surname format not valid");
                }
                tfSurName.setStyle("-fx-border-color:green"); 
                errorLabelSurName.setText("");
                fieldSurNameValid=true;
                validateAllFields();
            }
        }
        catch (Exception e){
            tfSurName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelSurName.setText(e.getMessage());
            errorLabelSurName.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
    }
    
    /**
     * Method for validating textField middle initial
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleMIFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
           if(oldValue){
            String text= tfMidInit.getText();
            boolean valid = text.matches("[A-Z]+");
            //Validates that is no longer than 1 letter
            if(text.length()>1){
                throw new Exception("Input just one initial");
                }

            //Checks if its empty

            if(text.isEmpty()){
                throw new Exception("Middle initial field is empty");
                }
            //Validates that it has the right format
            if(!valid){
                throw new Exception("Middle initial format invalid");
                }
            tfMidInit.setStyle("-fx-border-color:green");
            errorLabelName.setText("");
            fieldMIValid= true;
            validateAllFields();
            }
             
        }
        catch (Exception e){
            tfMidInit.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelName.setText(e.getMessage());
            errorLabelName.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
        
        
        
    }
    
    /**
     * Method for validating textField State
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleStateFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue){
                String text = tfState.getText();
                boolean valid = text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");

                //Checks if its empty
                if(text.isEmpty()){
                    throw new Exception ("State field empty");
                }
                //Validates the length of the text
                if(text.length()>30){
                    throw new Exception("State text too long");
                }
                //Validates that it has the right format

                if (!valid){
                    throw new Exception("State format not valid");
                }
                tfState.setStyle("-fx-border-color:green");
                errorLabelState.setText("");
                fieldStateValid= true;
                validateAllFields();
            }
        }
        catch(Exception e){
            tfState.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelState.setText(e.getMessage());
            errorLabelState.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
        
        
    }
    
    /**
     * Method for validating textField City
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleCityFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue){
                String text = tfCity.getText();
                boolean valid = text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");

                //Checks if its empty
                if(text.isEmpty()){
                    throw new Exception ("City field empty");
                }
                //Validates the length of the text
                if(text.length()>30){
                    throw new Exception("City text too long");
                }
                //Validates that it has the right format

                if (!valid){
                    throw new Exception("City format not valid");
                }
                tfCity.setStyle("-fx-border-color:green");
                errorLabelCity.setText("");
                fieldCityValid=true;
                validateAllFields();
            }
        }
        catch(Exception e){
            tfCity.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelCity.setText(e.getMessage());
            errorLabelCity.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
        
        
    }
    
    /**
     * Method for validating textField Adress
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleAdressFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
        if(oldValue){
            String text = tfAdress.getText();
            boolean valid = text.matches("[0-9a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");

            //Checks if its empty
            if(text.isEmpty()){
                    throw new Exception ("Adress field empty");
                }
            //Validates the length of the text
            if(text.length()>30){
                throw new Exception("Adress text too long");
              }
            //Validates that it has the right format
            if (!valid){
                throw new Exception("Adress format not valid");
            }
            tfAdress.setStyle("-fx-border-color:green");
            errorLabelStreet.setText("");
            fieldAdressValid=true;
            validateAllFields();

            } 
        }
        catch(Exception e){
            tfAdress.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelStreet.setText(e.getMessage());
            errorLabelStreet.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
    }
    
    /**
     * Method for validating textField ZIP
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleZIPFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
           if(oldValue){
           String text = tfZip.getText();
           boolean valid = text.matches("[0-9]+");

           //Checks if its empty
           if(text.isEmpty()){
               throw new Exception("ZIP field is empty");
           }
           //Validates the length of the text, only 5 characters
           if(text.length()>5 || text.length()<5){
               throw new Exception("ZIP length not valid (5 numbers)");
           }
           //Validates that it has the right format

           if(!valid){
               throw new Exception("ZIP format not valid");
           }
           tfZip.setStyle("-fx-border-color:green");
           errorLabelState.setText("");
           fieldZIPValid=true;
           validateAllFields();
        } 
        }
        catch(Exception e){
            tfZip.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelState.setText(e.getMessage());
            errorLabelState.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        } 
    }
    
    /**
     * Method for validating textField Phone
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handlePhoneFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue){
                String text = tfPhone.getText();
                boolean valid = text.matches("[0-9 +]+");

                //Validates that it has the right format
                if(!valid){
                    throw new Exception("Phone format invalid");
                }
                //Validates the length of the text
                if(text.length()>15){
                    throw new Exception("Phone number too long");
                }
                //Checks if its empty

                if(text.isEmpty()){
                    throw new Exception("Phone number is empty");
                }
                tfPhone.setStyle("-fx-border-color:green");
                errorLabelPhone.setText("");
                fieldPhoneValid=true;
                validateAllFields();
            }
        }
        catch(Exception e){
            tfPhone.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelPhone.setText(e.getMessage());
            errorLabelPhone.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }  
    }
    /**
     * Method for validating textField Email
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleEmailFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue){
                String text = tfEmail.getText();
                String patronEmail = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

                //Checks if its empty
                if(text.isEmpty()){
                    throw new Exception("Email is empty");
                }
                //Validates the length of the text
                if(text.length()>100){
                    throw new Exception("Email too long");
                }
                //Validates that it has the right patron

                if(!text.matches(patronEmail)){
                    throw new Exception("Email format not valid");
                }
                tfEmail.setStyle("-fx-border-color:green");
                errorLabelEmail.setText("");
                fieldEmailValid=true;
                validateAllFields();
        }
        }
        catch(Exception e){
            tfEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelEmail.setText(e.getMessage());
            errorLabelEmail.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
           
    }
    
    /**
     * Method for validating passwordField Password
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handlePasswFocusChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue){
                String text = pfPassw.getText();
                String passwReq = "^(?=.*[A-Z])(?=.*\\d).{5,30}$";

                //Checks if its empty
                if(text.isEmpty()){
                    throw new Exception("Password is empty");
                }
                //Validates if it matches the requirements

                if(!text.matches(passwReq)){
                    throw new Exception("Password introduced not valid");
                }
                pfPassw.setStyle("-fx-border-color:green");
                errorLabelPassw.setText("");
                fieldPasswValid=true;
                validateAllFields();
            } 
        }
        catch(Exception e){
            pfPassw.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelPassw.setText(e.getMessage());
            errorLabelPassw.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }
         
    }
    
    /**
     * Method for validating passwordField Confirm password
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleConfirmPasswTextChange(ObservableValue observable, Object oldValue, Object newValue){
      try{
            
        String text = pfConfPassw.getText();

        String passwReq = "^(?=.*[A-Z])(?=.*\\d).{6,29}$";
        //Checks if its empty
        if(text.isEmpty()){
           throw new Exception("Password is empty");
        }
        //Validates if it matches the requirements
        if(!text.matches(passwReq)){
           throw new Exception("Password introduced not valid");
        }
        //Validates if its the same as the password field

        if(!text.equals(pfPassw.getText())){
           throw new Exception("The password entered does not match ");
        }
        pfConfPassw.setStyle("-fx-border-color:green");
        errorLabelConfPassw.setText("");
        fieldConfPasswValid = true;
        validateAllFields();
        } 
        
        catch(Exception e){
            pfConfPassw.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            errorLabelConfPassw.setText(e.getMessage());
            errorLabelConfPassw.setStyle("-fx-text-fill: red");
            bbutton.setDisable(true);
            LOGGER.info(e.getMessage());
        }  
          
    } 

    /**
     * Method for handling the click on the hyperlink
     * @param event 
     */
    private void handleHyperlinkOnAction(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("ProyectoSignIn.fxml"));
            Parent root = loader.load();
            
            ProjectInterfacesController controller = loader.getController();
            controller.init(this.stage,root);
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }

    
    /**
     * Method for validating that all fields are correct
     */
    private void validateAllFields(){
        if(fieldNameValid&&fieldSurNameValid&&fieldMIValid&&fieldStateValid&&fieldCityValid&&fieldAdressValid&&fieldZIPValid&&fieldPhoneValid&&fieldEmailValid&&fieldPasswValid&&fieldConfPasswValid){
            bbutton.setDisable(false);
        }
    }

}
