package recycle;

import java.util.Date;

/*
 * Author - Khushali Bhatt
 * Interface that defines methods to implement in RecycleMachine class
 */
public interface IStatisticsForRCM { 
	public double getTotalCacheOrCouponsIssued();
	public int numberOfTimeMachineEmptied(Date startTime, Date endTime); 
	public double getTotalWeight();	
}