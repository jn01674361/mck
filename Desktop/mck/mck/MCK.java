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

    public static double dStartToBox;
    public static double d2CHAIRAREA;
    public static double d1STEPBOARDAREA;
    public static double d2STEPBOARDAREA;
    public static double d1BOXAREA;
    public static double dCompletion;
    public static double dBackHome=0.15;
    
    public static double dElapsedTime;
    public static long lElapsedTime;
    
    public static DataContainer Data;
    public static MatlabEngine eng;

    
    public static boolean bEnteredBoxArea=false;
    
    //declare RB enumeration
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
        

        int iListLength=5;
        int iNoMoCapData= 12;
        

        String sFolderPath = "C:\\Users\\PMIL\\Desktop\\jn\\MoCapExec";
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
        
        Data = new DataContainer();
        Data.commandList=sCommands;
        Data.iListLength = iListLength;
        Data.counter=0;
        configure();  
        
        Data.initTime = System.currentTimeMillis();
              
        
        try{            
            while(bEnteredBoxArea==false || dCompletion>dBackHome){
             
                dCompletion = Completion(eng);
                
                lElapsedTime = (System.currentTimeMillis() - Data.initTime)/100;
                dElapsedTime=(double) lElapsedTime;
                
                System.out.println("completion "+String.valueOf(100*dCompletion)+"  %");
                System.out.println("time "+String.valueOf(dElapsedTime)+"  *10^-1 s");
                
                if(checkChairArea()==true){
                    Movement123678();
                    System.out.println("CHAIR");
                }
                else if(checkStepboardArea()==true){
                    Movement123678();
                    System.out.println("STEPBOARD");
                }
                else if(checkBoxArea()==true){
                    Movement45();
                    bEnteredBoxArea=true;
                    System.out.println("BOX");
                }
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
        
        
        Data.setLeftShouldervNorm(eng);
        Data.setRightShouldervNorm(eng);
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);
        Data.setLeftKneeAngV(eng);    
        Data.setRightKneeAngV(eng);
        Data.setLeftKneeAngl(eng);    
        Data.setRightKneeAngl(eng);              
        
        Data.counter++;
        //System.out.println("counter "+String.valueOf(Data.counter));
        
        PrintShouldervNorm();
        PrintHipvNorm();
        PrintKneeAngV();
        PrintKneeAngl();
      
    }
    public static void Movement45()throws Exception{
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
        
        Data.counter++;
        //System.out.println("counter "+String.valueOf(Data.counter));
        
        PrintShouldervNorm();
        PrintHipvNorm();
        PrintKneeAngV();        
        PrintKneeAngl();
        PrintElbowAngV();
        PrintElbowAngl();
         



    }

    public static void Gait()throws Exception{
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);

        
        Data.counter++;
        //System.out.println("counter "+String.valueOf(Data.counter));
        
        PrintHipvNorm();

    }


    public static boolean checkChairArea(){
        if(dCompletion<dCHAIRAREA){return true;}
        else{return false;}
        
    }
    public static boolean checkStepboardArea(){
        if(dCompletion<d2STEPBOARDAREA && dCompletion>d1STEPBOARDAREA{return true;}
        else{return false;}
        
    }
    public static boolean checkBoxArea(){
        if(dCompletion>d1BOXAREA){return true;}
        else{return false;}
    }

    public static void configure()throws Exception{
        //guide for configuring the necessary distances to be able to keep track of where in the track the subject is 

        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other just in front of the BOX at the end of track.");
        pressEnterToContinue();
        dStartToBox = GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2);
        System.out.println("Track length configured.");

        System.out.print("Place one marker on the floor in the middle of the two front legs of the chair, and the other where the CHAIR AREA ENDS.");
        pressEnterToContinue();
        d2CHAIRAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        System.out.println("CHAIR AREA CONFIGURED"+ String.valueOf(d2CHAIRAREA));

        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other where the STEPBOARD AREA BEGINS.");
        pressEnterToContinue();
        d1STEPBOARDAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        
        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other where the STEPBOARD AREA ENDS.");
        pressEnterToContinue();
        d2STEPBOARDAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        System.out.println("STEPBOARD AREA CONFIGURED"+ String.valueOf(d2STEPBOARDAREA));

        System.out.println("Place ocne marker on the floor in the middle of the two front legs of the chair, and the other where the BOX AREA BEGINS.");
        pressEnterToContinue();
        d1BOXAREA = (GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2))/dStartToBox;
        System.out.println("BOX AREA CONFIGURED" + String.valueOf(d1BOXAREA));            
        
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
        eng.eval("currentPos=RBPosition("+sMarker1+", Client);");
        eng.eval("endPos=RBPosition("+sMarker2+", Client);");
        eng.eval("Distance=sqrt(sum( (currentPos-endPos).^2));");
        eng.eval("Distance=double(Distance);");
        double dist = eng.getVariable("Distance");
        return dist;
    }
    
    public static double distFromStart(String sMarker) throws Exception{
    
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
        *Arguments:
        *
        *eng - Matlab workspace

        *Averages the position of the two feet markers

        */
       
        double dProgress1 = distFromStart(sLeftKnee);
        double dProgress2 = distFromStart(sRightKnee);
        
        double dCompletion = (dProgress1+dProgress2)/(2.0*dStartToBox);

        return dCompletion;


    }



    }


    

