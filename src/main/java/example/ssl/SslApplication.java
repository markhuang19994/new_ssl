package example.ssl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootApplication(scanBasePackages = {"example", "common", "com"})
public class SslApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SslApplication.class, args);
        System.out.println(System.lineSeparator());
        System.out.println(getHandlersPrintString(ctx));
    }

    public static String getHandlersPrintString(ApplicationContext ctx) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = ctx.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        StringBuilder sb = new StringBuilder();
        int maxLength = 0;
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            String sKey = String.valueOf(entry.getKey());
            String sVal = String.valueOf(entry.getValue());
            sb.append(String.format("rule:%s\nmethod:%s\n#underline#\n", sKey, sVal));
            int length = sKey.length() + sVal.length();
            maxLength = Math.max(maxLength, length);
        }
        return sb.toString().replace(
                "#underline#",
                Arrays.stream(new String[maxLength])
                        .map(s -> "")
                        .collect(Collectors.joining("-"))
        );
    }

}
