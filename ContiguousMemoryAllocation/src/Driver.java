import aeshliman.memory.Simulation;

public class Driver
{
	public static void main(String[] args)
	{
		Simulation sim = new Simulation("ContiguousMemoryAllocation/config.txt");
		sim.run();
		
	}
}
