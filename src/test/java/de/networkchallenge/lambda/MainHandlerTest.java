package de.networkchallenge.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.HttpMethod;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.mockito.Mockito;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

/**
 * Unit test for simple MainHandler.
 */
public class MainHandlerTest
{

    @Test
    public void handle() throws ContainerInitializationException
    {

        final MainHandler mainHandler = new MainHandler();
        Context contextMock = Mockito.mock(Context.class);
        Mockito.when(contextMock.getLogger()).thenReturn(Mockito.mock(LambdaLogger.class));
        final AwsProxyResponse response = mainHandler.handleRequest(
                new AwsProxyRequestBuilder().path("/example").method(HttpMethod.GET).build(),
                new MockLambdaContext());
        assertNotNull("Repsonse must not be null", response);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

}
