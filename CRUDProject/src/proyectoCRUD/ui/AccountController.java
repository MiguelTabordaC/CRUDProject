/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.AccountRESTClient;
import proyectoCRUD.model.Account;
import proyectoCRUD.model.AccountType;
import proyectoCRUD.model.Customer;

/**
 * Declaracion de las respectivas variables y las respectivas columnas de la
 * tabla
 *
 * @author luis felipe
 */
public class AccountController {
    
    @FXML 
    private Window menuAccount;
    @FXML 
    private MenuController menuController;
    @FXML
    private Button btnRefresh, btnDelete, btnMovement, btnExit;
    @FXML
    private ToggleButton btnAdd;
    @FXML
    private TableView<Account> tbvAccounts;
    @FXML
    private TableColumn<Account, Long> tcId;
    @FXML
    private TableColumn<Account, String> tcDescription;
    @FXML
    private TableColumn<Account, AccountType> tcType;
    @FXML
    private TableColumn<Account, Double> tcBeginBalance, tcBalance, tcCreditLine;
    @FXML
    private TableColumn<Account, Date> tcBeginBalanceTimestamp;

    private static final Logger LOGGER = Logger.getLogger("proyectoCRUD.ui");
    private Stage stage;
    private Customer customer;
    private AccountRESTClient client = new AccountRESTClient();
    private final Stage AccountStage = new Stage();
    private Account newAccounts;

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
        try {
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
            //El botón Delete está deshabilitado.
            btnDelete.setDisable(true);
            //El botón Movements está deshabilitado hasta seleccionar una cuenta
            btnMovement.setDisable(true);
            //Asociar eventos a manejadores
            tcId.setCellValueFactory(
                    new PropertyValueFactory<>("id"));
            tcId.setEditable(false);
            tcDescription.setCellValueFactory(
                    new PropertyValueFactory<>("description"));
            tcDescription.setEditable(true);
            tcType.setCellValueFactory(
                    new PropertyValueFactory<>("type"));
            tcType.setEditable(true);
            tcBeginBalance.setCellValueFactory(
                    new PropertyValueFactory<>("beginBalance"));
            tcBeginBalance.setEditable(true);
            tcBalance.setCellValueFactory(
                    new PropertyValueFactory<>("balance"));
            tcBalance.setEditable(false);
            tcCreditLine.setCellValueFactory(
                    new PropertyValueFactory<>("creditLine"));
            tcCreditLine.setEditable(false);
            tcBeginBalanceTimestamp.setCellValueFactory(
                    new PropertyValueFactory<>("beginBalanceTimestamp"));
            tcBeginBalanceTimestamp.setEditable(false);

            //Declaración de las CellFactory
            tcDescription.setCellFactory(TextFieldTableCell.forTableColumn());
            tcBeginBalance.setCellFactory(
                    TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            tcCreditLine.setCellFactory(
                    TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            tcType.setCellFactory(
                    ComboBoxTableCell.forTableColumn(AccountType.values()));

            //Declaración de los EditOnCommit
            tcDescription.setOnEditCommit(this::handleDescription);
            tcType.setOnEditCommit(this::handleType);
            tcBeginBalance.setOnEditCommit(this::handleBeginBalance);
            tcCreditLine.setOnEditCommit(this::handleCreditLine);

            //Manejadores de los botones
            btnMovement.setOnAction(this::handleMovementOnAction);
            btnDelete.setOnAction(this::handleDelete);
            btnRefresh.setOnAction(this::handleRefresh);
            tbvAccounts.getSelectionModel().selectedItemProperty().addListener(this::handleAccountTable);
            btnAdd.setOnAction(this::handleCreate);
            btnExit.setOnAction(this::handleExitOnAction);

            //Carga de datos en la tabla
            tbvAccounts.setItems(FXCollections.observableArrayList(
                    client.findAccountsByCustomerId_XML(new GenericType<List<Account>>() {
                    },
                            customer.getId().toString())));
            //Mostrar la ventana
            stage.show();
            AccountStage.show();
            //Cerrar la ventana
            stage.setOnCloseRequest(this::handleExitOnAction);
            AccountStage.setOnCloseRequest(this::handleExitOnAction);

        } catch (Exception e) {
            handleAlert("Error al obtener los datos");
        }

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
     * @param event
     */
    private void handleDescription(TableColumn.CellEditEvent<Account, String> event) {
        Account account = event.getRowValue();
        String newValue = event.getNewValue();

        if (account != newAccounts) {
            account.setDescription(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        if (newValue == null || newValue.trim().isEmpty()) {
            handleAlert("La descripción no puede estar vacía");
            account.setDescription(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        account.setDescription(newValue);

    }

    /**
     *
     * @param event
     */
    private void handleType(TableColumn.CellEditEvent<Account, AccountType> event) {
        Account account = event.getRowValue();
        AccountType newType = event.getNewValue();

        if (account != newAccounts) {
            account.setType(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        account.setType(newType);

        if (newType == AccountType.CREDIT) {
            tcCreditLine.setEditable(true);
        } else {
            account.setCreditLine(null);
            tcCreditLine.setEditable(false);
        }

        tbvAccounts.refresh();

    }

    /**
     *
     * @param event
     */
    private void handleCreditLine(TableColumn.CellEditEvent<Account, Double> event) {
        Account account = event.getRowValue();
        Double newValue = event.getNewValue();

        if (account != newAccounts || account.getType() != AccountType.CREDIT) {
            account.setCreditLine(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        if (newValue == null || newValue < 0) {
            handleAlert("Credit line cannot be negative");
            account.setCreditLine(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        account.setCreditLine(newValue);

    }

    /**
     *
     * @param event
     */
    private void handleBeginBalance(TableColumn.CellEditEvent<Account, Double> event) {
        Account account = event.getRowValue();
        Double newValue = event.getNewValue();

        if (account != newAccounts) {
            account.setBeginBalance(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        if (newValue == null || newValue < 0) {
            handleAlert("Begin balance cannot be negative");
            account.setBeginBalance(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        account.setBeginBalance(newValue);
        account.setBalance(newValue);

    }

    /**
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handleAccountTable(ObservableValue observable, Object oldValue, Object newValue) {

        if (newValue != null) {
            btnDelete.setDisable(false);
            btnMovement.setDisable(false);
        } else {
            btnDelete.setDisable(true);
            btnMovement.setDisable(true);

        }
    }

    /**
     *
     * @param event Button AddAccount se queda pulsado hasta no terminar de
     * crear
     */
    private void handleCreate(ActionEvent event) {
        if (btnAdd.isSelected()) {
            btnDelete.setDisable(true);
            btnRefresh.setDisable(true);
            btnMovement.setDisable(true);
            //tbvAccounts.edit(newAccounts.size() -1, tcDescription,tcType,tcBeginBalance);
            createNewAccount();
        } else {
            btnDelete.setDisable(false);
            btnRefresh.setDisable(false);
            btnMovement.setDisable(false);
            exitNewAccount();
        }

    }

    /**
     * Creacion de la accion cuando se pulsa el boton Add
     */
    private void createNewAccount() {

        Account account = new Account();
        long numero;
        boolean existe;

        // Definicion de los límites (6 a 15 dígitos)
        long minimo = 100_000L;
        long maximo = 1_000_000_000_000_000L;

        do {
            //Generación del número aleatorio
            numero = ThreadLocalRandom.current().nextLong(minimo, maximo);

            //Compruebo si ya existe en la lista de la tabla
            long finalNumero = numero;
            existe = tbvAccounts.getItems().stream()
                    .anyMatch(a -> a.getId() != null && a.getId().equals(finalNumero));

        } while (existe); //Si existe, se repite el proceso

        //Declaración de valores predeterminados
        account.setId(numero);
        Set<Customer> customers = new HashSet<>();
        customers.add(customer);
        account.setCustomers(customers);
        tbvAccounts.getItems().add(account);
        tbvAccounts.getSelectionModel().clearSelection();
        tbvAccounts.getSelectionModel().select(account);
        tbvAccounts.scrollTo(account);
        account.setBeginBalanceTimestamp(new Date());
        account.setType(AccountType.STANDARD);
        account.setBalance(0.0);
        account.setCreditLine(0.0);
        newAccounts = account;
    }

    /**
     * Accion consecuente al pulsar nuevamente create para finalizar de crear
     * una nueva cuenta
     */
    private void exitNewAccount() {
        try {
            tbvAccounts.setEditable(false);
            if (newAccounts != null) {
                if (newAccounts.getDescription() == null
                        || newAccounts.getDescription().trim().isEmpty()) {
                    handleAlert("Description is required");
                    btnAdd.setSelected(true);
                    return;
                }
                if (newAccounts.getType() == AccountType.CREDIT
                        && newAccounts.getCreditLine() == null) {
                    handleAlert("Credit line is required for credit accounts");
                    btnAdd.setSelected(true);
                    return;
                }
                try {
                    client.createAccount_XML(newAccounts);
                } catch (Exception e) {
                    handleAlert("Error creating account");
                    btnAdd.setSelected(true);
                    return;
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        newAccounts = null;
        tbvAccounts.refresh();
    }

    /**
     *
     * @param event accion de refrescar la tabla
     */
    private void handleRefresh(ActionEvent event) {

        try {
            tbvAccounts.setItems(FXCollections.observableArrayList(
                    client.findAccountsByCustomerId_XML(new GenericType<List<Account>>() {
                    }, customer.getId().toString())));
        } catch (Exception e) {
            handleAlert("Error, when refresh table!");
        }
    }

    /**
     * @parama event Manejador del borrado de la cuenta
     */
    private void handleDelete(ActionEvent event) {

        try {
            Account select = tbvAccounts.getSelectionModel().getSelectedItem();
            if (select.getMovements() == null || select.getMovements().isEmpty()) {

                if (handleConfirm("Are you sure you want to delete this account?")) {

                    client.removeAccount(select.getId().toString());
                    tbvAccounts.getItems().remove(select);
                    btnDelete.setDisable(true);
                }
                event.consume();
            } else {
                throw new Exception("You cannot delete the account\nbecause it still has movements");
            }
        } catch (Exception e) {
            handleAlert(e.getMessage());
        }

    }

    /**
     *
     * @param event Maneja el cambio de ventana hacia movement
     */
    private void handleMovementOnAction(ActionEvent event) {
        try {

            Account selectAccount = tbvAccounts.getSelectionModel().getSelectedItem();

            if (selectAccount == null) {
                handleAlert("You must select an account first");
                return;
            }
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("Movement.fxml"));
            Parent root = loader.load();

            MovementController controller = loader.getController();
            controller.setAccount(selectAccount);
            controller.init(this.stage, root);

        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            handleAlert("Error, when going to movement!");
        }
    }

    /**
     *
     * @param event Manejador del boton exit
     */
    private void handleExitOnAction(Event event) {
        try {

            if (handleConfirm("Are you sure you want to go out?")) {
                //Lanzamos la ventana emergente para pedir confirmación de salida
                Stage stage = (Stage) btnExit.getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("SignIn.fxml"));
                Parent root = loader.load();

                SignInController controller = loader.getController();
                controller.init(this.stage, root);
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
     * @param mensaje de error en el programa
     */
    private void handleAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     *
     * @param mensaje para confirmar la salida
     * @return Devuelve un mensaje de confirmacion
     */
    private boolean handleConfirm(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // showAndWait() devuelve un Optional que contiene el botón presionado
        Optional<ButtonType> result = alert.showAndWait();

        // Retorna true solo si el usuario presionó el botón OK
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
