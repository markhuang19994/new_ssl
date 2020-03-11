package example.ssl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.Map
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@SpringBootApplication(scanBasePackages = ["example", "common", "com"])
class SslApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SslApplication.class, args);
        System.out.println(System.lineSeparator());
        System.out.println(getHandlersPrintString(ctx));

        CountDownLatch cdl = new CountDownLatch(1)
        AtomicBoolean isShutdown = new AtomicBoolean(false)
        final ExecutorService es = Executors.newScheduledThreadPool(1, {
            Thread t = new Thread(it)
            t.setDaemon(true)
            t
        })
        es.scheduleAtFixedRate({
            if (isShutdown.get()) return
            def gitDir = '/usr/local/docker/dummy_api' as File
            def lastHashTxt = '/usr/local/docker/lh.txt' as File

            ['git', 'fetch', '--progress', 'origin', 'master'].execute(null, gitDir)
            def hash = ['git', 'rev-parse', 'origin/master^{commit}'].execute(null, gitDir).text

            if (!lastHashTxt.exists()) {
                lastHashTxt.withWriter {
                    lastHashTxt.write(hash)
                }
                return
            }

            if (lastHashTxt.text != hash) {
                lastHashTxt.withWriter {
                    lastHashTxt.write(hash)
                }

                isShutdown.set(true)
                cdl.countDown()
            }
        }, 0, 5, TimeUnit.SECONDS)
        cdl.await()
        SpringApplication.exit(ctx)
        Thread t = new Thread({
            def ips = ['./start.sh'].execute(null, '/usr/local/project/new_ssl' as File)
                    .inputStream

            int tmp;
            while ((tmp = ips.read()) != -1) {
                print((char) tmp)
            }
        })
    }

    static String getHandlersPrintString(ApplicationContext ctx) {
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
        return sb.toString().replace("#underline#", '-' * maxLength);
    }

}
