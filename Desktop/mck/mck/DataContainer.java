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

/**
 *
 * @author Joar Nykvist
 */
public class DataContainer {
    
    public double[] blankDList = new double[1];
    
    public MoCapData LeftShouldervNorm = new MoCapData(blankDList,blankDList);
    public MoCapData RightShouldervNorm= new MoCapData(blankDList,blankDList);
    public MoCapData LeftHipvNorm= new MoCapData(blankDList,blankDList);
    public MoCapData RightHipvNorm= new MoCapData(blankDList,blankDList);
    public MoCapData LeftKneeAngV= new MoCapData(blankDList,blankDList);
    public MoCapData RightKneeAngV= new MoCapData(blankDList,blankDList);        
    public MoCapData LeftElbowAngV= new MoCapData(blankDList,blankDList);
    public MoCapData RightElbowAngV= new MoCapData(blankDList,blankDList);
    public MoCapData MeanTorsovNorm= new MoCapData(blankDList,blankDList);
    public MoCapData MeanTorsoaNorm= new MoCapData(blankDList,blankDList);
    public MoCapData LeftElbowAngl= new MoCapData(blankDList,blankDList);
    public MoCapData RightElbowAngl= new MoCapData(blankDList,blankDList);
    public MoCapData xDirLeftHip= new MoCapData(blankDList,blankDList);
    public MoCapData yDirLeftHip= new MoCapData(blankDList,blankDList);
    public MoCapData zDirLeftHip= new MoCapData(blankDList,blankDList);
    public MoCapData xDirRightHip= new MoCapData(blankDList,blankDList);
    public MoCapData yDirRightHip= new MoCapData(blankDList,blankDList);
    public MoCapData zDirRightHip= new MoCapData(blankDList,blankDList);
    
    public double dTimeMov1;
    public double dTimeMov2;
    public double dTimeMov3;
    public double dTimeMov4;
    public double dTimeMov5;
    public double dTimeMov6;
    public double dTimeMov7;
    public double dTimeMov8;

    public long initTime;
    
    public int iListLength;

    public String[] commandList;
    
    public MoCapData getLeftShouldervNorm(){return LeftShouldervNorm;}    
    public MoCapData getRightShouldervNorm(){return RightShouldervNorm;}
    public MoCapData getLeftHipvNorm(){return LeftHipvNorm;}    
    public MoCapData getRightHipvNorm(){return RightHipvNorm;}
    public MoCapData getLeftKneeAngV(){return LeftKneeAngV;}    
    public MoCapData getRightKneeAngV(){return RightKneeAngV;}
    public MoCapData getLeftElbowAngV(){return LeftElbowAngV;}    
    public MoCapData getRightElbowAngV(){return RightElbowAngV;}
    public MoCapData getMeanTorsovNorm(){return MeanTorsovNorm;}    
    public MoCapData getMeanTorsoaNorm(){return MeanTorsoaNorm;}
    public MoCapData getLeftElbowAngl(){return LeftElbowAngl;}    
    public MoCapData getRightElbowAngl(){return RightElbowAngl;}
    public MoCapData getLeftKneeAngl(){return LeftKneeAngl;}
    public MoCapData getRightKneeAngl(){return RightKneeAngl;}
    public MoCapData getxDirLeftHip(){return xDirLeftHip;}    
    public MoCapData getyDirLeftHip(){return yDirLeftHip;}
    public MoCapData getzDirLeftHip(){return zDirLeftHip;}    
    public MoCapData getxDirRightHip(){return xDirRightHip;}
    public MoCapData getyDirRightHip(){return yDirRightHip;}    
    public MoCapData getzDirRightHip(){return zDirRightHip;}
    public String[] getcommandList(){return commandList;}
    public int getiListLength(){return iListLength;}
    public double getdTimeMov1(){return getTimeMov1;}
    public double getdTimeMov2(){return getTimeMov2;}
    public double getdTimeMov3(){return getTimeMov3}
    public double getdTimeMov4(){return getTimeMov4;}
    public double getdTimeMov5(){return getTimeMov5;}
    public double getdTimeMov6(){return getTimeMov6;}
    public double getdTimeMov7(){return getTimeMov7;}

    
    public void setLeftShouldervNorm(MatlabEngine proxy)throws Exception{
        /*
        if(LeftShouldervNorm==null){System.out.println("LeftShouldervNorm null");}
        else{System.out.println("LeftShouldervNorm OK");} 
        if(LeftShouldervNorm.MoCap==null){System.out.println("LeftShouldervNorm.MoCap null");}
        else{System.out.println("LeftShouldervNorm.MoCap OK");}
        if(commandList==null){System.out.println("commandlist null");}
        else{System.out.println("commandlist OK");}
        
        System.out.println("counter"+String.valueOf(counter));
        System.out.println("length"+String.valueOf(LeftShouldervNorm.MoCap.length));
        */
        if(counter<LeftShouldervNorm.MoCap.length){
                       
            LeftShouldervNorm.enterMoCapData(commandList[0], proxy, initTime, false, counter);}
        else{
//            if(proxy==null){System.out.println("proxy null");}
//            else{System.out.println("proxy OK");}
//            if(stp==null){System.out.println("stopwatch null");}
//            else{System.out.println("stopwatch OK");}
            LeftShouldervNorm.enterMoCapData(commandList[0], proxy, initTime, true, LeftShouldervNorm.MoCap.length-1);}
        
        
    }
    public void setRightShouldervNorm(MatlabEngine proxy)throws Exception{
        if(RightShouldervNorm.MoCap.length==iListLength){RightShouldervNorm.enterMoCapData(commandList[1], proxy, initTime, false, counter);}
        else{RightShouldervNorm.enterMoCapData(commandList[1], proxy, initTime, true, RightShouldervNorm.MoCap.length-1);}
        
    }
    public void setLeftHipvNorm(MatlabEngine proxy)throws Exception{
        if(LeftHipvNorm.MoCap.length==iListLength){LeftHipvNorm.enterMoCapData(commandList[2], proxy, initTime, false, counter);}
        else{LeftHipvNorm.enterMoCapData(commandList[2], proxy, initTime, true, LeftHipvNorm.MoCap.length-1);}
        
    }
    public void setRightHipvNorm(MatlabEngine proxy)throws Exception{
        if(RightHipvNorm.MoCap.length==iListLength){RightHipvNorm.enterMoCapData(commandList[3], proxy, initTime, false, counter);}
        else{RightHipvNorm.enterMoCapData(commandList[3], proxy, initTime, true, RightHipvNorm.MoCap.length-1);}
        
    }
    public void setLeftKneeAngV(MatlabEngine proxy)throws Exception{
        if(LeftKneeAngV.MoCap.length==iListLength){LeftKneeAngV.enterMoCapData(commandList[4], proxy, initTime, false, counter);}
        else{LeftKneeAngV.enterMoCapData(commandList[4], proxy, initTime, true, LeftKneeAngV.MoCap.length-1);}
    }
    public void setRightKneeAngV(MatlabEngine proxy)throws Exception{
        if(RightKneeAngV.MoCap.length==iListLength){RightKneeAngV.enterMoCapData(commandList[5], proxy, initTime, false, counter);}
        else{RightKneeAngV.enterMoCapData(commandList[5], proxy, initTime, true, RightKneeAngV.MoCap.length-1);}
    }
    public void setLeftElbowAngV(MatlabEngine proxy)throws Exception{
        if(LeftElbowAngV.MoCap.length==iListLength){LeftElbowAngV.enterMoCapData(commandList[6], proxy, initTime, false, counter);}
        else{LeftElbowAngV.enterMoCapData(commandList[6], proxy, initTime, true, LeftElbowAngV.MoCap.length-1);}
    }
    public void setRightElbowAngV(MatlabEngine proxy)throws Exception{
        if(RightElbowAngV.MoCap.length==iListLength){RightElbowAngV.enterMoCapData(commandList[7], proxy, initTime, false, counter);}
        else{RightElbowAngV.enterMoCapData(commandList[7], proxy, initTime, true, RightElbowAngV.MoCap.length-1);}
    }
    public void setMeanTorsovNorm(MatlabEngine proxy)throws Exception{
        if(MeanTorsovNorm.MoCap.length==iListLength){MeanTorsovNorm.enterMoCapData(commandList[8], proxy, initTime, false, counter);}
        else{MeanTorsovNorm.enterMoCapData(commandList[8], proxy, initTime, true, MeanTorsovNorm.MoCap.length-1);}
    }
    public void setMeanTorsoaNorm(MatlabEngine proxy)throws Exception{
        if(MeanTorsoaNorm.MoCap.length==iListLength){MeanTorsoaNorm.enterMoCapData(commandList[9], proxy, initTime, false, counter);}
        else{MeanTorsoaNorm.enterMoCapData(commandList[9], proxy, initTime, true, MeanTorsoaNorm.MoCap.length-1);}
    }
    public void setLeftElbowAngl(MatlabEngine proxy)throws Exception{
        if(LeftElbowAngl.MoCap.length==iListLength){LeftElbowAngl.enterMoCapData(commandList[10], proxy, initTime, false, counter);}
        else{LeftElbowAngl.enterMoCapData(commandList[10], proxy, initTime, true, LeftElbowAngl.MoCap.length-1);}
    }
    public void setRightElbowAngl(MatlabEngine proxy)throws Exception{
        if(RightElbowAngl.MoCap.length==iListLength){RightElbowAngl.enterMoCapData(commandList[11], proxy, initTime, false, counter);}
        else{RightElbowAngl.enterMoCapData(commandList[11], proxy, initTime, true, RightShouldervNorm.MoCap.length-1);}
    }
    public void setLeftKneeAngl(MatlabEngine proxy)throws Exception{
        if(LeftKneeAngl.MoCap.length==iListLength){LeftKneeAngl.enterMoCapData(commandList[10], proxy, initTime, false, counter);}
        else{LeftKneeAngl.enterMoCapData(commandList[18], proxy, initTime, true, LeftKneeAngl.MoCap.length-1);}
    }
    public void setRightKneeAngl(MatlabEngine proxy)throws Exception{
        if(RightKneeAngl.MoCap.length==iListLength){RightKneeAngl.enterMoCapData(commandList[11], proxy, initTime, false, counter);}
        else{RightKneeAngl.enterMoCapData(commandList[19], proxy, initTime, true, LeftKneeAngl.MoCap.length-1);}
    }
    public void setxDirLeftHip(MatlabEngine proxy)throws Exception{
        if(xDirLeftHip.MoCap.length==iListLength){xDirLeftHip.enterMoCapData(commandList[12], proxy, initTime, false, counter);}
        else{xDirLeftHip.enterMoCapData(commandList[12], proxy, initTime, true, xDirLeftHip.MoCap.length-1);}
    }
    public void setyDirLeftHip(MatlabEngine proxy)throws Exception{
        if(yDirLeftHip.MoCap.length==iListLength){yDirLeftHip.enterMoCapData(commandList[13], proxy, initTime, false, counter);}
        else{yDirLeftHip.enterMoCapData(commandList[13], proxy, initTime, true, yDirLeftHip.MoCap.length-1);}
    }
    public void setzDirLeftHip(MatlabEngine proxy)throws Exception{
        if(zDirLeftHip.MoCap.length==iListLength){zDirLeftHip.enterMoCapData(commandList[14], proxy, initTime, false, counter);}
        else{zDirLeftHip.enterMoCapData(commandList[14], proxy, initTime, true, zDirLeftHip.MoCap.length-1);}
    }
    public void setxDirRightHip(MatlabEngine proxy)throws Exception{
        if(xDirRightHip.MoCap.length==iListLength){xDirRightHip.enterMoCapData(commandList[15], proxy, initTime, false, counter);}
        else{xDirRightHip.enterMoCapData(commandList[15], proxy, initTime, true, xDirRightHip.MoCap.length-1);}
    }
    public void setyDirRightHip(MatlabEngine proxy)throws Exception{
        if(yDirRightHip.MoCap.length==iListLength){yDirRightHip.enterMoCapData(commandList[16], proxy, initTime, false, counter);}
        else{yDirRightHip.enterMoCapData(commandList[16], proxy, initTime, true, yDirRightHip.MoCap.length-1);}
    }
    public void setzDirRightHip(MatlabEngine proxy)throws Exception{
        if(cDirRightHip.MoCap.length==iListLength){zDirRightHip.enterMoCapData(commandList[17], proxy, initTime, false, counter);}
        else{zDirRightHip.enterMoCapData(commandList[17], proxy, initTime, true, zDirRightHip.MoCap.length-1);}
    }
    
    
}
