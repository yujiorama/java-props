package example.props;

import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class Props implements RequestHandler<Object, String> {
    public String props() {
        return Collections.list(System.getProperties().keys()).stream()
            .filter(k -> k.toString().startsWith("java."))
            .map(k ->
                String.join("", new String[]{
                    k.toString(), "=", System.getProperty(k.toString()).toString()}))
            .sorted()
            .collect(Collectors.joining("|"));
    }

    // handler=example.props.Props:handleRequest
    @Override
    public String handleRequest(Object str, Context context) {
        String data = props();
        LambdaLogger logger = context.getLogger();
        logger.log(data);
        return data;
    }

    /**
     * This function listens at endpoint "/api/PropsFunction". Two ways to invoke it using "curl" command in bash:
     * 1. curl {your host}/api/PropsFunction
     */
    @FunctionName("PropsFunction")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {
        String data = props();
        context.getLogger().info(data);
        return request.createResponseBuilder(HttpStatus.OK).body(data).build();
    }

    public static void main(String[] args) {
        System.out.println(new Props().props());
    }
}
