/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mck;

import com.mathworks.engine.*;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.*;
import java.lang.*;
import org.apache.commons.lang3.ArrayUtils;
/**
 *
 * @author PMIL
 */
public class MoCapData {
    
    public double[] Time = new double[0];
    public double[] MoCap= new double[0];
    public double dMoCapValue;
    
    public MoCapData(double[] Time, double[] MoCap){
    
        this.Time = Time;
        this.MoCap= MoCap;
        
    }
    
    public double[] getTime(){return Time;}    
    public double[] getMoCap(){return MoCap;}
    
    public void enterMoCapData(String MLfunction, MatlabEngine proxy, long t0, boolean full, int counter)throws Exception {              
        
        /**
        * Method for creating a 2d-list with time and MoCap data
        * 
        * Arguments:
        * MLfunction - the name of the function to call in MatLab, including opening parenthesis but not closing parenthesis
        * proxy - matlab workspace
        * t0 - stopwatch time
        * stp - started StopWatch object
        * full - true if the list has no empty indices except possibly the last one, false otherwise
        * counter - tells the method in which index to put the element if the list is not full
        * 
        */
        dMoCapValue=0;
        
        proxy.eval("a="+MLfunction+";");
        
        proxy.eval("a=double(a);");
        dMoCapValue= proxy.getVariable("a");
        
         
         
        //get time and convert to double
        
        long lElapsedTime = (System.currentTimeMillis() - t0)/100;
        double dElapsedTime=(double) lElapsedTime;
        
        
//        System.out.println("gettime in mocap  "+ String.valueOf(stp.getTime()));
//        System.out.println("t0 "+String.valueOf(t0));
//        System.out.println("elapsedtime  "+String.valueOf(elapsedTime));
        //System.out.println("delapsedtime"+String.valueOf(dElapsedTime));
        
        
        //Data = new MoCapData(dElapsedTime, dMoCapValue); 
        
        if(full==true){
            
            //if the list is full then we know that we can add the data at the end
//            Time[Time.length-1]=dElapsedTime;
//            MoCap[MoCap.length-1]=dMoCapValue;
            Time=ArrayUtils.add(Time, dElapsedTime);
            MoCap=ArrayUtils.add(MoCap, dMoCapValue);
            
            
            //and clear the first index and shift the others to the left
            Time=ArrayUtils.remove(Time, 0);
            MoCap=ArrayUtils.remove(MoCap, 0);
                    } 
        else {
            
            //if the list is not full we need to look for the first empty slot
//            Time[counter]=dElapsedTime;
//            MoCap[counter]=dMoCapValue;
            //System.out.println("MoCapValue:"+ String.valueOf(MoCap[counter]));
            
            Time=ArrayUtils.add(Time, dElapsedTime);
            MoCap=ArrayUtils.add(MoCap, dMoCapValue);
        }
        
                      
    
    }
    
    
}
