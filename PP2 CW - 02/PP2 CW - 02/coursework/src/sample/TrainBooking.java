package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;                      //importing all necessary packages
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import com.mongodb.MongoClientSettings;

import static com.mongodb.client.model.Projections.exclude;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class TrainBooking extends Application {

    static final int SEAT_CAPACITY = 42;                                        //global train seat capacity variable

    @Override
    public void start(Stage primaryStage){
        GuiElements guiElements = new GuiElements();                            //Object of the GuiElements class
        primaryStage.getIcons().add(new Image("file:/C:/Users/Ammuuu/Downloads/W1761196/coursework/Pictures/icon.png"));//icon for program
        List<Passenger> bookingDetails = new ArrayList<>();                     //passenger data structure

        selectDate(bookingDetails, primaryStage, guiElements);
    }//calling the selectDate method in the start method so it is automatically called once program runs

    ///////////////////////////////////////////////////////////*****DATE SELECTION*****///////////////////////////////////////////////////////////////
    private void selectDate(List<Passenger> bookingDetails, Stage window, GuiElements guiElements){
        //////////////////////////////////////ALL REQUIRED GUI ELEMENTS CALLED BY THE OBJECT CREATED//////////////////////////////////////////////////
        Label label = guiElements.labels(50, 0, "Denuwara Menike Intercity Express Train", "label");
        Label label1 = guiElements.labels(140, 70, "-First Class AC Compartment-", "label");
        Label ticketPrices = guiElements.labels(10, 170, "Peradeniya   1000/= \nHatton          1200/= \nNanuoya       1500/= " +
                "\nHaputale       1600/= \nElla                " + "1600/= \n" + "Badulla          1700/=" , "ticket");
        ComboBox<String> allJourneys = guiElements.allJourneys();
        Button confirm = guiElements.buttons("CONFIRM", 220, 330, "continueBtn"); confirm.setMinWidth(400);
        DatePicker datePicker = guiElements.datePicker();
        AnchorPane anchor = guiElements.anchor();

        confirm.setOnAction(event -> {
            window.close();
            LocalDate localDate = datePicker.getValue();                        //getting chosen date and journey
            String journey = allJourneys.getValue();
            displayMenu(bookingDetails, window, journey, localDate, guiElements);//calling the displayMenu method which will run the program
        });

        /////////////////////////////////////////////////////////WINDOW CLOSE REQUEST ////////////////////////////////////////////////////////////////
        window.setOnCloseRequest(event -> {
            event.consume();                                //tells the window that it is taking responsibility on handling window closing
            guiElements.closeDateGui(window);               //calling the method which closes the date gui
        });

        /////////////////////////////////////////////////////////////SCENE CREATION///////////////////////////////////////////////////////////////////
        anchor.getChildren().addAll(label, label1, datePicker, confirm, allJourneys, ticketPrices); //anchor pane to hold all the gui elements
        Scene scene = guiElements.scene(anchor, 800, 400, "style.css");          //scene of the gui, also called from the object
        window.setScene(scene);
        window.setTitle("TRAIN TICKET BOOKING SYSTEM");
        window.show();                                      //setting the scene of the window, title and showing the window
    }

    /////////////////////////////////////////////////////////******DISPLAY MENU*****//////////////////////////////////////////////////////////////////
    private void displayMenu(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        //creating scanner object and obtaining user choice, and converting to lower case to facilitate checking
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Denuwara Menike Intercity Express Train. Please choose an option to continue");
        System.out.println("\"A\" - Add seats \n\"V\" - View all seats \n\"E\" - Display empty seats " +
                "\n\"F\" - Find customer's reserved seats \n\"D\" - Delete customer booking " +
                "\n\"O\" - Sort customers in alphabetical order \n\"S\" - Save data \n\"L\" - Load data" +
                "\n\"Q\" - Quit");

        String inputMain = sc.next().toLowerCase();

        switch (inputMain){
            case "a":           //calling the appropriate methods depending on users option choice, passing the same data structures as parameters
                addSeats(bookingDetails, window, journey, localDate, guiElements);
                break;
            case "v":
                viewSeats("VIEW MODE", bookingDetails, window, journey, localDate, guiElements);
                break;
            case "e":
                displayEmptySeats(bookingDetails, window, journey, localDate, guiElements);
                break;
            case "f":
                findSeats(bookingDetails, window, journey, localDate, guiElements);
                break;
            case "d":
                deleteSeats(bookingDetails, window, journey, localDate, guiElements);
                break;
            case "o":
                sortNames(bookingDetails, window, journey, localDate, guiElements);
                break;
            case "s":
                saveFile(bookingDetails, window, journey, localDate, guiElements);
                break;
            case "l":
                loadFile(bookingDetails, window, journey, localDate, guiElements);
                break;
            case "q":
                System.out.println("Thank you for using Denuwara Menike Intercity :) \nHave a safe ride!");
                System.exit(0);
                break;
            default://if the user enters something other than the given options an invalid message is printed
                System.out.println("Please enter a VALID option");
                displayMenu(bookingDetails, window, journey, localDate, guiElements);
                break;
        }
    }

    //////////////////////////////////////////////////////////*****ADD SEATS*****/////////////////////////////////////////////////////////////////////
    private void addSeats(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements) {
        Label label = guiElements.labels(480, 50, "BOOK YOUR SEATS", "label");
        ImageView imageViewLay = guiElements.imageViewLay("file:/C:/Users/Ammuuu/Downloads/W1761196/coursework/Pictures/Train.png",
                500, 220, 370, 600);
        Label greenLabel = guiElements.labels(350, 150, "Green Represents available seats", "info");
        Label redLabel = guiElements.labels(900, 150, "Red Represents reserved seats", "info");
        Label nameLabel = guiElements.labels(350, 610, "Enter Your Name: ", "nameLabel");
        TextField nameInput = guiElements.textFields(490, 605, 30, 200, "Enter name in full");
        Label nicLabel = guiElements.labels(780, 610, "Enter your NIC: ", "nameLabel");
        TextField nicInput = guiElements.textFields(900, 605, 30, 230, "Enter NIC");
        Button continueBtn = guiElements.buttons("CONFIRM", 1150, 600, "continueBtn");
        Label driver = guiElements.labels(190, 600, "DRIVER", "driver");
        ImageView imageViewConfirm = guiElements.imageViewLay("file:/C:/Users/Ammuuu/Downloads/W1761196/coursework/Pictures/person.png",
                0, 0, 100, 100);
        GridPane layout = guiElements.gridPane(80);int columnIndex = 0;int rowIndex = 0;int changeRow = 0;
        AnchorPane anchor = guiElements.anchor();
        ///////////////////////////////////////////////////GUI ELEMENTS FROM THE CLASS////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////SEAT CREATION//////////////////////////////////////////////////////////////////////
        Button[] allSeats = viewGridLayout();                                     //calling the layout method from below
        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            layout.add(allSeats[i - 1], columnIndex, rowIndex);                   //adding the button, the col and row index to the layout
            columnIndex++;                                                        //incrementing the columnIndex and changeRow values
            changeRow++;
            if (changeRow == 4) {
                columnIndex = 0;                        //if the changeRow value is equal to 4 set columnIndex, changeRow to 0, increment rowIndex
                changeRow = 0;                          //what this does, is after 4 seats have been added the next 4 go onto the next line
                rowIndex++;                             //columnIndex and rowIndex specify the point at which the button starts
            }                                           //the point at which each button starts is 1 index greater than the previous buttons col index
        }

        ///////////////////////////////////////////////////////////////BUTTON ON ACTION///////////////////////////////////////////////////////////////
        List<Integer> selectedSeats = new ArrayList<>();      //creating a temporary arrayList to hold the currently selected seats

        //Button action for loop
        for (int i=1; i<=SEAT_CAPACITY; i++) {                //a temporary variable to hold the values of i for each iteration inside the outer loop
            int finalI = i;                                   //this is because lambda expressions require an effectively final temp variable
            allSeats[i-1].setOnMouseEntered(event -> allSeats[finalI -1].setCursor(Cursor.HAND));
            allSeats[i-1].setOnAction(event -> {
                allSeats[finalI-1].setDisable(true);          //disabling the button on clicking of it, to prevent reserving the same seat twice
                allSeats[finalI - 1].setStyle("-fx-background-color: transparent; -fx-text-fill: #fff; -fx-border-color: #f00;");    //styling
                selectedSeats.add(finalI);                    //adding the chosen seat to the temporary arrayList
            });
        }

        //continue buttons actions upon clicking
        continueBtn.setOnAction(event -> {
            String userName = nameInput.getText().toLowerCase().trim(); //converting the input to lowercase, and trimming it to facilitate sorting
            String nic = nicInput.getText().trim();
            if(userName.trim().equals("")){                             //if no name was input throw an error alert
                guiElements.errorAlert("Please Enter a Valid Name!");
                return;
            }
            else if (selectedSeats.isEmpty()){                          //if the temporary arrayList is empty(no seats selected) throw an error alert
                guiElements.errorAlert("Please select at least one seat!");
                return;
            }else if(nic.length() != 5){                                //if nic was of a length not equal to 5 throw an error
                guiElements.errorAlert("Please enter a valid NIC!");
                return;
            }

            //this for loop loops through the data structure to check whether the currently entered nic was already entered by a customer
            for (Passenger passenger : bookingDetails){
                if (passenger.getNIC().equals(nic)){
                    guiElements.errorAlert("Please enter a unique NIC!");
                    return;
                }
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Name : " + userName + "\nNIC : " + nic + "\nSeats  : " +
                    selectedSeats + "\nDo you confirm your booking?", ButtonType.YES, ButtonType.NO);
            confirmAlert.setGraphic(imageViewConfirm);
            confirmAlert.showAndWait();                      //an alert popup to check whether customer has confirmed the chosen seats
            if (confirmAlert.getResult()==ButtonType.YES) {
                window.close();                              //closing window upon confirmation, adding name, nic and seats reserved to data structure
                for (int seat : selectedSeats) {
                    Passenger passenger = new Passenger();
                    passenger.setSeatsBooked(seat);
                    passenger.setName(userName);
                    passenger.setNIC(nic);
                    bookingDetails.add(passenger);
                }
            }else{
                confirmAlert.close();                        //if user didn't confirm return to the window
                return;
            }
            displayMenu(bookingDetails, window, journey, localDate, guiElements);
        }); //calling the display method again to continuously prompt the user

        //for loop to set the color and disability of each button the next time add is called, following checks all indexes, 1-42 against the
        //seats booked of every passenger in the Passenger list, if they are equal disable it and change its style, if not let it remain green
        for (Passenger passenger : bookingDetails){
            for (int j=1; j<=SEAT_CAPACITY; j++) {
                if (passenger.getSeatsBooked() == j) {
                    allSeats[j - 1].setStyle("-fx-background-radius: 10; -fx-background-color: #e91e63; -fx-text-fill: #fff;" +
                            " -fx-border-color: #e91e63; -fx-border-radius: 10");
                    allSeats[j - 1].setDisable(true);
                }
                else {
                    allSeats[j - 1].setId("greenBtn");
                }
            }
        }

        //////////////////////////////////////////////////////////WINDOW CLOSE REQUEST////////////////////////////////////////////////////////////////
        window.setOnCloseRequest(event -> {
            event.consume();
            closeScenes(bookingDetails, window, journey, localDate, guiElements);
        });

        ////////////////////////////////////////////////////////////SCENE CREATION////////////////////////////////////////////////////////////////////
        //the parent pane to hold all the elements created initially
        anchor.getChildren().addAll(layout, nameLabel, continueBtn, nameInput, label, driver, greenLabel, redLabel, imageViewLay, nicInput, nicLabel);
        Scene scene = guiElements.scene(anchor, 1366, 705, "style.css");
        window.setScene(scene);
        window.show();
    }

    //////////////////////////////////////////////////////////*****VIEW SEATS*****////////////////////////////////////////////////////////////////////
    //same follows for the view method, with different layout values
    private Button[] viewSeats(String heading, List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements) {
        ImageView imageViewLay = guiElements.imageViewLay("file:/C:/Users/Ammuuu/Downloads/W1761196/coursework/Pictures/Train3.png",
                800, 220, 370, 500);
        ImageView imageViewLay2 = guiElements.imageViewLay("file:/C:/Users/Ammuuu/Downloads/W1761196/coursework/Pictures/Train2.png",
                30, 220, 370, 500);
        Button okay = guiElements.buttons("Continue", 600, 100, "continueBtn");
        Label driver = guiElements.labels(680, 600, "DRIVER", "driver");
        Label label = guiElements.labels(550, 50, heading, "label");
        GridPane layout = guiElements.gridPane(560);int columnIndex = 0;int rowIndex = 0;int changeRow = 0;
        AnchorPane anchor = guiElements.anchor();

        Button[] allSeats = viewGridLayout();
        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            allSeats[i-1].setDisable(true);
            layout.add(allSeats[i - 1], columnIndex, rowIndex);
            columnIndex++;
            changeRow++;
            if (changeRow == 4) {
                columnIndex = 0;
                changeRow = 0;
                rowIndex++;
            }
        }

        for (Passenger passenger : bookingDetails){
            for (int j=1; j<=SEAT_CAPACITY; j++) {
                if (passenger.getSeatsBooked() == j) {
                    allSeats[j - 1].setStyle("-fx-background-radius: 10; -fx-background-color: #e91e63; -fx-text-fill: #fff;" +
                            " -fx-border-color: #e91e63; -fx-border-radius: 10");
                }
                else {
                    allSeats[j - 1].setId("greenBtn");
                }
            }
        }
        //the only difference between add and view is that there is not button action for view

        window.setOnCloseRequest(event -> {
            event.consume();
            closeScenes(bookingDetails, window, journey, localDate, guiElements);
        });

        okay.setOnAction(event -> {
            window.close();
            displayMenu(bookingDetails, window, journey, localDate, guiElements);
        });

        anchor.getChildren().addAll(layout, label, okay, driver, imageViewLay, imageViewLay2);
        Scene scene = guiElements.scene(anchor, 1366, 705, "style.css");
        window.setScene(scene);
        window.show();

        return allSeats;         //the seats array is returned, so it can be accessed by the display empty method
    }

    ////////////////////////////////////////////////////*****VIEW EMPTY SEATS*****////////////////////////////////////////////////////////////////////
    private void displayEmptySeats(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        Button[] all = viewSeats("EMPTY MODE", bookingDetails, window, journey, localDate, guiElements);
        //accessing the returned array and storing it in a new button array, at the same time calling the view method

        //for loop to handle visibility of selected buttons, each index, 1-42 is checked against seats booked of a passenger, if they are equal
        //set the visibility of that particular button to false, else set appropriate green style
        for (Passenger passenger : bookingDetails){
            for (int j=1; j<=SEAT_CAPACITY; j++) {
                if (passenger.getSeatsBooked() == j) {
                    all[j - 1].setVisible(false);
                }
                else {
                    all[j - 1].setId("greenBtn");
                }
            }
        }
    }

    //////////////////////////////////////////////////*****FIND SEATS UPON INPUT NAME*****////////////////////////////////////////////////////////////
    private void findSeats(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Name: ");               //creating a scanner object, getting input customer name converting it to lowercase,
        String userName = sc.nextLine().toLowerCase().trim();  //and trimming as names in the data structure is also stored the same way
        System.out.println("Enter your NIC: ");
        String nic = sc.nextLine().trim();
        boolean flag = false;

        //enhanced for loop to loop through each Passenger object in the list, if present the details of that passenger is displayed
        for (Passenger passenger : bookingDetails){
            if (passenger.getName().equals(userName) && passenger.getNIC().equals(nic)){
                System.out.println("Name : " + userName + " | NIC : " + nic + " | Journey : " + journey + " | Date : " + localDate +
                        " | Seats reserved : " + passenger.getSeatsBooked());
                flag = true;                                   //setting a boolean flag
            }else if (passenger.getNIC().equals(nic)){
                System.out.println("Name : " + passenger.getName() + " | NIC : " + nic + " | Journey : " + journey + " | Date : " + localDate +
                        " | Seats reserved : " + passenger.getSeatsBooked());
                flag = true;                                   //this section is for, if for instance incorrect name was entered, if the NIC was
            }                                                  //correct it will display the details
        }

        //the boolean flag is used in order to print a statement if any of the keys aren't equal to username, this is required because if we put it
        // inside the for loop as an else statement, the following sentence would be printed as many times as the if-condition isn't satisfied
        if (!flag){
            System.out.println(userName + " | " + nic +", hasn't booked a seat");
        }

        displayMenu(bookingDetails, window, journey, localDate, guiElements);
    }

    //////////////////////////////////////////////////*****DELETE RESERVATION UPON INPUT NAME*****////////////////////////////////////////////////////
    private void deleteSeats(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Name: ");             //getting user name input
        String userName = sc.nextLine().toLowerCase().trim();
        System.out.println("Enter your NIC: ");
        String nic = sc.nextLine().trim();
        boolean flag = false; boolean flag1 = false;

        //enhanced for loop to handle the deleting of customers
        for (Passenger passenger : new ArrayList<>(bookingDetails)){                     //the temporary arrayList was created as it wasn't possible
            if (passenger.getName().equals(userName) && passenger.getNIC().equals(nic)){ //to remove from and iterate through the same object
                bookingDetails.remove(passenger);                // at the same time, so an arrayList holding the Passenger list is created,
                flag = true;                                     //and if the input name was equal to a value in the arrayList the respective
            }else if (passenger.getNIC().equals(nic)){           //passenger is deleted from the main Passenger list
                userName = passenger.getName();
                bookingDetails.remove(passenger);
                flag1 = true;
            }
        }
        if (!flag){
            System.out.println(userName + " | " + nic + ", hasn't booked a seat");
        }

        if (flag1){
            System.out.println("Name: " + userName + " | NIC : "  + nic + " | Journey : " + journey + " | Date : " + localDate  +
                    " has been successfully deleted");
        }

        if (flag){                                               //the boolean flag has been used as the same purpose as the previous method
            System.out.println("Name : " + userName +  " | NIC:"  + nic + " | Journey : " + journey + " | Date : " + localDate  +
                    " has been successfully deleted");
        }

        displayMenu(bookingDetails, window, journey, localDate, guiElements);
    }

    ///////////////////////////////////////////////////////****SORT ALPHABETICALLY*****///////////////////////////////////////////////////////////////
    private void sortNames(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        Passenger temp;                                         //declaring a temp variable to facilitate sorting

        //nested for loop, to check the current value at index, with every other value at index+1
        for (int i=0;i<bookingDetails.size();i++){              //the compareTo method returns 0 of the first value is greater than the second
            for (int j=i+1;j<bookingDetails.size();j++){        //if indeed, that object is assigned to the temp variable
                if (bookingDetails.get(i).getName().compareTo(bookingDetails.get(j).getName())>0){
                    temp = bookingDetails.get(i);               //the object at index i+1 is assigned to the value at index i
                    bookingDetails.set(i, bookingDetails.get(j));
                    bookingDetails.set(j, temp);                //the object at index j is assigned to the temp variable
                }
            }
        }
        //following this loop, the passenger list is in alphabetical order

        //in this enhanced for loop we simply print the details of each passenger in the order
        for (Passenger passenger : bookingDetails){
            System.out.println("Name: " + passenger.getName() + " | NIC: " + passenger.getNIC() + " | Journey: " + journey + " | Date: " +
                    localDate + " | Seat reserved: " + passenger.getSeatsBooked());
        }

        displayMenu(bookingDetails, window, journey, localDate, guiElements);
    }

    ///////////////////////////////////////////////////////////////*****SAVE FILE*****////////////////////////////////////////////////////////////////
    private void saveFile(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build();
        MongoClient mongoClient = MongoClients.create(settings);                  //create mongoClient and get database
        MongoDatabase database = mongoClient.getDatabase("TrainBooking");      //directly saving POJO's weren't possible so settings had to change
        database.getCollection(localDate + " " + journey + " reservation").drop();
        MongoCollection<Passenger> collection = database.getCollection(localDate + " " + journey + " reservation", Passenger.class);
        //the databases are dropped before saving as it is required to overwrite instead of append
        //the collection is then obtained, this creates the collection, if the collection isn't there

        collection.insertMany(bookingDetails);                                    //and we insert the passenger list onto the collection
        System.out.println("Booking Details Were Successfully saved");
        displayMenu(bookingDetails, window, journey, localDate, guiElements);
    }

    ///////////////////////////////////////////////////////////////*****LOAD FILE*****////////////////////////////////////////////////////////////////
    private void loadFile(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("TrainBooking");
        MongoCollection<Passenger> collection = database.getCollection(localDate + " " + journey + " reservation", Passenger.class);
        FindIterable<Passenger> arrayListsDetails = collection.find();          //get an iterable of the collection we saved our list on
        arrayListsDetails.projection(exclude("_id"));              //and exclude off the id field name

        //this for loop simply loops through each passenger of the iterable and add them onto the data structure
        for (Passenger doc : arrayListsDetails) {
            bookingDetails.add(doc);
        }

        System.out.println("Booking Details Were Successfully Loaded");
        displayMenu(bookingDetails, window, journey, localDate, guiElements);
    }

    //////////////////////////////////////////////*********WINDOW CLOSE REQUEST************////////////////////////////////////////////////////////////
    private void closeScenes(List<Passenger> bookingDetails, Stage window, String journey, LocalDate localDate, GuiElements guiElements){
        Alert closeAlert = guiElements.closeWindowCommon();
        closeAlert.showAndWait();                       //an alert box to confirm whether the user wishes to actually close the scene
        if (closeAlert.getResult()==ButtonType.YES) {
            window.close();
            displayMenu(bookingDetails, window, journey, localDate, guiElements);
        }else{
            closeAlert.close();
        }
    }

    ///////////////////////////////////////////*******THE GRID PANE TO DISPLAY ALL 42 SEATS**********//////////////////////////////////////////////////
    private Button[] viewGridLayout() {
        Button[] allSeats = new Button[SEAT_CAPACITY];
        String seatNo;                              //a string seatNo to hold the values of the buttons
        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            if (i <= 9) {                           //creating the values of the buttons; if the values are less than 10 add a "0" in front
                seatNo = "0" + (i);
            } else {
                seatNo = "" + (i);
            }

            allSeats[i - 1] = new Button(seatNo);                         //assigning the created buttons to the specific index of the "allSeats" array
            allSeats[i - 1].setPadding(new Insets(10)); //setting padding for the buttons
            allSeats[i - 1].setId("greenBtn");                            //setting initial appearance of each button
        }
        return allSeats;
    }

    public static void main(String[] args){launch(args);}
}
