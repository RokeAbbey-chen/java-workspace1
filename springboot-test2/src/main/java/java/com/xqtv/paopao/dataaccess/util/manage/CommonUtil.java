package java.com.xqtv.paopao.dataaccess.util.manage;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import tv.xingqiu.common.constants.Constants;
import tv.xingqiu.common.constants.ErrorMessage;
import tv.xingqiu.common.exception.DataAccessException;
import tv.xingqiu.common.log.XqtvLogger;
import tv.xingqiu.common.util.DateUtil;
import tv.xingqiu.common.util.HttpsServiceUtil;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 */
public class CommonUtil {

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };
	private static final String HEX_DIGITS = "0123456789abcdef";

	public static String encrypt(String keyStr, String plainText) {
		if (StringUtils.isBlank(keyStr) || StringUtils.isBlank(plainText)) {
			return null;
		}
		byte[] encrypt = null;
		try {
			SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(Constants.AES_TYPE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encoded = Base64.encodeBase64(plainText.getBytes());
			encrypt = cipher.doFinal(encoded);
		} catch (Exception e) {
			XqtvLogger.error(CommonUtil.class, e.getLocalizedMessage());
			encrypt = new byte[0];
		}
		return new String(Base64.encodeBase64(encrypt));
	}

	public static String decrypt(String keyStr, String encryptData) throws Exception {
		if (StringUtils.isBlank(keyStr) || StringUtils.isBlank(encryptData)) {
			throw new DataAccessException(ErrorMessage.PARAM_ERROR);
		}
		byte[] decrypt = null;
		try {
			SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(Constants.AES_TYPE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
		} catch (Exception e) {
			XqtvLogger.error(CommonUtil.class, e.getLocalizedMessage());
			throw new DataAccessException(ErrorMessage.PARAM_ERROR);
		}
		return new String(Base64.decodeBase64(decrypt));
	}

	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public final static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	public static String getCityByIp(String ip) {
		String city = "";
		if (ip != null && !"".equals(ip)) {
			String responseContent = null;
			try {
				HttpsServiceUtil hutil = HttpsServiceUtil.getInstance();
				String url = "https://dm-81.data.aliyun.com//rest/160601/ip/getIpInfo.json";
				Map<String, String> param = new HashMap<String, String>();
				param.put("ip", ip);
				responseContent = hutil.doGetForString(url, param);
			} catch (Exception e) {
				XqtvLogger.error(CommonUtil.class, "get city fail!", e);
			}
			if (responseContent != null) {
				JSONObject info = JSONObject.parseObject(responseContent);
				if (info != null && info.containsKey("data")) {
					info = info.getJSONObject("data");
					if (info != null && info.containsKey("city")) {
						city = info.getString("city");
					}
				}
			}
		}

		return city;
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return MD5Encode(uuid);
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String sha1(String arg) {
		try {
			return computeSha1OfByteArray(arg.getBytes(("UTF-8")));
		} catch (UnsupportedEncodingException ex) {
			throw new UnsupportedOperationException(ex);
		}
	}

	private static String computeSha1OfByteArray(byte[] arg) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(arg);
			byte[] res = md.digest();
			return toHexString(res);
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException(ex);
		}
	}

	private static String toHexString(byte[] v) {
		StringBuilder sb = new StringBuilder(v.length * 2);
		for (int i = 0; i < v.length; i++) {
			int b = v[i] & 0xFF;
			sb.append(HEX_DIGITS.charAt(b >>> 4)).append(HEX_DIGITS.charAt(b & 0xF));
		}
		return sb.toString();
	}

	public static String getRmbStr(Double cash) {
		BigDecimal decimal = new BigDecimal(cash);
		return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static int getPercent(Float percent) {
		BigDecimal decimal = new BigDecimal(percent);
		BigDecimal decimal2 = new BigDecimal(100);
		return decimal2.multiply(decimal).setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
	}

	public static Float getPercentFloat(Float percent) {
		BigDecimal decimal = new BigDecimal(percent);
		BigDecimal decimal2 = new BigDecimal(100);
		return decimal2.multiply(decimal).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public static byte[] hmasha1(String text, String key) throws Exception {
		byte[] data = key.getBytes("UTF-8");
		SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
		return mac.doFinal(text.getBytes("UTF-8"));
	}

	public static int getCeil(Float value) {
		Double ceil = Math.ceil(value);
		return ceil.intValue();
	}

	public static String getFilterWordString(String str) {
		String result = "";
		try {
			StringBuffer output = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (isChinese(c)) {
					output.append("\\u" + Integer.toString(c, 16));
				} else {
					output.append(c);
				}
			}
			result = "(?i:" + output + ")";
		} catch (Exception e) {

		}
		return result;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public static String SHA1(String decript) {
		if (null == decript || 0 == decript.length()) {
			return null;
		}
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(decript.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] buf = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decript;
	}

	public static boolean isSuperPaoPao(Long endTime) {
		if (endTime != null && endTime > 0) {
			long currentTime = DateUtil.getTodayEndTime();
			return endTime > currentTime;
		}
		return false;
	}

	public static String getMatchRoomName(long userId, long matchId) {
		return userId > matchId ? matchId + "_" + userId : userId + "_" + matchId;
	}

	public static String getRobotMatchRoomName(long userId, long matchId) {
		return userId + "_" + matchId;
	}

	public static String getSharRoomName(long userId, int gameId) {
		return "Share" + gameId + "_" + userId;
	}

	public static String getGroupRoomName(long round) {
		return "Group" + "_" + round;
	}

	/**
	 * JSON格式判断
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isJson(String content) {
		try {
			JSONObject.parseObject(content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断是否数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

}
