package java.com.xqtv.paopao.dataaccess.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//import com.xqtv.paopao.dataaccess.dao.SociatyDao;

@Component("sociatyPayoutJob")
@Transactional
public class SociayPayoutJobService {

//    @Autowired
//    private SociatyDao dao;

    @Value("${server_id}")
    private int serverId;

    @Value("${env_prod}")
    private boolean envProd;

    public void doJob() throws Exception {
        if (serverId == 10001) {

        }
    }

}
