/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package donorsearch.pkg2;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.sql.*;

/**
 *
 * @author Wmilz
 */
public class DonorSearch2 extends Application {
    Stage current = new Stage();
    int x = 900;
    int y = 500; //Controls the dimensions of the window
    int organPatient = 0; //1 if organ, 2 if patient. Used to determine which view to transfer to
    boolean admin = false; //Used to determine if a user is an admin
    boolean firstRun = true;
    String listSelection = null; //Used to determine which entity is selected in the listview
    String ID; //used to save the current Organ ID
    int Hgap = 10;
    int Vgap = 10;
    
    Button submitBTN = new Button("Submit"); //BUTTON INITIALIZATION
    Button RecipientBTN = new Button("Recipient"); 
    Button OrganBTN = new Button("Organ");
    Button backBTN = new Button("<- Back");
    Button deleteBTN = new Button("Delete");
    Button claimBTN = new Button("Claim");
    Button searchBTN = new Button("Search");
    
    
    TextField userName = new TextField(); //PAGE 0 CONTROLS
    PasswordField passwordTextBox = new PasswordField();
    Text wrongText = new Text("Wrong Password! Try again.");
    
    ComboBox organType = new ComboBox(); //PAGE 1 CONTROLS
    ComboBox organSize = new ComboBox();
    ComboBox organCondition = new ComboBox();
    ComboBox bloodType = new ComboBox();
    ComboBox sortBy = new ComboBox();
    ComboBox hospitalLoc = new ComboBox();    
    ListView organList = new ListView(); 
    ListView claimedOrgans = new ListView(); 
    
    TextField organTypeText = new TextField(); //PAGE 3/4 CONTROLS
    TextField organDateText = new TextField();
    TextField organLocationText = new TextField();
    TextField organConditionText = new TextField();
    TextField organBloodTypeText = new TextField();
    TextField patientOrganText = new TextField();
    TextField patientInputDateText = new TextField();
    TextField patientSeverityText = new TextField();
    TextField patientNameText = new TextField(); 
    
    TextField patientfNameText = new TextField(); //PAGE 5 CONTROLS
    TextField patientlNameText = new TextField();
    TextField patientIDText = new TextField();
    
    ComboBox entityEdit = new ComboBox(); //PAGE 6 CONTROLS
    TextField entityID = new TextField();
        
    TextField currentfNameText = new TextField();
    TextField currentlNameText = new TextField();
    TextField currentDOBText = new TextField();
    TextField currentAreaCodeText = new TextField();
    TextField currentTelephoneText = new TextField();
    
    Connection DB = null;
    
    @Override
    
    public void start(Stage primaryStage) {
        current.setScene(LogInScene());
        current.show();
        initializeDB();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public Scene LogInScene(){ //PAGE 0
        admin = false;
        Text title = new Text("Organ Transplant Matcher");
        Text LogInText = new Text("Please Log In:");
        
        wrongText.setVisible(false);
        GridPane LogIn = new GridPane(); //CREATING GRIDPANE FOR FIRST SCENE
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(5);
        
        LogIn.getColumnConstraints().addAll(col, col, col, col, col);
        LogIn.getRowConstraints().addAll(row, row, row, row);
       
        submitBTN.setOnAction(e -> actionPerformed(submitBTN, 0));
        LogIn.setHalignment(title, HPos.CENTER);
        LogIn.setHalignment(userName, HPos.CENTER);
        LogIn.setHalignment(passwordTextBox, HPos.CENTER);
        LogIn.setHalignment(submitBTN, HPos.CENTER);
        LogIn.setHalignment(wrongText, HPos.CENTER);
        
        LogIn.add(title, 2, 3);
        LogIn.add(userName, 2, 4);
        LogIn.add(passwordTextBox, 2, 5);
        LogIn.add(submitBTN, 2, 6);
        LogIn.add(wrongText, 2, 7);
        LogIn.setHgap(Hgap);
        LogIn.setVgap(Vgap);
        Scene logIn = new Scene(LogIn, x, y);
        return logIn;
    } //PAGE 0
    
    public Scene buttonScene(){ //PAGE 1
        Text title = new Text("Organ Transplant Matcher");    
        Text Instructions = new Text("What are you searching for?"); //SECOND SCENE CONTROLS
        firstRun = true;
        RecipientBTN.setOnAction(e -> actionPerformed(RecipientBTN, 1));
        OrganBTN.setOnAction(e -> actionPerformed(OrganBTN, 1));
        backBTN.setOnAction(e -> actionPerformed(backBTN, 1));
        
        GridPane BTNSearch = new GridPane(); //CREATING GRIDPANE FOR SECOND SCENE
        GridPane.setHalignment(title, HPos.CENTER);
        BTNSearch.add(title, 2, 4);
        
        GridPane.setHalignment(Instructions, HPos.CENTER);
        BTNSearch.add(Instructions, 2, 5);
        GridPane.setHalignment(RecipientBTN, HPos.LEFT);
        BTNSearch.add(RecipientBTN, 2, 6);
        GridPane.setHalignment(OrganBTN, HPos.RIGHT);
        GridPane.setHalignment(backBTN, HPos.CENTER);
        BTNSearch.add(OrganBTN, 2, 6);
        BTNSearch.add(backBTN, 2, 7);
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(5);
        
        BTNSearch.getColumnConstraints().addAll(col, col, col, col, col);
        BTNSearch.getRowConstraints().addAll(row, row, row, row);
        
        BTNSearch.setHgap(Hgap);
        BTNSearch.setVgap(Vgap);
        
        Scene BTNSearchScene = new Scene(BTNSearch, x, y); 
        
        return BTNSearchScene;
    } //PAGE 1
    
    public Scene searchScene(String input){
        Text title = new Text("Organ Transplant Matcher");
        
        backBTN.setOnAction(e -> actionPerformed(backBTN, 2));
        submitBTN.setOnAction(e -> actionPerformed(submitBTN, 2));
        Text listViewTitle = new Text();
        Text organTypeT = new Text("Select Organ Type: ");
        Text organSizeT = new Text("Select Organ Size: ");
        Text bloodTypeT = new Text("Select Blood Type: ");
        Text hospitalLocation = new Text("Select Hospital Location: ");
        Text organConditionT = new Text("Enter Condition: ");
        
        
        
        
        if (firstRun == true){
        organType.getItems().clear();
        organSize.getItems().clear();
        bloodType.getItems().clear();
        sortBy.getItems().clear();
        hospitalLoc.getItems().clear();
        organCondition.getItems().clear();   
            
        organType.getItems().addAll("Heart", "Lung", "Spleen", "Liver", "Kidney");
        organSize.getItems().addAll("Small", "Medium", "Large");
        bloodType.getItems().addAll("O-", "O+", "A+", "A-", "B+", "B-", "AB+", "AB-");
        sortBy.getItems().addAll("Date Added", "Severity");
        hospitalLoc.getItems().addAll("Ascension Macomb", "Harbor Oaks", "McLaren Macomb", "Select Specialty", "Troy Beaumont", "St. Joseph Mercy Hospital", "Oakland Regional Hospital");
            if(input.equals("organ")){
                listViewTitle.setText("Organs Available:");
                organCondition.getItems().addAll("Damaged", "Healthy");
                organConditionT.setText("Enter Organ Condition: ");
                organPatient = 1;
            }else if(input.equals("patient")){
                listViewTitle.setText("Patients in Need:");
                organConditionT.setText("Enter Patient Severity: ");
                organCondition.getItems().addAll("Severe", "In Need");
                organPatient = 2;
            }
        firstRun = false;
        }
        
        organList.getItems().clear();
        organList.setOnMouseClicked(e -> actionPerformed(deleteBTN, 2));
        
        GridPane organView = new GridPane();
        organView.add(title, 2,0);
        organView.add(listViewTitle, 3, 0); 
        organView.add(organList, 3, 1);
        organView.setRowSpan(organList, 7);
        organView.add(organType, 2,1);
        organView.add(organTypeT, 1, 1);
        organView.add(organSizeT, 1, 2);
        organView.add(organConditionT, 1, 3);
        organView.add(bloodTypeT, 1, 4);
        organView.add(hospitalLocation, 1, 5);
        
        organView.add(organSize, 2,2);
        organView.add(organCondition, 2,3);
        organView.add(bloodType, 2,4);
        organView.add(hospitalLoc, 2,5);
        organView.setHalignment(backBTN, HPos.LEFT);
        organView.setHalignment(submitBTN, HPos.RIGHT);
        organView.add(backBTN, 2, 6); 
        organView.add(submitBTN, 2, 6);
        //organView.add(sortBy, 1, 1);
        organView.setHgap(Hgap);
        organView.setVgap(Vgap);
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(5);
        
        organView.getColumnConstraints().addAll(col, col, col, col, col);
        organView.getRowConstraints().addAll(row, row, row, row);
        //organView.setGridLinesVisible(true);
        Scene search = new Scene(organView, x, y);
        
        return search;
    } //PAGE 2
    
    public Scene organViewScene(){
        Text organInfo = new Text("Organ Information: "); 
        Text organType = new Text("Organ Type: "); 
        Text organDate = new Text("Organ Date: "); 
        Text organLocation = new Text("Organ Location: "); 
        Text organCondition = new Text("Organ Condition: "); 
        Text organBloodType = new Text("Organ Blood Type: ");
        
        backBTN.setOnAction(e -> actionPerformed(backBTN, 3));
        claimBTN.setOnAction(e -> actionPerformed(claimBTN, 3));
        GridPane organView = new GridPane();
        
        organView.add(organInfo, 1,2);
        organView.add(organType, 1,3);
        organView.add(organTypeText, 2,3);
        organView.add(organDate, 1, 4);
        organView.add(organDateText, 2, 4);
        organView.add(organLocation, 1, 5);
        organView.add(organLocationText, 2, 5);
        organView.add(organCondition, 1, 6);
        organView.add(organConditionText, 2, 6);
        organView.add(organBloodType, 1,7);
        organView.add(organBloodTypeText, 2, 7);
        organView.setHalignment(backBTN, HPos.CENTER);
        organView.setHalignment(claimBTN, HPos.CENTER);
        organView.add(backBTN, 3, 7); 
        organView.add(claimBTN, 3, 6);
        organView.setHgap(Hgap);
        organView.setVgap(Vgap);
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(5);
        
        organView.getColumnConstraints().addAll(col, col, col, col, col);
        organView.getRowConstraints().addAll(row, row, row, row);
        
        Scene organ = new Scene(organView, x, y);
        return organ;
    } //PAGE 3
    
    public Scene patientViewScene(){
        Text patientInfo = new Text("Patient Information:");
        Text patientOrgan = new Text("Organ Type Needed:");
        Text patientInputDate = new Text("Patient Input Date:");
        Text organLocation = new Text("Organ Location:");
        Text patientSeverity = new Text("Patient Severity:");
        Text organBloodType = new Text("Organ Blood Type:");
        Text patientName = new Text("Patient Name");
        
        backBTN.setOnAction(e -> actionPerformed(backBTN, 4));

        GridPane patientView = new GridPane();
           
        patientView.add(patientInfo, 1, 2);
        patientView.add(patientOrgan, 1, 3);
        patientView.add(patientOrganText, 2, 3);
        patientView.add(patientInputDate, 1, 4);
        patientView.add(patientInputDateText, 2, 4);
        patientView.add(organLocation, 1, 5);
        patientView.add(organLocationText, 2, 5);
        patientView.add(patientSeverity, 1, 6);
        patientView.add(patientSeverityText, 2, 6);
        patientView.add(organBloodType, 1, 7);
        patientView.add(organBloodTypeText, 2, 7);
        patientView.add(patientName, 1, 8);
        patientView.add(patientNameText, 2, 8);
        patientView.add(backBTN, 2, 9); 
        patientView.setHgap(Hgap);
        patientView.setVgap(Vgap);
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(5);
        
        patientView.getColumnConstraints().addAll(col, col, col, col, col);
        patientView.getRowConstraints().addAll(row, row, row, row);
        
        
        Scene patient = new Scene(patientView, x, y);
        return patient;
    } //PAGE 4
    
    public Scene claimScene(){
        Text patientfName = new Text("Please enter patients first name:");
        Text patientlName = new Text("Please enter patients last name:");
        Text patientID = new Text("Please enter the patients ID code:");
        Text claimed = new Text("Claimed Organs:");
        
        
        backBTN.setOnAction(e -> actionPerformed(backBTN, 5));
        submitBTN.setOnAction(e -> actionPerformed(submitBTN, 5));
        
        GridPane patientClaim = new GridPane();
        patientClaim.setRowSpan(claimedOrgans, 7);
        patientClaim.add(patientfName, 1, 2);
        patientClaim.add(patientlName, 1, 3);
        patientClaim.add(patientID, 1, 4);
        patientClaim.add(patientfNameText, 2, 2);
        patientClaim.add(patientlNameText, 2, 3);
        patientClaim.add(patientIDText, 2, 4);
        patientClaim.setHalignment(submitBTN, HPos.LEFT);
        patientClaim.setHalignment(backBTN, HPos.RIGHT);
        patientClaim.add(submitBTN, 1, 5);
        patientClaim.add(backBTN, 1, 5);
        patientClaim.add(claimed, 3, 1);
        patientClaim.add(claimedOrgans, 3, 2);
        patientClaim.setHgap(Hgap);
        patientClaim.setVgap(Vgap);
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(5);
        
        patientClaim.getColumnConstraints().addAll(col, col, col, col, col);
        patientClaim.getRowConstraints().addAll(row, row, row, row);
        
        
       ResultSet rs = executeQuery("select O_CODE FROM ORGAN WHERE O_CLAIMED = 'True'");
       fillClaimedList(rs);

        
        
        Scene claim = new Scene(patientClaim, x, y);
        return claim;
    } //PAGE 5
    
    public Scene adminScene(){
        
        Text entitySearch = new Text("Entity Search:");
        entityEdit.getItems().addAll("Patient", "Organ Donor");
        searchBTN.setOnAction(e -> actionPerformed(searchBTN, 6));
        submitBTN.setOnAction(e -> actionPerformed(submitBTN, 6));
        backBTN.setOnAction(e -> actionPerformed(backBTN, 6));
        
        
        bloodType.getItems().addAll("O-", "O+", "A+", "A-", "B+", "B-", "AB+", "AB-");   
        hospitalLoc.getItems().addAll("Ascension Macomb", "Harbor Oaks", "McLaren Macomb", "Select Specialty", "Troy Beaumont");
        organCondition.getItems().addAll("Severe", "In Need");
        
        Text currentEntity = new Text("Current Entity:");
        Text currentfName = new Text("First Name:");
        Text currentlName = new Text("Last Name:");
        Text currentDOB = new Text("DOB:");
        Text currentLocation = new Text("Location:");
        Text currentAreaCode = new Text("Area code:");
        Text currentTelephone = new Text("Phone Number:");
        Text currentBloodType = new Text("Blood Type:");
        Text currentSeverity = new Text("Condition:");

        GridPane admin = new GridPane();
        admin.setHalignment(submitBTN, HPos.CENTER); 
        admin.add(entitySearch, 1, 1);
        admin.add(entityEdit, 1, 2);
        admin.add(entityID, 1, 3);
        admin.add(submitBTN, 1, 5);
        admin.setHalignment(searchBTN, HPos.CENTER);
        admin.setHalignment(entityEdit, HPos.CENTER);
        admin.add(searchBTN, 1, 4);
        admin.setHalignment(backBTN, HPos.CENTER);
           
        admin.add(currentEntity, 2, 1);
        admin.add(currentfName, 2, 2);
        admin.add(currentfNameText, 3, 2);
        admin.add(currentlName, 4, 2);
        admin.add(currentlNameText, 5, 2);
        admin.add(currentDOB, 2, 3);
        admin.add(currentDOBText, 3, 3);
        admin.add(currentLocation, 2, 4);
        admin.add(hospitalLoc, 3, 4);
        admin.add(currentAreaCode, 2, 5);
        admin.add(currentAreaCodeText, 3, 5);
        admin.add(currentTelephone, 4, 5);
        admin.add(currentTelephoneText, 5, 5);
        
        
        admin.add(currentBloodType, 2, 6);
        admin.add(bloodType, 3, 6);
        admin.add(currentSeverity, 2, 7);
        admin.add(organCondition, 3, 7);
        admin.add(backBTN, 1, 6);
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(30);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(5);
        
        admin.getColumnConstraints().addAll(col, col, col, col, col, col, col);
        admin.getRowConstraints().addAll(row, row, row, row, row, row, row, row, row);
        //admin.setGridLinesVisible(true);
        
        admin.setHgap(Hgap);
        admin.setVgap(Vgap);
        
        Scene adminScene = new Scene(admin, x, y);
        return adminScene;
    } //PAGE 6
    
    public void changeScene(Scene input){
        current.hide();
        current.setScene(input);
        current.show(); 
    } //Driver responsible for changing current page
    
    public void actionPerformed(Button e, int page){
        switch(page){ //page is used to describe which page a button is trying to reach
            case 0:
                if (e.getText().equals(backBTN.getText())){
                changeScene(LogInScene());
                }else if (e.getText().equals(submitBTN.getText())){
                
                ResultSet rs = executeQuery("select A_USERNAME, A_PASSWORD, A_ACCESS FROM ACCESS WHERE A_USERNAME = '" + userName.getText() + "'"); 
                if(checkPassword(rs, userName.getText(), passwordTextBox.getText())){
                    if (admin == true){
                        changeScene(adminScene());
                    }else{
                        changeScene(buttonScene());
                    }
                }else{
                    wrongText.setVisible(true);
                }
                }           
                break;
            case 1:
                if (e.getText().equals(RecipientBTN.getText())){
                    changeScene(searchScene("patient"));
                }else if (e.getText().equals(OrganBTN.getText())){
                    changeScene(searchScene("organ"));
                }
                if (e.getText().equals(backBTN.getText())){
                    changeScene(LogInScene());
                }
                break;
            case 2:
                if (e.getText().equals(backBTN.getText())){
                    changeScene(buttonScene());
                }
                if (e.getText().equals(submitBTN.getText())){
                    if (organPatient == 1){
                        ResultSet rs = executeQuery("SELECT O_CODE, O_DATE FROM ORGAN, DONOR WHERE (organ.d_code = donor.d_code and O_TYPE = '" + organType.getValue() + "' and O_SIZE = '" + organSize.getValue() + 
                                "' and O_CONDITION = '" + organCondition.getValue() + "' AND D_BLOOD_TYPE = '" + bloodType.getValue() + "' AND D_HOSPITAL = '" + hospitalLoc.getValue() + "' and O_CLAIMED = 'FALSE');"); 
                        fillListView(rs);
                    }else if (organPatient == 2){
                        ResultSet rs = executeQuery("SELECT waitlist.w_code, r_fname, r_lname from waitlist, recipient WHERE (waitlist.r_code = recipient.r_code and W_O_TYPE = '" + organType.getValue() + "' and W_O_SIZE = '" + organSize.getValue() + 
                                "' and R_SEVERITY = '" + organCondition.getValue() + "' AND R_BLOOD_TYPE = '" + bloodType.getValue() + "' AND R_HOSPITAL = '" + hospitalLoc.getValue() + "' and W_CLAIMED = 'FALSE');"); 
                        fillListView(rs); 
                    }   
                }
                if (e.getText().equals(deleteBTN.getText())){
                listSelection = organList.getSelectionModel().getSelectedItem().toString();
                if (organPatient == 1){
                String sub = listSelection.substring(7);
                ID = sub;
                ResultSet rs = executeQuery("SELECT O_DATE FROM ORGAN, DONOR WHERE O_CODE = '" + sub +"' and organ.d_Code = donor.d_code;");
                fillEntityInfo(rs);
                changeScene(organViewScene());
                }else if (organPatient == 2){
                String sub = listSelection.substring(9);
                ResultSet rs = executeQuery("SELECT DISTINCT W_DATE, R_FNAME, R_LNAME FROM WAITLIST, RECIPIENT WHERE W_CODE = '" + sub +"' and waitlist.r_code = recipient.r_code;");
                fillEntityInfo(rs);    
                changeScene(patientViewScene());   
                }                       
                }
                break;
            case 3:
                if (e.getText().equals(backBTN.getText())){
                changeScene(searchScene("organ"));  
                }
                if (e.getText().equals(claimBTN.getText())){
                    changeScene(claimScene());
                }  
                break;
            case 4:
                if (e.getText().equals(backBTN.getText())){
                changeScene(searchScene("patient"));  
                }
                break;
            case 5:
                if (e.getText().equals(backBTN.getText())){
                changeScene(organViewScene());          
                }
                if (e.getText().equals(submitBTN.getText())){
                System.out.println(executeUpdate("INSERT INTO MATCHES (W_CODE, O_CODE) VALUES ('" + patientIDText.getText() + "', '" + ID + "');"));
                System.out.println(executeUpdate("UPDATE WAITLIST SET W_CLAIMED = 'TRUE' WHERE W_CODE = '"+patientIDText.getText()+"';"));
                System.out.println(executeUpdate("UPDATE ORGAN SET O_CLAIMED = 'TRUE' WHERE O_CODE = '" + ID + "';"));
                ResultSet rs = executeQuery("select O_CODE FROM ORGAN WHERE O_CLAIMED = 'True'");
                fillClaimedList(rs); 
                
                }
                break;   
            case 6:
                if (e.getText().equals(backBTN.getText())){
                    changeScene(LogInScene());
                }else if (e.getText().equals(submitBTN.getText())){
                    if(entityEdit.getValue().equals("Patient")){
                        executeUpdate("UPDATE RECIPIENT, WAITLIST SET R_DOB = '" + currentDOBText.getText() + "', R_HOSPITAL = '" + hospitalLoc.getValue() + 
                                "', R_FNAME = '" + currentfNameText.getText() + "', R_LNAME = '" + currentlNameText.getText() + "', R_AREA_CODE = '" + 
                                currentAreaCodeText.getText() + "', R_TELE = '" + currentTelephoneText.getText() + "', R_BLOOD_TYPE = '" + bloodType.getValue() + 
                                "', R_SEVERITY = '" + organCondition.getValue() + "' WHERE W_CODE = '" + entityID.getText() + "' AND WAITLIST.R_CODE = RECIPIENT.R_CODE;");
                    }
                    if(entityEdit.getValue().equals("Organ Donor")){
                        executeUpdate("UPDATE DONOR SET D_DOB = '" + currentDOBText.getText() + "', D_HOSPITAL = '" + hospitalLoc.getValue() + 
                                "', D_FNAME = '" + currentfNameText.getText() + "', D_LNAME = '" + currentlNameText.getText() + "', D_AREA_CODE = '" + 
                                currentAreaCodeText.getText() + "', D_TELE = '" + currentTelephoneText.getText() + "', D_BLOOD_TYPE = '" + bloodType.getValue() + 
                                "' WHERE D_CODE = '" + entityID.getText() + "';");
                    }
                }else if (e.getText().equals(searchBTN.getText())){
                    if(entityEdit.getValue().equals("Patient")){
                        ResultSet rs = executeQuery("SELECT * FROM RECIPIENT, WAITLIST WHERE W_CODE = '" + entityID.getText() + "' and RECIPIENT.R_CODE = WAITLIST.R_CODE;");
                        fillAdminInfo(rs, "patient"); 
                    }
                    if(entityEdit.getValue().equals("Organ Donor")){
                       ResultSet rs = executeQuery("SELECT * FROM DONOR, ORGAN WHERE O_CODE = '" + entityID.getText() + "' AND DONOR.D_CODE = ORGAN.D_CODE;");
                        fillAdminInfo(rs, "donor");
                    }
                }
                break;    
            default: break;
        }
    } //Driver responsible for everything to do with button clicks
    
    public void initializeDB(){
        String url = "jdbc:mysql://localhost:3306/?user=root/mydb&serverTimezone=EST";
        String username = "root";
        String password = "password";   
        
        try{
            DB = DriverManager.getConnection(url, username, password);
            System.out.println("connected");
            String start ="use mydb"; 
            Statement stmt = DB.createStatement();
            stmt.execute(start);
        }catch (SQLException e){
            System.err.println(e);
        }
    } //Database initialization
    
    public void testScript(){
        String query;
            try{
            query = ("select * from doctor;");
            Statement stmt = DB.createStatement();
            stmt.executeQuery(query);
                System.out.println("success");
            }catch (SQLException exc){

             System.err.println(exc);   
  
            }
    } //Debug test script
    
    public boolean checkPassword(ResultSet rs, String user, String pass){
        try{
            while(rs.next()){
                String password = rs.getString("A_PASSWORD");
                String username = rs.getString("A_USERNAME");
                String adminString = rs.getString("A_ACCESS");
                if(adminString.equals("admin")){
                    admin = true;
                }
                if(user.equals(username) && pass.equals(password)){
                    return true;
                }else{
                    return false;
                }
            }    
        }catch (Exception exc){
             System.err.println(exc);  
            }
        return false;
    } //Password checker

    public void fillListView(ResultSet rs){
        organList.getItems().clear();
        try{
            while(rs.next()){
                if(organPatient == 1){
                String code = "Organ: " + rs.getString("O_CODE");
                organList.getItems().add(code);
                }else if(organPatient == 2){
                String code = "Patient: " + rs.getString("W_CODE");
                String name = rs.getString("R_FNAME") + " " + rs.getString("R_LNAME");
                        
                organList.getItems().add(code + " - " + name);   
                }
            }
        }catch (SQLException exc){
            System.err.println(exc);   
        }
        
    } //fills the listView on page 2
    
    public void fillClaimedList(ResultSet rs){
        claimedOrgans.getItems().clear();
        try{
            while(rs.next()){             
                String code = "Organ: " + rs.getString("O_CODE");
                claimedOrgans.getItems().add(code); 
                }      
        }catch (SQLException exc){
            System.err.println(exc);   
        }
    }
    
    public void fillEntityInfo(ResultSet rs){
        try{
            while(rs.next()){
                if(organPatient == 1){
                String date = rs.getString("O_DATE");
                organDateText.setText(date);
                organTypeText.setText(organType.getValue().toString());
                organLocationText.setText(hospitalLoc.getValue().toString());
                organConditionText.setText(organCondition.getValue().toString());
                organBloodTypeText.setText(bloodType.getValue().toString());
                }else if(organPatient == 2){
                String date = rs.getString("W_DATE");
                String fname = rs.getString("R_FNAME");
                String lname = rs.getString("R_LNAME");
                patientOrganText.setText(organType.getValue().toString());
                organLocationText.setText(hospitalLoc.getValue().toString());
                organBloodTypeText.setText(bloodType.getValue().toString());
                patientSeverityText.setText(organCondition.getValue().toString());
                patientInputDateText.setText(date);
                patientNameText.setText(fname + " " + lname);
                
                
                }
            }
        }catch (SQLException exc){
            System.err.println(exc);   
        } 
    } //fills the entity info on pages 3/4
    
    public ResultSet executeQuery(String query){
        try{
            Statement stmt = DB.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query + " \ncompleted successfully");
            return rs;
        }catch (SQLException exc){
             System.err.println(exc);   
             return null;
        }       
    } //Driver to execute DB queries
    
    public String executeUpdate(String query){
        try{
            Statement stmt = DB.createStatement();
            stmt.executeUpdate(query);
            return "Success";
        }catch (SQLException exc){
             System.err.println(exc);   
             return "Fail";
        }   
    } //Driver to execute DB updates
    
    public void fillAdminInfo(ResultSet rs, String person){
        try{
            while(rs.next()){    
                String fname = null;
                String lname = null;
                String DOB = null;
                String Location = null;
                String area_code = null;
                String tele = null;
                String blood = null;
                String condition = null;
                if(person.equals("patient"))
                {
                    fname = rs.getString("R_FNAME");
                    lname = rs.getString("R_LNAME");
                    DOB = rs.getString("R_DOB");
                    Location = rs.getString("R_HOSPITAL");
                    area_code = rs.getString("R_AREA_CODE");
                    tele = rs.getString("R_TELE");
                    blood = rs.getString("R_BLOOD_TYPE");
                    condition = rs.getString("R_SEVERITY");
                }else if(person.equals("donor")){
                    fname = rs.getString("D_FNAME");
                    lname = rs.getString("D_LNAME");
                    DOB = rs.getString("D_DOB");
                    Location = rs.getString("D_HOSPITAL");
                    area_code = rs.getString("D_AREA_CODE");
                    tele = rs.getString("D_TELE");
                    blood = rs.getString("D_BLOOD_TYPE");
                    condition = rs.getString("O_CONDITION");
                }
                
                currentfNameText.setText(fname);
                currentlNameText.setText(lname);
                currentDOBText.setText(DOB);
                currentAreaCodeText.setText(area_code);
                currentTelephoneText.setText(tele);
                hospitalLoc.setValue(Location);
                organCondition.setValue(condition);
                bloodType.setValue(blood);
                }      
        }catch (SQLException exc){
            System.err.println(exc);   
        }
    }
}
