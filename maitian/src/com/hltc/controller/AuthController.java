package com.hltc.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.GetFederationTokenRequest;
import com.aliyuncs.sts.model.v20150401.GetFederationTokenResponse;
import com.google.gson.JsonArray;
import com.hltc.common.ErrorCode;
import com.hltc.common.GlobalConstant;
import com.hltc.common.Result;
import com.hltc.entity.FederationToken;
import com.hltc.entity.Token;
import com.hltc.entity.User;
import com.hltc.exception.StsException;
import com.hltc.service.IUserService;
import com.hltc.util.BeanUtil;
import com.hltc.util.LogUtil;

import static com.hltc.util.SecurityUtil.*;

/**
 * 授权模块控制器
 */
@Controller
@Scope("prototype")
@RequestMapping(value="/v1/auth")
public class AuthController {
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    public static final String STS_POP_API_VERSION = "2015-04-01";
    public static final String STS_API_VERSION = "1";
    private volatile String regionId = REGION_CN_HANGZHOU;
	@Autowired
	private IUserService userService;
	/**
	 * 获取ossToken
	 * @param jobj
	 * @return
	 */
	@RequestMapping(value="/oss_token.json", method=RequestMethod.POST)
	public @ResponseBody Object login_by_token(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token","content"}, true, String.class);
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step1 登录验证
		result = userService.loginByToken(jobj.getLong("userId"), jobj.getString("token"));
		if(null == result.get(Result.SUCCESS)) return result;
	
		JSONObject o = new JSONObject();
		try {
			o.put("ossToken", generateOSSToken("wxGYeoOqFGIikopt", "eQyS38ArhJo0fIotIuLoiz0FCx0J4N", jobj.getString("content")));
		} catch (Exception e) {
			LogUtil.error(e.getMessage()); 
			e.printStackTrace();
		}
		return Result.success(o);
	}
	
	@RequestMapping(value="/oss_federation_token.json", method = RequestMethod.POST)
	public @ResponseBody Object getFederationToken(@RequestBody JSONObject jobj){
		//step0 参数验证
		Map result = parametersValidate(jobj, "userId", true, new Class[]{Integer.class, Long.class});
		if(null == result.get(Result.SUCCESS))	return result;
		result = parametersValidate(jobj, new String[]{"token"}, true, String.class);
		if(null == result.get(Result.SUCCESS)) return result;
		
		//step1 登录验证
		result = userService.loginByToken(jobj.getLong("userId"), jobj.getString("token"));
		if(null == result.get(Result.SUCCESS)) return result;
		
			FederationToken ft = null;
			String policy = getPolicy(GlobalConstant.ALIYUN_USER_ID, GlobalConstant.ALIYUN_USER_NAME, GlobalConstant.OSS_BUCKET_NAME);
			try {
				ft =  getFederationToken0(GlobalConstant.ALIYUN_ACCESSKEY_ID, 
						GlobalConstant.ALIYUN_ACCESSKEY_SECRET, "user.maitian", policy,3600, ProtocolType.HTTPS);
			} catch (Exception e) {
				e.printStackTrace();
				return Result.fail(ErrorCode.OSS_FED_TOKEN_FAILED);
			}
			HashMap data = new HashMap();
			data.put("tmpAkId", ft.getAccessKeyId());
			data.put("tmpAkSecret", ft.getAccessKeySecret());
			data.put("securityToken", ft.getSecurityToken());
		return Result.success(data);
	}
	
    /**
     * 获取STS的Token。
     *
     * @param accessKeyId     阿里云用户accessKeyId
     * @param accessKeySecret 阿里云用户accessKeySecret
     * @param grantee         Token关联的应用用户名。
     * @param policy          Token对应的Policy，JSON格式。
     *                        即可以对什么产品（如OSS）的哪些资源（如OSS的某个目录或文件）允许或禁止做什么操作（如读或写）
     * @param expireSeconds   STS Token的过期时间，单位秒。
     * @return 输入要求的STS Token。
     * @throws StsException 操作出错
     */
	private FederationToken getFederationToken0(
            final String accessKeyId, final String accessKeySecret,
            final String grantee, final String policy, final long expireSeconds, ProtocolType protocolType) {
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            final GetFederationTokenRequest request = new GetFederationTokenRequest();
            request.setVersion(STS_POP_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);

            request.setStsVersion(STS_API_VERSION);
            request.setName(grantee);
            request.setPolicy(policy);
            request.setDurationSeconds(expireSeconds);

            final GetFederationTokenResponse response = client.getAcsResponse(request);

            final FederationToken federationToken = new FederationToken();
            federationToken.setRequestId(response.getRequestId());
            federationToken.setFederatedUser(response.getFederatedUser().getArn());
            federationToken.setAccessKeyId(response.getCredentials().getAccessKeyId());
            federationToken.setAccessKeySecret(response.getCredentials().getAccessKeySecret());
            federationToken.setSecurityToken(response.getCredentials().getSecurityToken());
            final String expiration = response.getCredentials().getExpiration();
            final DateTime dateTime = ISODateTimeFormat.dateTime()
                    .withZone(DateTimeZone.UTC).parseDateTime(expiration);
            federationToken.setExpiration(dateTime.toDate());

            return federationToken;
        } catch (StsException e) {
            throw e;
        } catch (ClientException e) {
            throw new StsException("Error to getFederationToken", e.getErrCode(), e.getErrCode(), e);
        } catch (Exception e) {
            throw new StsException("Error to getFederationToken", null, e.getMessage(), e);
        }
    }
	
	private String getPolicy(String userId, String userName, String bucketName) {
        return String.format(
                "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                // 限制只能执行指定prefix的罗列Bucket操作
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:ListObjects\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:%s\"\n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\", \n" +
                "            \"Condition\": { \n" +
                "                \"StringLike\": { \n" +
                "                    \"oss:Prefix\": \"%s/*\"\n" +
                "                }\n" +
                "            }\n" +
                "        }, \n" +
                // 限制只能对Bucket指定目录下的文件进行操作
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:PutObject\", \n" +
                "                \"oss:GetObject\", \n" +
                "                \"oss:DeleteObject\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:%s/*\"\n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}", bucketName, userName, bucketName);
    }
	
	public static void main(String[] args) {
		AuthController ac = new AuthController();
		String policy = ac.getPolicy("1194627879869635", "2337223420@qq.com", "maitianditu");
		FederationToken ft = null;
		try {
			 ft = ac.getFederationToken0("wxGYeoOqFGIikopt","eQyS38ArhJo0fIotIuLoiz0FCx0J4N","user.maitian",policy,3600, ProtocolType.HTTPS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != ft){
			System.out.println(ft);
		}

		
	}
}
