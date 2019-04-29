package java.com.xqtv.paopao.dataaccess.spring;

import com.xqtv.paopao.dataaccess.service.cache.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/3/14 0014.
 */
@Component("perDayJob")
@Transactional
public class PerDayJobService {

    @Value("${server_id}")
    private int serverId;

    @Value("${env_prod}")
    private boolean envProd;

    @Autowired
    protected RedisService redisService;

    public void doJob() throws Exception {

    }

}
