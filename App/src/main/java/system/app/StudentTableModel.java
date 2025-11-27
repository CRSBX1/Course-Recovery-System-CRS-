/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
/**
 *
 * @author Lenovo
 */
public class StudentTableModel{
    private String[] colNames = {"Student ID","Name"};
    private DefaultTableModel tableModel;
    private EligibilityChecker check;
    
    public StudentTableModel(){
        check = new EligibilityChecker();
        tableModel = new DefaultTableModel(colNames,0){
            @Override public boolean isCellEditable(int r, int c){ return false;}
        };
        
        //Add table contents
        addTableContents();
    }
    
    public DefaultTableModel getTableModel(){
        return tableModel;
    }
    
    private void addTableContents(){
        for(Student s: DataRepository.studentList){
            if(!check.checkEnrollmentStatus(s)){
                tableModel.addRow(new Object[]{s.getStudentID(),s.getStudentName()});
            }   
        }
    }
    
    public void refreshTableContents(){
        for(int i = tableModel.getRowCount()-1;i>=0;i--){
            tableModel.removeRow(i);
        }
        
        addTableContents();
    }
}
