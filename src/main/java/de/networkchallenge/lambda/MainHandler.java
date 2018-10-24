package de.networkchallenge.lambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import de.networkchallenge.lambda.handler.ExampleController;
import de.networkchallenge.lambda.handler.VersionController;
import spark.Spark;

import static spark.Spark.before;

/**
 * MainHandler for all API-Gateway proxy requests
 */
public class MainHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse>
{

    private SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler =
            SparkLambdaContainerHandler.getAwsProxyHandler();
    private boolean initialized = false;

    /**
     * Lambda needs this to be public!
     *
     * @throws ContainerInitializationException
     */
    public MainHandler() throws ContainerInitializationException {
    }

    public static void main(String... args) {
        initSpark();
    }

    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        if (!initialized) {
            initialized = true;
            initSpark();
        }
        return handler.proxy(awsProxyRequest, context);
    }

    private static void initSpark() {
        defineRoutes();
        Spark.awaitInitialization();
    }

    private static void defineRoutes() {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
        new ExampleController();
        new VersionController();
    }
}
