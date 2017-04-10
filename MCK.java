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


/**
 *
 * @author PMIL
 */
public class MCK {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws Exception {
        
        
        boolean con = true;
        int counter=0;
        int iListLength=30;
        int iNoMoCapData= 18;
        long t0;
        
        
        String sFolderPath = "C:\\Users\\PMIL\\Desktop\\jn\\MoCapExec";
        String[] sCommands = new String[iNoMoCapData];                
        
        //declare MatLab commands
        String sVelocityNorm = "velocityNorm(Client,";
        String sAngularVelocity = "angularVelocity(Client,";
        String sAccNorm="accNorm(Client,";
        String sAngle = "angle(Client,";
        String sVelocityDir = "velocityDir(Client,";
        
        //declare RB enumeration
        String sLeftShoulder="11";
        String sRightShoulder="12";
        String sLeftElbow="8";
        String sRightElbow="7";
        String sLeftHand="4";
        String sRightHand="5";
        String sLeftHip="9";
        String sRightHip="10";
        String sLeftKnee="6";
        String sRightKnee="1";
        String sLeftFoot = "2";
        String sRightFoot = "3";
        
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
        
        /*
        sCommands[12]=sVelocityDir+sLeftHip+",'x')";
        sCommands[13]=sVelocityDir+sLeftHip+",'y')";
        sCommands[14]=sVelocityDir+sLeftHip+",'z')";
        sCommands[15]=sVelocityDir+sRightHip+",'x')";
        sCommands[16]=sVelocityDir+sRightHip+",'y')";
        sCommands[17]=sVelocityDir+sRightHip+",'z')";
        */
        for(int i =0;i<sCommands.length;i++){System.out.println(sCommands[i]);}
        
         
        MatlabEngine eng = MatlabEngine.startMatlab();

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
        
        DataContainer Data = new DataContainer();
        Data.counter=counter;
        Data.commandList=sCommands;
        Data.initTime = System.currentTimeMillis();
        
        
        try{
            while(con==true){

                // check if connected        
                //con = IsConnected(eng, null);

    //            if(eng==null){System.out.println("eng null");}
    //            else{System.out.println("eng OK");}
    //            if(stopWatch==null){System.out.println("stopwatch null");}
    //            else{System.out.println("stopwatch OK");}

                Data.setLeftShouldervNorm(eng);  
                //System.out.println("HERE"+String.valueOf(Data.LeftShouldervNorm.MoCap[0]));
                //System.out.println("counter"+String.valueOf(Data.counter));
                //System.out.println("length"+String.valueOf(Data.LeftShouldervNorm.MoCap.length));
                Data.setRightShouldervNorm(eng);
//    //            System.out.println("HERE"+String.valueOf(Data.LeftShouldervNorm.MoCap[0]));
                Data.setLeftHipvNorm(eng);    
                Data.setRightHipvNorm(eng);
                Data.setLeftKneeAngV(eng);    
                Data.setRightKneeAngV(eng);
                Data.setLeftElbowAngV(eng);    
                Data.setRightElbowAngV(eng);
                Data.setMeanTorsovNorm(eng);    
                Data.setMeanTorsoaNorm(eng);
                Data.setLeftElbowAngl(eng);    
                Data.setRightElbowAngl(eng);
//                
//                System.out.println("MCK TIME"+String.valueOf(stopWatch.getTime()));
                
                /*
                Data.setxDirLeftHip(eng);    
                Data.setyDirLeftHip(eng);
                Data.setzDirLeftHip(eng);    
                Data.setxDirRightHip(eng);
                Data.setyDirRightHip(eng);    
                Data.setzDirRightHip(eng);
                */

    //            MoCapData LeftShouldervNorm = Data.getLeftShouldervNorm();
    //            double[] MCP = LeftShouldervNorm.getMoCap();
    //            
    //            System.out.println("LATEST"+String.valueOf(MCP[0]));
    //            System.out.println("");
                  
                  //ShouldervNorm
                  for(int i=0;i<Data.LeftShouldervNorm.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftShouldervNorm.Time[i]) + " LEFTSHOULDERVNORM            "+ String.valueOf(Data.LeftShouldervNorm.MoCap[i])+ " T "+ String.valueOf(Data.RightShouldervNorm.Time[i]) +" RIGHTSHOULDERVNORM            " + String.valueOf(Data.RightShouldervNorm.MoCap[i]));
                }
//                 
//                  //HipvNorm
                  for(int i=0;i<Data.LeftHipvNorm.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftHipvNorm.Time[i]) + " LEFTHIPVNORM            "+ String.valueOf(Data.LeftHipvNorm.MoCap[i])+ " T "+ String.valueOf(Data.RightHipvNorm.Time[i]) +" RIGHTHIPVNORM            " + String.valueOf(Data.RightHipvNorm.MoCap[i]));
                }
                  //KneeAngV
                  for(int i=0;i<Data.LeftKneeAngV.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftKneeAngV.Time[i]) + " LEFTKNEEANGV            "+ String.valueOf(Data.LeftKneeAngV.MoCap[i])+ " T "+ String.valueOf(Data.RightKneeAngV.Time[i]) +" RIGHTKNEEANGV            " + String.valueOf(Data.RightKneeAngV.MoCap[i]));
                }
//                  
//                  //ElbowAngV
                  for(int i=0;i<Data.LeftElbowAngV.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftElbowAngV.Time[i]) + " LEFTELBOWANGV            "+ String.valueOf(Data.LeftElbowAngV.MoCap[i])+ " T "+ String.valueOf(Data.RightElbowAngV.Time[i]) +" RIGHTELBOWANGV            " + String.valueOf(Data.RightElbowAngV.MoCap[i]));
                }
//                  
//                  //ElbowAngl
                  for(int i=0;i<Data.LeftElbowAngl.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.LeftElbowAngl.Time[i]) + " LEFTELBOWANGL            "+ String.valueOf(Data.LeftElbowAngl.MoCap[i])+ " T "+ String.valueOf(Data.RightElbowAngl.Time[i]) +" RIGHTELBOWANGL            " + String.valueOf(Data.RightElbowAngl.MoCap[i]));
                }
//                  //MeanTorsoVNorm
                  for(int i=0;i<Data.MeanTorsovNorm.MoCap.length;i++){
                    
                    System.out.println("T "+ String.valueOf(Data.MeanTorsovNorm.Time[i]) + " TORSO V            "+ String.valueOf(Data.MeanTorsovNorm.MoCap[i])+ " T "+ String.valueOf(Data.MeanTorsoaNorm.Time[i]) +" TORSO A            " + String.valueOf(Data.MeanTorsoaNorm.MoCap[i]));
                }
                  

                /*
                System.out.print(String.valueOf(Data.xDirLeftHip.MoCap[0]));
                System.out.print(String.valueOf(Data.yDirLeftHip.MoCap[0]));
                System.out.print(String.valueOf(Data.zDirLeftHip.MoCap[0]));
                System.out.print(String.valueOf(Data.xDirRightHip.MoCap[0]));
                System.out.print(String.valueOf(Data.yDirRightHip.MoCap[0]));
                System.out.print(String.valueOf(Data.zDirRightHip.MoCap[0]));
                */

                Data.counter++;
                if(Data.counter==30){System.out.println("ALL LISTS ARE FILLED - OK TO START");}





            }
        }catch(java.lang.ArrayIndexOutOfBoundsException e){
            System.out.println("INDEX OUT OF RANGE" );
            for(int i=0;i<Data.LeftShouldervNorm.MoCap.length;i++){
                    
                    System.out.println(String.valueOf(Data.LeftShouldervNorm.MoCap[i]));
                }
        
        }
        
        
        // disconnect natnet 
        eng.eval("Client.disconnect");
        
        // remove directory from matalab search path
        eng.eval("rmpath('"+sFolderPath+"')");
        
        // close connection
        eng.disconnect();
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
        Object connection = eng.getVariable("Client.IsConnected");        
        String scon = (String) connection;
        double con = Double.parseDouble(scon);
        
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
        // TODO code application logic here
    }
    

