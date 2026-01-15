/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import proyectoCRUD.ui.AccountController;
import proyectoCRUD.ui.SignInController;

/**
 *
 * @author felipe
 */
public class SignInApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/SignIn.fxml"));
        Parent root = (Parent)loader.load();
        SignInController controller = loader.getController();
        
        controller.init(stage, root);
       
        
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
