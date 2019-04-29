package paopao.util;

public class Constants {
    // 参数解密
    public static final String AES_TYPE = "AES/ECB/PKCS5Padding";
    public static final String AES_KEY = "paopao46c87p85ic";
    public static final String TOKEN_AES_KEY = "paopao46c85853s1";

    // 阿里云

    public static String ALIYUN_ACCESS_KEY = "LTAI9eRCDdYtpYZp";
    public static String ALIYUN_SECRET_KEY = "XVMaI1H4OvUTQavp5fhypV28exzxrJ";
    public static String MNS_ADDR = "http://1960213045151106.mns.cn-hangzhou-internal.aliyuncs.com/";

    public static String ALIYUN_LOG_HOST = "cn-hangzhou-staging-intranet.sls.aliyuncs.com";
    public static String ALIYUN_SEARCH_APP_TEST = "xqtv_search_test";
    public static String ALIYUN_FOLLOW_LIST_APP = "xqtv_follow_list";
    public static String ALIYUN_FOLLOW_LIST_APP_TEST = "xqtv_follow_list_test";
    public static String ALIYUN_GIFT_LIST_APP = "xqtv_gift_list";
    public static String ALIYUN_GIFT_LIST_APP_TEST = "xqtv_gift_list_test";
    public static String ALIYUN_OPENSEARCH_HOST = "http://opensearch-cn-hangzhou.aliyuncs.com";
    
    // 七牛直播 & 图片 &鉴黄
    public static final String QINIU_ACCESS = "4OgXtlFltHNWD85tRJYOZEDJ2AVdB6KyJdwUMmaI";
    public static final String QINIU_SECRET = "CqEiXE4xJ8jd0WTpm7P2r62Hg9keMSO8vwMTfO0F";
    public static final String QINIU_IMAGE_PATH = "paopao";

    // 微信支付
    public static final String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    // IOS支付
    public static final String APPLY_PAY_URL = "https://buy.itunes.apple.com/verifyReceipt";
    public static final String APPLY_PAY_TEST_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

    public static final String DEFAULT_ICON = "default_avstar.png";

    // 应用宝
    public static final String YYB_APP_KEY = "6Mv1k6uOAXGcU74c";
    public static final String YYB_LIVE_URL = "http://live.xingqiu.tv/live/yyb_show/anchor_id/";

    public static final String MIDAS_GET_BALANCE = "https://ysdk.qq.com/mpay/get_balance_m";
    public static final String MIDAS_PAY = "https://ysdk.qq.com/mpay/pay_m";
    public static final String MIDAS_APPKEY = "6667BC7Xu0gs5VLStDtOs8kfcPSBgHlz";
    public static final String MIDAS_PAY_SIG = "/mpay/pay_m";
    public static final String MIDAS_GET_SIG = "/mpay/get_balance_m";
    /* 闭环 */
    public static final String MIDAS_H5_KEY = "4dbc7dfb9979b8cce83bafadf5ab0fea";

    // 指尖互动渠道
    public static final String FINGERMOB_URL = "http://api.fingermob.com/api/activate";
    public static final String FINGERMOB_APPKEY = "7413A570B6A06FBCC119287D6A099F6E";

    // 取部分信息列表时分页配置

    // public static final int MESSAGE_PAGE_SIZE = 15;
    // public static final int FRIEND_PAGE_SIZE = 30;
    // public static final int FRIEND_BLACK_SIZE = 2;
    // public static final int FOLLOW_RECOMMEND_PAGE_SIZE = 12;
    // public static final int FANS_PAGE_SIZE = 15;
    // public static final int GIFT_PAGE_SIZE = 15;
    // public static final int LIVE_RECORDS_PAGE_SIZE = 15;
    // public static final int PRIZE_RECORDS_PAGE_SIZE = 100;
    // public static final int PRIZE_HISTORY_PAGE_SIZE = 20;
    // public static final int SEARCH_PAGE_SIZE = 15;
    // public static final int ALBUM_PAGE_SIZE = 8;
    // public static final int YELLOW_TEST_SIZE = 49;
    // public static final int PAYOUT_PAGE_SIZE = 30;
    public static final int ORDER_PAGE_SIZE = 15;
    public static final int USER_GAME_PAGE_SIZE = 60;
    
    // 固定数量常量
    public static final int UPDATE_USER_HOUR = 2;
    public static final int PUSH_INTERVAL_SECONDS = 30 * 60;

    // 转换比例
    public static final float CHARM_TO_RMB = 0.004f; // 魅力->现金
    public static final float HIGH_CHARM_TO_RMB = 0.006f; // high魅力->现金
    public static final int EXP_TO_GOLD = 2; // 经验->金币

    // 信息
    public static final long SYSTEM_MESSAGE_ID = 100000L;
    public static final long STRANGER_MESSAGE_ID = 200000L;
    public static final String SMS_MESSAGE = "验证码：$1，请在10分钟内操作，勿告知他人。【星球部落】";
    public static final String WITHDRAW_LIMIT_MESSAGE = "恭喜！您已累计登录%s日,获得提现额度%s元，红包额度满20元即可提现哦！";

    public static final String PAYOUT_PAYING = "您的提现申请已通过，等待转账。";
    public static final String PAYOUT_REGECT = "您的提现申请未通过，请咨询客服。";
    public static final String PAYOUT_PAYED = "您申请的提现已转账到您的支付宝。";
    public static final String PAYOUT_WX_PAYED = "您申请的提现已发放至您的微信零钱中。";

    public static final String SCORE_MESSAGE = "“$1”，您的直播（$2-$3）经运营人员综合评审后，评分$4。评价内容：$5";
    public static final String SHARE_LOGIN_MESSAGE = "在您的盛情邀请下，您的好友“$1”登录星球部落啦！活动期间该好友充值，您可享有1%的邀请奖励哦。";
    public static final String SHARE_ORDER_MESSAGE = "恭喜，获得好友“$1”邀请奖励$2元。请在收益中查看！";

    public static final String SOCIATY_PAYOUT_MESSAGE = "您的公会管理已经申请提现%s元，根据分成比例%s%%，您可获得%s元。";
    public static final String SOCIATY_PAYOUT_APPROVE_MESSAGE = "您的公会管理已经成功提现%s元，根据分成比例%s%%，您可获得%s元，您可以向他索要收益哦。";
    public static final String SOCIATY_PAYOUT_REJECT_MESSAGE = "您的公会管理申请提现%s元被拒绝，详情请咨询公会管理。";
    public static final String LIVE_TOO_LONG_MESSAGE = "亲！你一天累计直播时长已超过限定的12小时了，请尽快停播休息！【星球官方提醒】";
    public static final String LIVE_GAME_TOO_LONG_MESSAGE = "亲！该游戏今日开播时长已达上限，请下线休息或适当进行其他游戏娱乐。【星球官方提醒】";
    public static final String LIVE_TOO_LONG_MANAGER_MESSAGE = "有主播累计直播时长已超过限定的12小时了，主播：%s，运营负责人：%s【星球部落】";
    public static final String LIVE_GAME_TOO_LONG_MANAGER_MESSAGE = "有主播累计游戏直播时长已达上限，主播：%s，运营负责人：%s【星球部落】";

    // 成就

    public static final String ACHIEVEMENT_MESSAGE = "恭喜您达成了【%s】成就，获得%s游戏币%s%s。 ";

    // exp
    public static final String LEVEL_UP_MESSAGE = "恭喜！您的游戏等级达到%s级，获得奖励%s游戏币和%s个%s。";
    
    public static final String MOBILE = "13758291169";
    
    public static final int QQ_SMS_MESSAGE = 65717;
    
    public static final int QQ_SMS_MONITOR = 145506;

    public static final int QQ_SMS_COMMON_TEMPLATE = 152368;// 腾讯短信服务通用模板， 只传一个参数

    public static final String SMS_SIGN = "泡泡乐园";

    public static final int LIVE_HOUR_LIMIET = 12;

    /* cache key */
    public static final String USER_INFO_KEY = "userInfo";
    public static final String USER_INFO_HASH_KEY = "user";
    public static final String HASH_USER_INFO_KEY = "hashUser";

    public static final String MANAGEMENT_USER_KEY = "manageUser";

    public static final String YELLOW_PICTURE = "yellowPicture";
    public static final String YELLOW_TEXT = "yellowText";

    public static final String GIFT_PIECE = "piece";

    public static final int MEMCACHE_TIMEOUT = 60000;

    // lucky gift
    public static final long DEFAULUT_JACKPOT_POOL = 1000000;
    public static final int JACKPOT_MAX_MUL = 500;

    // config
    public static final String CONFIG_NEW = "_new";
    public static final String CONFIG_HIGH = "_high";

    // 抓娃娃输赢
    public static final String COST_WIN = "cost_win";

    // anchor task
    public static final String CURRENT_LEVEL = "currentLevel";
    public static final String REWARD_NUM = "rewardNum";

    // 邀请一个人提升额度值
    public static final double INVITE_WITHDRAW_LIMIT = 3D;
    public static final String INVITE_WITHDRAW_LIMIT_MESSAGE = "您的好友$name已通过你分享的二维码进驻星球部落。您的提现额度增加3元，赶紧和小伙伴互抢红包吧!";
    public static final String INVITE_WITHDRAW_LIMIT_MESSAGE_QQ = "您的好友$name已在星球部落绑定手机号了，给您增加了提现额度3元。";
    public static final String INVITE_WITHDRAW_LIMIT_MESSAGE_QQ1 = "您的好友$name已通过你分享的二维码进驻星球部落，等TA绑定手机后就可以给您增加提现额度3元啦！";
    // 每日被使用道具限制
    public static final int BEUSED_ITEM_LIMIT = 30;
    // 匹配
    public static final int MATCH_FAIL_LIMIT = 10;

    // 推送
    public static final int PUSH_SIZE = 500;

    // redis pipeline page
    public static final int PIPELINE_PAGE_SIZE = 1000;

    public static final String ZONENAME = "Games";

    public static final Integer PORT = 9871;

    public static final String SFSIP = "47.96.1.255";

    public static final String SERVER_IP = "192.168.1.1";//微信支付中传的ip 目测可以乱传

    public static final String WX_P_KEY = "HangZhouPaoMianWangLuo1666888888";

    public static final String DELIMITER = "_";

}
