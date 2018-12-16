import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// This is the monitor class which maintains the state of the threads
// and also syncronization between threads
class Monitor {
	
	private int numOfPhilosophers;
	final Lock lock;

	private States[] state;
	final Condition[] condition;

	
	Monitor(int numOfPhilosophers) {
		this.numOfPhilosophers = numOfPhilosophers;
		lock = new ReentrantLock();
		state = new States[numOfPhilosophers];
		condition = new Condition[numOfPhilosophers];
		
		for (int i = 0; i < numOfPhilosophers; i++) {
			state[i] = States.THINKING;
			condition[i] = lock.newCondition();
		}
	}

	
	// philosopher i picks up both the chopsticks.
	public void pickUp(int chopstickNumber) {
		final String PICK_UP = "picks up";
		lock.lock();
		try {
		
			state[chopstickNumber] = States.HUNGRY;

			
			if ((state[(chopstickNumber - 1 + numOfPhilosophers) % numOfPhilosophers] != States.EATING)
					&& (state[(chopstickNumber + 1) % numOfPhilosophers] != States.EATING)) {

				Util.print(PICK_UP, "LEFT", chopstickNumber + 1);
				Util.print(PICK_UP, "RIGHT", chopstickNumber + 1);

				state[chopstickNumber] = States.EATING;
			} else { // Whenever one neighbor eats, it has to sleep and wait
				try {
					condition[chopstickNumber].await();
					
					Util.print(PICK_UP, "LEFT", chopstickNumber + 1);
					Util.print(PICK_UP, "RIGHT", chopstickNumber + 1);

					state[chopstickNumber] = States.EATING;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} finally {
			lock.unlock();
		}
	}

	// After eating put down chopsticks
	public void putDown(int i) {
		final String PUTS_DOWN = "puts down";
		lock.lock();
		try {
			Util.print(PUTS_DOWN, "RIGHT", i + 1);
			Util.print(PUTS_DOWN, "LEFT", i + 1);

			state[i] = States.THINKING;
			
			int left = (i - 1 + numOfPhilosophers) % numOfPhilosophers;
			int left2 = (i - 2 + numOfPhilosophers) % numOfPhilosophers;

			if ((state[left] == States.HUNGRY) && (state[left2] != States.EATING)) {
				condition[left].signal();
			}
		
			if ((state[(i + 1) % numOfPhilosophers] == States.HUNGRY)
					&& (state[(i + 2) % numOfPhilosophers] != States.EATING)) {
				condition[(i + 1) % numOfPhilosophers].signal();
			}
		} finally {
			lock.unlock();
		}
	}
}