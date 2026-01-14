/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.io.IOException;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import proyectoCRUD.logic.MovementRESTClient;
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
    /*@FXML
    private TableView tbMovement;
    @FXML
    private TableColumn tbColDate;
    @FXML
    private TableColumn tbColAmount;
    @FXML
    private TableColumn tbColType;
    @FXML
    private TableColumn tbColBalance;
    @FXML
     */
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

    private SplitMenuButton selectAccount;
    @FXML
    private DatePicker datePickDesde;
    @FXML
    private DatePicker datePickHasta;

    private Customer customer;
    private Stage stage;
    private static final Logger LOGGER = Logger.getLogger("ProjectInterfacesApplication.ui");

    MovementRESTClient restClient = new MovementRESTClient();

    public void init(Stage stage, Parent root) {
        try {
            this.stage = stage;
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setTitle("Movements");
            stage.setResizable(false);

            //stage.setOnCloseRequest();
            btNewMovement.setDisable(false);
            btUndo.setDisable(true);
            btCancel.setDisable(false);

            btNewMovement.setOnAction(this::handlebtNewMovementOnAction);
            btUndo.setOnAction(this::handlebtUndoOnAction);
            btCancel.setOnAction(this::handlebtCancelOnAction);

            tbColDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            tbColAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            tbColType.setCellValueFactory(new PropertyValueFactory<>("description"));
            tbColBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
            tbMovement.getSelectionModel().selectedItemProperty().addListener(this::handleMovementTableSelectionChanged);

            long accountId = 2654785441L;
            String id = String.valueOf(accountId);

            ObservableList<Movement> movements = FXCollections.observableArrayList(restClient.findMovementByAccount_XML(
                    new GenericType<List<Movement>>() {},id));
            
            tbMovement.setItems(movements);
            LOGGER.info(movements.toString());

        } catch (Exception e) {
            //new Alert(AlertType.INFORMATION,e.getLocalizedMessage()).showAndWait();
            //LOGGER.warning(e.getLocalizedMessage());
            LOGGER.info(e.getMessage());
        }

        stage.show();

    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private void handleMovementTableSelectionChanged(ObservableValue observable, Object odlValue, Object newValue) {

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
        tbMovement.getItems().remove(tbMovement.getSelectionModel().getSelectedItem());
        tbMovement.refresh();
    }

    private void handlebtNewMovementOnAction(ActionEvent event) {
        //tbMovement.getItems().add(new Movement(tbColDate.getTimestamp(),tbColAmount.getAmount()));

    }

}
