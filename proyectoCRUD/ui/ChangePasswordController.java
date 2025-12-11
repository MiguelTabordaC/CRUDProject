/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javax.ws.rs.InternalServerErrorException;
import proyectoCRUD.logic.CustomerRESTClient;
import proyectoCRUD.model.Customer;

/**
 *
 * @author miguel
 */
public class ChangePasswordController {
   // private String email;
   // private String password;
    @FXML
    private Button btChangePass;
    @FXML
    private Button btExit;
    @FXML
    private PasswordField tfOldPass;
    @FXML
    private PasswordField tfNewPass;
    @FXML
    private PasswordField tfConfirmNewPass;
    @FXML
    private Label lbOldErrorLabel;
    @FXML
    private Label lbNewErrorLabel;
    @FXML
    private Label lbConfirmErrorLabel;
    private Customer customer;
    private Stage stage;
    
    private static final Logger LOGGER = Logger.getLogger("ProjectInterfacesApplication.ui");
    
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
    
    public void init(Stage stage, Parent root) {
        
        this.stage=stage;
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        //Set the window title
        stage.setTitle("Change Password");
        //The window must not be resizable
        stage.setResizable(false);
        
        //stage.setOnCloseRequest();
        
        //The Change Password button is disabled until the fields are complete
        btChangePass.setDisable(true);
        //The "Cancel" button is enabled
        btExit.setDisable(false);
        //asociar eventos a manejadores
        btChangePass.setOnAction(this::handlebtChangePassOnAction);
        btExit.setOnAction(this::handlebtExitOnAction);
        //Asiciacion de manejadores a properties
        tfOldPass.focusedProperty().addListener(this::handletfOldPassOnFocusChange);
        tfNewPass.focusedProperty().addListener(this::handletfNewPassOnFocusChange);
        tfConfirmNewPass.textProperty().addListener(this::handletfConfirmNewPassOnFocusChange);
        
        
        
        
        //stage.setOnCloseRequest(this:: handlebtExitOnAction);
        
        
        
        
        stage.show();
        
    }
    //Old Password
    /**
     * 
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handletfOldPassOnFocusChange(ObservableValue observable,Boolean oldValue,Boolean newValue){
        try{
            if(oldValue){
            String pass = tfOldPass.getText();
            if(pass.isEmpty()){
                btChangePass.setDisable(true);
                throw new Exception ("Old Password is empty");
            }
            if(!pass.equals(customer.getPassword()))
                throw new Exception("Incorrect password");
            
            boolean oldPassValid = !tfOldPass.getText().trim().isEmpty();
            boolean newPassValid = !tfNewPass.getText().trim().isEmpty();
            boolean confirmNewPassValid = !tfConfirmNewPass.getText().trim().isEmpty();
            //Button "changes password" is avalible only if all the fields are with content
            boolean camposCompletos = oldPassValid && newPassValid && confirmNewPassValid;
            btChangePass.setDisable(!camposCompletos);
            
            tfOldPass.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
            lbOldErrorLabel.setText("");
            }
        }catch (Exception e){
            tfOldPass.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            lbOldErrorLabel.setText(e.getMessage());
        }
    }
    //New Password
    /**
     * 
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handletfNewPassOnFocusChange(ObservableValue observable,Boolean oldValue,Boolean newValue){
        try{
            if(oldValue){
                String pass = tfNewPass.getText();
                String passValid = "^(?=.*[A-Z])(?=.*\\d).{5,30}$";
                
                if(pass.isEmpty()){
                    btChangePass.setDisable(true);
                    throw new Exception ("The Password field is empty");
                }        
                if(tfOldPass.getText().equals(tfNewPass.getText()))
                    throw new Exception ("The new password is the same as the previous one");
                if(!pass.matches(passValid))
                    throw new Exception ("The New Password neew at least 1 Capital,1 lower case and at least 5 characters");
                
                boolean oldPassValid = !tfOldPass.getText().trim().isEmpty();
                boolean newPassValid = !tfNewPass.getText().trim().isEmpty();
                boolean confirmNewPassValid = !tfConfirmNewPass.getText().trim().isEmpty();
                //Button "changes password" is avalible only if all the fields are with content
                boolean camposCompletos = oldPassValid && newPassValid && confirmNewPassValid;
                btChangePass.setDisable(!camposCompletos);
                
                tfNewPass.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
                lbNewErrorLabel.setText("");
            }
        }catch (Exception e){
            tfNewPass.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            lbNewErrorLabel.setText(e.getMessage());
        }
    }
    //Conform New Password
    /**
     * 
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handletfConfirmNewPassOnFocusChange(ObservableValue observable,Object oldValue,Object newValue){
        try{
            
            if(!tfNewPass.getText().equals(tfConfirmNewPass.getText())){
                throw new Exception ("The password is not the same");  
            }
            
            boolean oldPassValid = !tfOldPass.getText().trim().isEmpty();
            boolean newPassValid = !tfNewPass.getText().trim().isEmpty();
            boolean confirmNewPassValid = !tfConfirmNewPass.getText().trim().isEmpty();
            //Button "changes password" is avalible only if all the fields are with content
            boolean camposCompletos = oldPassValid && newPassValid && confirmNewPassValid;
            btChangePass.setDisable(!camposCompletos);
   
            tfConfirmNewPass.setStyle("-fx-border-color: green; -fx-border-width: 1px;");
            lbConfirmErrorLabel.setText("");
            
        }catch (Exception e){
            tfConfirmNewPass.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            lbConfirmErrorLabel.setText(e.getMessage());
        }
    }
    //Botones
    /**
     * 
     * @param event 
     */
    private void handlebtChangePassOnAction(ActionEvent event){
        try{
            if(tfOldPass.getText().isEmpty()){
                throw new Exception("Old Password field is not valid");
            }
            if(tfNewPass.getText().isEmpty() || !tfNewPass.getText().matches("^(?=.*[A-Z])(?=.*\\d).{5,30}$")){
                throw new Exception("The New Password neew at least 1 Capital,1 lower case and at least 5 characters");
            }
            if(!tfConfirmNewPass.getText().equals(tfConfirmNewPass.getText())){
                throw new Exception("Password is not the same");
            }
            
            boolean oldPassValid = !tfOldPass.getText().trim().isEmpty();
            boolean newPassValid = !tfNewPass.getText().trim().isEmpty();
            boolean confirmNewPassValid = !tfConfirmNewPass.getText().trim().isEmpty();
            //Button "changes password" is avalible only if all the fields are with content
            boolean camposCompletos = oldPassValid && newPassValid && confirmNewPassValid;
            btChangePass.setDisable(!camposCompletos);

            CustomerRESTClient restClient = new CustomerRESTClient();
            customer = restClient.findCustomerByEmailPassword_XML(Customer.class,customer.getEmail(),customer.getPassword());
            
            //customer.setId(customer.getId());
            customer.setPassword(tfConfirmNewPass.getText());
            // Llamar al método PUT del cliente REST
            restClient.edit_XML(customer, customer.getId());
             
            restClient.close();

            //customer.setPassword(tfConfirmNewPass.getText());
            new Alert(AlertType.INFORMATION,"User password succesfully change!!").showAndWait();
            //Customer.setPassword(tfConfirmNewPass.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProyectoSignIn.fxml"));
            Parent root = (Parent)loader.load();
            ProjectInterfacesController controller = loader.getController();
            controller.init(stage, root);
            }
        //Catch server error 500
        catch(InternalServerErrorException e){
            new Alert(AlertType.INFORMATION,"Internal server error, please wait or contact your service provider").showAndWait();
            
        }catch (Exception e){
            //lbConfirmErrorLabel.setText(e.getMessage());
            new Alert(AlertType.INFORMATION,e.getLocalizedMessage()).showAndWait();
            //LOGGER.warning("Error");
        }
    }
    /**
     * 
     * @param event 
     */
    private void handlebtExitOnAction(ActionEvent event){
        try{
            new Alert(AlertType.INFORMATION,"Are you sure you want to leave?").showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProyectoSignIn.fxml"));
            Parent root = (Parent)loader.load();
            ProjectInterfacesController controller = loader.getController();
            controller.init(this.stage, root);
            
        }
        //Catch server error 500
        catch(InternalServerErrorException e){
            new Alert(AlertType.INFORMATION,"Internal server error, please wait or contact your service provider").showAndWait();
            
        }catch(Exception e){
            new Alert(AlertType.INFORMATION,e.getLocalizedMessage()).showAndWait();
            
        }
    }
    
    /**
     * Evento para cerrar la ventana en vez de la aplicación
     * 
     * @param event
     */
   /* private void handlebtExitOnAction(Event event){
        try{
            this.stage.close();
            event.consume();
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }*/
    
}