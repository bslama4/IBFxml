package IB_App;

import java.util.*;

public class SimpleMovingAverage {
    private Queue<Double> Dataset = new LinkedList<Double>();
    private int period = 0;
    private double sum = 0;
    
    public SimpleMovingAverage(int period)
    {
        this.period = period;
    }
    
    public void addData(double num)
    {
        sum += num;
        Dataset.add(num);
        
        if(Dataset.size() > period)
        {
            sum -= Dataset.remove();
        }
    }
    
    public int getPeriod()
    {
        return this.period;
    }
    
    public double getMean()
    {
        return sum / period;
    }
}