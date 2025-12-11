/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import proyectoCRUD.ui.ChangePasswordController;
import proyectoCRUD.ui.ProjectInterfacesController;

/**
 *
 * @author miguel
 */
public class ChangePasswordAplication extends Application {
              
    @Override
    public void start(Stage stage) throws Exception {
         
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/ProyectoSignIn.fxml"));
        Parent root = (Parent)loader.load();
        ProjectInterfacesController controller = loader.getController();
        
        controller.init(stage, root);*/
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/ChangePassword.fxml"));
        Parent root = (Parent)loader.load();
        ChangePasswordController controller = loader.getController();
        controller.init(stage, root);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
}
