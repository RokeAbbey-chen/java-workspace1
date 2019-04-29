package java.com.xqtv.paopao.dataaccess.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.xqtv.paopao.dataaccess.dao.UserReasonDao;
//import com.xqtv.paopao.dataaccess.domain.UserReasonPo;
//import com.xqtv.paopao.dataaccess.dto.PageResult;

@Service
@Transactional
public class UserReasonManageServiceImpl {

    /* 无用代码 注释
    @Autowired
    private UserReasonDao userReasonDao;

    @Transactional(readOnly = true)
    public PageResult selectUserReport(JSONObject condition, int page, int count) {

        UserReasonPo po = ManageUtil.covertFromJson(condition, UserReasonPo.class);
        Page<UserReasonPo> poList;
        if (po != null) {
            Specification<UserReasonPo> spec = ManageUtil.createSpecification(po, null, page, count);
            poList = userReasonDao.findAll(spec, new PageRequest(page > 0 ? page - 1 : 0, count, Direction.DESC, "id"));
        } else {
            poList = userReasonDao.findAll(new PageRequest(page > 0 ? page - 1 : 0, count, Direction.DESC, "id"));
        }
        JSONArray array = ManageUtil.covertToArray(poList.getContent());

        return new PageResult(array, page, count, poList.getTotalElements(), poList.getTotalPages());
    }

    public boolean read(int id) throws DataAccessException {
        UserReasonPo po = userReasonDao.findOne(id);
        if (po == null) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }

        po.setStatus(1);
        userReasonDao.save(po);
        return true;
    }
    */

}
