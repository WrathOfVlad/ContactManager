package com.contactmanager.utils.viewutils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.ArrayUtils;

import com.contactmanager.datamodel.itemtypes.DateItem;

public class CustomDatePicker {
		
	int month = Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = Calendar.getInstance().get(java.util.Calendar.YEAR);
    //create object of JLabel with alignment
    JLabel l = new JLabel("", JLabel.CENTER);
    //define variable
    String day = "";
    //declaration
    JDialog d;
    //create object of JButton
    JButton[] button = new JButton[49];
	
	public CustomDatePicker(/*JFrame parent*/) {
		d = new JDialog();
        //set modal true
        d.setModal(true);
        //define string
        String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
        //create JPanel object and set layout
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        //set size
        p1.setPreferredSize(new Dimension(450, 120));
        //for loop condition
        for (int x = 0; x < button.length; x++) {		
            final int selection = x;
            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBackground(Color.white);
            
            if (x < 7){
            	button[x].setText(header[x]);
                //set fore ground colour
                button[x].setForeground(Color.red);
            }
            else{
            	button[x].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) 
                    {
                          day = button[selection].getActionCommand();
                          //call dispose() method
                          if(day.equals("")) return;
                          
                          d.dispose();
                    }
                });
            }
            p1.add(button[x]);//add button
        }
        //create JPanel object with grid layout
        JPanel p2 = new JPanel(new GridLayout(1, 3));
        
        String[] monthNames = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        JComboBox<String> monthComboBox = new JComboBox<String>(monthNames);
        monthComboBox.setSelectedIndex(month);
        monthComboBox.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
		        @SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String monthString = (String)cb.getSelectedItem();
		        month = ArrayUtils.indexOf(monthNames,monthString);
		        displayDate();
		    }
		});
        
        
        List<String> yearList = new ArrayList<>();
        for(int i=year-150;i<=year+50;i++){
        	yearList.add(String.valueOf(i));
	    }
        
        JComboBox<String> yearComboBox = new JComboBox<String>(yearList.toArray(new String[yearList.size()]));
        yearComboBox.setSelectedIndex(yearList.indexOf(String.valueOf(year)));
        yearComboBox.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
		        @SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String yearString = (String)cb.getSelectedItem();
		        year = Integer.parseInt(yearString);
		        displayDate();
		    }
		});
        
        //create object of button for previous month
        p2.add(monthComboBox);
        //p2.add(l);//add label
        p2.add(yearComboBox);
        
        
        //create object of button for next month
        
        //set border alignment
        d.add(p2, BorderLayout.NORTH);
        d.add(p1, BorderLayout.CENTER);
        
        d.pack();
        //set location
        //d.setLocationRelativeTo(parent);
        //call method
        displayDate();
        //set visible true
        d.setVisible(true);
        p2.requestFocus();
	}
	public void displayDate() 
    {
    	for (int x = 7; x < button.length; x++) {
    		button[x].setText("");//set text
    	}
    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DateItem.dateFormat);	
    	java.util.Calendar cal = java.util.Calendar.getInstance();	
    	cal.set(year, month, 1); //set year, month and date
     	//define variables
    	int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
    	int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
    	//condition
    	for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
    	//set text
    	button[x].setText("" + day);
    	l.setText(sdf.format(cal.getTime()));
    	//set title
    	d.setTitle("Date Picker");
    }
	public String setPickedDate() 
    {
    	//if condition
	      if (day.equals("")) return day;
	      	
	      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DateItem.dateFormat);
	      java.util.Calendar cal = java.util.Calendar.getInstance();
	      cal.set(year, month, Integer.parseInt(day));
	      return sdf.format(cal.getTime());
    }
}
