//////////////////////////////////////////////////////////////GUI ELEMENTS CLASS/////////////////////////////////////////////////////////////////////////

package sample;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;                   //importing necessary fx nodes
import javafx.stage.Stage;

import java.time.LocalDate;

public class GuiElements {
    DropShadow shadow = new DropShadow();       //object of DropShadow class for button effects

    //anchorPane node, for the containers
    public AnchorPane anchor(){
        AnchorPane anchor = new AnchorPane();
        anchor.setStyle("-fx-background-color: #222; -fx-border-color: #f00;");
        return anchor;
    }

    //an alternate anchor pane used for the report, takes id, layouts and min sizes.
    public AnchorPane reportAnchor(){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setId("reportPane");
        return anchorPane;
    }

    //the scroll pane, layout for the final report gui, which displays the details of people in the queue
    public ScrollPane reportScroll(int width, int height, int layX, int layY, String style){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(width, height);
        scrollPane.setStyle(style);
        scrollPane.setLayoutX(layX);
        scrollPane.setLayoutY(layY);
        return scrollPane;
    }

    //label node, holds parameters for layout x, y text content and its id values
    public Label labels(int layX, int layY, String text, String id){
        Label labels = new Label();
        labels.setLayoutX(layX);
        labels.setLayoutY(layY);
        labels.setText(text);
        labels.setId(id);
        labels.setAlignment(Pos.CENTER);
        return labels;
    }

    //textField node, holds parameters for layout x, y text content, height and width values
    public TextField textFields(int layX, int layY, int height, int width, String prompt){
        TextField textField = new TextField();
        textField.setLayoutX(layX);
        textField.setLayoutY(layY);
        textField.setMinHeight(height);
        textField.setMinWidth(width);
        textField.setPromptText(prompt);
        return textField;
    }

    //ImageView node, holds parameters for layout x, y, height, width and the image file location values
    public ImageView imageViewLay(String imageFile, int layX, int layY, int height, int width){
        Image imageLay = new Image(imageFile);
        ImageView imageViewLay = new ImageView(imageLay);
        imageViewLay.setFitHeight(height);
        imageViewLay.setFitWidth(width);
        imageViewLay.setX(layX);
        imageViewLay.setY(layY);
        return imageViewLay;
    }

    //button node, holds parameters for layout x, y text content and its id values, further holds the shadow as an effect and cursor hover effect
    public Button buttons(String btnText, int layX, int layY, String id){
        Button button = new Button();
        button.setText(btnText);
        button.setLayoutX(layX);
        button.setLayoutY(layY);
        button.setId(id);
        button.setCursor(Cursor.HAND);
        button.setEffect(shadow);
        return button;
    }

    //ComboBox node, for the datePicker and journey gui, holds the list of journeys x and y layouts, default value and min width
    public ComboBox<String> allJourneys(){
        ComboBox<String> allJourneys =new ComboBox<>();
        allJourneys.setOnMouseEntered(event -> allJourneys.setCursor(Cursor.HAND));
        allJourneys.getItems().addAll("Colombo - Badulla", "Colombo - Polgahawela", "Colombo - Peradeniya Junction",
                "Peradeniya Junction - Nanuoya", "Peradeniya Junction - Badulla", "Colombo - Gampola", "Colombo - Nawalapitiya",
                "Colombo - Hatton", "Colombo - Thalawakele", "Colombo - Nanuoya", "Nanuoya - Badulla", "Colombo - Haputale", "Colombo - Diyatalawa",
                "Colombo - Bandarawela", "Colombo - Ella", "Badulla - Ella", "Badulla - Bandarawela", "Badulla - Diyatalawa",
                "Badulla - Haputale", "Badulla - Nanuoya", "Nanuoya - Colombo", "Nanuoya - Peradeniya Junction", "Peradeniya Junction - Colombo",
                "Badulla - Thalawakele", "Badulla - Hatton", "Badulla - Nawalapitiya", "Badulla - Gampola", "Badulla - Peradeniya Junction",
                "Peradeniya Junction - Colombo", "Badulla - Polgahawela", "Badulla - Maradana", "Badulla - Colombo");
        allJourneys.setLayoutX(220);
        allJourneys.setLayoutY(250);
        allJourneys.setMinWidth(400);
        allJourneys.setValue("Colombo - Badulla");
        return allJourneys;
    }

    //datePicker node, with disabled editing ability to prevent entering of fake text, and disabling of past dates, setting default date, x y layouts,
    //min widths
    public DatePicker datePicker(){
        DatePicker datePicker = new DatePicker();
        datePicker.setOnMouseEntered(event -> datePicker.setCursor(Cursor.HAND));
        datePicker.getEditor().setDisable(true);
        datePicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today)<0);
            }
        });
        datePicker.setLayoutX(220);
        datePicker.setLayoutY(170);
        datePicker.setMinWidth(400);
        datePicker.setValue(LocalDate.now());
        return datePicker;
    }

    //hBox node, holds parameters for layout x and spacing between elements in the container
    public HBox hbox(int spacing, int layX, int layY){
        HBox layoutSeats = new HBox(spacing);
        layoutSeats.setLayoutX(layX);
        layoutSeats.setLayoutY(layY);
        return layoutSeats;
    }

    //vBox node, holds parameters for layout x, y and spacing between elements in the container
    public VBox vbox(int spacing, int layX, int layY){
        VBox vbox = new VBox(spacing);
        vbox.setLayoutX(layX);
        vbox.setLayoutY(layY);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    //gridPane node, holds parameters for layout x, further has default vGap, hGap and layout y values for elements in the container
    public GridPane gridPane(int layX){
        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(layX);
        gridPane.setLayoutY(160);
        gridPane.setHgap(20);
        gridPane.setVgap(5);
        return gridPane;
    }

    //tilePane node, holds default vGap, hGap and layout y and x values for elements in the container
    public TilePane tilePane(int layX, int layY){
        TilePane layoutTrain = new TilePane();
        layoutTrain.setVgap(25);
        layoutTrain.setHgap(50);
        layoutTrain.setLayoutY(layY);
        layoutTrain.setLayoutX(layX);
        return layoutTrain;
    }

    //scene node, which will take the anchor pane, a width height for the window, and the file for the css
    public Scene scene(AnchorPane anchor, int width, int height, String file){
        Scene scene = new Scene(anchor, width, height);
        String css = this.getClass().getResource(file).toExternalForm();
        scene.getStylesheets().add(css);
        return scene;
    }

    //Alert node, which is common for the confirmation alerts to be used, has a custom graphic, button types and a header text and title
    public Alert closeWindowCommon(){
        ImageView imageConfirm = imageViewLay("file:/C:/Users/Ammuuu/Downloads/W1761196/coursework/Pictures/person.png",
                0, 0, 100, 100);
        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.NO);
        closeAlert.setHeaderText("Do you REALLY want to exit?");
        closeAlert.setGraphic(imageConfirm);
        closeAlert.setTitle("Denuwara Menike Intercity");
        return closeAlert;
    }

    //method that handles the action done onto the alert from the above method, for the date gui
    public void closeDateGui(Stage window){
        Alert closeAlert = closeWindowCommon();
        closeAlert.showAndWait();
        if (closeAlert.getResult()==ButtonType.YES) {
            window.close();
        }else{
            closeAlert.close();
        }
    }

    //error alert for the train booking add method
    public void errorAlert(String text){
        ImageView imageConfirm = imageViewLay("file:/C:/Users/Ammuuu/Downloads/W1761196/coursework/Pictures/error.png",
                0, 0, 100, 100);
        Alert closeAlert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
        closeAlert.setHeaderText(text);
        closeAlert.setGraphic(imageConfirm);
        closeAlert.setTitle("Denuwara Menike Intercity");
        closeAlert.showAndWait();
        closeAlert.close();
    }
}
