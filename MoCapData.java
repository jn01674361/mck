//MOCAPDATA

package src.mck;

import com.mathworks.engine.*;
import java.lang.*;
import org.apache.commons.lang3.ArrayUtils;
/**
 *
 * @author Joar Nykvist
 */
public class MoCapData {

    //double arrays for storing data
    public double[] Time = new double[5];
    public double[] MoCap= new double[5];

    //used for setting motion data
    public double dMoCapValue;

    //setter
    public MoCapData(double[] Time, double[] MoCap){

        this.Time = Time;
        this.MoCap= MoCap;

    }

    //getters
    public double[] getTime(){
        return Time;
    }
    public double[] getMoCap(){
        return MoCap;
    }

    public void enterMoCapData(String MLfunction, MatlabEngine proxy, long t0, boolean full)throws Exception {

        /**
         * Method for creating a 2d-list with time and MoCap data
         *
         * Arguments:
         * MLfunction - the name of the function to call in MatLab, including opening parenthesis but not closing parenthesis
         * proxy - matlab workspace
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
        double dElapsedTime = (double) lElapsedTime;

        try{

            if(full==true){

                //if the list is full then we know that we can add the data at the end
                Time=ArrayUtils.add(Time, dElapsedTime);
                MoCap=ArrayUtils.add(MoCap, dMoCapValue);


                //and clear the first index and shift the others to the left
                Time=ArrayUtils.remove(Time, 0);
                MoCap=ArrayUtils.remove(MoCap, 0);
            }
            else {

                Time=ArrayUtils.add(Time, dElapsedTime);
                MoCap=ArrayUtils.add(MoCap, dMoCapValue);
            }
        }catch(java.lang.NullPointerException e){
            //if no MoCap data is recorded we add a zero with the current time
            if(full==true){
                Time=ArrayUtils.add(Time, dElapsedTime);
                MoCap=ArrayUtils.add(MoCap, 0.0);


                //and clear the first index and shift the others to the left
                Time=ArrayUtils.remove(Time, 0);
                MoCap=ArrayUtils.remove(MoCap, 0);
            }
            else{
                Time=ArrayUtils.add(Time, dElapsedTime);
                MoCap=ArrayUtils.add(MoCap, 0.0);
            }
        }

    }
}
