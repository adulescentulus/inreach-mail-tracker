package de.networkchallenge.lambda.handler;

import com.google.gson.Gson;
import de.networkchallenge.lambda.model.VersionResponse;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static spark.Spark.get;

/**
 * REST endpoint for showing version information
 */
public class VersionController {
    private static final String FILE_PATH = "build.properties";
    private static final Properties PROPERTIES = loadPropertiesFromPackage();

    public VersionController() {
        Gson gson = new Gson();
        get("/version", VersionController::getVersion, gson::toJson);
    }

    private static VersionResponse getVersion(Request request, Response response) {
        return new VersionResponse(PROPERTIES.getProperty("timestamp", "0"), PROPERTIES.getProperty("revision"));
    }

    private static Properties loadPropertiesFromPackage() {
        InputStream in = null;
        Properties prop = new Properties();
        try {
            in = VersionController.class.getClassLoader().getResourceAsStream(FILE_PATH);
            prop.load(in);
            System.out.println(prop.getProperty("name"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return prop;
    }
}
