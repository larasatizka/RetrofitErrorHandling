package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.ProfileActivity;
import id.putraprima.retrofit.R;
import id.putraprima.retrofit.RegisterActivity;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String NAME_KEY="name";
    public static final String VERSION_KEY="version";
    public static final String TOKEN_KEY="token";
    public static final String TOKEN_TYPE="token_type";
    TextView name, version;
    private LoginRequest loginRequest;
    EditText email, password;
    private String token, token_type;
    private SplashActivity splash;
    private LoginRequest log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.mainTxtAppName);
        version=findViewById(R.id.mainTxtAppVersion);
        email=findViewById(R.id.edtEmail);
        password=findViewById(R.id.edtPassword);

        name.setText(splash.getAppName(MainActivity.this));
        version.setText(splash.getAppVersion(MainActivity.this));
    }

    private void login() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> call = service.doLogin(new LoginRequest(email.getText().toString(),password.getText().toString()));
        call.enqueue(new Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
//                    SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor editor = preference.edit();
//                    editor.putString("token",response.body().getToken());
//                    editor.apply();
//                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
//                    startActivity(i);
                    token=response.body().getToken();
                    token_type=response.body().getToken_type();
                    Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra(TOKEN_KEY, token);
                    intent.putExtra(TOKEN_TYPE, token_type);
                    startActivity(intent);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (email.getText().toString().isEmpty()){
                        email.setError(error.getError().getEmail().get(0));
                    } else if (password.getText().toString().isEmpty()){
                        password.setError(error.getError().getPassword().get(0));
                    } else {
                        Toast.makeText(MainActivity.this, error.getError().getEmail().get(0), Toast.LENGTH_SHORT).show();
                    }
                    //email.setError(error.getError().getEmail().get(0));



                }


            }



            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Mendapatkan Token", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void prosesLogin(View view) {
        login();
    }

    public void prosesRegister(View view) {
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
