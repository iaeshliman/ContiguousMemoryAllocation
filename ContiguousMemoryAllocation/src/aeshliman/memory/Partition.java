package aeshliman.memory;

public class Partition implements Comparable<Partition>
{
	// Instance Variables
	private int size;
	private int address;
	private CustomProcess process;
	
	// Constructors
	public Partition(int size, int address, CustomProcess process)
	{
		this.size = size;
		this.address = address;
		this.process = process;
	}
	
	public Partition(int size, int address)
	{
		this.size = size;
		this.address = address;
	}
	
	// Getters and Setters
	public int getSize() { return this.size; }
	public int getAddress() { return this.address; }
	
	// Operations
	public int compareTo(Partition partition) { return address-partition.address; }
	public void deallocate() { this.process = null; }
	public boolean isFree() { return process==null; }
	public void decrement() { process.decrement(); }
	public boolean isFinished() { return process.getProcessTime()==0; }
	
	// toString
	public String toString()
	{
		if(process==null) return "Free (" + size + "KB)";
		return "P" + process.getPID() + " [" + (int)Math.ceil((double)process.getProcessTime()/1000) + "s] (" + size + "KB)";
	}
}
