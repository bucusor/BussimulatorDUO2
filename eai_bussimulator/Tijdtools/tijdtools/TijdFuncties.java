package tijdtools;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class TijdFuncties implements ITijdFuncties{
	private static Time startTijd;
	private static Time simulatorTijd;
	private static Time verschil;
	private static int interval;
	private static int syncInterval;
	private static int syncCounter;


	public void initSimulatorTijden(int interval, int syncInterval){
		simulatorTijd=new Tijd(0,0,0);
		startTijd= GetCentralTime.getCentralTime();
		verschil=TijdCalculator.berekenVerschil(startTijd,simulatorTijd);
		TijdFuncties.interval =interval;
		syncCounter=syncInterval;
		TijdFuncties.syncInterval =syncInterval;
	}

	public String getSimulatorWeergaveTijd(){
		Time simulatorWeergaveTijd= simulatorTijd.copyTijd();
		simulatorWeergaveTijd.increment(verschil);
		return simulatorWeergaveTijd.toString();
	}

	public int getCounter(){
		return TijdCalculator.calculateCounter(simulatorTijd);
	}

	public int getTijdCounter(){
		return TijdCalculator.calculateCounter(simulatorTijd)+TijdCalculator.calculateCounter(verschil);
	}

	public void simulatorStep() throws InterruptedException{
		Thread.sleep(interval);
		simulatorTijd.increment(new Tijd(0,0,1));
		syncCounter--;
		if (syncCounter==0){
			syncCounter=syncInterval;
			synchroniseTijd();
		}
	}


	public static void synchroniseTijd(){
		Time huidigeTijd = GetCentralTime.getCentralTime();
		System.out.println("De werkelijke tijd is nu: "+ huidigeTijd.toString());
		Time verwachtteSimulatorTijd = simulatorTijd.copyTijd();
		verwachtteSimulatorTijd.increment(verschil);
		Time delay = TijdCalculator.berekenVerschil(huidigeTijd, verwachtteSimulatorTijd);
		verschil.increment(delay);
	}

//	public static Tijd getCentralTime()
//	{
//		try {
//			HTTPFuncties httpFuncties = new HTTPFuncties();
//			String result = httpFuncties.executeGet("json");
//			return new ObjectMapper().readValue(result, Tijd.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return new Tijd(0,0,0);
//		}
//	}




}