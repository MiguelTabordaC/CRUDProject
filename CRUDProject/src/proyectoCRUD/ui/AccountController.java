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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.AccountRESTClient;
import proyectoCRUD.model.Account;
import proyectoCRUD.model.AccountType;
import proyectoCRUD.model.Customer;

/**
 * Controlador de la interfaz gráfica para la gestión de Cuentas Bancarias (Account.fxml).
 * Esta clase maneja la lógica de visualización, creación, edición y borrado de cuentas,
 * así como la navegación hacia la vista de movimientos. Se comunica con el servidor
 * mediante {@link AccountRESTClient}.
 * * @author luis felipe
 */
public class AccountController {

    @FXML
    private HBox menuAccount;
    @FXML
    private MenuController menuAccountController;
    ;
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
     * Inicializa la etapa principal de la ventana de Gestión de Cuentas.
     * Configura la escena, título, propiedades de las columnas (CellFactories),
     * manejadores de eventos de edición y carga los datos iniciales del cliente.
     * @param stage La etapa (Stage) principal de esta ventana.
     * @param root El nodo raíz del diseño FXML cargado.
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

            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(param -> new TextFieldTableCell<Account, String>(new javafx.util.converter.DefaultStringConverter()) {
                @Override
                public void startEdit() {
                    Account account = getTableView().getItems().get(getIndex());

                    if (newAccounts != null && account != newAccounts) {
                        return;
                    }

                    super.startEdit();
                }
            });
            tcDescription.setEditable(true);
            tcDescription.setOnEditCommit(this::handleDescription);

            tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
            tcType.setCellFactory(param -> new ComboBoxTableCell<Account, AccountType>(AccountType.values()) {
                @Override
                public void startEdit() {
                    Account account = getTableView().getItems().get(getIndex());

                    if (account != newAccounts) {
                        return;
                    }
                    super.startEdit();
                }
            });
            tcType.setEditable(true);
            tcType.setOnEditCommit(this::handleType);

            tcBeginBalance.setCellValueFactory(new PropertyValueFactory<>("beginBalance"));
            tcBeginBalance.setCellFactory(param -> new TextFieldTableCell<Account, Double>(new DoubleStringConverter()) {
                @Override
                public void startEdit() {
                    Account account = getTableView().getItems().get(getIndex());

                    if (account != newAccounts) {
                        return;
                    }
                    super.startEdit();
                }
            });
            tcBeginBalance.setEditable(true);
            tcBeginBalance.setOnEditCommit(this::handleBeginBalance);

            tcBalance.setCellValueFactory(
                    new PropertyValueFactory<>("balance"));
            tcBalance.setEditable(false);

            tcCreditLine.setCellValueFactory(new PropertyValueFactory<>("creditLine"));
            tcCreditLine.setCellFactory(param -> new TextFieldTableCell<Account, Double>(new DoubleStringConverter()) {
                @Override
                public void startEdit() {
                    Account account = getTableView().getItems().get(getIndex());

                    if (newAccounts != null && account != newAccounts) {
                        return;
                    }

                    if (account.getType() != AccountType.CREDIT) {
                        return;
                    }

                    super.startEdit();
                }
            });
            tcCreditLine.setEditable(true);
            tcCreditLine.setOnEditCommit(this::handleCreditLine);

            tcBeginBalanceTimestamp.setCellValueFactory(
                    new PropertyValueFactory<>("beginBalanceTimestamp"));
            tcBeginBalanceTimestamp.setEditable(false);
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
            tbvAccounts.setEditable(true);
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
     * Recibe el objeto Cliente desde el controlador anterior (SignIn).
     * Este método es necesario para cargar los datos específicos del usuario logueado.
     * @param customer El objeto Customer autenticado.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Maneja el evento de confirmación de edición en la columna Descripción.
     * Valida que el texto no esté vacío y actualiza el servidor si es una cuenta existente.
     * @param event Evento de edición de celda.
     */
    private void handleDescription(TableColumn.CellEditEvent<Account, String> event) {
        Account account = event.getRowValue();
        String newValue = event.getNewValue();

        if (newAccounts != null && account != newAccounts) {
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

        if (account.getId() != null && account != newAccounts) {
            client.updateAccount_XML(account);
        }
    }

     /**
     * Maneja el evento de cambio de Tipo de Cuenta (Standard/Credit).
     * Ajusta la visibilidad o edición de la línea de crédito según el tipo seleccionado.
     * @param event Evento de edición de celda.
     */
    private void handleType(TableColumn.CellEditEvent<Account, AccountType> event) {
        Account account = event.getRowValue();
        AccountType newType = event.getNewValue();

        if (newAccounts != null && account != newAccounts) {
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

        if (account.getId() != null && account != newAccounts) {
            client.updateAccount_XML(account);
        }

        tbvAccounts.refresh();
    }

    /**
     * Maneja la edición de la Línea de Crédito.
     * Valida que el valor no sea negativo y que la cuenta sea de tipo Crédito.
     * @param event Evento de edición de celda.
     */
    private void handleCreditLine(TableColumn.CellEditEvent<Account, Double> event) {
        Account account = event.getRowValue();
        Double newValue = event.getNewValue();

        if (newAccounts != null && account != newAccounts) {
            account.setCreditLine(event.getOldValue());
            tbvAccounts.refresh();
            return;
        }

        if (account.getType() != AccountType.CREDIT) {
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

        if (account.getId() != null && account != newAccounts) {
            client.updateAccount_XML(account);
        }
    }

    /**
     * Maneja la edición del Balance Inicial.
     * Sincroniza el balance inicial con el balance actual.
     * @param event Evento de edición de celda.
     */
    private void handleBeginBalance(TableColumn.CellEditEvent<Account, Double> event) {
        Account account = event.getRowValue();
        Double newValue = event.getNewValue();

        if (newAccounts != null && account != newAccounts) {
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

        if (account.getId() != null && account != newAccounts) {
            client.updateAccount_XML(account);
        }
    }

    /**
     * Listener para la selección de filas en la tabla.
     * Habilita o deshabilita botones según si hay una fila seleccionada.
     * Se bloquea si estamos en modo creación para evitar perder el foco.
     * @param observable Objeto observable.
     * @param oldValue Valor anterior.
     * @param newValue Nueva fila seleccionada.
     */
    private void handleAccountTable(ObservableValue observable, Object oldValue, Object newValue) {

        if (newAccounts != null) {
            return;
        }

        if (newValue != null) {
            btnDelete.setDisable(false);
            btnMovement.setDisable(false);
        } else {
            btnDelete.setDisable(true);
            btnMovement.setDisable(true);
        }
    }

    /**
     * Manejador del botón "AddAccount" (ToggleButton).
     * Alterna entre iniciar el proceso de creación y confirmar el guardado.
     * * @param event Evento del botón.
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
     * Lógica interna para preparar una nueva cuenta en la tabla.
     * Genera un ID aleatorio, inserta una fila vacía y cambia el estado de la UI
     * (Botón Add -> Save, Botón Delete -> Cancel).
     */
    private void createNewAccount() {
Account account = new Account();
        long numero;
        boolean existe;

        // Definicion de los límites (6 a 15 dígitos)
        long minimo = 100_000L;
        long maximo = 1_000_000_000_000_000L;

        do {
            // Generación del número aleatorio
            numero = ThreadLocalRandom.current().nextLong(minimo, maximo);

            // Compruebo si ya existe en la lista de la tabla
            long finalNumero = numero;
            existe = tbvAccounts.getItems().stream()
                    .anyMatch(a -> a.getId() != null && a.getId().equals(finalNumero));

        } while (existe); 

 
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
        tbvAccounts.setEditable(true); 


        btnAdd.setText("Save");
        btnDelete.setText("Cancel");
        btnDelete.setDisable(false);
        btnRefresh.setDisable(true);
        btnMovement.setDisable(true);
    }

    /**
     * Lógica interna para finalizar la creación y guardar en la base de datos.
     * Realiza validaciones finales y llama al servicio REST.
     */
    private void exitNewAccount() {
        try {
            // Validaciones
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

                    btnAdd.setText("AddAccount");
                    btnDelete.setText("Delete");
                    btnRefresh.setDisable(false);

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
     * Recarga los datos de la tabla desde el servidor.
     * Útil para sincronizar cambios realizados por otros usuarios.
     * @param event Evento del botón Refresh.
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
     * Manejador dual para el botón Delete/Cancel.
     * Si estamos en modo creación, funciona como CANCELAR (elimina la fila temporal).
     * Si estamos en modo normal, funciona como BORRAR (elimina la cuenta de la BD).
     * @param event Evento del botón.
     */
    private void handleDelete(ActionEvent event) {

        try {
            // Modo cancelar
            if (newAccounts != null) {

                tbvAccounts.getItems().remove(newAccounts);
                newAccounts = null;

                tbvAccounts.getSelectionModel().clearSelection();
                tbvAccounts.refresh();

                // Restauracion de botones
                btnAdd.setText("AddAccount");
                btnAdd.setSelected(false);

                btnDelete.setText("Delete");
                btnDelete.setDisable(true);

                btnRefresh.setDisable(false);
                btnMovement.setDisable(true);

                return;
            }

            // Borrado normal
            Account select = tbvAccounts.getSelectionModel().getSelectedItem();

            if (select != null) {
                if (select.getMovements() == null || select.getMovements().isEmpty()) {

                    if (handleConfirm("Are you sure you want to delete this account?")) {

                        client.removeAccount(select.getId().toString());
                        tbvAccounts.getItems().remove(select);
                        btnDelete.setDisable(true);
                        tbvAccounts.getSelectionModel().clearSelection();
                    }
                    event.consume();
                } else {
                    throw new Exception("You cannot delete the account\nbecause it still has movements");
                }
            }
        } catch (Exception e) {
            handleAlert(e.getMessage());
        }
    }

    /**
     * Navega a la ventana de Movimientos (Movement.fxml) para la cuenta seleccionada.
     * @param event Evento del botón Movements.
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
     * Gestiona la salida de la ventana actual hacia la pantalla de Login (SignIn).
     * Solicita confirmación antes de salir.
     * @param event Evento de salida (Botón Exit o cerrar ventana).
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
     * Muestra una alerta de error al usuario.
     * @param mensaje Mensaje descriptivo del error.
     */
    private void handleAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un cuadro de diálogo de confirmación.
     * @param mensaje Pregunta a realizar al usuario.
     * @return true si el usuario pulsa OK, false en caso contrario.
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
