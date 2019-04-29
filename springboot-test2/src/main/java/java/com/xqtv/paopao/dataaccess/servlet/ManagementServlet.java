package java.com.xqtv.paopao.dataaccess.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xqtv.paopao.dataaccess.service.cache.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.xingqiu.common.constants.Constants;
import tv.xingqiu.common.constants.ErrorMessage;
import tv.xingqiu.common.exception.DataAccessException;
import tv.xingqiu.common.util.CommonUtil;
import tv.xingqiu.common.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.com.xqtv.paopao.dataaccess.log.BCLogger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//import com.xqtv.paopao.dataaccess.dto.PageResult;
//import com.xqtv.paopao.dataaccess.service.cache.ResourceService;
//import tv.xingqiu.common.util.CommonUtil;

public abstract class ManagementServlet {

//    @Autowired
//    protected ResourceService resourceService;

    @Autowired
    protected RedisService redisService;

    @Value("${env_prod}")
    private boolean online;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateIP(request);
            JSONObject json = getJson(request);
            return processGet(json, request);
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

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            validateIP(request);
            JSONObject json = getJson(request);
            try {
                Long handlerId = json.getLong("handler_id");
                if (handlerId == null) {
                    handlerId = 0L;
                }
                BCLogger.error(getClass(), handlerId + " do post: " + json.toJSONString());
            } catch (Exception e) {

            }
            return processPost(json, request);
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

    private static final Set<String> VALID_IP_SET = new HashSet<>(Arrays.asList("118.31.40.142", "47.96.1.255"));
    protected void validateIP(HttpServletRequest request) throws DataAccessException {
        if (online) {
            String ipAddress = CommonUtil.getIpAddress(request);
            if (!VALID_IP_SET.contains(ipAddress)) {
                BCLogger.error(getClass(), "remote ip: " + ipAddress);
                throw new DataAccessException(ErrorMessage.PARAM_ERROR);
            }
        }
    }

    protected JSONObject getJson(HttpServletRequest request) throws Exception {
        String params = request.getParameter("params");
        if (StringUtils.isBlank(params)) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }
        String json = CommonUtil.decrypt(Constants.AES_KEY, params);
        if (StringUtils.isBlank(json)) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }
        return JSONObject.parseObject(json);
    }

    protected String getSuccess(JSON obj) {
        JSONObject json = new JSONObject();
        if (obj != null) {
            json.put("code", 200);
            json.put("value", obj);
        } else {
            json.put("code", 201);
        }
        json.put("message", "");
        json.put("time", DateUtil.getCurrentTime());
        return json.toJSONString();
    }

    // 在返回字符传中加入成功信息
    protected String getSuccess() {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", "");
        json.put("time", DateUtil.getCurrentTime());
        return json.toJSONString();
    }

    protected String getSuccess(String msg) {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("message", msg);
        json.put("time", DateUtil.getCurrentTime());
        return json.toJSONString();
    }

    protected String getError(ErrorMessage error) {
        JSONObject json = new JSONObject();
        if (error == ErrorMessage.USER_TOKEN_ERROR) {
            json.put("code", 401);
        } else {
            json.put("code", 400);
        }

        json.put("message", error.getMessage());
        json.put("time", DateUtil.getCurrentTime());
        json.put("errorcode", error.getCode());
        return json.toJSONString();
    }

    /*
    无用代码 注释
    protected String getSuccess(PageResult list) {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("time", DateUtil.getCurrentTime());
        json.put("value", list.getArray());
        json.put("page", list.getPage());
        json.put("count", list.getSize());
        json.put("total_count", list.getTotalCount());
        json.put("total_page", list.getTotalPage());
        json.put("message", "");
        return json.toJSONString();
    }

    protected String getSuccess(PageResult list, String key, Object value) {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("time", DateUtil.getCurrentTime());
        json.put("value", list.getArray());
        json.put("page", list.getPage());
        json.put("count", list.getSize());
        json.put("total_count", list.getTotalCount());
        json.put("total_page", list.getTotalPage());
        json.put("message", "");
        json.put(key, value);
        return json.toJSONString();
    }*/

    protected String getSuccess(String key, Object value) {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("time", DateUtil.getCurrentTime());
        json.put("message", "");
        json.put(key, value);
        return json.toJSONString();
    }

    // 将错位文本变成json字符串
    protected String getError(String message) {
        JSONObject json = new JSONObject();
        json.put("code", 400);

        json.put("message", message);
        json.put("time", DateUtil.getCurrentTime());
        json.put("errorcode", 0);
        return json.toJSONString();
    }

    // 通过params获得json对象
    protected JSONObject getJson(String params) throws Exception {
        // 判断params是否空
        if (StringUtils.isBlank(params)) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }
        // 根据key将params解密成正常json字符串
        String json = CommonUtil.decrypt(Constants.AES_KEY, params);
        if (StringUtils.isBlank(json)) {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }
        // 讲json字符串转换成json对象
        return JSONObject.parseObject(json);
    }

    protected String processGet(JSONObject json, HttpServletRequest request) throws Exception {
        return getError(ErrorMessage.NOT_SUPPORT);
        // return processPost(params, request, response);
    }

    protected String processPost(JSONObject json, HttpServletRequest request) throws Exception {
        return getError(ErrorMessage.NOT_SUPPORT);
        // return processGet(params, request, response);
    }

}
