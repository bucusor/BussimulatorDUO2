package tijdtools;

public class TijdCalculator {
    public static int calculateCounter(Time tijd){
        return tijd.getUur()*3600+tijd.getMinuut()*60+tijd.getSeconde();
    }

    public static Time berekenVerschil(Time reverentieTijd, Time werkTijd){
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

}
