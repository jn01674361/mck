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
    
    public void Run(){
        
        String[] argv = new String[0];
        try{main(argv);}
        catch(Exception e){System.out.println("COULD NOT EXECUTE");}
        
    }

    /**
     * @param args the command line arguments
     */

    public static double dStepBoardHeight;
    public static double dStartToFrontStepBoard;
    public static double dStartToBackStepBoard;
    public static double dStartToBox;
    public static double dCompletion;
    public static double dKneeStraight;
    public static double dZMargin;
    public static double dTrackMargin;
    
    public static DataContainer Data;
    public static MatlabEngine eng;
    
    public static boolean bMovement1Completed=false;
    public static boolean bMovement2Completed=false;
    public static boolean bMovement3Completed=false;
    public static boolean bMovement4Completed=false;
    public static boolean bMovement5Completed=false;
    public static boolean bMovement51Completed=false;
    public static boolean bMovement52Completed=false;    
    public static boolean bMovement6Completed=false;
    public static boolean bMovement7Completed=false;
    public static boolean bMovement8Completed=false;

    public static boolean bMovement1Started=false;
    public static boolean bMovement2Started=false;
    public static boolean bMovement3Started=false;
    public static boolean bMovement4Started=false;
    public static boolean bMovement41Started=false;
    public static boolean bMovement42Started=false;
    public static boolean bMovement5Started=false;
    public static boolean bMovement6Started=false;
    public static boolean bMovement7Started=false;
    public static boolean bMovement8Started=false;
    
    //declare RB enumeration
    public static String sLeftShoulder="11";
    public static String sRightShoulder="12";
    public static String sLeftElbow="8";
    public static String sRightElbow="7";
    public static String sLeftHand="4";
    public static String sRightHand="5";
    public static String sLeftHip="9";
    public static String sRightHip="10";
    public static String sLeftKnee="6";
    public static String sRightKnee="1";
    public static String sLeftFoot = "2";
    public static String sRightFoot = "3";
    public static String sMeasure1 ="13";
    public static String sMeasure2="14";
    
    public static String textfilename = String.valueOf(System.currentTimeMillis());
    public static PrintWriter writer;
    
    
    
    
    /**
     *
     * @throws Exception
     */
    public static void main(String[] args)throws Exception {
        

        

        writer = new PrintWriter(textfilename, "UTF-8");
        
    

        boolean con = true;
        int iListLength=30;
        int iNoMoCapData= 20;
        long t0;
        
        

        
        dKneeStraight = 115.0;

        double dZMargin = 0;
        double dTrackMargin = 0;

        
        String sFolderPath = "C:\\Users\\PMIL\\Desktop\\jn\\MoCapExec";
        String[] sCommands = new String[iNoMoCapData];                
        
        //declare MatLab commands
        String sVelocityNorm = "velocityNorm(Client,";
        String sAngularVelocity = "angularVelocity(Client,";
        String sAccNorm="accNorm(Client,";
        String sAngle = "angle(Client,";
        String sVelocityDir = "velocityDir(Client,";
        
        

        
        //add Matlab commands and parameters to commandlist 
        sCommands[0]=sVelocityNorm+sLeftShoulder+")";
        sCommands[1]=sVelocityNorm+sRightShoulder+")";
        sCommands[2]=sVelocityNorm+sLeftHip+")";
        sCommands[3]=sVelocityNorm+sRightHip+")";
        sCommands[4]=sAngularVelocity+sLeftFoot+","+sLeftHip+","+sLeftKnee+")";
        sCommands[5]=sAngularVelocity+sRightFoot+","+sRightHip+","+sRightKnee+")";
        sCommands[6]=sAngularVelocity+sLeftHand+","+sLeftShoulder+","+sLeftElbow+")";
        sCommands[7]=sAngularVelocity+sRightHand+","+sRightShoulder+","+sRightElbow+")";
        sCommands[8]="("+sVelocityNorm+sRightHip+")+"+sVelocityNorm+sLeftHip+")+"+sVelocityNorm+sLeftShoulder+")+"+sVelocityNorm+sRightShoulder+")" +")/4";
        sCommands[9]="("+sAccNorm+sRightHip+")+"+sAccNorm+sLeftHip+")+"+sAccNorm+sLeftShoulder+")+"+sAccNorm+sRightShoulder+")" +")/4";
        sCommands[10]=sAngle+sLeftHand+","+sLeftShoulder+","+sLeftElbow+")";
        sCommands[11]=sAngle+sRightHand+","+sRightShoulder+","+sRightElbow+")";
        
        
        sCommands[12]=sVelocityDir+sLeftHip+",1)";
        sCommands[13]=sVelocityDir+sLeftHip+",2)";
        sCommands[14]=sVelocityDir+sLeftHip+",3)";
        sCommands[15]=sVelocityDir+sRightHip+",1)";
        sCommands[16]=sVelocityDir+sRightHip+",2)";
        sCommands[17]=sVelocityDir+sRightHip+",3)";

        sCommands[18]=sAngle+sLeftFoot+","+sLeftHip+","+sLeftKnee+")";
        sCommands[19]=sAngle+sRightFoot+","+sRightHip+","+sRightKnee+")";
        

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
        
        //for(int i =1;i<12;i++){
          //  eng.eval("Client.getFrame('rigidbody',"+i+").RigidBody.x");
        //}
                                
        //con = IsConnected(eng, "p");                
        
        Data = new DataContainer();
        Data.commandList=sCommands;
        Data.iListLength = iListLength;
        
        //put configure back and coment out movement bool below, reverse if you dont want to have to configure to run
        configure();  
        //bMovement1Started=true;
        
        Data.initTime = System.currentTimeMillis();
              
        
        try{            
            while(bMovement8Completed==false){

                //check if connected        
                con = IsConnected(eng, null);               
                dCompletion = Completion(eng);

                                

                if(bMovement1Started==true && bMovement1Completed==false){
                    Movement123678(1);
                }
                else if(bMovement1Completed==true && bMovement2Started==false){
                    Gait();
                    checkMovement2Started();                    
                }
                else if(bMovement2Started==true && bMovement2Completed==false){
                    System.out.println("2 STARTED");
                    Movement123678(2);
                    checkMovement3Started();
                }                
                else if(bMovement3Started==true && bMovement3Completed==false){
                    System.out.println("3 STARTED");
                    Movement123678(3);
                }
                else if(bMovement3Completed == true && bMovement4Started==false){
                    Gait();
                    checkMovement4Started();
                }
                else if(bMovement4Started==true && bMovement4Completed==false){
                    System.out.println("4 STARTED");
                    Movement45(4);
                    checkMovement5Started();
                }
                else if(bMovement5Started==true && bMovement5Completed==false){
                    System.out.println("5 STARTED");
                    Movement45(5);
                }
                else if(bMovement5Completed==true && bMovement6Started==false){
                    Gait();
                    checkMovement6Started();
                }
                else if(bMovement6Started==true && bMovement6Completed==false){
                    System.out.println("6 STARTED");
                    Movement123678(6);
                }
                else if(bMovement6Completed == true && bMovement7Started == false){
                    Gait();
                    checkMovement7Started();
                }
                else if(bMovement7Started==true && bMovement7Completed==false){
                    System.out.println("7 STARTED");
                    Movement123678(7);
                    checkMovement8Started();
                }
                else if(bMovement8Started==true && bMovement8Completed==false){
                    System.out.println("8 STARTED");
                    Movement123678(8);
                }

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

        writer.close();
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
                    
                    System.out.println("T "+ String.valueOf(Data.LeftKneeAngl.Time[i]) + " LEFT KNEE ANGLE "+ String.valueOf(Data.LeftKneeAngl.MoCap[i])+ " T "+ String.valueOf(Data.RightKneeAngl.Time[i]) +" RIGHT KNEE ANGLE " + String.valueOf(Data.RightKneeAngl.MoCap[i]));
                }
    }

    public static void PrintElbowAngl(){
        for(int i=0;i<Data.LeftElbowAngl.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftElbowAngl.Time[i]) + " LEFT ELBOW ANGLE "+ String.valueOf(Data.LeftElbowAngl.MoCap[i])+ " T "+ String.valueOf(Data.RightElbowAngl.Time[i]) +" RIGHT ELBOW ANGLE " + String.valueOf(Data.RightElbowAngl.MoCap[i]));
                }
            }

    public static void PrintTorsoVandA(){
         for(int i=0;i<Data.MeanTorsovNorm.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.MeanTorsovNorm.Time[i]) + " TORSO VELOCITY "+ String.valueOf(Data.MeanTorsovNorm.MoCap[i])+ " T "+ String.valueOf(Data.MeanTorsoaNorm.Time[i]) +" TORSO ACCELERATION " + String.valueOf(Data.MeanTorsoaNorm.MoCap[i]));
                }
    }

    public static void PrintXDirHip(){
        for(int i=0;i<Data.xDirLeftHip.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.xDirLeftHip.Time[i]) + "  V_X LEFT HIP "+ String.valueOf(Data.xDirLeftHip.MoCap[i])+ " T "+ String.valueOf(Data.xDirRightHip.Time[i]) +" V_X RIGHT HIP " + String.valueOf(Data.xDirRightHip.MoCap[i]));
                }
    }

    public static void PrintYDirHip(){
        for(int i=0;i<Data.yDirLeftHip.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.yDirLeftHip.Time[i]) + "  V_Y LEFT HIP"+ String.valueOf(Data.yDirLeftHip.MoCap[i])+ " T "+ String.valueOf(Data.yDirRightHip.Time[i]) +" V_Y RIGHT HIP " + String.valueOf(Data.yDirRightHip.MoCap[i]));
                }
    }

    public static void PrintZDirHip(){
        for(int i=0;i<Data.zDirLeftHip.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.zDirLeftHip.Time[i]) + "  V_Z LEFT HIP "+ String.valueOf(Data.zDirLeftHip.MoCap[i])+ " T "+ String.valueOf(Data.zDirRightHip.Time[i]) +" V_Z RIGHT HIP " + String.valueOf(Data.zDirRightHip.MoCap[i]));
                }
    }

    public static void Movement123678(int iMovement)throws Exception{
        
        
        Data.setLeftShouldervNorm(eng);
        Data.setRightShouldervNorm(eng);
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);
        Data.setLeftKneeAngV(eng);    
        Data.setRightKneeAngV(eng);
        Data.setMeanTorsovNorm(eng);    
        Data.setMeanTorsoaNorm(eng);
        Data.setLeftKneeAngl(eng);    
        Data.setRightKneeAngl(eng);              
        Data.setxDirLeftHip(eng);    
        Data.setyDirLeftHip(eng);
        Data.setzDirLeftHip(eng);    
        Data.setxDirRightHip(eng);
        Data.setyDirRightHip(eng);    
        Data.setzDirRightHip(eng);

        PrintShouldervNorm();
        PrintHipvNorm();
        PrintKneeAngV();
        PrintKneeAngl();
        PrintTorsoVandA();
        PrintXDirHip();
        PrintYDirHip();
        PrintZDirHip();

        if(iMovement ==1){
            checkMovement1Completed();
            if(bMovement1Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov1 = currentTime - Data.initTime;
            }
        }
        else if(iMovement ==2){
            checkMovement2Completed();
            if(bMovement2Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov2 = currentTime - Data.initTime;
            }
        }
        else if(iMovement ==3){
            checkMovement3Completed();
            if(bMovement3Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov3 = currentTime - Data.initTime;
            }

        }
        else if(iMovement ==6){
            checkMovement6Completed();
            if(bMovement6Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov6 = currentTime - Data.initTime;
            }
        }
        else if(iMovement ==7){
            checkMovement7Completed();
            if(bMovement7Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov7 = currentTime - Data.initTime;
            }
        }
        else if(iMovement ==8){
            checkMovement8Completed();
            if(bMovement8Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov8 = currentTime - Data.initTime;
            }
        }
        else{System.out.println("Movement123678 used incorrectly.");}        
    }
    public static void Movement45(int iMovement)throws Exception{
        Data.setLeftShouldervNorm(eng);
        Data.setRightShouldervNorm(eng);
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);
        Data.setLeftKneeAngV(eng);    
        Data.setRightKneeAngV(eng);
        Data.setLeftElbowAngV(eng);
        Data.setRightElbowAngV(eng);
        Data.setMeanTorsovNorm(eng);    
        Data.setMeanTorsoaNorm(eng);
        Data.setLeftKneeAngl(eng);    
        Data.setRightKneeAngl(eng); 
        Data.setLeftElbowAngl(eng);
        Data.setRightElbowAngl(eng);             
        Data.setxDirLeftHip(eng);    
        Data.setyDirLeftHip(eng);
        Data.setzDirLeftHip(eng);    
        Data.setxDirRightHip(eng);
        Data.setyDirRightHip(eng);    
        Data.setzDirRightHip(eng);

        PrintShouldervNorm();
        PrintHipvNorm();
        PrintKneeAngV();
        PrintKneeAngl();
        PrintElbowAngV();
        PrintElbowAngl();
        PrintTorsoVandA();
        PrintXDirHip();
        PrintYDirHip();
        PrintZDirHip();

        if(iMovement ==4){
            checkMovement4Completed();
            if(bMovement4Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov4 = currentTime - Data.initTime;
            }
        }
        else if(iMovement ==5){
            checkMovement5Completed();
            if(bMovement5Completed==true){
                double currentTime = (double) System.currentTimeMillis();
                Data.dTimeMov5 = currentTime - Data.initTime;
            }
        }        
        else{System.out.println("Movement45 used incorrectly.");}  


    }

    public static void Gait()throws Exception{
        Data.setLeftHipvNorm(eng);    
        Data.setRightHipvNorm(eng);
        Data.setMeanTorsovNorm(eng);    
        Data.setMeanTorsoaNorm(eng);
        Data.setxDirLeftHip(eng);    
        Data.setyDirLeftHip(eng);
        Data.setzDirLeftHip(eng);    
        Data.setxDirRightHip(eng);
        Data.setyDirRightHip(eng);    
        Data.setzDirRightHip(eng);

        PrintHipvNorm();
        PrintTorsoVandA();
        PrintXDirHip();
        PrintYDirHip();
        PrintZDirHip();
    }




    public static void configure()throws Exception{
        //guide for configuring the necessary distances to be able to keep track of where in the track the subject is 

        //place one marker at the start and one at the end
        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other just in front of the BOX at the end of track.");
        pressAnyKeyToContinue();
        dStartToBox = GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2);
        //writer.println(String.valueOf(dStartToBox));
        System.out.println("Track length configured.");

        //place one marker at the start and one just before the stepboard, on the floor
        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other just IN FRONT OF the STEPBOARD, on the floor.");
        pressAnyKeyToContinue();
        dStartToFrontStepBoard = GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2);
        //writer.println(String.valueOf(dStartToFrontStepBoard));
        System.out.println("Distance to step board configured.");

        //place on marker at the start and one just after the stepboard, on the floor
        System.out.println("Place one marker on the floor in the middle of the two front legs of the chair, and the other just BEHIND the STEPBOARD, on the floor.");
        pressAnyKeyToContinue();
        dStartToBackStepBoard = GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2);
        //writer.println(String.valueOf(dStartToBackStepBoard));
        System.out.println("Distance to end of step board configured.");

        //place one marker on top of the stepboard and one below
        System.out.println("Place one of the markers BELOW the STEPBOARD, and the other ON TOP, directly above.");
        pressAnyKeyToContinue();
        dStepBoardHeight = GetDistanceBetweenTwoMarkers(sMeasure1, sMeasure2);
        //writer.println(String.valueOf(dStepBoardHeight));
        System.out.println("Step board height configured.");
        
        dZMargin = 0.1*dStepBoardHeight;
        dTrackMargin=0.05*dStartToBox;
        
        System.out.println("Margin for stepboard height set to 0.1*height, margin for track set to 0.05*length.");
        
        System.out.println("Get ready to perform experiment. Data will start to be recorded once you continue.");
        pressAnyKeyToContinue();
        bMovement1Started=true;


    }
    
    
    private static void pressAnyKeyToContinue(){ 
        
        //Method for pressing any key to continue 

        System.out.println("Press any key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }

   
    
    public static boolean IsConnected(MatlabEngine eng, String printOption) throws Exception{
        
        /**
         * Method for cheking if the NatNet Client is connected to MoCap software
         * 
         * Arguments:
         * 
         * eng - MatLab workspace
         * printOption - enter "p" to print connection status, else enter null
         * 
         * DOESNT WORK, OBJECT CANNOT BE CONVERTED TO ANYTHING, NEED TO CREATE MATLAB FUNCTION REPLACING THIS METHOD
         * 
        */
        
        boolean bIsConnected;
        eng.eval("c=Client.IsConnected;");
        eng.eval("c=double(c);");
        double con = eng.getVariable("c");        
        
        if(con ==1.0){
            bIsConnected = true;
            if (printOption=="p"){System.out.println("CONNECTED");}
        }
        else{
            bIsConnected = false;
            if (printOption=="p"){System.out.println("NOT CONNECTED");}
        }
        return bIsConnected;
    }

    public static double GetDistanceBetweenTwoMarkers(String sMarker1, String sMarker2)throws Exception{
        eng.eval("currentPos=RBPosition("+sMarker1+", Client)");
        eng.eval("endPos=RBPosition("+sMarker2+", Client)");
        eng.eval("Distance=sqrt(sum( (currentPos-endPos).^2))");
        eng.eval("Distance=double(Distance)");
        double dist = eng.getVariable("Distance");
        return dist;
    }

    public static double GetRBVectorLength(String sMarker)throws Exception{

        eng.eval("Position = RBPosition("+sMarker+", Client)");
        eng.eval("length = norm(Position);");
        eng.eval("length=double(length);");
        double le= eng.getVariable("length");
        return le;
    }

    public static double GetZComponent(String sMarker)throws Exception{

        eng.eval("Position = RBPosition("+sMarker+", Client)");
        eng.eval("z = Position(3);");
        eng.eval("z=double(z);");
        double zComp= eng.getVariable("z");
        return zComp;
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
       
        double dProgress1 = GetRBVectorLength(sRightFoot);
        double dProgress2 = GetRBVectorLength(sLeftFoot);
        double dCompletion = (dProgress1+dProgress2)/(2.0*dStartToBox);

        return dCompletion;


    }

    public static void checkMovement1Completed()throws Exception{


        if(Data.LeftKneeAngl.MoCap[Data.LeftKneeAngl.MoCap.length -1]> dKneeStraight && Data.RightKneeAngl.MoCap[Data.RightKneeAngl.MoCap.length -1]> dKneeStraight){
            bMovement1Completed=true;
        }

    }
    
    public static void checkMovement2Completed()throws Exception{

        if(GetZComponent(sRightFoot)<dStepBoardHeight+dZMargin && GetZComponent(sRightFoot)>dStepBoardHeight-dZMargin && GetZComponent(sLeftFoot)<dStepBoardHeight+dZMargin &&GetZComponent(sLeftFoot)<dStepBoardHeight-dZMargin){
            bMovement2Completed= true;
        }


    }
    
    public static void checkMovement3Completed()throws Exception{
        if(GetZComponent(sRightFoot)<dStepBoardHeight-dZMargin && GetZComponent(sLeftFoot)<dStepBoardHeight-dZMargin){
            bMovement3Completed= true;            
        }


    }
    
    public static void checkMovement4Completed(){
        if(Data.LeftKneeAngl.MoCap[Data.LeftKneeAngl.MoCap.length -1]> dKneeStraight && Data.RightKneeAngl.MoCap[Data.RightKneeAngl.MoCap.length -1]> dKneeStraight){
            bMovement4Completed = true;
        }


    }

    public static void checkMov51(){
        if(Data.LeftKneeAngl.MoCap[Data.LeftKneeAngl.MoCap.length -1]< dKneeStraight && Data.RightKneeAngl.MoCap[Data.RightKneeAngl.MoCap.length -1]< dKneeStraight){
            bMovement51Completed = true;
        }
    }

    public static void checkMov52(){
        if(Data.LeftKneeAngl.MoCap[Data.LeftKneeAngl.MoCap.length -1]> dKneeStraight && Data.RightKneeAngl.MoCap[Data.RightKneeAngl.MoCap.length -1]> dKneeStraight){
            bMovement52Completed = true;
        }
    }
    

    
    public static void checkMovement5Completed(){
        if(bMovement51Completed==true && bMovement52Completed==true){bMovement5Completed=true;}        }
    
    
    public static void checkMovement6Completed()throws Exception{
        if(GetZComponent(sRightFoot)<dStepBoardHeight+dZMargin && GetZComponent(sRightFoot)>dStepBoardHeight-dZMargin && GetZComponent(sLeftFoot)<dStepBoardHeight+dZMargin &&GetZComponent(sLeftFoot)<dStepBoardHeight-dZMargin){
            bMovement6Completed= true;}
    }
    
    public static void checkMovement7Completed()throws Exception{
        if(GetZComponent(sRightFoot)<dStepBoardHeight-dZMargin && GetZComponent(sLeftFoot)<dStepBoardHeight-dZMargin){
            bMovement6Completed= true;            
        }

    }
    public static void checkMovement8Completed(){
        if(Data.LeftKneeAngl.MoCap[Data.LeftKneeAngl.MoCap.length -1]< dKneeStraight && Data.RightKneeAngl.MoCap[Data.RightKneeAngl.MoCap.length -1]< dKneeStraight){
            bMovement8Completed = true;
        }


    }

    //bools for started movements
    
    public static void checkMovement2Started(){
        if(dCompletion< (dStartToFrontStepBoard/dStartToBox)+ dTrackMargin && dCompletion> (dStartToFrontStepBoard/dStartToBox)- dTrackMargin ){
            bMovement2Started=true;
        }


    }
    
    public static void checkMovement3Started(){
        if(bMovement2Completed==true){bMovement3Started=true;}
    }
    
    public static void checkMovement4Started(){
        if(dCompletion<1.0+dTrackMargin && dCompletion>1.0- dTrackMargin){
            bMovement4Started=true;
        }


    }
    public static void checkMovement41Started(){
        if(Data.LeftKneeAngl.MoCap[Data.LeftKneeAngl.MoCap.length -1]< dKneeStraight && Data.RightKneeAngl.MoCap[Data.RightKneeAngl.MoCap.length -1]< dKneeStraight){
            bMovement41Started = true;
        }

    }
    public static void checkMovement42Started(){
        if(Data.LeftKneeAngl.MoCap[Data.LeftKneeAngl.MoCap.length -1]> dKneeStraight && Data.RightKneeAngl.MoCap[Data.RightKneeAngl.MoCap.length -1]> dKneeStraight){
            bMovement42Started = true;
        }


    }

    public static void checkMovement5Started(){
        if(bMovement41Started==true && bMovement42Started==true){bMovement5Started=true;}


    }
    
    public static void checkMovement6Started(){
        if((dStartToBox-(dStartToBackStepBoard-dStartToFrontStepBoard)/dStartToBox +dTrackMargin >dCompletion && dCompletion> (dStartToBox-(dStartToBackStepBoard-dStartToFrontStepBoard))/dStartToBox - dTrackMargin)){
            bMovement6Started=true;

        }
    }
    
    public static void checkMovement7Started(){
        if(bMovement6Completed==true){bMovement7Started=true;}

    }
    public static void checkMovement8Started(){
        if(dCompletion<dTrackMargin){bMovement8Completed=true;}

    }


    }


    

