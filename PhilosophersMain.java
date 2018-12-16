
// In this class, philosopher threads are created.
public class PhilosophersMain implements Runnable {

	public static final int NUM_OF_PHILOSOPHER = 5;
	public static final int MAX_EAT_TIMES = 2;

	
	private int identifier;
	private int maxEat; 
	private Monitor monitor;
	private Thread thread;
	private int lengthOfSleep; 

	PhilosophersMain(int identifier, int maxEat, Monitor monitor) {
		this.identifier = identifier;
		this.maxEat = maxEat;
		this.monitor = monitor;
		lengthOfSleep = 10; 
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		int count = 1;
		while (count <= maxEat) {
			monitor.pickUp(identifier);
			eat(count);
			monitor.putDown(identifier);
			++count;
		}
	}

	// Printing the states of the philosopher threads.
	void eat(int count) {
		System.out.format("Philosopher %d eats (%d times)\n", identifier + 1, count);
		
		try {
			Thread.sleep(lengthOfSleep);
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) {

		Monitor mon = new Monitor(NUM_OF_PHILOSOPHER);
		PhilosophersMain[] p = new PhilosophersMain[NUM_OF_PHILOSOPHER];

		System.out.println("Philosophers start having dinner now\n");
        System.out.println("----------------------------------------");

		// Starting of philosopher threads
		for (int i = 0; i < NUM_OF_PHILOSOPHER; i++)
			p[i] = new PhilosophersMain(i, MAX_EAT_TIMES, mon);

		
		for (int i = 0; i < NUM_OF_PHILOSOPHER; i++)
			try {
				p[i].thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		System.out.println("------------------------------------------");
		

		System.out.println("Stop dinner");
	}
}