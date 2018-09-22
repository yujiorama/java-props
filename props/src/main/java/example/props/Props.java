package example.props;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

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

    @Override
    public String handleRequest(Object str, Context context) {
        String data = props();
        LambdaLogger logger = context.getLogger();
        logger.log(data);
        return data;
    }

    public static void main(String[] args) {
        System.out.println(new Props().props());
    }
}
