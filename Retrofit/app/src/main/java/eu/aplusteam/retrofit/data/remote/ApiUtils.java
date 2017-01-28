package eu.aplusteam.retrofit.data.remote;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: anton<br/>
 * Date: 28.01.17<br/>
 * Time: 22:17<br/>
 */
public class ApiUtils {
    public static final String BASE_URL = "https://api.stackexchange.com/2.2/";

    public static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
