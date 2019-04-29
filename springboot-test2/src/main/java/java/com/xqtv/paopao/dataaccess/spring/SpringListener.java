package java.com.xqtv.paopao.dataaccess.spring;

import com.xqtv.paopao.dataaccess.service.cache.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import tv.xingqiu.common.log.XqtvLogger;

@Component
public class SpringListener implements ApplicationListener<ApplicationEvent> {

    @Value("${server_id}")
    private int serverId;

    @Value("${env_prod}")
    private boolean isProd;

    private static boolean isStarted;

    @Autowired
    protected RedisService redisService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("spring 起来了");
        if (event instanceof ContextRefreshedEvent && !isStarted) {
            XqtvLogger.error(getClass(), "初始化！！！");
            System.out.println("server id: " + serverId);
            isStarted = true;
        }
    }

}
