package bussimulator;


public class Runner implements Runnable {
	@Override
	public void run() {
		Simulator simulator = new Simulator();
		simulator.startSimulator();
	}

}
