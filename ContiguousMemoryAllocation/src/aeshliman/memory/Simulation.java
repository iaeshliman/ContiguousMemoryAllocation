package aeshliman.memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import aeshliman.datastructure.SortedList;

public class Simulation
{
	// Instance Variables
	private int memoryMax;
	private int procSizeMax;
	private int numProc;
	private int maxProcTime;
	private Algorithm algorithm;
	
	private Queue<CustomProcess> processes;
	private SortedList<Partition> partitions;
	private int time;
	private int allocationCount;
	private int holes;
	private int averageHoleSize;
	private int totalHoleSize;
	private double percentFreeMemory;
	
	// Constructors
	{
		memoryMax = 1024;
		procSizeMax = 512;
		numProc = 10;
		maxProcTime = 10000;
		algorithm = Algorithm.FIRST_FIT;
		
		processes = new LinkedList<CustomProcess>();
		partitions = new SortedList<Partition>();
		time = 0;
		allocationCount = 0;
		holes = 1;
		averageHoleSize = 1024;
		totalHoleSize = 1024;
		percentFreeMemory = 100;
	}
	
	public Simulation(String path)
	{
		loadConfig(path);
	}
	
	// Getters and Setters
	
	
	// Operations
	public void run()
	{
		partitions.add(new Partition(memoryMax,0)); // Start with one empty partition of all memory
		for(int i=0; i<numProc; i++) // Create all processes
		{
			int processSize = (int)(Math.random() * (procSizeMax+1));
			int processTime = (int)(Math.random() * (maxProcTime+1));
			processes.add(new CustomProcess(i, processSize, processTime));
			System.out.println("Time " + time + ": Process " + i + " created - Size: " + processSize + "KB Time: " + processTime + "ms");
		}
		
		Scanner scan = new Scanner(System.in);
		
		// Run through the simulation
		while(true)
		{
			System.out.println("Time " + time + ": " + partitionsString());
			System.out.println(processesString());
			System.out.println("Holes: " + holes + "   Average Size: " + averageHoleSize + "   Total Size: "
					+ totalHoleSize + String.format("   Free Memory %.2f", percentFreeMemory) + "%");
			System.out.print("Press enter to continue");
			scan.nextLine();
			// Partition for processes until needing to wait
			while(!processes.isEmpty())
			{
				// Current process to attempt a partition for
				CustomProcess process = processes.peek();
				
				Partition partition = null;
				boolean done = false;
				for(Partition part : partitions) // Iterate through each partition searching for a valid free partition
				{
					if(part.isFree()&&part.getSize()>=process.getProcessSize()) // Checks partition is free and large enough
					{
						if(partition==null) // Selects first valid partition regardless of algorithm
						{
							partition = part;
							if(algorithm==Algorithm.FIRST_FIT) done = true; // If first fit break after first partition is found
						}
						else
						{
							switch(algorithm)
							{
							case FIRST_FIT:
								break;
							case BEST_FIT:
								if(part.getSize()<partition.getSize()) partition = part; // Selects the smallest valid partition
								break;
							case WORST_FIT:
								if(part.getSize()>partition.getSize()) partition = part; // Selects the largest valid partition
								break;
							}
						}
					}
					if(done) break;
				}
				
				if(partition==null) { break; } // No valid partition was found must wait a cycle
				else
				{
					Partition processPartition = new Partition(process.getProcessSize(),partition.getAddress(),process);
					System.out.println("Time " + time + ": Allocation of partition | " + processPartition + " | at address " + processPartition.getAddress());
					partitions.add(processPartition);
					allocationCount++;
					totalHoleSize -= processPartition.getSize();
					if(partition.getSize()!=process.getProcessSize())
					{
						Partition freePartition = new Partition(partition.getSize()-process.getProcessSize(),partition.getAddress()+process.getProcessSize());
						partitions.add(freePartition);
					}
					partitions.remove(partition);
					processes.poll(); // Remove process from queue
				}
			}
			
			time++;
			
			// Deallocate all finished partitions
			for(Partition partition : partitions)
			{
				if(!partition.isFree())
				{
					partition.decrement();
					if(partition.isFinished())
					{
						System.out.println("Time " + time + ": Deallocation of partition | " + partition + " | at address " + partition.getAddress());
						partition.deallocate();
						allocationCount--;
						holes++;
						totalHoleSize += partition.getSize();
					}
				}
			}
			partitionMerge();
			averageHoleSize = totalHoleSize/holes;
			percentFreeMemory = ((double)totalHoleSize/memoryMax)*100;
			if(allocationCount==0&&processes.size()==0) break;
		}
		System.out.println(partitionsString());
	}
	
	private void loadConfig(String path)
	{
		try(Scanner scan = new Scanner(new File(path));)
		{
			while(scan.hasNext())
			{
				String[] line = scan.nextLine().split("\\s*=\\s*");
				if(line.length!=2) continue;
				switch(line[0])
				{
				case "MEMORY_MAX":
					memoryMax = Integer.parseInt(line[1]);
					break;
				case "PROC_SIZE_MAX":
					procSizeMax = Integer.parseInt(line[1]);
					break;
				case "NUM_PROC":
					numProc = Integer.parseInt(line[1]);
					break;
				case "MAX_PROC_TIME":
					maxProcTime = Integer.parseInt(line[1]);
					break;
				default:
					System.err.println("Invalid key " + line[0] + " with value " + line[1]);
					break;
				}
			}
		}
		catch(FileNotFoundException e) { e.printStackTrace(); }
	}
	
	public void partitionMerge()
	{
		Partition partition = null;
		
		for(Partition part : partitions)
		{
			if(part.isFree())
			{
				if(partition==null) partition = part;
				else
				{
					Partition newPart = new Partition(partition.getSize()+part.getSize(),partition.getAddress());
					System.out.println("Time " + time + ": Free partitions merged at addresses " + partition.getAddress() + " and " + part.getAddress());
					partitions.remove(part);
					partitions.remove(partition);
					partitions.add(newPart);
					partition = newPart;
					holes--;
				}
			}
			else
			{
				partition = null;
			}
		}
	}
	
	// toString
	public String partitionsString()
	{
		String toString = "| ";
		for(Partition partition : partitions) { toString += partition + " | "; }
		return toString.trim();
	}
	
	public String processesString()
	{
		String toString = "Waiting Processes: ";
		for(CustomProcess process : processes) toString += "[P" + process.getPID() + " - " + process.getProcessSize() + "KB], ";
		return toString.substring(0,toString.length()-2);
	}	
}
