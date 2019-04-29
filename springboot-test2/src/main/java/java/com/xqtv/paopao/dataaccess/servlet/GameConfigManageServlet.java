package java.com.xqtv.paopao.dataaccess.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xqtv.paopao.dataaccess.dto.GameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tv.xingqiu.common.constants.ErrorMessage;
import tv.xingqiu.common.enums.EConfigType;
import tv.xingqiu.common.exception.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import java.com.xqtv.paopao.dataaccess.service.impl.GameManageServiceImpl;
import java.util.List;

//import com.nicia.bocai.dataaccess.dto.GameConfig;
//import com.nicia.bocai.dataaccess.service.impl.GameManageServiceImpl;

@Controller
@RequestMapping("/gameconfig")
public class GameConfigManageServlet extends ManagementServlet {

    @Autowired
    private GameManageServiceImpl gameManageService;

    @Override
    protected String processGet(JSONObject json, HttpServletRequest request) throws Exception {
        if (!json.containsKey("column")) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }
        JSONObject columns = json.getJSONObject("column");
        List<GameConfig> list;
        if ("game_config".equals(json.getString("table")) && "select".equals(json.getString("method"))) {
            if (columns.containsKey("type")) {
                int type = columns.getInteger("type");
                list = gameManageService.getGameConfigs(type);
            } else {
                list = gameManageService.getGameConfigs();
            }
            JSONArray array = new JSONArray();
            for (GameConfig data : list) {
                EConfigType config = EConfigType.getByKey(data.getKey());
                if (config != null) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", config.getDesc());
                    obj.put("key", data.getKey());
                    obj.put("value", data.getValue());
                    obj.put("split_info", data.getSplitInfo());
                    obj.put("column_info", data.getColumnInfo());
                    obj.put("type", data.getType());
                    obj.put("package", data.getPackageType());
                    array.add(obj);
                } else {
                    JSONObject obj = new JSONObject();
                    obj.put("name", data.getKey());
                    obj.put("key", data.getKey());
                    obj.put("value", data.getValue());
                    obj.put("split_info", data.getSplitInfo());
                    obj.put("column_info", data.getColumnInfo());
                    obj.put("type", data.getType());
                    obj.put("package", data.getPackageType());
                    array.add(obj);
                }
            }

            return getSuccess(array);

        }
        throw new DataAccessException(ErrorMessage.PARAM_ERROR);
    }

    @Override
    protected String processPost(JSONObject json, HttpServletRequest request) throws Exception {
        if (!json.containsKey("column")) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }

        JSONObject columns = json.getJSONObject("column");

        if ("game_config".equals(json.getString("table")) && "update".equals(json.getString("method"))) {
            if (columns.containsKey("key") && columns.containsKey("value")) {
                String key = columns.getString("key");
                String value = columns.getString("value");
                int packageType = 0;
                if (columns.containsKey("package_type")) {
                    packageType = columns.getIntValue("package_type");
                }
                gameManageService.saveGameConfig(key, value, packageType);
                return getSuccess();
            }
        }

        throw new DataAccessException(ErrorMessage.PARAM_ERROR);
    }
}
