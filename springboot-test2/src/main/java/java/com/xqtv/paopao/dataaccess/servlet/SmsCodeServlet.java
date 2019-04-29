package java.com.xqtv.paopao.dataaccess.servlet;

import com.alibaba.fastjson.JSONObject;
import com.xqtv.paopao.dataaccess.service.impl.SmsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tv.xingqiu.common.enums.ESmsType;
import tv.xingqiu.common.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manag_sms_code")
public class SmsCodeServlet extends ManagementServlet {

    @Autowired
    private SmsServiceImpl smsService;

    @Override
    protected String processPost(JSONObject json, HttpServletRequest request) throws Exception {
        String nationCode = "86";
        String mobile = json.getString("mobile");
        smsService.sendQQSms(ESmsType.LOGIN, mobile, CommonUtil.getIpAddress(request), nationCode);
        return getSuccess();

    }

}
