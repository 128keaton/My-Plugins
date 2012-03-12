package com.github.xrevolut1onzx.broadcaster;

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
	 * Keeps track of the number of seconds
	 * between server start and the first
	 * broadcasting of this message
	 */
	private int offsetDelay;
	/** True if the Op should see the message, only affected when using Op permission type */
	private Boolean opSeesMessage;
	/** True if the player should see the message, only affected when using Op permission type*/
	private Boolean playerSeesMessage;
	
	/**
	 * Makes a new message to be sent to the players
	 * @param m The string version of the message to send
	 * @param d The delay between recurring messages
	 * @param r If the message is recurring, true. Else, false
	 */
	public BroadcastMessage(Boolean u, String m, int d, Boolean r, int o, Boolean oSM, Boolean pSM)
	{
		isInUse = u;
		message = m;
		delay = d;
		recurring = r;
		offsetDelay = o;
		opSeesMessage = oSM;
		playerSeesMessage = pSM;
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
	
	/**
	 * Sets the offset delay
	 * @param o The number of seconds that'll be assigned to the offset delay
	 */
	public void setOffsetDelay(int o)
	{
		offsetDelay = o;
	}
	
	/**
	 * Gets the offset delay
	 * @return The number of seconds currently assigned to the offset delay
	 */
	public int getOffsetDelay()
	{
		return offsetDelay;
	}
	
	/**
	 * Sets if the Op sees the message or not
	 * @param oSM True if the Op sees the message, else false
	 */
	public void setOpSeesMessage(Boolean oSM)
	{
		opSeesMessage = oSM;
	}
	
	/**
	 * Gets if the Op sees the message or not
	 * @return True if the Op sees the message, else false
	 */
	public Boolean getOpSeesMessage()
	{
		return opSeesMessage;
	}
	
	/**
	 * Sets if the player sees the message or not
	 * @param pSM True if the player sees the message, else false
	 */
	public void setPlayerSeesMessage(Boolean pSM)
	{
		playerSeesMessage = pSM;
	}
	
	/**
	 * Gets if the player sees the message or not
	 * @return True if the player sees the message, else false
	 */
	public Boolean getPlayerSeesMessage()
	{
		return playerSeesMessage;
	}
	
}
