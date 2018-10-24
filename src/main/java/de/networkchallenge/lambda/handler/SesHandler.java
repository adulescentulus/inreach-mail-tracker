package de.networkchallenge.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class SesHandler implements RequestStreamHandler {
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        String jsonString = convert(input, Charset.forName("utf-8"));
        context.getLogger().log(jsonString);
        SESEvent event = mapper.readValue(jsonString, SESEvent.class);
        String result = checkUserBinded(event, context);
        output.write(result.getBytes("utf-8"));
    }
    public String checkUserBinded(SESEvent event, Context c) {
        c.getLogger().log("Inside the function\n");
        if (event == null || event.getRecords() == null) {
            c.getLogger().log("Events is null\n");
            return "STOP_RULE";
        }
        c.getLogger().log("Need to load MessageId from S3: " + event.getRecords().get(0).getSES().getMail().getMessageId());
        return "CONTINUE";
    }

    public String convert(InputStream inputStream, Charset charset) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
