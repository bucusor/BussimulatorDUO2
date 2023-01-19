package bussimulator;

public interface Route {
    Halte getHalte(int positie);
    int getRichting(int positie);
    int getLengte();
    String getName();
}
