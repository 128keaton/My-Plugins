package xrevolut1onzx.broadcaster;

public class BroadcastMessage
{
	/** If the message is in use or not */
	private Boolean isInUse;
	/** The message to be sent to the players */
	private String message;
	/** The delay between recurring messages */
	private int delay;
	/** If the message is repeating or not */
	private Boolean recurring;
	
	/**
	 * Makes a new message to be sent to the players
	 * @param m The string version of the message to send
	 * @param d The delay between recurring messages
	 * @param r If the message is recurring, true. Else, false
	 */
	public BroadcastMessage(Boolean u, String m, int d, Boolean r)
	{
		isInUse = u;
		message = m;
		delay = d;
		recurring = r;
	}
	
	/**
	 * Sets if the messages is in use or not
	 * @param u If the message is in use, true. If not, false
	 */
	public void setInUse(Boolean u)
	{
		isInUse = u;
	}
	
	/**
	 * Gets if the message is in use or not
	 * @return If the message is in ue, return true. If not, false
	 */
	public Boolean getInUse()
	{
		return isInUse;
	}
	
	/**
	 * Sets the message that's sent to the player
	 * @param m The string of the message to send
	 */
	public void setMessage(String m)
	{
		message = m;
	}
	
	/**
	 * Gets the message to be sent to the player
	 * @return The string of the message being sent to the player
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Sets the delay between messages
	 * @param d The delay in seconds between messages
	 */
	public void setDelay(int d)
	{
		delay = d;
	}
	
	/**
	 * Gets the delay between messages
	 * @return The number of seconds between messages
	 */
	public int getDelay()
	{
		return delay;
	}
	
	/**
	 * Sets if the message repeats or not
	 * @param r If the message repeats, true. If not, false
	 */
	public void setRecurring(Boolean r)
	{
		recurring = r;
	}
	
	/**
	 * Gets whether the message repeats or not
	 * @return If the message repeats, true. If not, false
	 */
	public Boolean getRecurring()
	{
		return recurring;
	}
	
}
