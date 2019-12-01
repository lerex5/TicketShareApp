package com.lerex.tr;

import java.io.*;
import android.content.res.Resources;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CitiesJSON {

    private String jsonString;
    private static final String LOGTAG = CitiesJSON.class.getSimpleName();

    public CitiesJSON(Resources resources, int id) {
        InputStream resourceReader = resources.openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
            }
        }

        jsonString = writer.toString();
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}