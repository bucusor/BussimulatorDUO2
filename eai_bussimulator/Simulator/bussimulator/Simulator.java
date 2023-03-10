package bussimulator;

import tijdtools.ITijdFuncties;
import tijdtools.TijdFuncties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Simulator {
    private static HashMap<Integer, ArrayList<Bus>> busStart = new HashMap<Integer,ArrayList<Bus>>();
    private static ArrayList<Bus> actieveBussen = new ArrayList<Bus>();
    private static int interval=1000;
    private static int syncInterval=5;
    static BusFactory busFactory;

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
        busFactory = new BusFactory();
        createBus(1);
        createBus(-1);
        return Collections.min(busStart.keySet());
    }

    public static void createBus(int direction){
        addBus(3, busFactory.createBus(Lijnen.LIJN1, Bedrijven.ARRIVA, direction));
        addBus(5, busFactory.createBus(Lijnen.LIJN2, Bedrijven.ARRIVA, direction));
        addBus(4, busFactory.createBus(Lijnen.LIJN3, Bedrijven.ARRIVA, direction));
        addBus(6, busFactory.createBus(Lijnen.LIJN4, Bedrijven.ARRIVA, direction));
        addBus(3, busFactory.createBus(Lijnen.LIJN5, Bedrijven.FLIXBUS, direction));
        addBus(5, busFactory.createBus(Lijnen.LIJN6, Bedrijven.QBUZZ, direction));
        addBus(4, busFactory.createBus(Lijnen.LIJN7, Bedrijven.QBUZZ, direction));
        addBus(6, busFactory.createBus(Lijnen.LIJN1, Bedrijven.ARRIVA, direction));
        addBus(12, busFactory.createBus(Lijnen.LIJN4, Bedrijven.ARRIVA, direction));
        addBus(10, busFactory.createBus(Lijnen.LIJN5, Bedrijven.FLIXBUS, direction));
        addBus(3, busFactory.createBus(Lijnen.LIJN8, Bedrijven.QBUZZ, direction));
        addBus(5, busFactory.createBus(Lijnen.LIJN8, Bedrijven.QBUZZ, direction));
        addBus(14, busFactory.createBus(Lijnen.LIJN3, Bedrijven.ARRIVA, direction));
        addBus(16, busFactory.createBus(Lijnen.LIJN4, Bedrijven.ARRIVA, direction));
        addBus(13, busFactory.createBus(Lijnen.LIJN5, Bedrijven.FLIXBUS, direction));
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
    public void startSimulator() {
        ITijdFuncties tijdFuncties = StartTijd.starter();
        tijdFuncties.initSimulatorTijden(interval, syncInterval);
        int volgende = initBussen();
        while ((volgende >= 0) || !actieveBussen.isEmpty()) {
            counter = tijdFuncties.getCounter();
            tijd = tijdFuncties.getTijdCounter();
            System.out.println("De tijd is:" + tijdFuncties.getSimulatorWeergaveTijd());
            volgende = (counter == volgende) ? startBussen(counter) : volgende;
            moveBussen(tijd);
            sendETAs(tijd);
            try { tijdFuncties.simulatorStep();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
