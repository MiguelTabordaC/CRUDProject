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
import proyectoCRUD.ui.SignUpController;

/**
 *
 * @author david
 */
public class SignUpApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("proyectoCRUD/ui/SignUp.fxml"));
        Parent root = (Parent)loader.load();
        SignUpController controller = loader.getController();
        controller.init(stage, root);
   
    }
    
    /**
     * @param args the command line arguments
     */
public static void main(String[] args) {
        launch(args);
    }
    
}
