package id.putraprima.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, password, confirm;
    public static final String NAME_KEY="name";
    public static final String VERSION_KEY="version";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=findViewById(R.id.edtName);
        email=findViewById(R.id.edtEmail);
        password=findViewById(R.id.edtPassword);
        confirm=findViewById(R.id.edtConfirm);

        Bundle extras=getIntent().getExtras();
        if (extras!=null){

        }
    }

    public void prosesSubmit(View view) {
        register();
    }

    private void register() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<RegisterResponse> call = service.doRegister(new RegisterRequest(name.getText().toString(), email.getText().toString(),password.getText().toString(), confirm.getText().toString()));
        if (name.getText().toString().isEmpty()){
            name.setError("Nama harus diisi");
        }else if (password.getText().toString().length()<8 || password.getText().toString().isEmpty()){
            password.setError("Panjang password minimal 8");
        } else if(!password.getText().toString().equals(confirm.getText().toString())){
            password.setError("Konfirmasi password tidak sesuai");
            confirm.setError("Konfirmasi password tidak sesuai");
        } else if(isValidEmail(email.getText().toString())==false || email.getText().toString().isEmpty()){
            email.setError("Format email harus benar");
        } else {
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    Toast.makeText(RegisterActivity.this,"Berhasil register", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Gagal register", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean isValidEmail(String email){
        boolean validate;
        String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

        if(email.matches(emailPattern)){
            validate=true;
        } else if(email.matches(emailPattern2)){
            validate=true;
        } else {
            validate=false;
        }
        return validate;
    }

}
