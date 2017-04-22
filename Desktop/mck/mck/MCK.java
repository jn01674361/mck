/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mck;

import com.mathworks.engine.*;
import java.util.*;
import java.lang.*;
import java.time.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.ArrayUtils;
import java.io.PrintWriter;


/**
 *
 * @author PMIL
 */
public class MCK {
        

    /**
     * @param args the command line arguments
     */

    //distaces
    public static double dStartToBox;
    public static double d2CHAIRAREA;
    public static double d1STEPBOARDAREA;
    public static double d2STEPBOARDAREA;
    public static double d1BOXAREA;
    public static double dCompletion;
    public static double dBackHome=0.15;
    
    public static double dElapsedTime;
    public static long lElapsedTime;
    
    //class containging mocap data
    public static DataContainer Data;

    //matlab workspace
    public static MatlabEngine eng;

    //bool for knowing if the subject has reached the box zone yet or not
    public static boolean bEnteredBoxArea=false;
    
    //declare RB enumeration, this is set in Motive when the RB's are imported
    public static String sLeftShoulder="11";
    public static String sRightShoulder="12";
    public static String sLeftElbow="8";
    public static String sRightElbow="7";
    public static String sLeftHand="4";
    public static String sRightHand="5";
    public static String sLeftHip="9";
    public static String sRightHip="10";
    public static String sLeftKnee="1";
    public static String sRightKnee="6";
    public static String sLeftFoot = "2";
    public static String sRightFoot = "3";
    public static String sMeasure1 ="13";
    public static String sMeasure2="14";
    
    
    
    
    
    /**
     *
     * @throws Exception
     */
    public static void main(String[] args)throws Exception {
        
        //number of arrays and array length of mocapdata objects
        int iListLength=5;
        int iNoMoCapData= 12;
        
        //path to matlab files
        String sFolderPath = "C:\\Users\\PMIL\\Desktop\\jn\\MoCapExec";
        
        //array containing commands to run in matlab
        String[] sCommands = new String[iNoMoCapData];                
        
        //declare MatLab commands
        String sVelocityNorm = "velocityNorm(Client,";
        String sAngularVelocity = "angularVelocity(Client,";
        String sAccNorm="accNorm(Client,";
        String sAngle = "angle(Client,";
        
                
        //add Matlab commands and parameters to commandlist 
        sCommands[0]=sVelocityNorm+sLeftShoulder+")";
        sCommands[1]=sVelocityNorm+sRightShoulder+")";
        sCommands[2]=sVelocityNorm+sLeftHip+")";
        sCommands[3]=sVelocityNorm+sRightHip+")";
        sCommands[4]=sAngularVelocity+sLeftFoot+","+sLeftHip+","+sLeftKnee+")";
        sCommands[5]=sAngularVelocity+sRightFoot+","+sRightHip+","+sRightKnee+")";
        sCommands[6]=sAngularVelocity+sLeftHand+","+sLeftShoulder+","+sLeftElbow+")";
        sCommands[7]=sAngularVelocity+sRightHand+","+sRightShoulder+","+sRightElbow+")";
        sCommands[8]=sAngle+sLeftHand+","+sLeftShoulder+","+sLeftElbow+")";
        sCommands[9]=sAngle+sRightHand+","+sRightShoulder+","+sRightElbow+")";
        sCommands[10]=sAngle+sLeftFoot+","+sLeftHip+","+sLeftKnee+")";
        sCommands[11]=sAngle+sRightFoot+","+sRightHip+","+sRightKnee+")";
        

        for(int i =0;i<sCommands.length;i++){System.out.println(sCommands[i]);}
        
         //initiate matlab workspace
        eng = MatlabEngine.startMatlab();

        // add directory to matlab search path
        eng.eval("addpath('"+sFolderPath+"')");        
        // programmatically run natnet sdk
        eng.eval("run('natnet.m')");                
        // create instance of natnet
        eng.eval("Client=natnet()");        
        // set host ip
        eng.eval("Client.HostIP='127.0.0.1'");        
        // set client ip
        eng.eval("Client.HostIP='127.0.0.1'");        
        // connect to server
        eng.eval("Client.connect");                            
        
        //initiate datacontainer object
        Data = new DataContainer();
        //add matlab commands
        Data.commandList=sCommands;
        //add array length
        Data.iListLength = iListLength;
        //start the counter
        Data.counter=0;
        //configure distances 
        configure();  
        //set time origin
        Data.initTime = System.currentTimeMillis();
              
        
        try{            
            while(bEnteredBoxArea==false || dCompletion>dBackHome){
                //while the subject is not in the starting position and has not yet reached the box area

                //check how much of the track has been completed
                dCompletion = Completion(eng);
                
                //get how much time has passed and convert to seconds *10^-1
                lElapsedTime = (System.currentTimeMillis() - Data.initTime)/100;
                dElapsedTime=(double) lElapsedTime;
                
                System.out.println("completion "+String.valueOf(100*dCompletion)+"  %");
                System.out.println("time "+String.valueOf(dElapsedTime)+"  *10^-1 s");
                
                //if the subject is in the chair zone, set and print the relevant data
                if(checkChairArea()==true){
                    Movement123678();
                    System.out.println("CHAIR");
                }
                //subject in stepboard zone
                else if(checkStepboardArea()==true){
                    Movement123678();
                    System.out.println("STEPBOARD");
                }
                //subject in box zone
                else if(checkBoxArea()==true){
                    Movement45();
                    bEnteredBoxArea=true;
                    System.out.println("BOX");
                }
                //if the subject is inbetween zones, set and print the relevant data for gait
                else{Gait();}


            }
        }catch(java.lang.ArrayIndexOutOfBoundsException e){
            System.out.println("INDEX OUT OF RANGE" );
        
        }
        
        
        // disconnect natnet 
        eng.eval("Client.disconnect");
        
        // remove directory from matalab search path
        eng.eval("rmpath('"+sFolderPath+"')");
        
        // close connection
        eng.disconnect();
    }

    //All print methods work analogously, loop through a specific array and print the mocap data along with the time 

    public static void PrintShouldervNorm(){
        for(int i=0;i<Data.LeftShouldervNorm.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftShouldervNorm.Time[i]) + " LEFT SHOULDER VELOCITY NORM "+ String.valueOf(Data.LeftShouldervNorm.MoCap[i])+ " T "+ String.valueOf(Data.RightShouldervNorm.Time[i]) +" RIGHT SHOULDER VELOCITY NORM " + String.valueOf(Data.RightShouldervNorm.MoCap[i]));
                }
    }

    public static void PrintHipvNorm(){
        for(int i=0;i<Data.LeftHipvNorm.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftHipvNorm.Time[i]) + " LEFT HIP VELOCITY NORM"+ String.valueOf(Data.LeftHipvNorm.MoCap[i])+ " T "+ String.valueOf(Data.RightHipvNorm.Time[i]) +" RIGHT HIP VELOCITY NORM " + String.valueOf(Data.RightHipvNorm.MoCap[i]));
                }
    }

    public static void PrintKneeAngV(){
        for(int i=0;i<Data.LeftKneeAngV.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftKneeAngV.Time[i]) + " LEFT KNEE ANGULAR VELOCITY"+ String.valueOf(Data.LeftKneeAngV.MoCap[i])+ " T "+ String.valueOf(Data.RightKneeAngV.Time[i]) +" RIGHT KNEE ANGULAR VELOCITY " + String.valueOf(Data.RightKneeAngV.MoCap[i]));
                }
    }

    public static void PrintElbowAngV(){
        for(int i=0;i<Data.LeftElbowAngV.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftElbowAngV.Time[i]) + " LEFT ELBOW ANGULAR VELOCITY "+ String.valueOf(Data.LeftElbowAngV.MoCap[i])+ " T "+ String.valueOf(Data.RightElbowAngV.Time[i]) +" RIGHT ELBOW ANGULAR VELOCITY " + String.valueOf(Data.RightElbowAngV.MoCap[i]));
                }
    }

    public static void PrintKneeAngl(){
        for(int i=0;i<Data.LeftKneeAngl.MoCap.length;i++){
                    //System.out.println(String.valueOf(Data.LeftKneeAngl.MoCap.length));
                    System.out.println("T "+ String.valueOf(Data.LeftKneeAngl.Time[i]) + " LEFT KNEE ANGLE "+ String.valueOf(Data.LeftKneeAngl.MoCap[i])+ " T "+ String.valueOf(Data.RightKneeAngl.Time[i]) +" RIGHT KNEE ANGLE " + String.valueOf(Data.RightKneeAngl.MoCap[i]));
                }
    }

    public static void PrintElbowAngl(){
        for(int i=0;i<Data.LeftElbowAngl.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftElbowAngl.Time[i]) + " LEFT ELBOW ANGLE "+ String.valueOf(Data.LeftElbowAngl.MoCap[i])+ " T "+ String.valueOf(Data.RightElbowAngl.Time[i]) +" RIGHT ELBOW ANGLE " + String.valueOf(Data.RightElbowAngl.MoCap[i]));
                }
            }

    
    public static void Movement123678()throws Exception{
        
        //sets the data deemed relevant for movements in chair zone and stepboard zone
        Data.setLeftShouldervNorm(eng);
        Data.setRightShouldervNorm(eng);
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);
        Data.setLeftKneeAngV(eng);    
        Data.setRightKneeAngV(eng);
        Data.setLeftKneeAngl(eng);    
        Data.setRightKneeAngl(eng);              
        
        //increase counter by 1
        Data.counter++;
        
        //print the recorded data
        PrintShouldervNorm();
        PrintHipvNorm();
        PrintKneeAngV();
        PrintKneeAngl();
      
    }
    public static void Movement45()throws Exception{
        
        //sets the data deemed relevant for movements in the box zone
        Data.setLeftShouldervNorm(eng);
        Data.setRightShouldervNorm(eng);
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);
        Data.setLeftKneeAngV(eng);    
        Data.setRightKneeAngV(eng);
        Data.setLeftElbowAngV(eng);
        Data.setRightElbowAngV(eng);
        Data.setLeftKneeAngl(eng);    
        Data.setRightKneeAngl(eng); 
        Data.setLeftElbowAngl(eng);
        Data.setRightElbowAngl(eng);    
        
        //increase counter by 1
        Data.counter++;

        //print the recorded data
        PrintShouldervNorm();
        PrintHipvNorm();
        PrintKneeAngV();        
        PrintKneeAngl();
        PrintElbowAngV();
        PrintElbowAngl();
         



    }

    public static void Gait()throws Exception{
        
        //set the data deemed relevant for gait
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);

        //increase counter by 1
        Data.counter++;

        //print the recorded data
        PrintHipvNorm();

    }

    //returns true if the subject is in what is recorded as the chair zone
    public static boolean checkChairArea(){
        if(dCompletion<dCHAIRAREA){return true;}
        else{return false;}
        
    }
    //returns true if the subject is in what is recorded as the stepboard zone
    public static boolean checkStepboardArea(){
        if(dCompletion<d2STEPBOARDAREA && dCompletion>d1STEPBOARDAREA{return true;}
        else{return false;}
        
    }
    //returns true if the subject is in what is recorded as the box zone
    public static boolean checkBoxArea(){
        if(dCompletion>d1BOXAREA){return true;}
        else{return false;}
    }

    public static void configure()throws Exception{
        //guide for configuring the necessary distances to be able to keep track of where in the track the subject is 

        //length of track from starting position to box at the end
        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other just in front of the BOX at the end of track.");
        pressEnterToContinue();
        dStartToBox = GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2);
        System.out.println("Track length configured.");

        //configure the end  limit of the chair area
        System.out.print("Place one marker on the floor in the middle of the two front legs of the chair, and the other where the CHAIR AREA ENDS.");
        pressEnterToContinue();
        d2CHAIRAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        System.out.println("CHAIR AREA CONFIGURED"+ String.valueOf(d2CHAIRAREA));

        //configure the start limit of the stepboard area
        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other where the STEPBOARD AREA BEGINS.");
        pressEnterToContinue();
        d1STEPBOARDAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        
        //configure the end limit of the stepboard end
        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other where the STEPBOARD AREA ENDS.");
        pressEnterToContinue();
        d2STEPBOARDAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        System.out.println("STEPBOARD AREA CONFIGURED"+ String.valueOf(d2STEPBOARDAREA));

        //configure the start limit of the box area
        System.out.println("Place ocne marker on the floor in the middle of the two front legs of the chair, and the other where the BOX AREA BEGINS.");
        pressEnterToContinue();
        d1BOXAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        System.out.println("BOX AREA CONFIGURED" + String.valueOf(d1BOXAREA));            
        
        //return measuring markers to starting position for measuring completion during experiment
        System.out.println("Place the measuring markers as close to the knee marker starting positions as possible. Experiment starts once you continue.");
        pressEnterToContinue();


    }
    
    
    private static void pressEnterToContinue(){ 
        
        //Method for pressing any key to continue, for me it only works with enter though. 

        System.out.println("Press ENTER to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

    public static double GetDistanceBetweenTwoMarkers(String sMarker1, String sMarker2)throws Exception{

        //returns the norm of the vector difference between two RB vectors
        //parameters are the numbers of the two RBs as they are enumerated in Motive

        eng.eval("currentPos=RBPosition("+sMarker1+", Client);");
        eng.eval("endPos=RBPosition("+sMarker2+", Client);");
        eng.eval("Distance=sqrt(sum( (currentPos-endPos).^2));");
        eng.eval("Distance=double(Distance);");
        double dist = eng.getVariable("Distance");
        return dist;
    }
    
    public static double distFromStart(String sMarker) throws Exception{
        
        //gets the distance from a specific marker to one of the measuring markers 

        eng.eval("Pos1 = RBPosition("+ sMeasure1+", Client);");
        eng.eval("Pos2 = RBPosition("+ sMarker+", Client);");
        eng.eval("dist=norm(Pos1-Pos2);");
        eng.eval("dist=double(dist);");
        double dist = eng.getVariable("dist");
        return dist;
    }
    
        

    public static double Completion(MatlabEngine eng)throws Exception{
        /*
        *Method for getting how much of the track has been completed
        *
        * = 0 in start position
        * = 1 by the box at the end of the track
        *
        *Arguments:
        *
        *eng - Matlab workspace

        *Averages the position of the two knee markers

        */
       
        //get distances from start for both knees 
        double dProgress1 = distFromStart(sLeftKnee);
        double dProgress2 = distFromStart(sRightKnee);
        
        //sum and divide by 2 for average
        //divide by track length to get fraction of completion        
        double dCompletion = (dProgress1+dProgress2)/(2.0*dStartToBox);

        return dCompletion;


    }



    }


    

