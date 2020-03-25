package example.ssl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

@SpringBootApplication(scanBasePackages = ["example", "common", "com"])
class SslApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SslApplication.class)

    static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SslApplication.class, args)
        LOGGER.debug(System.lineSeparator())
        LOGGER.debug(getHandlersPrintString(ctx))

        def envProfile = System.properties['env.profile'] ?: 'sit'
        LOGGER.debug "envProfile>>>>>>>>>>>>> $envProfile"

        CountDownLatch cdl = new CountDownLatch(1)
        AtomicBoolean isShutdown = new AtomicBoolean(false)
        final ExecutorService es = Executors.newScheduledThreadPool(1, {
            Thread t = new Thread(it)
            t.setDaemon(true)
            t
        })
        es.scheduleAtFixedRate({
            try {
                if (isShutdown.get()) return
                if (isRestTime(20, 7, 'GMT+8')) {
                    LOGGER.debug('Is rest time...')
                    TimeUnit.HOURS.sleep(1)
                    return
                }
                def gitDir = '/usr/local/docker/dummy_api' as File
                def lastHashTxt = '/usr/local/docker/lh.txt' as File

                ['./update_git_repo.sh'].execute(null, '/usr/local/project/new_ssl' as File).waitFor()
                TimeUnit.SECONDS.sleep(2)
                def hash = ['git', 'rev-parse', 'origin/master^{commit}'].execute(null, gitDir).text

                if (!lastHashTxt.exists()) {
                    lastHashTxt.parentFile.mkdirs()
                    lastHashTxt.withWriter {
                        lastHashTxt.write(hash)
                    }
                    return
                }

                LOGGER.debug 'now hash:' + hash
                LOGGER.debug 'last hash:' + lastHashTxt.text
                LOGGER.debug ''
                LOGGER.debug ''

                if (lastHashTxt.text != hash) {
                    lastHashTxt.withWriter {
                        lastHashTxt.write(hash)
                    }

                    isShutdown.set(true)
                    cdl.countDown()
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e)
            }
        }, 0, envProfile == 'aws' ? 20 : 5, TimeUnit.SECONDS)
        cdl.await()
        SpringApplication.exit(ctx)
        Thread t = new Thread({
            try {
                for (int i = 0; i < 3; i++) {
                    def startCmd = ['./start.sh', 'notFirst']
                    if (envProfile == 'aws') {
                        startCmd << 'aws'
                    }
                    def p = startCmd.execute(null, '/usr/local/project/new_ssl' as File)
                    p.waitFor(180, TimeUnit.SECONDS)
                    if (p.exitValue() != 0) {
                        LOGGER.error '\n' + p.inputStream.text
                        LOGGER.error '\n' + p.errorStream.text
                        LOGGER.error('啟動失敗，3分鐘後重新嘗試...')
                        TimeUnit.MINUTES.sleep(3)
                        continue
                    }
                    break
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e)
            }
        }).start()
    }

    static String getHandlersPrintString(ApplicationContext ctx) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = ctx.getBean(RequestMappingHandlerMapping.class)
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods()
        StringBuilder sb = new StringBuilder()
        int maxLength = 0
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            String sKey = String.valueOf(entry.getKey())
            String sVal = String.valueOf(entry.getValue())
            sb.append(String.format("rule:%s\nmethod:%s\n#underline#\n", sKey, sVal))
            int length = sKey.length() + sVal.length()
            maxLength = Math.max(maxLength, length)
        }
        return sb.toString().replace("#underline#", '-' * maxLength)
    }

    static isRestTime(int restStartHour, int restEndHour, String gmtTime) {
        def tz = TimeZone.getTimeZone(gmtTime)
        def hour = LocalDateTime.now(tz.toZoneId()).hour
        if (restStartHour < restEndHour) {
            return hour >= restStartHour && hour <= restEndHour
        } else {
            return hour <= restStartHour && hour >= restEndHour
        }
    }

}
