package java.com.xqtv.paopao.dataaccess.service.impl;

import com.xqtv.paopao.dataaccess.dao.UserDao;
import com.xqtv.paopao.dataaccess.domain.UserPo;
import com.xqtv.paopao.dataaccess.dto.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.xingqiu.common.constants.ErrorMessage;
import tv.xingqiu.common.exception.DataAccessException;
import tv.xingqiu.common.util.BeanCopyUtils;
import tv.xingqiu.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserBanManageServiceImpl {

    @Autowired
    private UserDao userDao;

    public List<UserInfo> getUserInfoByIp(String ip) throws DataAccessException {
        if (StringUtils.isBlank(ip)) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }
        List<UserInfo> list = new ArrayList<>();
        List<UserPo> poList = userDao.findByIp(ip);
        if (CollectionUtils.isNotEmpty(poList)) {
            list = BeanCopyUtils.copyList(poList, UserInfo.class);
        }

        return list;
    }

    // public int relieveUserIp(String ip) {
    // List<UserPo> poList = userDao.findByIp(ip);
    // int i = 0;
    // if (CollectionUtils.isNotEmpty(poList)) {
    // System.out.println(poList.size());
    // for (UserPo po : poList) {
    // Long userId = po.getUserId();
    // UserInfo userInfo = resourceService.getUserInfo(userId);
    // if (userInfo != null && userInfo.getStatus() == EStatus.BAN.getCode()) {
    // userInfo.setStatus(EStatus.NORMAL.getCode());
    // resourceService.cacheUserInfo(userInfo);
    // i++;
    // if (i % 1000 == 0) {
    // System.out.println("done: " + i);
    // }
    // }
    // }
    // }
    // return i;
    // }

}
