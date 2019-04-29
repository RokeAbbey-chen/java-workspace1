package java.com.xqtv.paopao.dataaccess.service.impl;

import com.xqtv.paopao.dataaccess.dao.GameConfigDao;
import com.xqtv.paopao.dataaccess.dao.GamePkDao;
import com.xqtv.paopao.dataaccess.dao.UserSmsDao;
import com.xqtv.paopao.dataaccess.domain.GameConfigPo;
import com.xqtv.paopao.dataaccess.dto.GameConfig;
import com.xqtv.paopao.dataaccess.service.cache.StaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.xingqiu.common.enums.EStaticData;
import tv.xingqiu.common.util.BeanCopyUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GameManageServiceImpl {

    @Autowired
    private StaticDataService staticDataService;

    @Autowired
    private GameConfigDao gameConfigDao;

    @Autowired
    private UserSmsDao smsDao;

    @Autowired
    private GamePkDao gamePkDao;

    //
    public List<EStaticData> getReloadList() {
        List<EStaticData> list = new ArrayList<EStaticData>();
        for (EStaticData data : EStaticData.values()) {
            if (data.IsCanreload()) {
                list.add(data);
            }
        }
        return list;
    }

    //
    public boolean reload(String key) {
        EStaticData data = EStaticData.getByKey(key);
        if (data != null) {
            staticDataService.initStaticData(data);
            return true;
        }
        return false;
    }

    //
    public List<GameConfig> getGameConfigs(int type) {
        Iterable<GameConfigPo> poList = gameConfigDao.findByType(type);
        return BeanCopyUtils.copyList(poList, GameConfig.class);
    }

    public List<GameConfig> getGameConfigs() {

        Iterable<GameConfigPo> poList = gameConfigDao.findAll();
        return BeanCopyUtils.copyList(poList, GameConfig.class);
    }
    @Transactional
    public boolean saveGameConfig(String key, String value, int packageType) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        GameConfigPo po = gameConfigDao.findOne(key);

        if (po != null) {
            value = value == null ? "" : value;
            po.setValue(value);
            gameConfigDao.save(po);
            return true;
        }
        return false;
    }

/*
    无用代码 注释
    @Transactional
    public boolean saveGameConfig(String key, String value, int packageType) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        GameConfigPo po = gameConfigDao.findOne(new GameConfigPk(key, packageType));
        if (po != null) {
            value = value == null ? "" : value;
            po.setValue(value);
            gameConfigDao.save(po);
            return true;
        }
        return false;
    }

    // 获得验证码列表
    public PageResult getSmsList(JSONObject condition, int page, int count) {
        if (condition.isEmpty() || !condition.containsKey("mobile")) {
            Page<UserSmsPo> poList = smsDao.findAll(new PageRequest(page - 1, count, Direction.DESC, "createDate"));
            JSONArray array = ManageUtil.covertToArray(poList.getContent());
            return new PageResult(array, page, count, poList.getTotalElements(), poList.getTotalPages());
        } else {
            List<UserSmsPo> poList = new ArrayList<UserSmsPo>();
            String mobile = condition.getString("mobile");
            UserSmsPo po = smsDao.findOne(mobile);
            if (po != null) {
                poList.add(po);
            }
            JSONArray array = ManageUtil.covertToArray(poList);
            return new PageResult(array, 1, count, poList.size(), 1);
        }
    }

    // 查询游戏列表
    public PageResult getPkGameList(int page, int count) {
        Page<GamePkPo> poList = gamePkDao.findAll(new PageRequest(page - 1, count, Direction.ASC, "id"));
        JSONArray array = ManageUtil.covertToArray(poList.getContent());
        return new PageResult(array, page, count, poList.getTotalElements(), poList.getTotalPages());
    }

    // 根据id更新游戏信息
    @Transactional
    public boolean savePkGame(JSONObject columns) throws DataAccessException {
        Integer id = columns.getInteger("id");
        GamePkPo po = gamePkDao.findOne(id);
        if (po == null) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }

        GamePkPo gamePkPo = ManageUtil.covertFromJson(columns, GamePkPo.class);
        gamePkPo.setExtClass(po.getExtClass());
        gamePkPo.setCreateDate(po.getCreateDate());
        gamePkDao.save(gamePkPo);
        return true;
    }
*/
}
