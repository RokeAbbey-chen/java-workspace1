package java.com.xqtv.paopao.dataaccess.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.com.xqtv.paopao.dataaccess.service.impl.GameManageServiceImpl;

@Component("perHourJob")
@Transactional
public class PerHourJobService {

    @Autowired
    private GameManageServiceImpl gameManageService;

    @Value("${server_id}")
    private int serverId;

    public void doJob() {
        if (serverId == 10001) {

        }
    }

}
