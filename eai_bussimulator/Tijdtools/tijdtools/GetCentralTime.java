package tijdtools;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GetCentralTime {

    public static Tijd getCentralTime()
    {
        try {
            HTTPFuncties httpFuncties = new HTTPFuncties();
            String result = httpFuncties.executeGet("json");
            return new ObjectMapper().readValue(result, Tijd.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Tijd(0,0,0);
        }
    }
}
