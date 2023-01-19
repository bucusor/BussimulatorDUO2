package tijdtools;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class TijdFuncties implements ITijdFuncties {
	private Time startTijd;
	private Time simulatorTijd;
	private Time verschil;
	private int interval;
	private int syncInterval;
	private int syncCounter;

	public void initSimulatorTijden(int interval, int syncInterval){
		simulatorTijd=new Tijd(0,0,0);
		startTijd= ITijdFuncties.getCentralTime();
		verschil=berekenVerschil(startTijd,simulatorTijd);
		this.interval=interval;
		this.syncCounter=syncInterval;
		this.syncInterval=syncInterval;
	}

	public String getSimulatorWeergaveTijd(){
		Time simulatorWeergaveTijd= simulatorTijd.copyTijd();
		simulatorWeergaveTijd.increment(verschil);
		return simulatorWeergaveTijd.toString();
	}

	public int getCounter(){
		return calculateCounter(simulatorTijd);
	}

	public int getTijdCounter(){
		return calculateCounter(simulatorTijd)+calculateCounter(verschil);
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

	public int calculateCounter(Time tijd){
		return tijd.getUur()*3600+tijd.getMinuut()*60+tijd.getSeconde();
	}

	public Time berekenVerschil(Time reverentieTijd, Time werkTijd){
		int urenVerschil = reverentieTijd.getUur()-werkTijd.getUur();
		int minutenVerschil = reverentieTijd.getMinuut()-werkTijd.getMinuut();
		int secondenVerschil = reverentieTijd.getSeconde()-werkTijd.getSeconde();
		if (secondenVerschil<0){
			minutenVerschil--;
			secondenVerschil+=60;
		}
		if (minutenVerschil<0){
			urenVerschil--;
			minutenVerschil+=60;
		}
		return new Tijd(urenVerschil, minutenVerschil, secondenVerschil);
	}

	public void synchroniseTijd(){
		Time huidigeTijd = ITijdFuncties.getCentralTime();
		System.out.println("De werkelijke tijd is nu: "+ huidigeTijd.toString());
		Time verwachtteSimulatorTijd = simulatorTijd.copyTijd();
		verwachtteSimulatorTijd.increment(verschil);
		Time delay = berekenVerschil(huidigeTijd, verwachtteSimulatorTijd);
		verschil.increment(delay);
	}



}