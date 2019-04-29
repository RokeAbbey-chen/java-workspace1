package java.com.xqtv.paopao.dataaccess.servlet;

import com.alibaba.fastjson.JSONObject;
import com.xqtv.paopao.dataaccess.dto.UserInfo;
import com.xqtv.paopao.dataaccess.service.UserInfoService;
import com.xqtv.paopao.dataaccess.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.xingqiu.common.constants.ErrorMessage;
import tv.xingqiu.common.exception.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.com.xqtv.paopao.dataaccess.log.BCLogger;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/manag_get_user_info")
public class UserInfoServlet extends ManagementServlet {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LoginServiceImpl loginService;

    // 刷新缓存
    @Override
    protected String processGet(JSONObject json, HttpServletRequest request) throws Exception {
        long userId = json.getLong("user_id");
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        return getSuccess(getData(userInfo));
    }

    private JSONObject getData(UserInfo userInfo) throws IllegalAccessException {

        JSONObject json = new JSONObject();
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("wxJSCode", "wx_js_code");
        if (userInfo != null) {
            getDataHelper(json, userInfo, nameMap);
        }
        json.toJSONString();
        return json;
    }

    public static final Pattern HUMP2_ = Pattern.compile("([A-Z])");

    private void getDataHelper(JSONObject json, Object obj, Map<String, String> nameMap) throws IllegalAccessException {
        Field[] fds = obj.getClass().getDeclaredFields();
        for (Field f : fds) {
            f.setAccessible(true);
            String oldName = f.getName();
            String newName = nameMap == null ? null : nameMap.get(f.getName());
            StringBuilder sb = new StringBuilder();
            if (newName == null) {
                Matcher m = HUMP2_.matcher(oldName);
                int head = 0;
                int end = 0;
                boolean flag = true;
                while (m.find()) {
                    flag = false;
                    end = m.end() - 1;
                    sb.append(oldName.substring(head, end));
                    sb.append('_');
                    sb.append((char) (oldName.charAt(end) + (-'A' + 'a')));
                    head = end + 1;
                }
                sb.append(oldName.substring(head, oldName.length()));
                newName = flag ? oldName : sb.toString();
            }
            json.put(newName, f.get(obj) + "");
        }
    }

    @RequestMapping("/change_user_status")
    @ResponseBody
    public String changeUserStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateIP(request);
            JSONObject json = getJson(request);
            Long userId = json.getLong("user_id");
            Integer status = json.getInteger("status");
            UserInfo userInfo = userInfoService.getUserInfo(userId);
            userInfo.setStatus(status);
            loginService.saveUserInfo(userInfo, 0);
            return getSuccess(getData(userInfo));
        } catch (DataAccessException e) {
            if (e.getError() != null) {
                BCLogger.error(getClass(), e.getError().getMessage() + ", request: " + request.getParameter("params"));
                return getError(e.getError());
            } else {
                BCLogger.error(getClass(), e.getMessage() + ", request: " + request.getParameter("params"));
                return getError(e.getMessage());
            }
        } catch (Exception e) {
            BCLogger.error(getClass(), e.getLocalizedMessage() + ", request: " + request.getParameter("params"), e);
            return getError(ErrorMessage.SERVER_ERROR);
        }
    }

    // public String doGet(HttpServletRequest request, HttpServletResponse response) {
    // try {
    // validateIP(request);
    // JSONObject json = getJson(request);
    // return processGet(json, request);
    // } catch (DataAccessException e) {
    // if (e.getError() != null) {
    // BCLogger.error(getClass(), e.getError().getMessage() + ", request: " + request.getParameter("params"));
    // return getError(e.getError());
    // } else {
    // BCLogger.error(getClass(), e.getMessage() + ", request: " + request.getParameter("params"));
    // return getError(e.getMessage());
    // }
    // } catch (Exception e) {
    // BCLogger.error(getClass(), e.getLocalizedMessage() + ", request: " + request.getParameter("params"), e);
    // return getError(ErrorMessage.SERVER_ERROR);
    // }
    // }
}
