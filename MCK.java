//MAIN

package src.mck;

import com.mathworks.engine.*;

import java.lang.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import src.shared.Information;
//import shared.Information;

/**
 *
 * @author Joar Nykvist
 */
public class MCK implements Runnable {//To be able to start this in a thread.

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

    //class containing mocap data
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

    private Information info;

    public MCK(Information info){
    	this.info = info;
    }


    /**
     *
     * @throws Exception
     */
    //public static void main(String[] args)throws Exception {
      @Override 
      public void run() {

        try{
            
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
            
            
            for(int i =0;i<sCommands.length;i++){
                System.out.println(sCommands[i]);
            }
            
            //initiate matlab workspace
            try{
                eng = MatlabEngine.startMatlab();
            } catch(EngineException e){
            } catch (InterruptedException | IllegalArgumentException | IllegalStateException ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                // add directory to matlab search path
                eng.eval("addpath('"+sFolderPath+"')");
            } catch (InterruptedException ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MatlabSyntaxException ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CancellationException ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EngineException ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            try {
                //configure distances
                configure();
            } catch (Exception ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            info.setDoneConfigurating(true);
            
            //set time origin
            Data.initTime = System.currentTimeMillis();
            
            while (!info.isCloseProgram()) { //Use same configuration for multiple walks on the track.
                System.out.println("Press 0 to close the program or 1 to take a walk.");
                startTrackingPerson();
            }
            
            // disconnect natnet
            eng.eval("Client.disconnect");
            
            // remove directory from matalab search path
            eng.eval("rmpath('"+sFolderPath+"')");
            
            try {
                // close connection
                eng.disconnect();
            } catch (EngineException ex) {
                Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch(InterruptedException ex){
            Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MatlabSyntaxException ex) {
            Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CancellationException ex) {
            Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EngineException ex) {
            Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MCK.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs tracking of the test subjects movements.
     */
    public void startTrackingPerson(){
        while (!info.isStart()&& !info.isCloseProgram()){ // While the user has not pressed to start, wait.
            try{
                TimeUnit.MILLISECONDS.sleep(500);
            }
            catch (InterruptedException e){
                e.printStackTrace();
                System.exit(1);
            }
        }

        try{
            while((bEnteredBoxArea==false || dCompletion>dBackHome || !info.isBackSitting())&& !info.isCloseProgram()){
                
                //while the subject is not in the starting position and has not yet reached the box area

                //check how much of the track has been completed
                dCompletion = Completion(eng);
                info.setCompletion(dCompletion); // Set the completion in the class provided by Main 

                //get how much time has passed and convert to seconds *10^-1
                lElapsedTime = (System.currentTimeMillis() - Data.initTime)/100;
                dElapsedTime=(double) lElapsedTime;

                System.out.println("completion "+String.valueOf(100*dCompletion)+"  %");
                System.out.println("time "+String.valueOf(dElapsedTime)+"  *10^-1 s");

                //if the subject is in the chair zone, set and print the relevant data
                if(checkChairArea()){
                    info.setActiveArea(0);
                    Movement123678();
                    System.out.println("CHAIR");
                }
                //subject in stepboard zone
                else if(checkStepboardArea()){
                    info.setActiveArea(1); 
                    Movement123678();
                    System.out.println("STEPBOARD");
                }
                //subject in box zone
                else if(checkBoxArea()){
                    info.setActiveArea(2); 
                    Movement45();
                    bEnteredBoxArea=true;
                    info.setEnteredBoxArea(bEnteredBoxArea);
                    System.out.println("BOX");
                }
                //if the subject is in between zones, set and print the relevant data for gait
                else{
                    info.setActiveArea(3);
                    Gait();
                    System.out.println("GAIT");
                }

            }
        }catch(java.lang.ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.println("INDEX OUT OF RANGE" );

        }catch(java.lang.Exception e){
            System.out.println("EXEPTION in startTrackingPerson" );
            e.printStackTrace();
        }
    
        bEnteredBoxArea=false;
        info.setEnteredBoxArea(false);
        //info.setStart(false);
    }

    //All print methods work analogously, loop through a specific array and print the mocap data along with the time

    public void PrintShouldervNorm(){
        for(int i=0;i<Data.LeftShouldervNorm.MoCap.length;i++){
            System.out.println("T "+ String.valueOf(Data.LeftShouldervNorm.Time[i]) + " LEFT SHOULDER VELOCITY NORM "+ String.valueOf(Data.LeftShouldervNorm.MoCap[i])+ " T "+ String.valueOf(Data.RightShouldervNorm.Time[i]) +" RIGHT SHOULDER VELOCITY NORM " + String.valueOf(Data.RightShouldervNorm.MoCap[i]));
        }
    }

    public void PrintHipvNorm(){
        for(int i=0;i<Data.LeftHipvNorm.MoCap.length;i++){
            System.out.println("T "+ String.valueOf(Data.LeftHipvNorm.Time[i]) + " LEFT HIP VELOCITY NORM"+ String.valueOf(Data.LeftHipvNorm.MoCap[i])+ " T "+ String.valueOf(Data.RightHipvNorm.Time[i]) +" RIGHT HIP VELOCITY NORM " + String.valueOf(Data.RightHipvNorm.MoCap[i]));
        }
    }

    public void PrintKneeAngV(){
        for(int i=0;i<Data.LeftKneeAngV.MoCap.length;i++){
            System.out.println("T "+ String.valueOf(Data.LeftKneeAngV.Time[i]) + " LEFT KNEE ANGULAR VELOCITY"+ String.valueOf(Data.LeftKneeAngV.MoCap[i])+ " T "+ String.valueOf(Data.RightKneeAngV.Time[i]) +" RIGHT KNEE ANGULAR VELOCITY " + String.valueOf(Data.RightKneeAngV.MoCap[i]));
        }
    }

    public void PrintElbowAngV(){
        for(int i=0;i<Data.LeftElbowAngV.MoCap.length;i++){
            System.out.println("T "+ String.valueOf(Data.LeftElbowAngV.Time[i]) + " LEFT ELBOW ANGULAR VELOCITY "+ String.valueOf(Data.LeftElbowAngV.MoCap[i])+ " T "+ String.valueOf(Data.RightElbowAngV.Time[i]) +" RIGHT ELBOW ANGULAR VELOCITY " + String.valueOf(Data.RightElbowAngV.MoCap[i]));
        }
    }

    public void PrintKneeAngl(){
        for(int i=0;i<Data.LeftKneeAngl.MoCap.length;i++){
            System.out.println("T "+ String.valueOf(Data.LeftKneeAngl.Time[i]) + " LEFT KNEE ANGLE "+ String.valueOf(Data.LeftKneeAngl.MoCap[i])+ " T "+ String.valueOf(Data.RightKneeAngl.Time[i]) +" RIGHT KNEE ANGLE " + String.valueOf(Data.RightKneeAngl.MoCap[i]));
        }
    }

    public void PrintElbowAngl(){
        for(int i=0;i<Data.LeftElbowAngl.MoCap.length;i++){
            System.out.println("T "+ String.valueOf(Data.LeftElbowAngl.Time[i]) + " LEFT ELBOW ANGLE "+ String.valueOf(Data.LeftElbowAngl.MoCap[i])+ " T "+ String.valueOf(Data.RightElbowAngl.Time[i]) +" RIGHT ELBOW ANGLE " + String.valueOf(Data.RightElbowAngl.MoCap[i]));
        }
    }


    public void Movement123678()throws Exception{

        //sets the data deemed relevant for movements in chair zone and stepboard zone
        Data.setLeftShouldervNorm(eng);
        Data.setRightShouldervNorm(eng);
        Data.setLeftHipvNorm(eng);
        Data.setRightHipvNorm(eng);
        Data.setLeftKneeAngV(eng);
        Data.setRightKneeAngV(eng);
        Data.setLeftKneeAngl(eng);
        Data.setRightKneeAngl(eng);

        //Set the relevant lists in Information object
        
        info.setLeftShouldervNorm(Data.getLeftShouldervNorm().getMoCap());
        info.setRightShouldervNorm(Data.getRightShouldervNorm().getMoCap());
        info.setLeftHipvNorm(Data.getLeftHipvNorm().getMoCap());
        info.setRightHipvNorm(Data.getRightHipvNorm().getMoCap());
        info.setLeftKneeAngV(Data.getLeftKneeAngV().getMoCap());
        info.setRightKneeAngV(Data.getRightKneeAngV().getMoCap());
        info.setLeftKneeAngl(Data.getLeftKneeAngl().getMoCap());
        info.setRightKneeAngl(Data.getRightKneeAngl().getMoCap());
        
        info.setLeftKneeAngVTime(Data.getLeftKneeAngV().getTime());
        info.setRightKneeAngVTime(Data.getRightKneeAngV().getTime());
        info.setLeftKneeAnglTime(Data.getLeftKneeAngl().getTime());
        info.setRightKneeAnglTime(Data.getRightKneeAngl().getTime());
         

        //increase counter by 1
        Data.counter++;

        //print the recorded data
       // PrintShouldervNorm();
       // PrintHipvNorm();
        PrintKneeAngV();
        PrintKneeAngl();

    }
    public void Movement45()throws Exception{

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

        //Set the relevant lists in Information object
        
        info.setLeftShouldervNorm(Data.getLeftShouldervNorm().getMoCap());
        info.setRightShouldervNorm(Data.getRightShouldervNorm().getMoCap());
        info.setLeftHipvNorm(Data.getLeftHipvNorm().getMoCap());
        info.setRightHipvNorm(Data.getRightHipvNorm().getMoCap());
        info.setLeftKneeAngV(Data.getLeftKneeAngV().getMoCap());
        info.setRightKneeAngV(Data.getRightKneeAngV().getMoCap());
        info.setLeftElbowAngV(Data.getLeftElbowAngV().getMoCap());
        info.setRightElbowAngV(Data.getRightElbowAngV().getMoCap());
        info.setLeftKneeAngl(Data.getLeftKneeAngl().getMoCap());
        info.setRightKneeAngl(Data.getRightKneeAngl().getMoCap());
        info.setLeftElbowAngl(Data.getLeftElbowAngl().getMoCap());
        info.setRightElbowAngl(Data.getRightElbowAngl().getMoCap());
         
        info.setLeftKneeAngVTime(Data.getLeftKneeAngV().getTime());
        info.setRightKneeAngVTime(Data.getRightKneeAngV().getTime());
        info.setLeftElbowAngVTime(Data.getLeftElbowAngV().getTime());
        info.setRightElbowAngVTime(Data.getRightElbowAngV().getTime());
        info.setLeftKneeAnglTime(Data.getLeftKneeAngl().getTime());
        info.setRightKneeAnglTime(Data.getRightKneeAngl().getTime());
        info.setLeftElbowAnglTime(Data.getLeftElbowAngl().getTime());
        info.setRightElbowAnglTime(Data.getRightElbowAngl().getTime());
        

        //increase counter by 1
        Data.counter++;

        //print the recorded data
       // PrintShouldervNorm();
       // PrintHipvNorm();
        PrintKneeAngV();
        PrintKneeAngl();
        PrintElbowAngV();
        PrintElbowAngl();




    }

    public void Gait()throws Exception{

        //set the data deemed relevant for gait
        Data.setLeftHipvNorm(eng);
        Data.setRightHipvNorm(eng);

        //Set the relevant lists in Information object
        
       /* info.setLeftShouldervNorm(Data.getLeftShouldervNorm().getMoCap());
        info.setRightShouldervNorm(Data.getRightShouldervNorm().getMoCap());
        info.setLeftHipvNorm(Data.getLeftHipvNorm().getMoCap());
        info.setRightHipvNorm(Data.getRightHipvNorm().getMoCap());
         */ //These values are not used by my code

        //increase counter by 1
        Data.counter++;

        //print the recorded data
       // PrintHipvNorm();

    }

    //returns true if the subject is in what is recorded as the chair zone
    public boolean checkChairArea(){
        if(dCompletion<d2CHAIRAREA){
            return true;
        }
        else{
            return false;
        }

    }
    //returns true if the subject is in what is recorded as the stepboard zone
    public boolean checkStepboardArea(){
        if(dCompletion<d2STEPBOARDAREA && dCompletion>d1STEPBOARDAREA){
            return true;
        }
        else{
            return false;}

    }
    //returns true if the subject is in what is recorded as the box zone
    public boolean checkBoxArea(){
        if(dCompletion>d1BOXAREA){
            return true;
        }
        else{
            return false;
        }
    }

    public void configure()throws Exception{
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
        System.out.println("Place the measuring markers as close to the knee marker starting positions as possible. Press enter to finnish configuration.");
        pressEnterToContinue();


    }


    private void pressEnterToContinue(){

        //Method for pressing any key to continue, for me it only works with enter though. 

        System.out.println("Press ENTER to continue...");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
    }

    public double GetDistanceBetweenTwoMarkers(String sMarker1, String sMarker2)throws Exception{

        //returns the norm of the vector difference between two RB vectors
        //parameters are the numbers of the two RBs as they are enumerated in Motive

        eng.eval("currentPos=RBPosition("+sMarker1+", Client);");
        eng.eval("endPos=RBPosition("+sMarker2+", Client);");
        eng.eval("Distance=sqrt(sum( (currentPos-endPos).^2));");
        eng.eval("Distance=double(Distance);");
       // double dist = eng.getVariable("Distance");
       // return dist;
        return eng.getVariable("Distance");
    }

    public double distFromStart(String sMarker) throws Exception{

        //gets the distance from a specific marker to one of the measuring markers 

        eng.eval("Pos1 = RBPosition("+ sMeasure1+", Client);");
        eng.eval("Pos2 = RBPosition("+ sMarker+", Client);");
        eng.eval("dist=norm(Pos1-Pos2);");
        eng.eval("dist=double(dist);");
        //double dist = eng.getVariable("dist");
       // return dist;
        return eng.getVariable("dist");
    }



    public double Completion(MatlabEngine eng)throws Exception{
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

        //double dCompletion = (dProgress1+dProgress2)/(2.0*dStartToBox);
        //return dCompletion;
        return (dProgress1+dProgress2)/(2.0*dStartToBox);

    }


}


    

