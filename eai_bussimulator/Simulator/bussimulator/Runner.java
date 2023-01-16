package bussimulator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import tijdtools.TijdFuncties;

public class Runner implements Runnable {

	private static HashMap<Integer,ArrayList<Bus>> busStart = new HashMap<Integer,ArrayList<Bus>>();
	private static ArrayList<Bus> actieveBussen = new ArrayList<Bus>();
	private static int interval=1000;
	private static int syncInterval=5;

	private static void addBus(int starttijd, Bus bus){
		ArrayList<Bus> bussen = new ArrayList<Bus>();
		if (busStart.containsKey(starttijd)) {
			bussen = busStart.get(starttijd);
		}
		bussen.add(bus);
		busStart.put(starttijd,bussen);
		bus.setbusID(starttijd);
	}

	private static int startBussen(int tijd){
		for (Bus bus : busStart.get(tijd)){
			actieveBussen.add(bus);
		}
		busStart.remove(tijd);
		return (!busStart.isEmpty()) ? Collections.min(busStart.keySet()) : -1;
	}

	public static void moveBussen(int nu){
		Iterator<Bus> itr = actieveBussen.iterator();
		while (itr.hasNext()) {
			Bus bus = itr.next();
			boolean eindpuntBereikt = bus.move();
			if (eindpuntBereikt) {
				bus.sendLastETA(nu);
				itr.remove();
			}
		}		
	}

	public static void sendETAs(int nu){
		Iterator<Bus> itr = actieveBussen.iterator();
		while (itr.hasNext()) {
			Bus bus = itr.next();
			bus.sendETAs(nu);
		}				
	}

	public static int initBussen(){
		createBus(1);
		createBus(-1);
		return Collections.min(busStart.keySet());
	}

	public static void createBus(int direction){
		addBus(3, new Bus(Lijnen.LIJN1, Bedrijven.ARRIVA, direction));
		addBus(5, new Bus(Lijnen.LIJN2, Bedrijven.ARRIVA, direction));
		addBus(4, new Bus(Lijnen.LIJN3, Bedrijven.ARRIVA, direction));
		addBus(6, new Bus(Lijnen.LIJN4, Bedrijven.ARRIVA, direction));
		addBus(3, new Bus(Lijnen.LIJN5, Bedrijven.FLIXBUS, direction));
		addBus(5, new Bus(Lijnen.LIJN6, Bedrijven.QBUZZ, direction));
		addBus(4, new Bus(Lijnen.LIJN7, Bedrijven.QBUZZ, direction));
		addBus(6, new Bus(Lijnen.LIJN1, Bedrijven.ARRIVA, direction));
		addBus(12, new Bus(Lijnen.LIJN4, Bedrijven.ARRIVA, direction));
		addBus(10, new Bus(Lijnen.LIJN5, Bedrijven.FLIXBUS, direction));
		addBus(3, new Bus(Lijnen.LIJN8, Bedrijven.QBUZZ, direction));
		addBus(5, new Bus(Lijnen.LIJN8, Bedrijven.QBUZZ, direction));
		addBus(14, new Bus(Lijnen.LIJN3, Bedrijven.ARRIVA, direction));
		addBus(16, new Bus(Lijnen.LIJN4, Bedrijven.ARRIVA, direction));
		addBus(13, new Bus(Lijnen.LIJN5, Bedrijven.FLIXBUS, direction));
	}

//	@Override
//	public void run() {
//		int tijd=0;
//		int volgende = initBussen();
//		while ((volgende>=0) || !actieveBussen.isEmpty()) {
//			System.out.println("De tijd is:" + tijd);
//			volgende = (tijd==volgende) ? startBussen(tijd) : volgende;
//			moveBussen(tijd);
//			sendETAs(tijd);
//			try {
//				Thread.sleep(interval);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			tijd++;
//		}
//	}
//	Om de tijdsynchronisatie te gebruiken moet de onderstaande run() gebruikt worden
//
	@Override
	public void run() {
		int tijd=0;
		int counter=0;
		TijdFuncties tijdFuncties = new TijdFuncties();
		tijdFuncties.initSimulatorTijden(interval,syncInterval);
		int volgende = initBussen();
		while ((volgende>=0) || !actieveBussen.isEmpty()) {
			counter=tijdFuncties.getCounter();
			tijd=tijdFuncties.getTijdCounter();
			System.out.println("De tijd is:" + tijdFuncties.getSimulatorWeergaveTijd());
			volgende = (counter==volgende) ? startBussen(counter) : volgende;
			moveBussen(tijd);
			sendETAs(tijd);
			try {
				tijdFuncties.simulatorStep();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
