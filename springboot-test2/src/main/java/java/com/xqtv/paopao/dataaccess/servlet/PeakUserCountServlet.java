package java.com.xqtv.paopao.dataaccess.servlet;

import com.alibaba.fastjson.JSONObject;
import com.xqtv.paopao.dataaccess.mongo.dao.SeverUserCountLogMongoRepository;
import com.xqtv.paopao.dataaccess.mongo.domain.SeverUserCountLogMo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tv.xingqiu.common.enums.EServer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("peak_user_count")
public class PeakUserCountServlet extends ManagementServlet{
    @Autowired
    SeverUserCountLogMongoRepository severUserCountLogMongoRepository;

    @Override
    protected String processGet(JSONObject json, HttpServletRequest request) throws Exception {
        JSONObject column;
        if (json.containsKey("column")) {
            column = json.getJSONObject("column");
        }
        else { column = json; }

        Integer serverId = json.getInteger("server_id");
        Integer gameId = json.getInteger("game_id");
        Long startTime = json.getLong("start_time");
        Long endTime = json.getLong("end_time");
        List<SeverUserCountLogMo> result = severUserCountLogMongoRepository.getServerCountLog(serverId, gameId, startTime, endTime);
        JSONObject r = new JSONObject();
        r.put("result", result);
        return getSuccess(r);
//        return super.processGet(json, request);

    }

    private List<Integer> getEServerCode(){
        List<Integer> result = new ArrayList<>();
        Field[] fields = EServer.class.getFields();
        try {
            Method getCode = EServer.class.getMethod("getCode");
            for (Field f : fields){
                f.setAccessible(true);
                if (f.isEnumConstant()) {
                    result.add((Integer) getCode.invoke(f.get(EServer.class)));
                }
            }
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
