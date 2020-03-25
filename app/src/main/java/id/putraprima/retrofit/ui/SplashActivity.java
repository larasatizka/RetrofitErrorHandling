package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.RegisterActivity;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    TextView lblAppName, lblAppTittle, lblAppVersion;
    public static final String NAME_KEY="name";
    public static final String VERSION_KEY="version";
    private static SharedPreferences pref;
    private LoginRequest loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupLayout();
        if (checkInternetConnection()) {
            checkAppVersion();
        } else{
            Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        setAppInfo();


    }

    private void setupLayout() {
        lblAppName = findViewById(R.id.lblAppName);
        lblAppTittle = findViewById(R.id.lblAppTittle);
        lblAppVersion = findViewById(R.id.lblAppVersion);
        //Sembunyikan lblAppName dan lblAppVersion pada saat awal dibuka
        lblAppVersion.setVisibility(View.INVISIBLE);
        lblAppName.setVisibility(View.INVISIBLE);
    }

    private boolean checkInternetConnection() {
        //TODO : 1. Implementasikan proses pengecekan koneksi internet, berikan informasi ke user jika tidak terdapat koneksi internet
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void setAppInfo() {
        //TODO : 5. Implementasikan proses setting app info, app info pada fungsi ini diambil dari shared preferences
        String appName = getAppName(SplashActivity.this);
        String appVersion = getAppVersion(SplashActivity.this);

        lblAppName.setText(appName);
        lblAppVersion.setText(appVersion);
        lblAppName.setVisibility(View.VISIBLE);
        lblAppVersion.setVisibility(View.VISIBLE);
        //lblAppVersion dan lblAppName dimunculkan kembali dengan data dari shared preferences

    }

    private void checkAppVersion() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<AppVersion> call = service.getAppVersion();
        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                Toast.makeText(SplashActivity.this, response.body().getApp(), Toast.LENGTH_SHORT).show();
                //Todo : 2. Implementasikan Proses Simpan Data Yang didapat dari Server ke SharedPreferences
                if(response.isSuccessful()){
                    if (response.body()!=null){
                        setAppName(SplashActivity.this, response.body().getApp());
                        setAppVersion(SplashActivity.this, response.body().getVersion());

                        //Todo : 3. Implementasikan Proses Pindah Ke MainActivity Jika Proses getAppVersion() sukses
                        String appName = getAppName(SplashActivity.this);
                        String appVersion = getAppVersion(SplashActivity.this);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }


            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
                //Todo : 4. Implementasikan Cara Notifikasi Ke user jika terjadi kegagalan koneksi ke server silahkan googling cara yang lain selain menggunakan TOAST
                Log.e("Retrofit Get", t.toString());
            }
        });
    }

    public static String getAppName(Context context) {
        pref=PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(NAME_KEY, "name");
    }

    public static void setAppName(Context context, String appName) {
        pref=PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(NAME_KEY, appName).apply();
    }
    public static String getAppVersion(Context context) {
        pref=PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(VERSION_KEY, "0");
    }

    public static void setAppVersion(Context context, String appVer) {
        pref=PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(VERSION_KEY, appVer).apply();
    }



}
