package aeshliman.memory;

public class CustomProcess
{
	// Instance Variables
	private int pid;
	private int processSize;
	private int processTime;
	
	// Constructors
	public CustomProcess(int pid, int processSize, int processTime)
	{
		this.pid = pid;
		this.processSize = processSize;
		this.processTime = processTime;
	}
	
	// Getters and Setters
	public int getPID() { return this.pid; }
	public int getProcessSize() { return this.processSize; }
	public int getProcessTime() { return this.processTime; }
	
	// Operations
	public void decrement()
	{
		this.processTime-=1000;
		if(processTime<0) processTime=0;
	}
	
	// toString
	public String toString()
	{
		
		String toString = "PID: " + pid + "   Size: " + processSize + "KB   Time: " + processTime + "ms";
		return toString;
	}
}
