/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 *
 * @author luisf
 */
public class MenuController {

    private static final String TITLE_MOVEMENTS = "Movements";
    private static final String TITLE_ACCOUNTS = "Accounts Management";
    private static final String TITLE_SIGNIN = "Sign In";

    @FXML
    public void menuExitApp(ActionEvent event) {
        //Salimos de la aplicación por completo
        Platform.exit();
    }

    @FXML
    public void menuLogOut(ActionEvent event) {
        
        Stage stage = getAccountStage(event);
        
        navigate(event, "SignIn.fxml", TITLE_SIGNIN);
        
         if (stage != null && TITLE_ACCOUNTS.equals(stage.getTitle())) {
            return; 
        }
    }

    @FXML
    private void helpAccount(ActionEvent event) {

    }

    @FXML
    private void helpMovement(ActionEvent event) {

    }

    @FXML
    private void helpCustomer(ActionEvent event) {

    }

    @FXML
    private void helpAboutApp(ActionEvent event) {

    }
    private void navigate(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = getAccountStage(event);
            if (stage == null) return;

            Scene scene = new Scene(root);
            stage.setTitle(title); // IMPORTANTE: Fijamos el título para poder comprobarlo después
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error de Navegación", "No se pudo cargar: " + fxmlFile);
        }
    }
     private Stage getAccountStage(ActionEvent event) {
        Object source = event.getSource();
        
        if (source instanceof MenuItem) {
            // Truco para obtener el stage desde un ítem de menú desplegable
            return (Stage) ((MenuItem) source).getParentPopup().getOwnerWindow();
        } else if (source instanceof Node) {
            // Forma normal para botones
            return (Stage) ((Node) source).getScene().getWindow();
        }
        return null;
    }
     
      private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
