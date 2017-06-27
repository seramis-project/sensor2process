package at.ac.wu.seramis.sensor2process.database.model;

import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;

import java.util.ArrayList;

public class Actor
{
	private String actorID = ""; // EPC in rtls_sample.csv, 8181 items
	
	private ArrayList<TemporalSenseEvent> reads = null;
	
	public Actor(String actorID)
	{
		this.actorID = actorID;
	}

	public ArrayList<TemporalSenseEvent> getReads() {
		return reads;
	}

	public void setReads(ArrayList<TemporalSenseEvent> reads) {
		this.reads = reads;
	}

	@Override
	public String toString() {
		//return "Actor [actorID=" + actorID + ", reads=" + reads + "]";
		return actorID;
	}
	
	
	
	//Gets string with route in X and Y
	public String getRoute(){
		
		String str = "";
		
		for (int i = 0; i < this.reads.size(); i++) {
			
			str = str + "X: " + this.reads.get(i).getX()+ " Y: " + this.reads.get(i).getY() + " \n";
			
		}
		return str;
		
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actorID == null) ? 0 : actorID.hashCode());
		result = prime * result + ((reads == null) ? 0 : reads.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		if (actorID == null) {
			if (other.actorID != null)
				return false;
		} else if (!actorID.equals(other.actorID))
			return false;
		if (reads == null) {
			if (other.reads != null)
				return false;
		} else if (!reads.equals(other.reads))
			return false;
		return true;
	}
	
	
	
	
}
