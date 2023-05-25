package com.personal_project.type;

//Interface for specifying the file types the GUI may hold e.g. XMl, JSON


import org.w3c.dom.Document;

import java.util.Date;

public interface CustomFileDuplication {

    //Enters the custom tag alongside the description of it. Returns true if successful
    //Customize represents whether the duplication should have custom attributes
    public boolean executeDuplication(int noOfTimes, String templateName, String pathway,boolean customize, int fileNumber);

    //Changing the mdofiication of the file, given the pathway of the String
    public void changeDuplicationDate(String pathway, Date fromDate, Date toDate);

}
