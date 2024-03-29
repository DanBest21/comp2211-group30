package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import Model.*;

/**
 * @author Benjamin Lellouch
 * The Controller will handle interactions between the back-end contained in the Model
 * package and the front-end View and Resources packages.
 */

public class Controller implements Initializable {

    // Injected Parameters for Airport Definition Window
    @FXML
    private TextField airportName;
    @FXML
    private Button airportDoneButton;

    // Injected Parameters for Obstacle Definition Window
    @FXML
    private TextField obstacleName;
    @FXML
    private TextField obstacleHeight;
    @FXML
    private Button obstacleDoneButton;

    // Injected Parameters for Runway Definition Window
    @FXML
    private TextField todaLeft;
    @FXML
    private TextField todaRight;
    @FXML
    private TextField toraLeft;
    @FXML
    private TextField toraRight;
    @FXML
    private TextField ldaLeft;
    @FXML
    private TextField ldaRight;
    @FXML
    private TextField asdaLeft;
    @FXML
    private TextField asdaRight;
    @FXML
    private Button runwayDoneButton;


    @FXML
    private ComboBox<Airport> airports;
    @FXML
    private ComboBox<String> runwayDegree;
    @FXML
    private ComboBox<String> runwayPosition;
    @FXML
    private Text complementDesignatorText;


    private ObservableList<Airport> airportObservableList;
    private ObservableList<String> runwayDegreeList;
    private ObservableList <String> runwayPositionList;
    private HashMap<String,String> oppositeDegreeMap;
    private HashMap<String,String> oppositePositionMap;
    private ObservableList<Obstacle> obstacles;




    /**
     *  Controller Constructor
     */
    public Controller()
    {
        airports = new ComboBox<>();
        runwayDegree = new ComboBox<>();
        runwayPosition = new ComboBox<>();
        airportObservableList = FXCollections.observableArrayList();
        obstacles = FXCollections.observableArrayList();
        populateObstacleList();
        runwayDegreeList = generateDegreeList();
        runwayPositionList = generatePositionList();
        oppositeDegreeMap = generateOppositeDegreeMap();
        oppositePositionMap = generateOppositePositionMap();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        airports.setItems(airportObservableList);
        runwayDegree.setItems(runwayDegreeList);
        runwayPosition.setItems(runwayPositionList);
    }

    /**
     *  Handles the opening of the Airport Definition window when the
     *  "Define new airport" menu item in the menu bar is pressed.
     */
    @FXML
    private void openAirportDefinition()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AirportDefinition.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setScene(definitionScene);
            definitionStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *  Handles the opening of the Runway Definition window when the
     *  "Define new runway" menu item in the menu bar is pressed.
     */
    @FXML
    private void openRunwayDefinition()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RunwayDefinition.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setScene(definitionScene);
            definitionStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *  Handles the opening of the Obstacle Definition window when the
     *  "Define new obstacle" menu item in the menu bar is pressed.
     */
    @FXML
    private void openObstacleDefinition()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObstacleDefinition.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setScene(definitionScene);
            definitionStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Reads the Textfields in AirportDefinition.fxml,
     * creates a new Airport and adds it the
     * list of Airports stored in this Controller
     */
    @FXML
    private void defineAirport()
    {
        //TODO create new Airport and add it to a List of Airports
        //TODO add Error pop-up when fields are empty
        String newAirportName = airportName.getText().replaceAll("\\s", "");
        if(!newAirportName.isEmpty())
        {
            airportObservableList.add(new Airport(newAirportName));
            Stage stage = (Stage) airportDoneButton.getScene().getWindow();
            stage.close();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Please fill all input fields");

            alert.showAndWait();
        }
    }

    /**
     * Reads the Textfields and ComboBox in RunwayDefinition.fxml
     *  and creates a new Runway and adds it to its
     *  respective Airport
     */
    @FXML
    private void defineRunway()
    {
        //TODO create new Runway and add them to their Respective Airport
        //TODO add Error pop-up when fields are empty

        Airport airport = airports.getValue();

        String designatorLeft = runwayDegree.getValue() + runwayPosition.getValue();
        String designatorRight = complementDesignatorText.getText();

        int todaLeft = Integer.parseInt(this.todaLeft.getText());
        int todaRight = Integer.parseInt(this.todaRight.getText());

        int toraLeft = Integer.parseInt(this.toraLeft.getText());
        int toraRight = Integer.parseInt(this.toraRight.getText());

        int asdaLeft = Integer.parseInt(this.asdaLeft.getText());
        int asdaRight = Integer.parseInt(this.asdaRight.getText());

        int ldaLeft = Integer.parseInt(this.ldaLeft.getText());
        int ldaRight = Integer.parseInt(this.ldaRight.getText());

        int[] leftRunwayParameters = {toraLeft, todaLeft,asdaLeft,ldaLeft};
        int[] rightRunwayParameters = {toraRight, todaRight,asdaRight,ldaRight};
        Runway newRunway = new Runway(designatorLeft,designatorRight,leftRunwayParameters,rightRunwayParameters);
        airport.addRunway(newRunway);

        Stage stage = (Stage) runwayDoneButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Reads the Textfields in ObstacleDefinition.fxml,
     * creates a new Obstacle and adds it to a list
     * of Obstacles stored in this Controller
     */
    @FXML
    private void defineObstacle()
    {
        //TODO create new Obstacle and add it to a List of Obstacles
        //TODO add Error pop-up when fields are empty
        String newObstacleName = obstacleName.getText();
        // may throw number format exception so needs some polishing
        int newObstacleHeight =  Integer.parseInt(obstacleHeight.getText());
        obstacles.add(new Obstacle(newObstacleName,newObstacleHeight));
        Stage stage = (Stage) obstacleDoneButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Fills in the obstacle list with a few predefined obstacles and their heights
     */
    private void populateObstacleList()
    {
        obstacles.add(new Obstacle("Broken Down Rover Vehicle",2));
        obstacles.add(new Obstacle("Barricades",1));
        obstacles.add(new Obstacle("Lighting Pole",5));
        obstacles.add(new Obstacle("Broken Down Aircraft",19));
    }

    /**
     * Dynamically updates the designator of the right runway
     */
    @FXML
    private void updateDesignator()
    {
        String newDegree = getOppositeDegree(runwayDegree.getValue());
        String newPosition =  getOppositePosition(runwayPosition.getValue());

        if (newDegree == null)
        {
            complementDesignatorText.setText(newPosition);
        }
        else if ( newPosition == null)
        {
            complementDesignatorText.setText(newDegree);
        }
        else
        {
            complementDesignatorText.setText( newDegree + newPosition);
        }
    }






















    private String getOppositeDegree(String degree)
    {
        return oppositeDegreeMap.get(degree);
    }

    private String getOppositePosition(String position)
    {
        return oppositePositionMap.get(position);
    }



    private ObservableList<String> generateDegreeList()
    {
        return FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36");
    }



    private ObservableList<String> generatePositionList()
    {
        return FXCollections.observableArrayList("L","R","C");
    }


    private HashMap<String,String> generateOppositePositionMap()
    {
        HashMap<String,String> oppositeMap = new HashMap<>();
        oppositeMap.put("L","R");
        oppositeMap.put("R", "L");
        oppositeMap.put("C", "C");
        return oppositeMap;
    }


    private HashMap<String,String> generateOppositeDegreeMap()
    {
        HashMap<String,String> oppositeMap = new HashMap<>();
        oppositeMap.put("36", "18");
        oppositeMap.put("01", "19");
        oppositeMap.put("02", "20");
        oppositeMap.put("03", "21");
        oppositeMap.put("04", "22");
        oppositeMap.put("05", "23");
        oppositeMap.put("06", "24");
        oppositeMap.put("07", "25");
        oppositeMap.put("08", "26");
        oppositeMap.put("09", "27");
        oppositeMap.put("10", "28");
        oppositeMap.put("11", "29");
        oppositeMap.put("12", "30");
        oppositeMap.put("13", "31");
        oppositeMap.put("14", "32");
        oppositeMap.put("15", "33");
        oppositeMap.put("16", "34");
        oppositeMap.put("17", "35");
        oppositeMap.put("18", "36");
        oppositeMap.put("19", "01");
        oppositeMap.put("20", "02");
        oppositeMap.put("21", "03");
        oppositeMap.put("22", "04");
        oppositeMap.put("23", "05");
        oppositeMap.put("24", "06");
        oppositeMap.put("25", "07");
        oppositeMap.put("26", "08");
        oppositeMap.put("27", "09");
        oppositeMap.put("28", "10");
        oppositeMap.put("29", "11");
        oppositeMap.put("30", "12");
        oppositeMap.put("31", "13");
        oppositeMap.put("32", "14");
        oppositeMap.put("33", "15");
        oppositeMap.put("34", "16");
        oppositeMap.put("35", "17");
        return oppositeMap;
    }

}
