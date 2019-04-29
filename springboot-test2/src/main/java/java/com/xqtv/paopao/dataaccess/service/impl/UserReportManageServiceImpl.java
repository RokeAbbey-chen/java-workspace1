package java.com.xqtv.paopao.dataaccess.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.xqtv.paopao.dataaccess.dao.UserReportDao;
//import com.xqtv.paopao.dataaccess.domain.UserReportPo;

@Service
@Transactional
public class UserReportManageServiceImpl {

    /*
    无用代码 注释
    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserReportDao userReportDao;

    @Autowired
    private RedisService redisService;

    // @Transactional(readOnly = true)
    // public PageResult selectUserReport(JSONObject condition, int page, int
    // count) {
    //
    // UserReportPo userReportPo = ManageUtil.covertFromJson(condition,
    // UserReportPo.class);
    // Specification<UserReportPo> spec =
    // ManageUtil.createSpecification(userReportPo, null, page, count);
    // Page<UserReportPo> poList = userReportDao.findAll(spec, new
    // PageRequest(page > 0 ? page - 1 : 0, count,
    // Direction.DESC, "id"));
    //
    // JSONArray array = ManageUtil.covertToArray(poList.getContent());
    // for (int i = 0; i < array.size(); i++) {
    // JSONObject json = array.getJSONObject(i);
    // UserInfo userInfo =
    // resourceService.getUserInfo(json.getLong("anchor_id"));
    // json.put("nick", userInfo.getName());
    // json.put("mobile", userInfo.getMobile());
    // json.put("icon", userInfo.getIconBig());
    // json.put("anchor_status", userInfo.getStatus());
    // }
    // return new PageResult(array, page, count, poList.getTotalElements(),
    // poList.getTotalPages());
    // }

    @Transactional(rollbackFor = Exception.class)
    public boolean banUser(long userId, int status, Long handlerId) throws DataAccessException {
        if (status < 1 || status > 2) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }

        List<UserReportPo> poList = userReportDao.findByAnchorId(userId);
        if (CollectionUtils.isEmpty(poList)) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }

        for (UserReportPo reportPo : poList) {
            reportPo.setStatus(1);
            reportPo.setHandlerId(handlerId);
        }
        userReportDao.save(poList);

        UserInfoPo po = userInfoDao.findOne(userId);
        if (po == null) {
            throw new DataAccessException(ErrorMessage.USER_NOT_EXITS);
        }
        po.setStatus(status);
        userInfoDao.save(po);

        UserInfo userInfo = redisService.getUserInfo(userId);
        if (userInfo != null) {
            userInfo.setStatus(status);
            redisService.cacheUserInfo(userInfo);
        }

        return true;
    }

    public boolean addMark(int id, String mark, Long handlerId) throws DataAccessException {
        UserReportPo po = userReportDao.findOne(id);
        if (po == null) {
            throw new DataAccessException(ErrorMessage.SERVER_ERROR);
        }
        po.setMark(mark);
        po.setHandlerId(handlerId);
        userReportDao.save(po);

        return true;
    }

    public boolean addMark(int id, String mark, Long handlerId, int status) throws DataAccessException {
        UserReportPo po = userReportDao.findOne(id);
        if (po == null) {
            throw new DataAccessException(ErrorMessage.SERVER_ERROR);
        }
        po.setMark(mark);
        po.setStatus(status);
        po.setHandlerId(handlerId);
        userReportDao.save(po);

        return true;
    }
    */
}
