/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * @author DefaultPC
 */
public class TCPNode extends Application {
    public static Socket sock;
    public static BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in)), receiveRead;
    public static PrintWriter pwrite;
    public static OutputStream ostream;
    public static InputStream istream;

    public static ServerSocket sersock;

    //GUI elements
    private Button chooseClient = new Button("Client");
    private Button chooseServer = new Button("Server");

    private TextField chooseIP = new TextField();
    private TextField chooseSocket = new TextField();

    private TextField enterMessage = new TextField();
    private Button sendMessage = new Button("Send");


    public static void main(String[] args) throws Exception {
        launch(args);


        /*System.out.println("Do you want to connect to another client? Please enter 'Yes' or 'No'");
        String clientOrServer = keyRead.readLine();
        
        if(clientOrServer.equalsIgnoreCase("Yes")){
            startClient();
        } else
            startServer();*/
    }

    public static void startClient() throws IOException {
        sock = new Socket("127.0.0.1", 3000);
        ostream = sock.getOutputStream();
        pwrite = new PrintWriter(ostream, true);
        istream = sock.getInputStream();
        receiveRead = new BufferedReader(new InputStreamReader(istream));
        System.out.println("Start the chitchat, type and press Enter key (Enter endTrigger to stop communication)");
        String receiveMessage, sendMessage;
        String clientTimeSent;
        String clientTimeReceieved;
        boolean endTrigger = false;
        while (!endTrigger) {
            sendMessage = keyRead.readLine();  // keyboard reading
            clientTimeSent = new SimpleDateFormat("HH:mm:ss.S").format(new java.util.Date());
            System.out.println("Message '" + sendMessage + "' sent at " + clientTimeSent + ".");
            if (sendMessage.equals("endTrigger"))
                endTrigger = true;
            pwrite.println(sendMessage);       // sending to server
            pwrite.flush();// flush the data
            if (!endTrigger && (receiveMessage = receiveRead.readLine()) != null) { //receive from server

                clientTimeReceieved = new SimpleDateFormat("HH:mm:ss.S").format(new java.util.Date());
                System.out.println("Message '" + receiveMessage + "' received at " + clientTimeReceieved + "."); // displaying at DOS prompt
                if (receiveMessage.equals("endTrigger"))
                    endTrigger = true;
            }
        }
        System.out.println("Closing socket.");
        sock.close();
    }

    public static void startServer() throws IOException {
        sersock = new ServerSocket(3000);
        System.out.println("Server  ready for chatting");
        sock = sersock.accept();
        ostream = sock.getOutputStream();
        pwrite = new PrintWriter(ostream, true);
        istream = sock.getInputStream();
        receiveRead = new BufferedReader(new InputStreamReader(istream));
        String receiveMessage, sendMessage;
        String serverTimeSent;
        String serverTimeReceived;
        boolean endTrigger = false;
        while (!endTrigger) {
            if ((receiveMessage = receiveRead.readLine()) != null) {
                serverTimeReceived = new SimpleDateFormat("HH:mm:ss.S").format(new java.util.Date());
                System.out.println("Message '" + receiveMessage + "' received at " + serverTimeReceived + ".");
                if (receiveMessage.equals("endTrigger"))
                    endTrigger = true;
            }
            //Allows server to send communication if endTrigger = false
            if (!endTrigger) {
                sendMessage = keyRead.readLine();
                serverTimeSent = new SimpleDateFormat("HH:mm:ss.S").format(new java.util.Date());
                System.out.println("Message '" + sendMessage + "' sent at " + serverTimeSent + ".");
                pwrite.println(sendMessage);
                pwrite.flush();
                if (sendMessage.equals("endTrigger"))
                    endTrigger = true;
            }
        }
        System.out.println("Closing socket.");
        sock.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox startScreen = new VBox(20);
        HBox roles = new HBox(10);
        HBox connections = new HBox(10);

        //Display buttons for setting up connection
        roles.getChildren().add(new Label("Choose your role:\nSelecting 'Client' will allow you to join a session.\nSelecting" +
                "'Server' will allow you to create your own session."));
        roles.getChildren().add(chooseClient);
        roles.getChildren().add(chooseServer);

        connections.getChildren().add(new Label("Enter an IP Address:"));
        connections.getChildren().add(chooseIP);

        connections.getChildren().add(new Label("Enter a socket number:"));
        connections.getChildren().add(chooseSocket);

        startScreen.setAlignment(Pos.CENTER);
        roles.setAlignment(Pos.CENTER);
        connections.setAlignment(Pos.CENTER);
        startScreen.getChildren().addAll(roles, connections);

        //Set functionality for buttons
        chooseClient.setOnMouseClicked(event -> {
                    try {
                        startClient();
                    } catch (IOException e) {
                        System.out.println("IO Exception.");
                    }
                }
        );

        chooseServer.setOnMouseClicked(event -> {
                    try {
                        startServer();
                    } catch (IOException e) {
                        System.out.println("IO Exception.");
                    }
                }
        );


        Scene firstScreen = new Scene(startScreen, 700, 700);
        primaryStage.setTitle("CS 4504 Phase 2");
        primaryStage.setScene(firstScreen);
        primaryStage.show();

    }
}
