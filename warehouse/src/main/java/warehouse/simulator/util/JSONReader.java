package warehouse.simulator.util;

import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {
    public static JSONObject readJSON(File f)
    {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = null;
        try (Reader reader = new FileReader(f))
        {
            jsonObj = (JSONObject) parser.parse(reader);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return jsonObj;
    }
}
