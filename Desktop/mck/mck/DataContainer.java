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

    public MoCapData LeftKneeAngl= new MoCapData(blankDList,blankDList);
    public MoCapData RightKneeAngl= new MoCapData(blankDList,blankDList);
    
    public MoCapData LeftElbowAngl= new MoCapData(blankDList,blankDList);
    public MoCapData RightElbowAngl= new MoCapData(blankDList,blankDList);         

    public long initTime;
    
    public int counter;
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
    public MoCapData getLeftElbowAngl(){return LeftElbowAngl;}    
    public MoCapData getRightElbowAngl(){return RightElbowAngl;}
    public MoCapData getLeftKneeAngl(){return LeftKneeAngl;}
    public MoCapData getRightKneeAngl(){return RightKneeAngl;}
    public String[] getcommandList(){return commandList;}
    public int getiListLength(){return iListLength;}
    public int getcounter(){return counter;}

    
    public void setLeftShouldervNorm(MatlabEngine proxy)throws Exception{                
        if(counter < iListLength-1){LeftShouldervNorm.enterMoCapData(commandList[0], proxy, initTime, false);}
        else{LeftShouldervNorm.enterMoCapData(commandList[0], proxy, initTime, true);}                
    }
    public void setRightShouldervNorm(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){RightShouldervNorm.enterMoCapData(commandList[1], proxy, initTime, false);}
        else{RightShouldervNorm.enterMoCapData(commandList[1], proxy, initTime, true);}        
    }
    public void setLeftHipvNorm(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){LeftHipvNorm.enterMoCapData(commandList[2], proxy, initTime, false);}
        else{LeftHipvNorm.enterMoCapData(commandList[2], proxy, initTime, true);}
        
    }
    public void setRightHipvNorm(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){RightHipvNorm.enterMoCapData(commandList[3], proxy, initTime, false);}
        else{RightHipvNorm.enterMoCapData(commandList[3], proxy, initTime, true);}
        
    }
    public void setLeftKneeAngV(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){LeftKneeAngV.enterMoCapData(commandList[4], proxy, initTime, false);}
        else{LeftKneeAngV.enterMoCapData(commandList[4], proxy, initTime, true);}
    }
    public void setRightKneeAngV(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){RightKneeAngV.enterMoCapData(commandList[5], proxy, initTime, false);}
        else{RightKneeAngV.enterMoCapData(commandList[5], proxy, initTime, true);}
    }
    public void setLeftElbowAngV(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){LeftElbowAngV.enterMoCapData(commandList[6], proxy, initTime, false);}
        else{LeftElbowAngV.enterMoCapData(commandList[6], proxy, initTime, true);}
    }
    public void setRightElbowAngV(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){RightElbowAngV.enterMoCapData(commandList[7], proxy, initTime, false);}
        else{RightElbowAngV.enterMoCapData(commandList[7], proxy, initTime, true);}
    }    
    public void setLeftElbowAngl(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){LeftElbowAngl.enterMoCapData(commandList[10], proxy, initTime, false);}
        else{LeftElbowAngl.enterMoCapData(commandList[10], proxy, initTime, true);}
    }
    public void setRightElbowAngl(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){RightElbowAngl.enterMoCapData(commandList[11], proxy, initTime, false);}
        else{RightElbowAngl.enterMoCapData(commandList[11], proxy, initTime, true);}
    }
    public void setLeftKneeAngl(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){LeftKneeAngl.enterMoCapData(commandList[10], proxy, initTime, false);}
        else{LeftKneeAngl.enterMoCapData(commandList[18], proxy, initTime, true);}
    }
    public void setRightKneeAngl(MatlabEngine proxy)throws Exception{
        if(counter < iListLength-1){RightKneeAngl.enterMoCapData(commandList[11], proxy, initTime, false);}
        else{RightKneeAngl.enterMoCapData(commandList[19], proxy, initTime, true);}
    }
    
    
}
