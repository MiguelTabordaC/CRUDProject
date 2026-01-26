/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.MovementRESTClient;
import proyectoCRUD.model.Account;
import proyectoCRUD.model.Customer;
import proyectoCRUD.model.Movement;

/**
 *
 * @author miguel
 */
public class MovementController {

    @FXML
    private Button btNewMovement;
    @FXML
    private Button btUndo;
    @FXML
    private Button btCancel;
    @FXML
    private Label lbIdAcount;
    @FXML
    private Label lbErrorAmount;
    @FXML
    private TextField tfAmount;
    
    /*@FXML
    private TableView tbMovement;
    @FXML
    private TableColumn tbColDate;
    @FXML
    private TableColumn tbColAmount;
    @FXML
    private TableColumn tbColType;
    @FXML
    private TableColumn tbColBalance;*/
    @FXML
    private TableView<Movement> tbMovement;
    @FXML
    private TableColumn<Movement, Date> tbColDate;
    @FXML
    private TableColumn<Movement, String> tbColAmount;
    @FXML
    private TableColumn<Movement, String> tbColType;
    @FXML
    private TableColumn<Movement, String> tbColBalance;
    @FXML
    private ComboBox selectType;
    
    private Double balance;
    private Customer customer;
    private Account account;
    private Stage stage;
    
    private static final Logger LOGGER = Logger.getLogger("ProjectInterfacesApplication.ui");
    
    MovementRESTClient restClient = new MovementRESTClient();
    
    long accountId = 2654785441L;
    String id = String.valueOf(accountId);

    public void init(Stage stage, Parent root) {
        try {
            this.account = new Account();
            this.account.setId(accountId);
            this.account.setBalance(2000.00);
            
            this.stage = stage;
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setTitle("Movements");
            stage.setResizable(false);
                
            //stage.setOnCloseRequest();
            btNewMovement.setDisable(false);
            //btUndo.setDisable(true);
            btCancel.setDisable(false);
            
            ObservableList<String> type = FXCollections.observableArrayList("Deposit","Payment");
            selectType.setItems(type);

            tfAmount.focusedProperty().addListener(this::handleAmountOnFocusedChange);
            selectType.focusedProperty().addListener(this::handleTypeOnFocusedChange);
                    
            btNewMovement.setOnAction(this::handlebtNewMovementOnAction);
            btUndo.setOnAction(this::handlebtUndoOnAction);
            btCancel.setOnAction(this::handlebtCancelOnAction);

            tbColDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            tbColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            tbColType.setCellValueFactory(new PropertyValueFactory<>("description"));
            tbColBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
            //tbMovement.getSelectionModel().selectedItemProperty().addListener(this::handleMovementTableSelectionChanged);

            ObservableList<Movement> movements = FXCollections.observableArrayList(restClient.findMovementByAccount_XML(
                    new GenericType<List<Movement>>() {},id));
            
                    
            lbIdAcount.setText(id);
            tbMovement.setItems(movements);
            LOGGER.info(movements.toString());
            
        } catch (Exception e) {
            //new Alert(AlertType.INFORMATION,e.getLocalizedMessage()).showAndWait();
            //LOGGER.warning(e.getLocalizedMessage());
            LOGGER.info(e.getMessage());
        }

        stage.show();

    }
    public Double getBalance(Long id){
        
        return this.balance;
    }
    public void setAccount(Account account){
        this.account = account;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private void handleAmountOnFocusedChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue){
                if(tfAmount.getText().isEmpty()){
                    lbErrorAmount.setText("The amount is empty");
                    throw new IllegalArgumentException("The amount is empty");
                }
                lbErrorAmount.setText("");
            }
        }
        catch (Exception e){
            LOGGER.info(e.getMessage());
        }
    }
    private void handleTypeOnFocusedChange(ObservableValue observable, Boolean oldValue, Boolean newValue){
        try{
            if(oldValue)   {
                if(!selectType.hasProperties()){
                    lbErrorAmount.setText("You have to select the type");
                    throw new IllegalArgumentException("You have to select the type");
                }
                lbErrorAmount.setText("");
            }
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }
    //BOTONES
    /**
     *
     * @param event
     */
    private void handlebtCancelOnAction(ActionEvent event) {
        try {

            new Alert(AlertType.INFORMATION, "Are you sure you want to leave?").showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProyectoSignIn.fxml"));
            Parent root = (Parent) loader.load();
            //AccountController controller = loader.getController();
            //controller.init(this.stage, root);

        } catch (InternalServerErrorException e) {
            new Alert(AlertType.INFORMATION, "Internal server error, please wait or contact your service provider").showAndWait();

        } catch (IOException e) {
            new Alert(AlertType.INFORMATION, e.getLocalizedMessage()).showAndWait();

        }
    }

    private void handlebtUndoOnAction(ActionEvent event) {
        try{
            Movement lastMovement = tbMovement.getItems().stream()
                    .max(Comparator.comparing(Movement::getTimestamp)).orElse(null);
            String rm = String.valueOf(lastMovement.getId());
            if (lastMovement != null) {
                tbMovement.getItems().remove(lastMovement);
                btUndo.setDisable(true);
                
            }
            
            tbMovement.refresh();
            restClient.remove(rm);
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
        }
    }

    private void handlebtNewMovementOnAction(ActionEvent event) {
        try{
            Movement movement = new Movement();
            String tipo = (String) selectType.getValue();
            
            /*if(tfAmount.getText().isEmpty()){
                lbErrorAmount.setText("The amount is empty");
                throw new IllegalArgumentException("The amount is empty");
            }
            if(!selectType.hasProperties()){
                lbErrorAmount.setText("You have to select the type");
                throw new IllegalArgumentException("You have to select the type");
            }*/
            //lbErrorAmount.setText("");
            double amount = Double.valueOf(tfAmount.getText());
            double balance = account.getBalance();
            double newBalance;
            
            movement.setAmount(amount);
            movement.setDescription(tipo);
            Date timestamp= new Date();
            movement.setTimestamp(timestamp);
            
            if(tipo.equals("Deposit")){
                newBalance = balance + amount;
                movement.setBalance(newBalance);
                this.account.setBalance(newBalance); 
            }
            if(tipo.equals("Payment")){
                newBalance = balance - amount;
                movement.setBalance(newBalance);
                this.account.setBalance(newBalance);
            }
            
            tbMovement.getItems().add(movement);
            tbMovement.refresh();
            
            restClient.create_XML(movement, id);
            LOGGER.info(movement.toString());
            
        }
        catch(Exception e){
            LOGGER.info(e.getMessage());
        }

    }

}
