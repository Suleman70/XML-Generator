package com.personal_project.type;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
//TODO java.lang.NullPointerException: Cannot invoke "java.io.File.exists()" because the return value of "javax.swing.JFileChooser.getSelectedFile()" is null
public class GUI{

    //GUI Features Fields
    private JFrame frame;
    private JPanel fillInPanel;
    private JTextField noOfFilesField;
    private JTextField fileNameField;
    private JDatePickerImpl fromDateImp;
    private JDatePickerImpl toDateImp;
    private JFileChooser fileChooser;
    private JProgressBar progressBar;
    private JButton submitButton;
    private JButton selectFileButton;
    private JLabel completeLabel;

    private JLabel selectFileLabel;

    private JLabel errMsg;


    //Constructor
    public GUI() {
        createGUI();
    }

    public void createGUI() {

        frame = new JFrame("XML Generator");
        fillInPanel = new JPanel();

        //Adding Number of Files GUI Features
         JLabel noOfFilesLabel = new JLabel("No Of Files");
        noOfFilesField = new JTextField();
        noOfFilesLabel.setBounds(10,20,80,25);
        noOfFilesField.setBounds(100,20,165,25);
        fillInPanel.add(noOfFilesLabel);
        fillInPanel.add(noOfFilesField);

        //-----------------------

        //Adding Error Msg Label GUI
        errMsg = new JLabel("<html>Maximum 50000 Files Supported <br> File Number Will Follow File Name<html>");
        fillInPanel.add(errMsg);
        errMsg.setBounds(10,220,400,80);

        //-----------------------

        //Adding File Name GUI Features
        JLabel fileNameLabel = new JLabel("File Name");
        fileNameField = new JTextField();
        fileNameLabel.setBounds(10,50,80,25);
        fileNameField.setBounds(100,50,165,25);
        fillInPanel.add(fileNameLabel);
        fillInPanel.add(fileNameField);

        //-----------------------

        //Setting Properties for Date (Formatting)
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        //Before Date GUI Features

        JLabel fromDateLabel = new JLabel("From Date");
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);

        fromDateImp = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        fromDateImp.getModel().setDay(1);
        fromDateImp.getModel().setYear(1995);
        fromDateImp.getModel().setDay(1);
        fromDateLabel.setBounds(10,85,80,25);
        fromDateImp.setBounds(100,85,120,30);
        fillInPanel.add(fromDateImp);
        fillInPanel.add(fromDateLabel);


        //To date GUI Features
        JLabel toDateLabel = new JLabel("To Date");
        UtilDateModel toDateModel = new UtilDateModel();
        toDateModel.setSelected(true);
        JDatePanelImpl toDatePanel = new JDatePanelImpl(toDateModel,p);
        toDateImp = new JDatePickerImpl(toDatePanel, new DateLabelFormatter());
        toDateImp.setBounds(100,120,120,30);
        toDateLabel.setBounds(10,120,80,25);
        fillInPanel.add(toDateImp);
        fillInPanel.add(toDateLabel);

        //-----------------------

        //Select Directory GUI Features
        selectFileButton = new JButton("Select Directory");
        selectFileLabel = new JLabel("Selected: Nothing");
        selectFileButton.setBounds(10,185,130,25);
        selectFileLabel.setBounds(10,155,500,25);
        fillInPanel.add(selectFileButton);
        fillInPanel.add(selectFileLabel);
        fileChooser = new JFileChooser(); //File Chooser, this is declared before, to outline all errors in the GUI

        //Event Listener for Selecting File from Local Directory
        selectFileButton.addActionListener(e -> {
            selectDirectoryActionListener();
        });

        //-----------------------

        //Warning Information
        JLabel warningLabel = new JLabel("<html><span style='color:orange'>Warning!</span> clicking start will duplicate your  selected number of files <br>" + " to your selected directory</html>" );
        warningLabel.setBounds(10,310,500,30);
        fillInPanel.add(warningLabel);

        //-----------------------

        //Submit Button
        submitButton = new JButton("Start");
        submitButton.setBounds(10,350,80,25);
        fillInPanel.add(submitButton);

        //Progress Bar
        progressBar = new JProgressBar(0,100);
        progressBar.setBounds(100,350,180,25);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        fillInPanel.add(progressBar);
        completeLabel = new JLabel("");
        completeLabel.setBounds(10,220,400,80);
        fillInPanel.add(completeLabel);
        //-----------------------

        //GUI Configuration
        frame.setResizable(true);
        fillInPanel.setLayout(null);
        frame.setSize(450,440);
        frame.add(fillInPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //-----------------------

        //Event Listener for Submit Button - Executing the Duplication
        submitButton.addActionListener((e -> {
            Thread thread = new Thread(() -> {

                try {
                     submitButtonActionListener();

                }
                catch(Exception ex){
                    System.out.println(ex);
                    errMsg.setText("<html><span style='color:red'>Something Went Wrong... Try reopening</span></html>");
                }
            });
            thread.start();
        }));

    }


    //When user clicks "Select Directory", helper method for Action Listener
    private void selectDirectoryActionListener(){
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        int result = fileChooser.showDialog(null, "Select");
        if (result == JFileChooser.APPROVE_OPTION) {
            selectFileLabel.setText(this.fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    //When user clicks "Start", helper method for Action Listener

    private void submitButtonActionListener(){

        if(inputValidationChecker() == true) {

            int noOfDuplications = Integer.parseInt(noOfFilesField.getText());
            String fileName = fileNameField.getText();
            String directorySelected = fileChooser.getSelectedFile().getAbsolutePath();

            //Repeating No Of Times depending on user's input
            for (int i = 1; i <= noOfDuplications; i++) {
                completeLabel.setText("");
                XMLFileDuplication xml = new XMLFileDuplication();
                Boolean xmlCheck =xml.executeDuplication(noOfDuplications, fileName, directorySelected, true, i); //Executing Duplication

                if(xmlCheck == false){
                    errMsg.setText("<html><span style='color:red'>Something Went Wrong... Try reopening</span></html>");
                    break;
                }

                String filePathway = directorySelected + '/' + fileName + i + ".xml";
                xml.changeDuplicationDate(filePathway, (Date) fromDateImp.getModel().getValue(), (Date) toDateImp.getModel().getValue()); //Changing the modification date

                int percentComplete = (int) (((double) i / noOfDuplications) * 100);
                progressBar.setValue(percentComplete);
            }
            completeLabel.setText("<html><span style='color:green'>Complete! " + noOfDuplications+ " files generated" + "</span></html>");
        }
        else{
            completeLabel.setText(""); //Just in case, complete is still showing
        }

    }

    //Overall checker for validation by calling helper methods, returns false if validation fails, also sets errMsg JLabel
    private boolean inputValidationChecker(){
        boolean valid = true;
        String msg = "";
       if(checkNoOfFilesField( noOfFilesField) == false){ //Error Msg for NoOfFields
           msg =  msg + "*File number must be number between 0 and 50000 inclusive <br>";
           valid = false;
       }
       if(checkTemplateNameField(fileNameField) == false){ //Error Msg for Template Name
           msg =  msg + "*File name must be alphanumerical characters and not empty <br>";
           valid = false;
        }
       if(checkDatesValid(fromDateImp, toDateImp) == false){ //Error Msg for Invalid Date(s)
           msg =  msg + "*Date must not be empty and from date must come before to date <br>";
           valid = false;
       }
        if(checkDirectoryExists(fileChooser) == false){ //Error Msg for Invalid Date(s)
            msg =  msg + "*Directory does not exist <br>";
            valid = false;
        }

       //errMsg.setText(msg);
       errMsg.setText("<html> <span style='color:red'>" + msg + "</span></html>"); //Setting the Error Msg Label
       return valid;


    }

    //Helper method for validating NoOfFiles Field
    private boolean checkNoOfFilesField(JTextField noOfFilesFieldText){

        //Checking if Empty
        if(noOfFilesFieldText.getText() == null || noOfFilesFieldText.getText() == ""){
            return false;
        }

        //Must be Between 0 and 50000 Files and a number
        try{
            int noOfFiles = Integer.parseInt(noOfFilesFieldText.getText());
            if(noOfFiles >= 0 && noOfFiles <= 50000){
                return true;
            }
            else{
                return false;
            }
        }
        catch(NumberFormatException e){
            return false;
        }
    }

    //Helper method for validating templateNameFieldText
    private boolean checkTemplateNameField(JTextField templateNameFieldText) {
        System.out.println("template : " + templateNameFieldText.getText());
        String removeInvalidCharString = templateNameFieldText.getText().replaceAll("[\\\\/:*?\"<>|]", "_"); //Removing Invalid Symbols
        templateNameFieldText.setText(removeInvalidCharString);
        if (templateNameFieldText.getText().equals("") || templateNameFieldText.getText() == null) {
            System.out.println("Should be empty");
            return false;
        }
        //Checking if alphanumerical character
        try {
            int testNumber = Integer.parseInt(templateNameFieldText.getText()); //Meaning it has only numbers, so it is disallowed
            return false;

        } catch (NumberFormatException e) {

            return true; //Means there is Alphanumerical characters, which is allowed
        }


    }


    //Helper method for validating if dates are valid
    private boolean checkDatesValid(JDatePickerImpl fromDate, JDatePickerImpl toDate){
        Date first = (Date) fromDate.getModel().getValue();
        Date second  = (Date) toDate.getModel().getValue();
        if(first == null ||  second == null){
            return false;
        }
        else {
            return first.getTime() <= second.getTime(); //Checks whether beforeDate is before the toDate by comparing double
        }
    }
    //Helper method for validating whether directory exists or not

    private boolean checkDirectoryExists(JFileChooser chooser){
        if(chooser.getSelectedFile() == null || chooser == null){
            System.out.println("Returning False it should be`");
            return false;
        }
        else {
            return chooser.getSelectedFile().exists();
        }
    }


}