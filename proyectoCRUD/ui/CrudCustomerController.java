/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.CustomerRESTClient;
import proyectoCRUD.model.Customer;

/**
 *
 * @author david
 */
public class CrudCustomerController {
    
    @FXML
    private TableColumn tbID;
    @FXML
    private TableColumn tbName;
    @FXML
    private TableColumn tbMidInit;
    @FXML
    private TableColumn tbSurname;
    @FXML
    private TableColumn tbStreet;
    @FXML
    private TableColumn tbCity;
    @FXML
    private TableColumn tbState;
    @FXML
    private TableColumn tbZip;
    @FXML
    private TableColumn tbEmail;
    @FXML
    private TableColumn tbPhone;
    @FXML
    private TableColumn tbPassw;
    @FXML
    private TableView tbCustomers;
    @FXML
    private Button bAdd;
    @FXML
    private Button bExit;
    @FXML
    private Button bDelete;
    @FXML
    private Button bRefresh;
    
    private final Stage CustomerStage = new Stage();
    private Scene scene;
    
    private final CustomerRESTClient clientManager = new CustomerRESTClient();
    
    private static final Logger LOGGER=Logger.getLogger("projectinterfaces.ui");
    
    public void init(Stage stage, Parent root) {
        
        LOGGER.info("Initializing window");
        
        scene = new Scene(root); 
        CustomerStage.setScene(scene);
        CustomerStage.setTitle("Customers administration");
        CustomerStage.setResizable(false);
        CustomerStage.show();
        
        bDelete.setDisable(true);
        
        bExit.setOnAction(this::handleBtExitOnAction);
        bDelete.setOnAction(this::handleBtDeleteOnAction);
        bRefresh.setOnAction(this::handleBtRefreshOnAction);
        
        CustomerStage.setOnCloseRequest(this::handleBtExitOnAction);
        tbCustomers.getSelectionModel().selectedItemProperty().addListener(this::handleCustomerTableSelectionChanged);
        
        tbID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbID.setEditable(true);
        tbName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tbName.setEditable(true);
        tbMidInit.setCellValueFactory(new PropertyValueFactory<>("middleInitial"));
        tbMidInit.setEditable(true);
        tbSurname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tbSurname.setEditable(true);
        tbStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tbStreet.setEditable(true);
        tbCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tbCity.setEditable(true);
        tbState.setCellValueFactory(new PropertyValueFactory<>("state"));
        tbState.setEditable(true);
        tbZip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        tbZip.setEditable(true);
        tbEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbEmail.setEditable(true);
        tbPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tbPhone.setEditable(true);
        tbPassw.setCellValueFactory(new PropertyValueFactory<>("password"));
        tbPassw.setEditable(true);
        
        GenericType<List<Customer>> customers = new GenericType<List<Customer>>() {};
        
        List<Customer> customerList = clientManager.findAll_XML(customers);

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList(customerList);
        
        tbCustomers.setItems(allCustomers);
      
    }
    private void handleBtExitOnAction(Event event){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                          "Are you sure you want to exit?",
                           ButtonType.YES, ButtonType.NO);
            alert.setTitle("¡Confirm Exit!");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                //Lanzamos la ventana emergente para pedir confirmación de salida
                CustomerStage.close();
                

            }
            event.consume();
        }
        catch(Exception e){
            
        }
    }
    
    private void handleCustomerTableSelectionChanged(ObservableValue observable, Object oldValue, Object newValue){
        try{
            if(newValue != null){
                bDelete.setDisable(false);
            }
            else{
                bDelete.setDisable(true); 
            }
        }
        catch (Exception e){
            
        }
    }
    
    private void handleBtDeleteOnAction(Event event){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this Customer?",ButtonType.YES,ButtonType.NO);
            alert.setTitle("Deletion prompt");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                Customer customer = (Customer) tbCustomers.getSelectionModel().getSelectedItem();
                clientManager.remove(customer.getId().toString());
                tbCustomers.getItems().remove(customer);
                bDelete.setDisable(true);
            }
            event.consume();
            
        }
        catch(Exception e){
            
        }
        
    }
    private void handleBtRefreshOnAction(Event event){
        try{
            tbCustomers.refresh();
        }
        catch (Exception e){
            
        }
    }
        
}
