package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
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
    TextView name, version;
    private LoginRequest loginRequest;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.mainTxtAppName);
        version=findViewById(R.id.mainTxtAppVersion);
        email=findViewById(R.id.edtEmail);
        password=findViewById(R.id.edtPassword);

        Bundle extra=getIntent().getExtras();
        if (extra!=null){
            name.setText(extra.getString(NAME_KEY));
            version.setText(extra.getString(VERSION_KEY));
        }
        getToken();
    }

    private void getToken(){
        ApiInterface service=ServiceGenerator.createService(ApiInterface.class);
        Call<LoginRequest> call=service.getLoginRequest();
        call.enqueue(new Callback<LoginRequest>() {
            @Override
            public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {

            }

            @Override
            public void onFailure(Call<LoginRequest> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginResponse> call = service.doLogin(new LoginRequest(email.getText().toString(),password.getText().toString()));
        call.enqueue(new Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                getToken();
                Toast.makeText(MainActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
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
}
