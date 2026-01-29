/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import proyectoCRUD.model.Account;
import proyectoCRUD.ui.MovementController;
import proyectoCRUD.ui.SignInController;

/**
 *
 * @author miguel
 */
public class ChangePasswordAplication extends Application {
              
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/Movement.fxml"));
        Parent root = (Parent)loader.load();
        MovementController controller = loader.getController();
        //el siguiente codigo se borrara cuando felipe pase bien el account
        Account account = new Account();
        account.setId(2654785441L);
        //account.setBalance(10000.00);
        controller.setAccount(account);
        //fin del codigo a eliminar
        
        controller.init(stage, root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
}
