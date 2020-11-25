package se.iuh.holo_app_chat.services.interceptor;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import se.iuh.holo_app_chat.activities.MyApp;
import se.iuh.holo_app_chat.activities.login.LoginActivity;
import se.iuh.holo_app_chat.services.LoginService;
import se.iuh.holo_app_chat.services.ProfileService;
import se.iuh.holo_app_chat.services.response.LoginResponse;
import se.iuh.holo_app_chat.untils.ServiceGenerator;
import se.iuh.holo_app_chat.untils.SharedPrefManager;

public class TokenAuthenticator implements Interceptor {

    SharedPrefManager sharedPrefManager;

    public TokenAuthenticator() {
        sharedPrefManager = new SharedPrefManager(MyApp.getContext());
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response mainResponse = chain.proceed(chain.request());
        Request mainRequest = chain.request();

        ProfileService profileService= ServiceGenerator.createService(ProfileService.class, sharedPrefManager.getSPToken());

        if ( mainResponse.code() == 401 || mainResponse.code() == 403 ) {
            retrofit2.Response<LoginResponse> refreshToken = profileService.refreshToken(sharedPrefManager.getSPToken()).execute();
            if (refreshToken.isSuccessful()) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN, "Bearer " +
                        refreshToken.body().getToken());
                Request.Builder builder = mainRequest.newBuilder().header("Authorization",
                        sharedPrefManager.getSPToken())
                        .method(mainRequest.method(), mainRequest.body());
                mainResponse = chain.proceed(builder.build());
            }else{
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_LOGIN, false);
                Intent i = new Intent(MyApp.getContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getContext().startActivity(i);
            }
        } else if ( mainResponse.code() == 500 ){
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_LOGIN, false);
            Intent i = new Intent(MyApp.getContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApp.getContext().startActivity(i);
        }

        return mainResponse;
    }
}