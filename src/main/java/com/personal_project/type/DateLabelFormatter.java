package com.personal_project.type;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private String patternDate = "dd-MM-yyyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(patternDate);
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormat.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            Date time = cal.getTime();
            return dateFormat.format(time);
        }

        return "";
    }
}
