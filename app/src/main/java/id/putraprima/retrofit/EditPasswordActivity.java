package id.putraprima.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.PasswordRequest;
import id.putraprima.retrofit.api.models.PasswordResponse;
import id.putraprima.retrofit.api.models.Profile;
import id.putraprima.retrofit.api.models.ProfileRequest;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {
    public static final String TOKEN_KEY="token";
    public static final String TOKEN_TYPE="token_type";
    String token, token_type;
    EditText pass, conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        pass=findViewById(R.id.edtPassword);
        conf=findViewById(R.id.edtConfirm);

        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            token=extras.getString(TOKEN_KEY);
            token_type=extras.getString(TOKEN_TYPE);
        }

    }

    public void prosesSubmit(View view) {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<Profile>> call = service.editPassword(token_type+""+token, new PasswordRequest(pass.getText().toString(), conf.getText().toString()));
        call.enqueue(new Callback<Data<Profile>>() {
            @Override
            public void onResponse(Call<Data<Profile>> call, Response<Data<Profile>> response) {
                if (pass.getText().toString().length()<8 || pass.getText().toString().isEmpty()){
                    pass.setError("Panjang password minimal 8");
                } else if(!pass.getText().toString().equals(conf.getText().toString())){
                    pass.setError("Konfirmasi password tidak sesuai");
                    conf.setError("Konfirmasi password tidak sesuai");
                } else {
                    if (response.body()!=null){
                        Toast.makeText(EditPasswordActivity.this, "Change password success", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<Data<Profile>> call, Throwable t) {
                Toast.makeText(EditPasswordActivity.this, "Gagal change password", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void prosesLogout(View view) {
        Intent intent=new Intent(EditPasswordActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
