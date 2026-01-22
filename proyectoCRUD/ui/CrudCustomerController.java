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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
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
    private TableColumn<Customer, String> tbName;
    @FXML
    private TableColumn<Customer, String> tbMidInit;
    @FXML
    private TableColumn<Customer, String> tbSurname;
    @FXML
    private TableColumn<Customer, String> tbStreet;
    @FXML
    private TableColumn<Customer, String> tbCity;
    @FXML
    private TableColumn<Customer, String> tbState;
    @FXML
    private TableColumn<Customer, Integer> tbZip;
    @FXML
    private TableColumn<Customer, String> tbEmail;
    @FXML
    private TableColumn<Customer, Long> tbPhone;
    @FXML
    private TableColumn<Customer, String> tbPassw;
    @FXML
    private TableView<Customer> tbCustomers;
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
        
        GenericType<List<Customer>> customers = new GenericType<List<Customer>>() {};
        
        List<Customer> customerList = clientManager.findAll_XML(customers);

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList(customerList);
        
        tbCustomers.setItems(allCustomers);
        
        tbCustomers.setEditable(true);
        
        bExit.setOnAction(this::handleBtExitOnAction);
        bDelete.setOnAction(this::handleBtDeleteOnAction);
        bRefresh.setOnAction(this::handleBtRefreshOnAction);
        bAdd.setOnAction(this::handleBtAddOnAction);
        
        CustomerStage.setOnCloseRequest(this::handleBtExitOnAction);
        tbCustomers.getSelectionModel().selectedItemProperty().addListener(this::handleCustomerTableSelectionChanged);
        
        tbID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbName.setEditable(false);
        
        tbName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tbName.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbName.setEditable(true);
        tbName.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("Name field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setFirstName(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        tbMidInit.setCellValueFactory(new PropertyValueFactory<>("middleInitial"));
        tbMidInit.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbMidInit.setEditable(true);
        tbMidInit.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>1 || !newValue.trim().matches("[A-Z]+")){
                        throw new Exception("Middle initial field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setMiddleInitial(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        
        tbSurname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tbSurname.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbSurname.setEditable(true);
        tbSurname.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("Surname field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setLastName(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        
        tbStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tbStreet.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbStreet.setEditable(true);
        tbStreet.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[0-9a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("City field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setStreet(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        
        tbCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tbCity.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbCity.setEditable(true);
        tbCity.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("City field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setCity(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        
        tbState.setCellValueFactory(new PropertyValueFactory<>("state"));
        tbState.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbState.setEditable(true);
        tbState.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(newValue.trim().isEmpty() || newValue.trim().length()>30 || !newValue.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")){
                        throw new Exception("State field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setState(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        
        tbZip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        tbZip.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tbZip.setEditable(true);
        tbZip.setOnEditCommit(
                (CellEditEvent<Customer, Integer> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    Integer newValue = t.getNewValue();
                    //2)
                    if(newValue.toString().trim().isEmpty() || newValue.toString().trim().length()>5 || newValue.toString().trim().length()<5 || !newValue.toString().trim().matches("[0-9]+")){
                        throw new Exception("Zip field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setZip(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
    
        
        tbEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbEmail.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbEmail.setEditable(true);
        tbEmail.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(!newValue.trim().matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || newValue.trim().isEmpty() || newValue.trim().length()>100){
                        throw new Exception("Email field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setEmail(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        
        tbPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tbPhone.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        tbPhone.setEditable(true);
        tbPhone.setOnEditCommit(
                (CellEditEvent<Customer, Long> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    Long newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(!newValue.toString().trim().matches("[0-9 +]+") || newValue.toString().trim().length()>15 || newValue.toString().trim().isEmpty()){
                        throw new Exception("Phone field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setPhone(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        tbPassw.setCellValueFactory(new PropertyValueFactory<>("password"));
        tbPassw.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        tbPassw.setEditable(true);
        tbPassw.setOnEditCommit(
                (CellEditEvent<Customer, String> t) -> {
                try{
                    //1)
                    Customer item = ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())); 
                    String newValue = t.getNewValue();
                    String oldValue = item.getFirstName();
                    //2)
                    if(newValue.trim().isEmpty() || !newValue.trim().matches("^(?=.*[A-Z])(?=.*\\d).{5,30}$")){
                        throw new Exception("Password field not valid");
                    }
                    //3)
                    clientManager.edit_XML(item, item.getId());
                    //4)
                    item.setPassword(newValue);
                    //5)
                    tbCustomers.refresh();
                }  
                catch(Exception e){
                    LOGGER.info(e.getMessage());
                    tbCustomers.refresh();
                }
                
        });
        
        
       
      
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
            LOGGER.info(e.getMessage());
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
            LOGGER.info(e.getMessage());
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
            LOGGER.info(e.getMessage());
        }
        
    }
    private void handleBtRefreshOnAction(Event event){
        try{
            tbCustomers.refresh();
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
        }
    }
    
    private void handleBtAddOnAction(Event event){
        try{
            /*
            Customer customer = new Customer();
            clientManager.create_XML(customer);
            tbCustomers.getItems().add(customer);
            */
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }
        
}

