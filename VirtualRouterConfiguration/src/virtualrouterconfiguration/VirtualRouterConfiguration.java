/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualrouterconfiguration;

import configuration.ConfigurationInterface;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sharedPackage.RoutingTableKey;

/**
 *
 * @author maria afara
 */
public class VirtualRouterConfiguration extends Application {

    //ip address 10.0.0.1 hostname maria port 565
    //////be matra77 bsyev le 3mlt cnx 3lyhon  y3ne le hene my neighbors so hek 
    //lama 3m zid 7da 3l rip wmno mn dmn my neighbors ma bzido
    String command;
    String lastCommand;
    ArrayList<Integer> ports;
    ArrayList<RoutingTableKey> neighbors;
    ArrayList<RoutingTableKey> establishedneighbors;
    int port = -1;

    ConfigurationInterface configurationinterface;

    @Override
    public void start(Stage stage) throws RemoteException {

        ObservaleStringBuffer buffer = new ObservaleStringBuffer();
        HBox hostnameConnectionbox = new HBox();
        TextField txtHostname = new TextField();
        TextField txtRegistryPort = new TextField();
        txtRegistryPort.setPrefWidth(120);
        txtHostname.setPrefWidth(120);
        Button btnConnect = new Button("Connect");
        Label ip = new Label();
        TextField txtcommand = new TextField();
        txtcommand.setDisable(true);
        txtcommand.setPrefWidth(425);
        btnConnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    Registry registry = LocateRegistry.getRegistry("localhost", Integer.parseInt(txtRegistryPort.getText()));

                    configurationinterface = (ConfigurationInterface) registry.lookup(txtHostname.getText());
                    buffer.append("enter a port.................");
                    buffer.append(System.getProperty("line.separator"));
                    txtRegistryPort.setDisable(true);
                    stage.setTitle("Configuration of router " + configurationinterface.getHostname());
                    txtHostname.setDisable(true);
                    ip.setText(configurationinterface.getLocalHost().getHostAddress());
                    txtcommand.setDisable(false);

                } catch (RemoteException ex) {
                    Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        hostnameConnectionbox.getChildren().addAll(txtHostname, txtRegistryPort, btnConnect, ip);
        ports = new ArrayList<>();
        neighbors = new ArrayList<>();
        establishedneighbors = new ArrayList<>();

        Label lbl = new Label("Router#");
        lbl.setPrefWidth(125);
        TextArea textArea = new TextArea();
        //textArea.setPrefHeight(650);
        textArea.setEditable(false);
        textArea.textProperty().bind(buffer);

        HBox commandsbox = new HBox();

        // buffer.append("Router#");
        txtcommand.setOnAction(e -> {
            command = txtcommand.getText();
            lastCommand = lbl.getText();

            switch (lastCommand) {
                case "Router#":
                    switch (command) {
                        case "config t":
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            lbl.setText("Router(config)#");
                            break;

                        case "configuration terminal":
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            lbl.setText("Router(config)#");
                            break;

                        case "show ip route":

                            break;
                        case "":
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            break;
                        default:
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            buffer.append("%unknown command or computer name , or unable to find computer address");
                            buffer.append(System.getProperty("line.separator"));
                            break;
                    }

                    break;

                case "Router(config)#":
                    String[] config_command_array = command.split(" ");
                    String first_config_command = config_command_array[0];
                    switch (first_config_command) {
                        case "interfaces":
                            int sizeofinterfaces = config_command_array.length;
                            if (sizeofinterfaces > 1) {
                                System.out.println("size " + sizeofinterfaces);
                                try {
                                    for (int j = 0; j < sizeofinterfaces - 1; j++) {

                                        ports.add(Integer.parseInt(config_command_array[j + 1]));
                                        System.out.println("->" + config_command_array[j + 1] + " added");
                                    }
                                    for (int k = 0; k < ports.size(); k++) {

                                        //initializePort 
                                        System.out.println("trying to initialize k=" + k);
                                        configurationinterface.initializePort(ports.get(k));
                                        System.out.println("interface " + ports.get(k) + " is initialized");
                                    }
                                    System.out.println("interfaces.....");
                                    buffer.append(lbl.getText() + " " + command + System.getProperty("line.separator"));

                                } catch (NumberFormatException efe) {
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("%unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));
                                    System.out.println("syntax error");
                                } catch (RemoteException ex) {
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("%unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));
                                    Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                buffer.append(lbl.getText() + " " + command);
                                buffer.append(System.getProperty("line.separator"));
                                buffer.append("%unknown command or computer name , or unable to find computer address");
                                buffer.append(System.getProperty("line.separator"));
                                System.out.println("syntax error");

                            }

                            break;
                        case "interface":

                            boolean in = false;

                            if (config_command_array.length != 2) {
                                System.out.println("syntax error");
                                buffer.append(lbl.getText() + " " + command);
                                buffer.append(System.getProperty("line.separator"));
                                buffer.append("%unknown command or computer name , or unable to find computer address");
                                buffer.append(System.getProperty("line.separator"));
                            } else {
                                try {
                                    // checking valid integer using parseInt() method 
                                    port = Integer.parseInt(config_command_array[1]);
                                    for (int k = 0; k < ports.size(); k++) {
                                        if (ports.get(k) == port) {
                                            in = true;
                                        }
                                    }
                                    if (!in) {
                                        port = -1;
                                        System.out.println("syntax error");
                                        buffer.append(lbl.getText() + " " + command);
                                        buffer.append(System.getProperty("line.separator"));
                                        buffer.append("%unknown command or computer name , or unable to find computer address");
                                        buffer.append(System.getProperty("line.separator"));
                                    } else {
                                        buffer.append(lbl.getText() + " " + command);
                                        buffer.append(System.getProperty("line.separator"));
                                        lbl.setText("Router(config_if)#");
                                    }

                                } catch (NumberFormatException nfe) {
                                    System.out.println("syntax error");
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("%unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));
                                }
                            }
                            break;

                        case "router":
                            String second_config_command;
                            if (config_command_array.length == 2 && config_command_array[1].equals("rip")) {
                                second_config_command = config_command_array[1];

                                buffer.append(lbl.getText() + " " + command);
                                buffer.append(System.getProperty("line.separator"));
                                lbl.setText("Router(config_router)#");
                            } else {
                                buffer.append(lbl.getText() + " " + command);
                                buffer.append(System.getProperty("line.separator"));
                                buffer.append("%unknown command or computer name , or unable to find computer address");
                                buffer.append(System.getProperty("line.separator"));

                            }
                            break;

                        case "exit":
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            lbl.setText("Router#");
                            break;
                        case "":
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            break;
                        default:
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            buffer.append("%unknown command or computer name , or unable to find computer address");
                            buffer.append(System.getProperty("line.separator"));
                            break;

                    }
                    break;

                case "Router(config_if)#":
                    String[] config_if_command_array = command.split(" ");
                    String first_config_if_command = config_if_command_array[0];
                    switch (first_config_if_command) {
                        case "ip":
                            if (config_if_command_array.length != 7
                                    || !config_if_command_array[1].equals("address")
                                    || !config_if_command_array[3].equals("hostname")
                                    || !config_if_command_array[5].equals("port")) {
                                System.out.println("syntax error");
                                buffer.append(lbl.getText() + " " + command);
                                buffer.append(System.getProperty("line.separator"));
                                buffer.append("%unknown command or computer name , or unable to find computer address");
                                buffer.append(System.getProperty("line.separator"));
                            } else {
                                try {
                                    int nextport = Integer.parseInt(config_if_command_array[6]);
                                    InetAddress address = InetAddress.getByName(config_if_command_array[2]);
                                    String nexthostname = config_if_command_array[4];
                                    System.out.println("initialize conx at " + port + " with " + address.getHostAddress() + "-" + nexthostname + "to" + nextport);

                                    //initializeConx
                                    configurationinterface.initializeConnection(port, address, nexthostname, nextport);

                                    ///hon bade ziddd l neighborsss
                                    neighbors.add(new RoutingTableKey(address, nexthostname));
//                                    for (RoutingTableKey neighbor : neighbors) {
//                                        buffer.append(System.getProperty("line.separator"));
//                                        buffer.append("-->" + neighbor.getIp() + " " + neighbor.getHostname());
//                                        buffer.append(System.getProperty("line.separator"));
//                                    }

                                    /////////////////
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    lbl.setText("Router(config_if)#");
                                } catch (NumberFormatException efee) {
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("%unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));
                                } catch (UnknownHostException ex) {
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("%unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));
                                } catch (RemoteException ex) {
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("%unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));

                                    Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            break;

                        case "exit":
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            lbl.setText("Router(config)#");
                            break;
                        case "":
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            break;
                        default:
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            buffer.append("%unknown command or computer name , or unable to find computer address");
                            buffer.append(System.getProperty("line.separator"));
                            break;
                    }
                    break;
                case "Router(config_router)#":
                    String[] config_router_command_array = command.split(" ");
                    String first_config_router_command = config_router_command_array[0];
                    //network ip hostname
                    switch (first_config_router_command) {
                        case "network":
                            if (config_router_command_array.length == 3) {
                                try {
                                    InetAddress address = InetAddress.getByName(config_router_command_array[1]);
                                    String nexthostname = config_router_command_array[2];
                                    ////iza m3moul establish abel aw la lnetwork 
                                    boolean isneighbor = false, alreadyestblished = false;
                                    RoutingTableKey net = new RoutingTableKey(address, nexthostname);
                                    for (RoutingTableKey neighbor : neighbors) {
                                        //buffer.append("-->" + neighbor.getIp() + " " + neighbor.getHostname());
                                        if (net.equals(neighbor)) {
                                            isneighbor = true;
                                            //  buffer.append("--is neigh--");
                                        } else {
                                            // buffer.append("--not neigh--");

                                        }
                                    }
                                    if (isneighbor) {
//ip address 192.168.182.1 hostname r1 port 1

///network 192.168.182.1 r1
                                        boolean isestablishedalread = false;
                                        for (RoutingTableKey establishedneighbor : establishedneighbors) {
                                            if (establishedneighbor.equals(net)) {
                                                //already established
                                                isestablishedalread = true;
                                                buffer.append(lbl.getText() + " " + command);
                                                buffer.append(System.getProperty("line.separator"));
                                                buffer.append("already established%unknown command or computer name , or unable to find computer address");
                                                buffer.append(System.getProperty("line.separator"));
                                            }

                                        }
                                        if (!isestablishedalread) {
                                            buffer.append(lbl.getText() + " " + command);
                                            buffer.append(System.getProperty("line.separator"));
                                            establishedneighbors.add(net);
                                            System.out.println("network added");
//                                            break;
                                        }
                                    } else {//not neighbor so mamnu3
                                        buffer.append(lbl.getText() + " " + command);
                                        buffer.append(System.getProperty("line.separator"));
                                        buffer.append("is neigh %unknown command or computer name , or unable to find computer address");
                                        buffer.append(System.getProperty("line.separator"));

                                    }

                                } catch (UnknownHostException ex) {
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("address %unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));
                                }
                            } else {
                                buffer.append(lbl.getText() + " " + command);
                                buffer.append(System.getProperty("line.separator"));
                                buffer.append("command %unknown command or computer name , or unable to find computer address");
                                buffer.append(System.getProperty("line.separator"));

                            }
                            break;
                        case "exit":
                            if (establishedneighbors.size() > 0) {
                                try {
                                    //initialize routing protocol
                                    configurationinterface.initializeRoutingProtocol(establishedneighbors);
                                } catch (RemoteException ex) {
                                    buffer.append(lbl.getText() + " " + command);
                                    buffer.append(System.getProperty("line.separator"));
                                    buffer.append("command %unknown command or computer name , or unable to find computer address");
                                    buffer.append(System.getProperty("line.separator"));
                                    Logger.getLogger(VirtualRouterConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            buffer.append(lbl.getText() + " " + command);
                            buffer.append(System.getProperty("line.separator"));
                            lbl.setText("Router(config)#");
                            break;
                    }
                    break;

                case "":
                    buffer.append(lbl.getText() + " " + command);
                    buffer.append(System.getProperty("line.separator"));
                    break;
                default:
                    buffer.append(lbl.getText() + " " + command);
                    buffer.append(System.getProperty("line.separator"));
                    buffer.append("%unknown command or computer name , or unable to find computer address");
                    buffer.append(System.getProperty("line.separator"));
                    break;
            }
        });

        VBox rr = new VBox(3);
        rr.setVgrow(textArea, Priority.ALWAYS);
        commandsbox.getChildren().addAll(lbl, txtcommand);

        rr.getChildren().addAll(hostnameConnectionbox, textArea, commandsbox);

        stage.setScene(new Scene(rr, 600, 250));

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
