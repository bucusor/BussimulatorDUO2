package tijdtools;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public interface ITijdFuncties {

    void initSimulatorTijden(int interval, int syncInterval);
    String getSimulatorWeergaveTijd();
    int getCounter();
    int getTijdCounter();
    void simulatorStep() throws InterruptedException;


}
