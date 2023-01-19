package bussimulator;

public class BusFactory {
    public Bus createBus(Route lijn, Bedrijven bedrijf, int richting) {
        return new Bus(lijn, bedrijf, richting);
    }

}
