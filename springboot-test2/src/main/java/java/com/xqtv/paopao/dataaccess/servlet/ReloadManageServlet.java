package java.com.xqtv.paopao.dataaccess.servlet;

import com.alibaba.fastjson.JSONObject;
import com.xqtv.paopao.dataaccess.service.UserInfoService;
//import com.xqtv.paopao.dataaccess.service.impl.GameManageServiceImpl;
import com.xqtv.paopao.dataaccess.service.rmi.WebApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tv.xingqiu.common.constants.ErrorMessage;
import tv.xingqiu.common.exception.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import java.com.xqtv.paopao.dataaccess.service.impl.GameManageServiceImpl;

@Controller
@RequestMapping("/manag_reload")
public class ReloadManageServlet extends ManagementServlet {

    @Autowired
    private GameManageServiceImpl gameManageService;

    @Autowired
    private WebApi webApi;

    @Autowired
    private UserInfoService uis;

    @Override
    protected String processGet(JSONObject json, HttpServletRequest request) throws Exception {
        return processPost(json, request);
    }


    @Override
    protected String processPost(JSONObject json, HttpServletRequest request) throws Exception {
        if (json.containsKey("colunm")){
            json = json.getJSONObject("colunm");
        }
        if (json.containsKey("key")) {
            String key = json.getString("key");
            gameManageService.reload(key);
//            UserInfo ui = uis.getUserInfo(2785238L);
//            JSONObject obj = webApi.getConfigList(ui,0, 0);
//            System.out.println(obj.toString());
        }
        else {
            throw new DataAccessException(ErrorMessage.PARAM_ERROR);
        }
        return getSuccess();
    }

}
