/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 *
 * @author luisf
 */
public class MenuController {

    private MenuItem menuExit;
    private MenuItem menuLogOut;
    private MenuItem helpAccount;
    private MenuItem helpMovement;
    private MenuItem helpCustomer;
    private MenuItem helpAboutApp;
    private Stage stage;

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
        Scene scene = new Scene(root);
        stage.setScene(scene);
        this.stage = stage;
        menuExit.setOnAction(this::handleExit);
        menuLogOut.setOnAction(this::handleLogout);
        helpAccount.setOnAction(this::handleAccount);
        helpMovement.setOnAction(this::handleMovement);
        helpCustomer.setOnAction(this::handleCustomer);
        helpAboutApp.setOnAction(this::handleAboutApp);

    }

    private void handleExit(ActionEvent event) {
        //Salimos de la aplicación por completo
        Platform.exit();
    }
    private void handleLogout(){
        stage.close();
    }
    private void handleAccount(){
        
    }
    private void handleMovement(){
        
    }
    private void handleCustomer(){
        
    }
    private void handleAboutApp(){
        
    }

}
