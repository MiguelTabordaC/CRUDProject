/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
import proyectoCRUD.logic.AccountRESTClient;
import proyectoCRUD.model.Account;
import proyectoCRUD.model.Customer;

/**
 *
 * @author luis felipe
 */
public class AccountController {

    @FXML
    private TextField tfEmail;
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
    private TableView tbvAccounts;
    @FXML
    private TableColumn tcId;
    @FXML
    private TableColumn tcDescription;
    @FXML
    private TableColumn tcType;
    @FXML
    private TableColumn tcBeginBalance;
    @FXML
    private TableColumn tcBalance;
    @FXML
    private TableColumn tcCreditLine;
    @FXML
    private TableColumn tcBeginBalanceTimestamp;

    private static final Logger LOGGER = Logger.getLogger("projectinterfaces.ui");
    private Stage stage;
    private Customer customer;
    AccountRESTClient client = new AccountRESTClient();

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

        //Se crea la escena asociada al grafico de root.
        LOGGER.info("Initializing window");
        //Asociamos la escena a la primera ventana.
        Scene scene = new Scene(root);
        //Se establecen las propiedades de la vetana.
        stage.setScene(scene);
        this.stage = stage;
        //Establecer el titulo de la ventana
        stage.setTitle("Account");
        //La ventana no es redimensionable
        stage.setResizable(false);
        //El botón Login esta deshabilitado y el botón Exit esta habilitado.
        //btnLogin.setDisable(true);
        //btnExit.setDisable(false);
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

        //Carga de datos en la tabla
        Account[] accounts = client.findAccountsByCustomerId_XML(Account[].class, "102263301");
        ObservableList<Account> accountsData = FXCollections.observableArrayList(accounts);

        LOGGER.info(accountsData.toString());
        tbvAccounts.setItems(accountsData);

        //Mostrar la ventana
        stage.show();
        //Cerrar la ventana
        stage.setOnCloseRequest(this::handleExitOnAction);

    }

    /**
     *
     * @param event Manejador del boton exit
     */
    private void handleExitOnAction(Event event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to go out?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("¡Confirm Exit!");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            //Lanzamos la ventana emergente para pedir confirmación de salida
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.close();

        }
        event.consume();

    }

    private void setCustomer(Customer customer) {

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
     * @param mensaje de error
     */
    private void handleAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
