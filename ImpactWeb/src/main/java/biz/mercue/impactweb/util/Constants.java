package biz.mercue.impactweb.util;

public class Constants {
    public static String MAIL_STARTTLS = "";
    public static String MAIL_HOST = "";
    public static String MAIL_PORT = "";
    public static String MAIL_ACCOUNT = "";
    public static String MAIL_FROM = "";
    public static String MAIL_PASSWORD = "";
    public static String SYSTEM_EMAIL = "";

    public static String RECAPTCHA_SECRET_KEY = "";


    public static final String ENCODE_UTF_8 = "UTF-8";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    public static final String CONTENT_TYPE_PNG = "image/png";

    public static final int SHORT_IMAGE_NAME_LENGTH = 8;


    public static final String JSON_CODE = "code";
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_DATA = "data";

    public static final String JSON_TOKEN = "token";

    public static String IMAGE_UPLOAD_PATH = "";
    public static String IMAGE_LOAD_URL = "";
    public static String VIDEO_UPLOAD_PATH = "";
    public static String VIDEO_LOAD_URL = "";
    public static String FILE_UPLOAD_PATH = "";
    public static String FILE_LOAD_URL = "";


    public static String IMAGEMAGICK_PATH = "";
    public static String GRAPHICSMAGICK_PATH = "";
    public static String FFMPEG_PATH = "";


    public static String SYNC_FILE_UPLOAD_PATH = "";
    public static String SYNC_FILE_LOAD_URL = "";


    public static final String REDIRECT_LOGIN = "/login";
    public static final String REDIRECT_MAINPAGE = "/mainpage";
    public static final String REDIRECT_HTML = "/index.html";


    public static final String KEY_WIDTH = "width";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_SIZE = "size";
    public static final String KEY_SUFFIX = "suffix";
    public static final String KEY_RESULT = "result";


    public static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11";
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";


    public static final int INT_SUCCESS = 1;
    public static final String MSG_SUCCESS = "成功";
    public static final String MSG_MAIL_SEND_SUCCESS = "Email發送成功";
    public static final String MSG_EN_SUCCESS = "Success";


    public static final int INT_SYSTEM_PROBLEM = -1;
    public static final String MSG_SYSTEM_PROBLEM = "系統發生問題";
    public static final String MSG_EN_SYSTEM_PROBLEM = "System problem";

    public static final int INT_CANNOT_FIND_DATA = -2;
    public static final String MSG_CANNOT_FIND_DATA = "找不到資料";
    public static final String MSG_EN_CANNOT_FIND_DATA = "Can not find Data";

    public static final int INT_PASSWORD_ERROR = -3;
    public static final String MSG_PASSWORD_ERROR = "帳號或密碼錯誤";
    public static final String MSG_EN_PASSWORD_ERROR = "Account and password do not match";

    public static final int INT_USER_DUPLICATE = -4;
    public static final String MSG_USER_DUPLICATE = "Email已被註冊";
    public static final String MSG_EN_USER_DUPLICATE = "The email address is already in use by another account";


    public static final int INT_DATA_ERROR = -5;
    public static final String MSG_DATA_ERROR = "輸入資料錯誤";
    public static final String MSG_EN_DATA_ERROR = "The data you entered is not correct";

    public static final int INT_USER_LOGOUT = -6;
    public static final String MSG_USER_LOGOUT = "已登出系統";
    public static final String MSG_EN_USER_LOGOUT = "Your login session has expired";


    public static final int INT_NO_PERMISSION = -7;
    public static final String MSG_NO_PERMISSION = "無使用權限";
    public static final String MSG_EN_NO_PERMISSION = "You have no permission to operate";

    public static final int INT_ACCOUNT_UNAVAILABLE = -8;
    public static final String MSG_ACCOUNT_UNAVAILABLE = "帳號已停用";
    public static final String MSG_EN_ACCOUNT_UNAVAILABLE = "Your account is unavailable";

    public static final int INT_ACCESS_TOKEN_ERROR = -10;
    public static final String MSG_ACCESS_TOKEN_ERROR = "已登出系統";
    public static final String MSG_EN_ACCESS_TOKEN_ERROR = "Your login session has expired";


    public static final int INT_DATA_DUPLICATE = -11;
    public static final String MSG_DATA_DUPLICATE = "資料重複";
    public static final String MSG_EN_DATA_DUPLICATE = "The data you entered is not correct";

    public static final int SYSTEM_PAGE_SIZE = 20;


    public static String TUYA_CLIENT_ID = "";
    public static String TUYA_SECRET_KEY = "";
    public static String TUYA_SCHEMA = "";

    public static final String CATEGORY_DEFAULT_ID = "40w9dse0277455f634fw40439sd";

}
