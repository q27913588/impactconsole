package biz.mercue.impactweb.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import biz.mercue.impactweb.model.Account;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TuyaService {
	
private static  Logger log = Logger.getLogger(TuyaService.class.getName());


    private static volatile TuyaService tuyaService;
    
    private String tuya_url =  "https://openapi.tuyacn.com/v1.0";
    
    private OkHttpClient httpClient;
    
	private String access_token;
	
	private Date token_expire_date;
	
	private String refresh_token;

    //private constructor.
    private TuyaService(){

        //Prevent form the reflection api.
        if (tuyaService != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        
        httpClient = new OkHttpClient();
        generateToken();
        
        
    }

    public static TuyaService getInstance() {
        //Double check locking pattern
        if (tuyaService == null) { //Check for the first time
          
            synchronized (TuyaService.class) {   //Check for the second time.
              //if there is no instance available... create new one
            	if (tuyaService == null) { 
            		tuyaService = new TuyaService();
            	}
             }
         }
        

        return tuyaService;
    }
    
    
	private synchronized void  generateToken() {
		String url = null;
		if(StringUtils.isNULL(this.access_token) && StringUtils.isNULL(this.refresh_token)) {
			url = tuya_url + "/token?grant_type=1";
		}else {
			url = tuya_url + "/token/"+ this.refresh_token;
		}
		
		long ts = new Date().getTime();
		
		String sign = HMACSHA256( Constants.TUYA_CLIENT_ID+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id", Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .get()
				  .build();
		String responseStr = null;
		
		Call call = httpClient.newCall(request);
		try {
		    Response response = call.execute();
		    responseStr = response.body().string();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		

		if (!StringUtils.isNULL(responseStr)) {
			log.info("responseStr :"+responseStr);
			JSONObject response = new JSONObject(responseStr);
			if (response.optBoolean("success") == true) {
				JSONObject jsonResult = response.optJSONObject("result");
				if(jsonResult != null) {
					this.access_token = jsonResult.optString("access_token");
					this.refresh_token = jsonResult.optString("refresh_token");
					int expire_second = jsonResult.optInt("expire_time");
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.SECOND, expire_second);
					this.token_expire_date = calendar.getTime();
				}
			}
		}
		
	}
	
	
	public void rigisterUser(Account account) {
		log.info("rigisterUser");
		// usernameType 1. mobile 2.email 3. username
		try {
			String url = tuya_url + "/apps/%s/user";
			url = String.format(url, Constants.TUYA_SCHEMA);

			long ts = new Date().getTime();
			if (ts > this.token_expire_date.getTime()) {
				generateToken();
			}

			String sign = HMACSHA256(Constants.TUYA_CLIENT_ID + this.access_token + String.valueOf(ts),
					Constants.TUYA_SECRET_KEY);

			JSONObject postParamsObj = new JSONObject();
			postParamsObj.put("country_code", account.getAccount_country_code());
			postParamsObj.put("username", account.getAccount_email());
			postParamsObj.put("password",MD5Hash(account.getAccount_password()));
			postParamsObj.put("username_type", "2");

			MediaType mediaType = MediaType.parse("application/json");

			RequestBody body = RequestBody.create(mediaType, postParamsObj.toString());

			Request request = new Request.Builder().url(url).addHeader("client_id", Constants.TUYA_CLIENT_ID)
					.addHeader("sign", sign).addHeader("sign_method", "HMAC-SHA256").addHeader("t", String.valueOf(ts))
					.addHeader("access_token", this.access_token).post(body).build();
			String responseStr = null;

			Call call = this.httpClient.newCall(request);

			Response response = call.execute();
			responseStr = response.body().string();
			log.info("responseStr:" + responseStr);
			String uid = null;
			if (!StringUtils.isNULL(responseStr)) {
				
				JSONObject json = new JSONObject(responseStr);
				if (json.optBoolean("success") == true) {
					 account.setAccount_tuya_id(json.optJSONObject("result").optString("uid"));
					 account.setResult_code("200");
					 account.setResult("success");
				}else {
					 account.setResult_code(json.optString("code"));
					 account.setResult(json.optString("msg"));
				}
			}
		} catch (Exception e) {
			log.info("Exception:" + e.getMessage());
			 account.setResult_code("500");
			 account.setResult(e.getMessage());
		}

	}
	
	public List<Account> getUserList(int pageNo, int pageSize) {
		
		List<Account> listAccount = new ArrayList<Account>();
		try {
		String url = tuya_url + "/apps/%s/users?page_no=%s&page_size=%s";
	
		url = String.format(url, Constants.TUYA_SCHEMA, pageNo, pageSize);
		log.info("url:"+url);
		long ts = new Date().getTime();
		if(ts > this.token_expire_date.getTime()) {
			generateToken();
		}
		String sign = HMACSHA256( Constants.TUYA_CLIENT_ID+this.access_token+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id",  Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .addHeader("access_token", this.access_token)
				  .get()
				  .build();

		
		Call call = this.httpClient.newCall(request);
		
		Response response = call.execute();
		String responseStr = response.body().string();
		 JSONObject resultJson = new JSONObject(responseStr);
			if (resultJson.optBoolean("success") == true) {
				JSONArray array = (JSONArray) resultJson.optJSONObject("result").opt("list");
				for(int i = 0 ;i < array.length() ; i++) {
					JSONObject accountJSon =(JSONObject) array.get(i);
					Account account = new Account(); 
					account.setAccount_id(accountJSon.optString("uid"));
					account.setAccount_email(accountJSon.optString("email"));
					account.setAccount_name(accountJSon.optString("username"));
					account.setAccount_country_code(accountJSon.optString("country_code"));

					listAccount.add(account);
					
				}
			}
		
		
		
		}catch (Exception e) {
			log.info("Exception:"+e.getMessage());
		}

		return listAccount;
	}
    
    


	
	
	public String getDeviceStatus(String deviceId) {
		String url = tuya_url + "/devices/%s/status";
		url = String.format(url, deviceId);
		
		long ts = new Date().getTime();
		if(ts > this.token_expire_date.getTime()) {
			generateToken();
		}
		
		String sign = HMACSHA256(Constants.TUYA_CLIENT_ID+this.access_token+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id", Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .addHeader("access_token", this.access_token)
				  .get()
				  .build();
		String responseStr = null;
		
		Call call = this.httpClient.newCall(request);
		try {
		    Response response = call.execute();
		    responseStr = response.body().string();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return responseStr;
	}
	
	public String getDevicesStatus(List<String> deviceIdList) 
	{
		String deviceIds = deviceIdList.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",");
		String url = tuya_url + "/devices/status?device_ids=%s";
		url = String.format(url, deviceIds);
		

		long ts = new Date().getTime();
		
		if(ts > this.token_expire_date.getTime()) {
			generateToken();
		}
		
		String sign = HMACSHA256(Constants.TUYA_CLIENT_ID+this.access_token+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id", Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .addHeader("access_token", this.access_token)
				  .get()
				  .build();
		String responseStr = null;
		
		Call call = this.httpClient.newCall(request);
		try {
		    Response response = call.execute();
		    responseStr = response.body().string();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return responseStr;
	}
	
	public  String getDeviceInfo(String deviceId) 
	{
		String url = tuya_url + "/devices/%s";
		url = String.format(url, deviceId);
		

		long ts = new Date().getTime();
		if(ts > this.token_expire_date.getTime()) {
			generateToken();
		}
		
		String sign = HMACSHA256(Constants.TUYA_CLIENT_ID+this.access_token+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id",  Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .addHeader("access_token", this.access_token)
				  .get()
				  .build();
		String responseStr = null;
		
		Call call = this.httpClient.newCall(request);
		try {
		    Response response = call.execute();
		    responseStr = response.body().string();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return responseStr;
	}
	
	public  String getDevicesInfo(List<String> deviceIdList) 
	{
		String deviceIds = deviceIdList.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ",");
		String url = tuya_url + "/devices?device_ids=%s";
		url = String.format(url, deviceIds);
	
		long ts = new Date().getTime();
		if(ts > this.token_expire_date.getTime()) {
			generateToken();
		}
		
		String sign = HMACSHA256( Constants.TUYA_CLIENT_ID+this.access_token+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id",  Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .addHeader("access_token", this.access_token)
				  .get()
				  .build();
		String responseStr = null;
		
		Call call = this.httpClient.newCall(request);
		try {
		    Response response = call.execute();
		    responseStr = response.body().string();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return responseStr;
	}
	
	public  String removeDevice(String deviceId) 
	{
		String url = tuya_url + "/devices/%s";
		url = String.format(url, deviceId);
		

		long ts = new Date().getTime();
		if(ts > this.token_expire_date.getTime()) {
			generateToken();
		}
		
		String sign = HMACSHA256( Constants.TUYA_CLIENT_ID+this.access_token+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id",  Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .addHeader("access_token", this.access_token)
				  .delete()
				  .build();
		String responseStr = null;
		
		Call call = this.httpClient.newCall(request);
		try {
		    Response response = call.execute();
		    responseStr = response.body().string();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return responseStr;
	}
	
	public  String getUserDeviceList(String uid) 
	{
		String url = tuya_url + "/users/%s/devices";
		url = String.format(url, uid);
		
		long ts = new Date().getTime();
		if(ts > this.token_expire_date.getTime()) {
			generateToken();
		}
		
		String sign = HMACSHA256( Constants.TUYA_CLIENT_ID+this.access_token+String.valueOf(ts), Constants.TUYA_SECRET_KEY);
		
		Request request = new Request.Builder()
				  .url(url)
				  .addHeader("client_id",  Constants.TUYA_CLIENT_ID)
				  .addHeader("sign", sign)
				  .addHeader("sign_method", "HMAC-SHA256")
				  .addHeader("t", String.valueOf(ts))
				  .addHeader("access_token", this.access_token)
				  .get()
				  .build();
		String responseStr = null;
		
		Call call = this.httpClient.newCall(request);
		try {
		    Response response = call.execute();
		    responseStr = response.body().string();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return responseStr;
	}
	
	private String HMACSHA256(String data, String key) 
	{
	      try  {
	         SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
	         Mac mac = Mac.getInstance("HmacSHA256");
	         mac.init(signingKey);
	         byte[] bytes = mac.doFinal(data.getBytes());   
	         return Hex.encodeHexString(bytes).toUpperCase();
	      } catch (NoSuchAlgorithmException e) {
	         e.printStackTrace();
	      } catch (InvalidKeyException e) {
	        e.printStackTrace();
	      }
	      return null;
	}
	
	public static void main(String[] args) throws Exception {
		
		

	
		
	}
	
	private static String MD5Hash(String plain) {
		String myHash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
		
			md.update(plain.getBytes());
			byte[] digest = md.digest();
		    myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
		    //myHash = DatatypeConverter.printHexBinary(digest);
			
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException:"+e.getMessage());
			myHash = null;
		}
		return myHash;
	}
}

