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
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.controller.util.OpenView;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static lk.ijse.controller.Client1LoginFormController.*;
import static lk.ijse.controller.Client1LoginFormController.image;

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
    public AnchorPane filePane;
    public Label emojilbl;
    public AnchorPane stickerPane;
    public Circle circle;
    public AnchorPane viewPane;
    public VBox partiVbox;
    public AnchorPane particatePane;
    public AnchorPane backgroundPane;
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
        txt.setStyle("-fx-background-color: #053c4f;" + "-fx-text-fill: white;");
        emojiPane.setVisible(false);
        filePane.setVisible(false);
        stickerPane.setVisible(false);
        viewPane.setVisible(false);
        particatePane.setVisible(false);
        backgroundPane.setVisible(false);


        Platform.runLater(() -> {
            scrollPane.lookup(".viewport").setStyle("-fx-background-color: transparent;");
            scrollPane.lookup(".scroll-bar").setStyle("-fx-background-color: transparent;");
            scrollPane.lookup(".scroll-bar:vertical").setStyle("-fx-background-color: transparent;");

        });

        if(image==null) {
            image = new Image("D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\no-profile-pic-icon-11 (1).jpg");
        }
        circle.setFill(new ImagePattern(image));

    }

    @Override
    public void run() {

       try {
           while (true) {

               String message = reader.readLine();

               if (message.contains("jointed")) {
                   HBox hBox = new HBox(10);
                   hBox.setAlignment(Pos.CENTER);

                   Text text1 = new Text(message);
                   TextFlow textFlow1 = new TextFlow(text1);
                   textFlow1.setStyle("-fx-background-color:#B2BEB5;" + "-fx-background-radius: 15px;" + "-fx-font-size: 13px;");
                   textFlow1.setPadding(new Insets(5, 10, 5, 10));

                   hBox.getChildren().add(textFlow1);
                   VBox.setAlignment(Pos.CENTER);
                   VBox.setSpacing(10);

                   Platform.runLater(() -> VBox.getChildren().add(hBox));

               } else {

                   String[] parts = message.split("\\Q" + "::" + "\\E");
                   String beforeCharacter = parts[0];

                   String afterCharacter = parts[1];

                   if (afterCharacter.startsWith("img")) {
                       String path = "";
                       ImageView imageView = null;

                       if (!afterCharacter.endsWith("emojii")) {

                           String[] part = message.split("\\Q" + "img" + "\\E");
                           path = part[1];
                           Image image = new Image(path, 100, 100, true, true);

                           imageView = new ImageView(image);
                           imageView.setFitHeight(120);
                           imageView.setFitWidth(120);
                       } else {
                           String[] part = message.split("\\Q" + "img" + "\\E");
                           String[] emojiPath = part[1].split("\\Q" + "emojii" + "\\E");
                           ;
                           path = emojiPath[0];

                           Image image = new Image(path);
                           imageView = new ImageView(image);
                           imageView.setFitHeight(50);
                           imageView.setFitWidth(50);
                       }

                       HBox hBox = new HBox(10);
                       hBox.setAlignment(Pos.BOTTOM_RIGHT);

                       if (!lblName.getText().equals(beforeCharacter)) {

                           VBox.setAlignment(Pos.BOTTOM_LEFT);
                           hBox.setAlignment(Pos.CENTER_LEFT);

                           Text text1 = new Text(" " + beforeCharacter + " : ");
                           text1.setStyle("-fx-font-size: 17px;");
                           text1.setFill(Color.WHITE);
                           hBox.getChildren().add(text1);
                           hBox.getChildren().add(imageView);

                       } else {
                           hBox.setAlignment(Pos.CENTER_RIGHT);
                           hBox.getChildren().add(imageView);
                           Text text1 = new Text(" : Me");
                           text1.setStyle("-fx-font-size: 17px;");
                           text1.setFill(Color.WHITE);
                           hBox.getChildren().add(text1);
                       }

                       Platform.runLater(() -> VBox.getChildren().addAll(hBox));

                   } else {
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

            System.out.println(userName+" Connected");

            reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);

            writer.println(userName+" jointed ");

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
        if(filePane.isVisible())
            filePane.setVisible(false);

        else if(!filePane.isVisible())
            if(!emojiPane.isVisible() && !stickerPane.isVisible()) {
                filePane.setVisible(true);
            }
        else if( !filePane.isVisible() && emojiPane.isVisible() && !stickerPane.isVisible() )
                emojiPane.setVisible(false);

        else if( !filePane.isVisible() && !emojiPane.isVisible() && stickerPane.isVisible() )
            stickerPane.setVisible(false);
    }


    public void btnAddOnAction(ActionEvent actionEvent) {
        OpenView.openView("client1LoginForm");
    }



    @FXML
    void angryOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\angry.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128544));
            txt.appendText(emoji);
        }

        /*byte_array = bytes.fromhex(hex_value)
        string_value = byte_array.decode('utf-8')*/

    }

    @FXML
    void annoyOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\annoy.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128553));
            txt.appendText(emoji);
        }
    }

    @FXML
    void crazyOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\crazy.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128540));
            txt.appendText(emoji);
        }
    }

    @FXML
    void cuteOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\cute.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128543));
            txt.appendText(emoji);
        }

    }

    @FXML
    void ghostOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\ghost.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128123));
            txt.appendText(emoji);
        }
    }

    @FXML
    void heartOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\heart.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128147));
            txt.appendText(emoji);
        }
    }

    @FXML
    void holoOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\holo.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128519));
            txt.appendText(emoji);
        }
    }

    @FXML
    void kissOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\kiss.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128536));
            txt.appendText(emoji);
        }
    }

    @FXML
    void laughOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\laugh.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128514));
            txt.appendText(emoji);
        }
    }

    @FXML
    void lovelyOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\lovely.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128525));
            txt.appendText(emoji);
        }
    }

    @FXML
    void sadOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\sad.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128577));
            txt.appendText(emoji);
        }
    }

    @FXML
    void shockOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\shock.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128562));
            txt.appendText(emoji);
        }
    }

    @FXML
    void smileOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\smile.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128513));
            txt.appendText(emoji);
        }
    }

    @FXML
    void sunGlassOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\sunglass.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128526));
            txt.appendText(emoji);
        }
    }

    @FXML
    void winkOnAction(MouseEvent event) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\wink.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(128546));
            txt.appendText(emoji);
        }
    }

    public void purpleOnAction(MouseEvent mouseEvent) {
        if(emojilbl.getText().equals("GIFs")) {
            String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\emoji\\purple.gif";
            writer.println(lblName.getText() + "::" + "img" + message + "emojii");
            writer.flush();

        }else {
            String emoji = new String(Character.toChars(129321));
            txt.appendText(emoji);
        }
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage)((ImageView)mouseEvent.getSource()).getScene().getWindow();
        stage.setMaximized(false);
        stage.setIconified(true);
    }

    public void bgChangeOnAction(MouseEvent mouseEvent) {
        backgroundPane.setVisible(true);
    }


    public void participateOnAction(MouseEvent mouseEvent) {
        if(viewPane.isVisible())
            viewPane.setVisible(false);

        else if(!viewPane.isVisible())
            if(!particatePane.isVisible()) {
                viewPane.setVisible(true);
            }
            else if( !filePane.isVisible() && particatePane.isVisible() )
                particatePane.setVisible(false);

    }



    public void EmojiOnAction(MouseEvent mouseEvent) {
        filePane.setVisible(false);
        emojiPane.setVisible(true);
        emojilbl.setText("Emoji");
    }

    public void GifOnAction(MouseEvent mouseEvent) {
        filePane.setVisible(false);
        emojiPane.setVisible(true);
        emojilbl.setText("GIFs");
    }

    public void StickerOnAction(MouseEvent mouseEvent) {
        filePane.setVisible(false);
        stickerPane.setVisible(true);
    }

    public void heartsOnAction(MouseEvent mouseEvent) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\stickers\\hearts.png";
        writer.println(lblName.getText() + "::" + "img" + message);
        writer.flush();
    }

    public void jesusOnAction(MouseEvent mouseEvent) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\stickers\\jesus.png";
        writer.println(lblName.getText() + "::" + "img" + message);
        writer.flush();
    }

    public void helloOnAction(MouseEvent mouseEvent) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\stickers\\hello.gif";
        writer.println(lblName.getText() + "::" + "img" + message);
        writer.flush();
    }

    public void brainOnAction(MouseEvent mouseEvent) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\stickers\\brain.png";
        writer.println(lblName.getText() + "::" + "img" + message);
        writer.flush();
    }

    public void teaOnAction(MouseEvent mouseEvent) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\stickers\\sip-tea.png";
        writer.println(lblName.getText() + "::" + "img" + message);
        writer.flush();
    }

    public void catOnAction(MouseEvent mouseEvent) {
        String message = "D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\stickers\\cat.png";
        writer.println(lblName.getText() + "::" + "img" + message);
        writer.flush();
    }

    public void viewOnAction(MouseEvent mouseEvent) {
        particatePane.setVisible(true);
        viewPane.setVisible(false);
        Image image = null;


        for (String user : users) {

            HBox hBox = new HBox();
            partiVbox.setAlignment(Pos.TOP_CENTER);
            partiVbox.setSpacing(5);

            for( Map.Entry<String, Image> entry : userLIst.entrySet()) {
                if (entry.getKey().equals(user)) {
                    image = entry.getValue();
                }
            }

            Circle circle = new Circle(15);
            if(image==null) {
                image = new Image("D:\\IJSE\\Working Projects\\Chat Application\\Client1\\src\\main\\resources\\Assets\\no-profile-pic-icon-11 (1).jpg");
            }
            circle.setFill(new ImagePattern(image));
            hBox.getChildren().add(circle);


            Text text = new Text(user);
            text.setStyle("-fx-font-size: 17px");
            text.setFill(Color.WHITE);
            hBox.getChildren().add(text);

            partiVbox.getChildren().addAll(hBox);
        }
    }

    public void backOnAction(MouseEvent mouseEvent) {
        backgroundPane.setVisible(false);
        Window window = ((Node) (mouseEvent.getSource())).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window);
        mouseEvent.consume();

        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        image = new Image(in);
        imgView.setImage(image);
    }
}
