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
import javax.ws.rs.ClientErrorException;
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
    private Label lbBalance;
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
    
    
    private Customer customer;
    private Account account;
    private Stage stage;
    private final Stage movementStage = new Stage();
    
    
    private static final Logger LOGGER = Logger.getLogger("ProyectoCRUD.ui");
    
    MovementRESTClient restClient = new MovementRESTClient();
    
    //long accountId = 2654785441L;
    //String id = String.valueOf(account.getId());
    
    

    public void init(Stage stage, Parent root) {
        try {
            /*this.account = new Account();
            this.account.setId(accountId);
            this.account.setBalance(2000.00);*/
            
            this.stage = stage;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            movementStage.setScene(scene);

            movementStage.setTitle("Movements");
            movementStage.setResizable(false);
                
            
            btNewMovement.setDisable(false);
            
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
            

            ObservableList<Movement> movements = FXCollections.observableArrayList(restClient.findMovementByAccount_XML(
                    new GenericType<List<Movement>>() {},account.getId().toString()));
            
                    
            lbIdAcount.setText(account.getId().toString());
            //lbBalance.setText(account.getBalance().toString());
            tbMovement.setItems(movements);
            LOGGER.info(movements.toString());
            
        } catch (Exception e) {
            //new Alert(AlertType.INFORMATION,e.getLocalizedMessage()).showAndWait();
            //LOGGER.warning(e.getLocalizedMessage());
            LOGGER.info(e.getMessage());
        }

        movementStage.show();

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
            //this.stage.close();
            new Alert(AlertType.INFORMATION, "Are you sure you want to leave?").showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Account.fxml"));
            Parent root = (Parent) loader.load();
            AccountController controller = loader.getController();
            controller.init(this.stage, root);

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
            String rm = (lastMovement.getId().toString());
            
            Double lastAmount = lastMovement.getAmount();
            String tipo = (String) selectType.getValue();
            
            //lbBalance.setText(account.getBalance().toString());
            
            if (lastMovement != null) {
                tbMovement.getItems().remove(lastMovement);
                btUndo.setDisable(true);
                if(tipo.equals("Deposit")){
                    account.setBalance(account.getBalance()+lastAmount);
                    lbBalance.setText(account.getBalance().toString());
                }
                if(tipo.equals("Payment")){
                    account.setBalance(account.getBalance()-lastAmount);
                    lbBalance.setText(String.valueOf(account.getBalance()));
                }
               
            }
            restClient.remove(rm);
            tbMovement.refresh();
            
        }
        catch(ClientErrorException e){
            LOGGER.info(e.getMessage());
        }
    }

    private void handlebtNewMovementOnAction(ActionEvent event) {
        try{
            Movement movement = new Movement();
            Date timestamp= new Date();
            String tipo = (String) selectType.getValue();
            double amount = Double.valueOf(tfAmount.getText());
            //double balance = this.account.getBalance();
            double newBalance;
           /* if(tfAmount.getText().isEmpty()){
                lbErrorAmount.setText("The amount is empty");
                throw new IllegalArgumentException("The amount is empty");
            }
            if(!selectType.hasProperties()){
                lbErrorAmount.setText("You have to select the type");
                throw new IllegalArgumentException("You have to select the type");
            }*/
            //lbErrorAmount.setText("");
            
            movement.setAmount(amount);
            movement.setDescription(tipo);
            movement.setTimestamp(timestamp);
            
            if(tipo.equals("Deposit")){
                /*newBalance = balance + amount;
                movement.setBalance(newBalance);
                this.account.setBalance(newBalance); */
                lbBalance.setText(String.valueOf(account.getBalance()));
            }
            if(tipo.equals("Payment")){
                /*newBalance = balance - amount;
                movement.setBalance(newBalance);
                this.account.setBalance(newBalance);*/
                lbBalance.setText(String.valueOf(account.getBalance()));
            }
            
            tbMovement.getItems().add(movement);
            tbMovement.refresh();
            
            btUndo.setDisable(false);
            restClient.create_XML(movement, account.getId().toString());
           // LOGGER.info(movement.toString());
            
        }
        catch(IllegalArgumentException | ClientErrorException e){
            LOGGER.info(e.getMessage());
        }

    }

}
