/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoCRUD.ui;

import proyectoCRUD.model.Customer;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import proyectoCRUD.logic.CustomerRESTClient;

/**
 * Clase de controlador FXML para la ventana Iniciar sesión.
 * Maneja el inicio de sesión del usuario, la navegación hasta el registro y la salida de la aplicación.
 * Interactúa con CustomerRESTClient para la autenticación del usuario.
 * @author Luis Felipe Acosta Osorno
 */
public class ProjectInterfacesController {

    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Hyperlink Register;
    @FXML
    private Button btnLogin;
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
        stage.setTitle("Sign In");
        //La ventana no es redimensionable
        stage.setResizable(false);
        //El botón Login esta deshabilitado y el botón Exit esta habilitado.
        btnLogin.setDisable(true);
        btnExit.setDisable(false);
        //Asociar eventos a manejadores
        Register.setOnAction(this::handleRegisterOnAction);
        btnLogin.setOnAction(this::handleLoginOnAction);
        btnExit.setOnAction(this::handleExitOnAction);
        tfEmail.textProperty().addListener(this::textChangeListener);
        pfPassword.textProperty().addListener(this::textChangeListener);
        tfEmail.focusedProperty().addListener(this::handletfEmailFocusChange);
        pfPassword.focusedProperty().addListener(this::handlepfPasswordFocusChange);

        //Mostrar la ventana
        stage.show();
    }
    /**
    * Inicializa la etapa principal de la ventana Iniciar sesión.
    * Configura la escena, las propiedades del escenario (título, redimensionable), los estados de control iniciales,
    * y oyentes para cambios de campo y enfoque, y controladores de acciones para botones e hipervínculos.
    * @param stage La etapa principal de esta ventana.
    * @param root El nodo raíz del diseño FXML para esta escena..
    */
   

    private void handleAlert(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    /**
     * Muestra un cuadro de diálogo de alerta de error con un mensaje específico.
     * @param mensaje El mensaje de error que se mostrará en la alerta.
     */

    private void handleRegisterOnAction(ActionEvent event) {
        try {
           
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            
           SignUpController controller = loader.getController();
            controller.init(this.stage,root);
            

        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            handleAlert("¡Error, when going to registry!");
        }
    }
    /**
    * Maneja la acción cuando se hace clic en el hipervínculo Registrar.
    * Carga y navega a la ventana de registro (SignUp.fxml).
    * Registra y muestra una alerta de error si falla la navegación.
    * @param event Evento ActionEvent generado para el hipervínculo. 
     */

    private void handleLoginOnAction(ActionEvent event) {
        try {
            //Comprobación de los campos
            if(!tfEmail.getText().matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || tfEmail.getText().isEmpty()){
                throw new Exception("¡Email and password must be to filled!");
            }
            //Crear un objeto customer
            CustomerRESTClient client = new CustomerRESTClient();

            Customer customer = client.findCustomerByEmailPassword_XML(Customer.class,
                    tfEmail.getText().trim(), pfPassword.getText().trim());
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("¡Welcome "+customer.getFirstName()+"!");
            alert.showAndWait();
        //Abrir la ventana de change password
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePassword.fxml"));
            Parent root = loader.load();
            ChangePasswordController controller = loader.getController();
        
            controller.setCustomer(customer);
            controller.init(this.stage, root);
          

            
        } catch (NotAuthorizedException a) {
            LOGGER.warning(a.getMessage());
            handleAlert("¡The email or password is incorrect!");
            tfEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            pfPassword.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            
            
        } catch (InternalServerErrorException b) {
            LOGGER.warning(b.getMessage());
            handleAlert("¡Internal server not found,\n"
                    + "please try again after a few minutes,\n "
                    + "if the problem persists, contact you company!");
            
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            handleAlert("¡Email and password must be to filled!");
            tfEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            pfPassword.setStyle("-fx-border-color: red; -fx-border-width: 2px;");

        }
    }

    /**
    * Maneja la acción cuando se hace clic en el botón Iniciar sesión.
    * Autentica al usuario llamando al cliente REST con el correo electrónico y la contraseña proporcionados.
    * Al iniciar sesión correctamente, muestra un mensaje de bienvenida.
    * Maneja excepciones específicas:
    * {NotAuthorizedException} para credenciales incorrectas (muestra alerta de error y resalta campos).
    * {InternalServerErrorException} para problemas del lado del servidor (muestra una alerta de error del servidor).
    * Otras excepciones para errores generales (por ejemplo, campos vacíos que no se detectan en los controles).
    * @param event Evento ActionEvent generado para el botón Login.
    */
    private void textChangeListener(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    handleChecks();
    }
    /**
     * @param llama al evento hanleChecks
     */
    private void handleChecks() {
        boolean fEmail = fEmail();
        boolean fPassword = fPassword();
        btnLogin.setDisable(!(fEmail && fPassword));
    }
    /**
    * Comprueba si los campos de correo electrónico y contraseña están completos.
    * Habilita el botón Iniciar sesión solo si {#fEmail()} y {#fPassword()} devuelven verdadero.
    */
    private boolean fEmail() {
        boolean isfEmail = tfEmail.getText().isEmpty();
        return !isfEmail;
    }
    /**
    * Comprueba si el campo de texto del correo electrónico *no* está vacío.
    * * @return verdadero si el campo de correo electrónico contiene texto, falso en caso contrario.
    */

    private boolean fPassword() {
        boolean isfPassword = pfPassword.getText().isEmpty();
        return !isfPassword;
    }
    /**
    * Comprueba si el campo de texto de la contraseña *no* está vacío.
    * @return verdadero si el campo de contraseña contiene texto; falso en caso contrario.
     */

    private void handleExitOnAction(ActionEvent event) {

        Alert alert = new Alert(AlertType.CONFIRMATION,
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
    /**
    * Maneja la acción cuando se hace clic en el botón Salir.
    * Muestra un cuadro de diálogo de confirmación y cierra la etapa de solicitud si el usuario confirma.
    * * @param event Evento ActionEvent generado para el botón Salir.
     */
    private void handlepfPasswordFocusChange(ObservableValue observable, 
            boolean oldValue, boolean newValue){
        
        if(newValue){
        pfPassword.setStyle("-fx-border-color: grey; -fx-border-width: 2px;");
        }
        
        
    }
    /**
    * Maneja el evento de cambio de enfoque para el campo de contraseña.
    * Cuando el campo gana foco (newValue es verdadero), restablece el estilo del borde a gris, 
    * eliminar cualquier resaltado de error anterior. 
    * * @param observable El valor observable que se escucha.
    * @param oldValue El estado de enfoque anterior.
    * @param newValue El nuevo estado de foco (verdadero si el campo tiene foco).
    */
    
    private void handletfEmailFocusChange(ObservableValue observable, 
            boolean oldValue, boolean newValue){
        
            if(newValue){
            tfEmail.setStyle("-fx-border-color: grey; -fx-border-width: 2px;");
            }
    
    }
    /**
    * Maneja el evento de cambio de enfoque para el campo de texto del correo electrónico.
    * Cuando el campo gana foco (newValue es verdadero), restablece el estilo del borde a gris, 
    * eliminar cualquier resaltado de error anterior.
    * * @param observable El valor observable que se escucha.
    * @param oldValue El estado de enfoque anterior.
    * @param newValue El nuevo estado de foco (verdadero si el campo tiene foco).
    */

}