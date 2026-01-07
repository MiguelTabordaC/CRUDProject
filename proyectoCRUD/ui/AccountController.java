/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnExit;
    private static final Logger LOGGER = Logger.getLogger("projectinterfaces.ui");
    private Stage stage;
    private String password;

    public void init(Stage stage, Parent root) {

        //Se crea la escena asociada al grafico de root.
        LOGGER.info("Initializing window");
        //Asociamos la escena a la primera ventana.
        Scene scene = new Scene(root);
        //Se establecen las propiedades de la vetana.
        stage.setScene(scene);
        this.stage=stage;
        //Establecer el titulo de la ventana
        stage.setTitle("Account");
        //La ventana no es redimensionable
        //stage.setResizable(false);
        //El botón Login esta deshabilitado y el botón Exit esta habilitado.
        //btnLogin.setDisable(true);
        //btnExit.setDisable(false);
        //Asociar eventos a manejadores
        btnExit.setOnAction(this::handleExitOnAction);
        //Mostrar la ventana
        stage.show();
    }
     private void handleExitOnAction(ActionEvent event) {

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

    }
     private void handleClose(WindowEvent event){
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
            
    }
}
