/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.AccountRESTClient;
import proyectoCRUD.model.Account;
import proyectoCRUD.model.AccountType;
import proyectoCRUD.model.Customer;
import proyectoCRUD.model.Movement;

/**
 *
 * @author luis felipe
 */
public class AccountController {

    @FXML
    private Button btnUpgrade;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnMovement;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnExit;
    @FXML
    private TableView<Account> tbvAccounts;
    @FXML
    private TableColumn<Account, Long> tcId;
    @FXML
    private TableColumn<Account, String> tcDescription;
    @FXML
    private TableColumn<Account, AccountType> tcType;
    @FXML
    private TableColumn<Account, Double> tcBeginBalance;
    @FXML
    private TableColumn<Account, Double> tcBalance;
    @FXML
    private TableColumn<Account, Double> tcCreditLine;
    @FXML
    private TableColumn<Account, Date> tcBeginBalanceTimestamp;
    
    private static final Logger LOGGER = Logger.getLogger("projectinterfaces.ui");
    private Stage stage;
    private Customer customer;
    private AccountRESTClient client = new AccountRESTClient();
    private final Stage AccountStage = new Stage();
    

    /**
     * Inicializa la etapa principal de la ventana Iniciar sesión. Configura la
     * escena, las propiedades del escenario (título, redimensionable), los
     * estados de control iniciales, y oyentes para cambios de campo y enfoque,
     * y controladores de acciones para botones e hipervínculos.
     *
     * @param stage La etapa principal de esta ventana.
     * @param root El nodo raíz del diseño FXML para esta escena..
     */
    public void init(Stage stage, Parent root) {
        try{
        //Se crea la escena asociada al grafico de root.
        LOGGER.info("Initializing window");
        //Asociamos la escena a la primera ventana.
        Scene scene = new Scene(root);
        //Se establecen las propiedades de la vetana.
        stage.setScene(scene);
        AccountStage.setScene(scene);
        this.stage = stage;
        //Establecer el titulo de la ventana
        AccountStage.setTitle("Account");
        //La ventana no es redimensionable
        stage.setResizable(false);
        AccountStage.setResizable(false);
        //El botón Delete esta deshabilitado.
        btnDelete.setDisable(false);
        //Asociar eventos a manejadores
        btnExit.setOnAction(this::handleExitOnAction);
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tcBeginBalance.setCellValueFactory(new PropertyValueFactory<>("beginBalance"));
        tcBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        tcCreditLine.setCellValueFactory(new PropertyValueFactory<>("creditLine"));
        tcBeginBalanceTimestamp.setCellValueFactory(new PropertyValueFactory<>("BeginBalanceTimestamp"));
        //btnMovement.setOnAction(this::handleMovementOnAction);
        btnDelete.setOnAction(this::handleDelete);
        btnRefresh.setOnAction(this::handleRefresh);
        tbvAccounts.getSelectionModel().selectedItemProperty().addListener(this::handleAccountTable);

        //Carga de datos en la tabla
        tbvAccounts.setItems(FXCollections.observableArrayList(
                client.findAccountsByCustomerId_XML(new GenericType<List<Account>>() {}, 
                        customer.getId().toString())));
        
        //Mostrar la ventana
        stage.show();
        AccountStage.show();
        //Cerrar la ventana
        stage.setOnCloseRequest(this::handleExitOnAction);
        AccountStage.setOnCloseRequest(this::handleExitOnAction);
        
        }catch(Exception e){
            handleAlert("Error al obtener los datos");
        }
        
    }
    private void handleRefresh(ActionEvent event){
        tbvAccounts.refresh();
        
        
    }
    private void handleAccountTable(ObservableValue observable, Object oldValue, Object newValue){
       
            if(newValue != null){
                btnDelete.setDisable(false);
            } else{
                btnDelete.setDisable(true);
            }
        
    }
    /**
     * @parama event Manejador del borrado de la cuenta
     */
    private void handleDelete(ActionEvent event){
        try{
        Account select = tbvAccounts.getSelectionModel().getSelectedItem();
        
           
        if(select.getMovements().equals(null)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete?",
                    ButtonType.OK, ButtonType.CANCEL);
            
            alert.setTitle("Confirm Exit!");
            alert.showAndWait(); 
            
            if (alert.getResult() == ButtonType.YES) {
                
                client.removeAccount(select.getId().toString());
                tbvAccounts.getItems().remove(select);
                btnDelete.setDisable(true);
            
            }
            event.consume();
        } else{
            throw new Exception("You cannot delete the account\nbecause it still has movements");
        }
        }catch(Exception e){
            handleAlert(e.getMessage());
        }
         
    }
    
    /**
     *
     * @param event Manejador del boton exit
     */
    private void handleExitOnAction(Event event) {
        try {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to go out?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("¡Confirm Exit!");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            //Lanzamos la ventana emergente para pedir confirmación de salida
            Stage stage = (Stage) btnExit.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("SignIn.fxml"));
            Parent root = loader.load();
            
            SignInController controller = loader.getController();
            controller.init(this.stage,root);
            AccountStage.close();
            }   
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            handleAlert("Error, when going to registry!");
        }
        event.consume();
       

    }
    /**
     * 
     * @param customer obtenemos el customer
     */
    public void setCustomer(Customer customer) {

        this.customer = customer;

    }

    /**
     *
     * @param event Maneja el cambio de ventana, hacia movement
     */

    /*private void handleMovementOnAction(ActionEvent event){
         try {
           
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("Movement.fxml"));
            Parent root = loader.load();
            
           MovementController controller = loader.getController();
            controller.init(this.stage,root);
            

        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            handleAlert("¡Error, when going to registry!");
        }
     }*/
    /**
     *
     * @param mensaje de error en el programa
     */
    private void handleAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
