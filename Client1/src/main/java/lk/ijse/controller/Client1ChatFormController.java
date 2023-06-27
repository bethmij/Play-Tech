package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.controller.util.OpenView;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static lk.ijse.controller.Client1LoginFormController.userName;

public class Client1ChatFormController extends Thread implements Initializable {

    public JFXButton btnEmoji;
    public JFXButton btnCamera;
    public JFXButton btnSend;
    public TextField txt;
    public VBox VBox;
    public Label lblName;
    public ScrollPane scrollPane;
    public ImageView imgView;
    public AnchorPane emojiPane;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    FileChooser fileChooser;
    File filePath;

    DataOutputStream dataOutputStream;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectSocket();
        lblName.setText(userName);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        emojiPane.setVisible(false);

        Platform.runLater(() -> {
            scrollPane.lookup(".viewport").setStyle("-fx-background-color: transparent;");
            scrollPane.lookup(".scroll-bar").setStyle("-fx-background-color: transparent;");
            scrollPane.lookup(".scroll-bar:vertical").setStyle("-fx-background-color: transparent;");

        });

        /*byte_array = bytes.fromhex(hex_value)
        string_value = byte_array.decode('utf-8')*/

    }

    @Override
    public void run() {

       try {
           while (true){

               String message = reader.readLine();

               String[] parts = message.split("\\Q" + "::" + "\\E");
               String beforeCharacter = parts[0];


               String afterCharacter = parts[1];

               if(afterCharacter.startsWith("img")){
                   String path = "";
                   ImageView imageView = null;

                   if(!afterCharacter.endsWith("emojii")) {
                       //for image
                       String[] part = message.split("\\Q" + "img" + "\\E");
                       path = part[1];
                       Image image = new Image(path, 100, 100, true, true);

                       imageView = new ImageView(image);
                       imageView.setFitHeight(120);
                       imageView.setFitWidth(120);
                   }else{
                       String[] part = message.split("\\Q" + "img" + "\\E");
                       String[] emojiPath = part[1].split("\\Q" + "emojii" + "\\E");;
                       path = emojiPath[0];

                       Image image = new Image(path);
                       imageView = new ImageView(image);
                       imageView.setFitHeight(50);
                       imageView.setFitWidth(50);
                   }

                   HBox hBox = new HBox(10);
                   hBox.setAlignment(Pos.BOTTOM_RIGHT);

                   if (!lblName.getText().equals(beforeCharacter)){
                       //System.out.println("not "+lblName.getText());
                       //HBox.setAlignment(Pos.TOP_LEFT);
                       //HBox.setPadding(new Insets(5,10,5,5));

                       VBox.setAlignment(Pos.BOTTOM_LEFT);
                       hBox.setAlignment(Pos.CENTER_LEFT);


                       Text text1 = new Text(" "+beforeCharacter+" : ");
                       text1.setStyle("-fx-font-size: 15px");
                       hBox.getChildren().add(text1);
                       hBox.getChildren().add(imageView);

                   }else {
                       hBox.setAlignment(Pos.BOTTOM_RIGHT);
                       hBox.getChildren().add(imageView);
                       Text text1 = new Text(" : Me");
                       text1.setStyle("-fx-font-size: 15px");
                       hBox.getChildren().add(text1);
                   }

                    Platform.runLater(() -> VBox.getChildren().addAll(hBox));
               }else {
                   TextFlow tempText = new TextFlow();

                   HBox hBox = new HBox(10);
                   if (!lblName.getText().equals(beforeCharacter)) {
                       Text name = new Text(beforeCharacter + " : ");
                       tempText.getChildren().add(name);

                       Text msg = new Text(afterCharacter);
                       tempText.getChildren().add(msg);
                       tempText.setMaxWidth(200);

                       tempText.setStyle("-fx-background-color:#ff6b81;" + "-fx-background-radius: 20px;" + "-fx-font-size: 17px;");
                       tempText.setPadding(new Insets(5, 10, 5, 10));


                       hBox.setPadding(new Insets(5));

                       VBox.setAlignment(Pos.TOP_LEFT);
                       hBox.setAlignment(Pos.CENTER_LEFT);
                       hBox.getChildren().add(tempText);

                   } else {
                       Text text1 = new Text(afterCharacter + " : Me");
                       TextFlow textFlow1 = new TextFlow(text1);
                       hBox.setAlignment(Pos.BOTTOM_RIGHT);
                       hBox.getChildren().add(textFlow1);
                       textFlow1.setStyle("-fx-background-color:#7bed9f;" + "-fx-background-radius: 20px;" + "-fx-font-size: 17px;");
                       textFlow1.setPadding(new Insets(5, 10, 5, 10));
                   }
                   Platform.runLater(() -> VBox.getChildren().addAll(hBox));
               }


                   if (!lblName.getText().equals(beforeCharacter)) {
                       continue;
                   } else if (afterCharacter.equalsIgnoreCase("bye")) {
                       break;
                   }
               }
                   reader.readLine();
                    writer.close();
                   socket.close();


       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public void connectSocket(){
        try {
            socket = new Socket("localhost",3000);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(userName);
            dataOutputStream.flush();

            System.out.println(userName+" Connected"); // name

            reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
            this.start();
        } catch (IOException e) {
            System.out.println(e);;
        }
    }

    public void btnSendOnAction(ActionEvent actionEvent) {

       String message = txt.getText();
       writer.println(lblName.getText()+"::"+message);


       writer.flush();
       txt.setText("");
       if(message.equalsIgnoreCase("bye")){
           System.exit(0);
       }

    }

    public void btnCameraOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image");
            this.filePath = fileChooser.showOpenDialog(stage);
            writer.println(lblName.getText()+ "::" + "img" + filePath.getPath());
            writer.flush();
        }catch (NullPointerException e){
            System.out.println(e);
            System.out.println("Image not Selected!");
        }
    }

    public void btnEmojiOnAction(ActionEvent actionEvent) {
        if(!emojiPane.isVisible())
            emojiPane.setVisible(true);
        else
            emojiPane.setVisible(false);
    }


    public void btnAddOnAction(ActionEvent actionEvent) {
        OpenView.openView("client1LoginForm");
    }



    @FXML
    void angryOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\angry.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void annoyOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\annoy.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void crazyOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\crazy.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void cuteOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\cute.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void ghostOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\ghost.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void heartOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\heart.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void holoOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\holo.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void kissOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\kiss.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void laughOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\laugh.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void lovelyOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\lovely.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void sadOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\sad.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void shockOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\shock.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void smileOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\smile.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void sunGlassOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\sunglass.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    @FXML
    void winkOnAction(MouseEvent event) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\wink.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    public void purpleOnAction(MouseEvent mouseEvent) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\purple.gif";
        writer.println(lblName.getText()+ "::" + "img" + message+"emojii");
        writer.flush();
        emojiPane.setVisible(false);
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage)((ImageView)mouseEvent.getSource()).getScene().getWindow();
        stage.setMaximized(false);
        stage.setIconified(true);
    }

    public void bgChangeOnAction(MouseEvent mouseEvent) {
    }

    public void participateOnAction(MouseEvent mouseEvent) {
    }


}
