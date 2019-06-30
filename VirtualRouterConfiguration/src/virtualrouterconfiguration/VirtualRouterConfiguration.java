/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualrouterconfiguration;
import configuration.ConfigurationInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author maria afara
 */
public class VirtualRouterConfiguration extends Application {
    
    int i = 0;
    int nb = 0;
    ConfigurationInterface configurationinterface;

    @Override
    public void start(Stage stage) throws RemoteException {

        ObservaleStringBuffer buffer = new ObservaleStringBuffer();
        HBox hostnameConnectionbox = new HBox();
        TextField txtHostname = new TextField();

        txtHostname.setPrefWidth(150);
        Button btnConnect = new Button("Connect");

        btnConnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    Registry registry = LocateRegistry.getRegistry("localhost", 1099);

                    configurationinterface = (ConfigurationInterface) registry.lookup(txtHostname.getText());
                   buffer.append("enter a port.................");
                    buffer.append(System.getProperty("line.separator"));

                    txtHostname.setDisable(true);
                } catch (RemoteException ex) {
                    Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        hostnameConnectionbox.getChildren().addAll(txtHostname, btnConnect);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.textProperty().bind(buffer);

        // HBox commandsbox = new HBox();
        TextField txtcommand = new TextField();

        txtcommand.setPrefWidth(150);
        TextField textField = new TextField();
        txtcommand.setOnAction(e -> {
            // add your code to be run here
            System.out.println("textFile");
            buffer.append("enter a port.................");

            buffer.append(System.getProperty("line.separator"));
            try {
                configurationinterface.initializePort(Integer.parseInt(txtcommand.getText()));
            } catch (RemoteException ex) {
                Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }

        });


        VBox rr = new VBox(3);
        rr.getChildren().addAll(hostnameConnectionbox, textArea, txtcommand);

        stage.setScene(new Scene(rr, 400, 400));

        stage.setTitle("Configuration");

        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
