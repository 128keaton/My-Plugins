package xrevolut1onzx.broadcastdonator;

public class BroadcastMessage
{
	
	String message;
	int delay;
	
	public BroadcastMessage()
	{
		message = "";
		delay = 0;
	}
	
	public BroadcastMessage(String m, int d)
	{
		message = m;
		delay = d;
	}
	
	public void setMessage(String m)
	{
		message = m;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setDelay(int d)
	{
		delay = d;
	}
	
	public int getDelay()
	{
		return delay;
	}
	
}
