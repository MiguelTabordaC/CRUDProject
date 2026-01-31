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
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

/**
 *
 * @author luisf
 */
public class MenuController {

    @FXML
    public void menuExitApp(ActionEvent event) {
        //Salimos de la aplicación por completo
        Platform.exit();
    }

    @FXML
    public void menuLogOut(ActionEvent event) {
      try {

            Stage stage = getAccountStage(event);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Parent root = loader.load();
            SignInController controller = loader.getController();
            controller.init(stage, root);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Log Out Error", "Could not load the sign-in window.");
        }
    }

    @FXML
    private void helpAccount(ActionEvent event) {
        ventanaAyuda("Help - Accounts", "/proyectoCRUD/ui/resources/helpAccount.html");
    }

    @FXML
    private void helpMovement(ActionEvent event) {
        ventanaAyuda("Help - Movements", "/proyectoCRUD/ui/resources/helpMovement.html");
    }

    @FXML
    private void helpCustomer(ActionEvent event) {
        ventanaAyuda("Help - Customers", "/proyectoCRUD/ui/resources/helpCustomer.html");
    }

    @FXML
    private void helpAboutApp(ActionEvent event) {
        ventanaAyuda("About the App", "/proyectoCRUD/ui/resources/helpAbout.html");
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
      private void ventanaAyuda(String title, String resourcePath) {
        try {
            Stage helpStage = new Stage();
            helpStage.setTitle(title);

            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            java.net.URL url = getClass().getResource(resourcePath);
            
            if (url == null) {
                showAlert("Error", "File not found", "Could not find file: " + resourcePath);
                return;
            }

            webEngine.load(url.toExternalForm());

            Scene scene = new Scene(webView, 600, 400);
            helpStage.setScene(scene);
            helpStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Help Error", "Could not open the help window.");
        }
    }

}
