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
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
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
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Berhasil register", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        ApiError error = ErrorUtils.parseError(response);
                        if (name.getText().toString().isEmpty()){
                            name.setError(error.getError().getName().get(0));
                        } else if (email.getText().toString().isEmpty() || isValidEmail(email.getText().toString())==false){
                            email.setError(error.getError().getEmail().get(0));
                        } else if (password.getText().toString().isEmpty()){
                            if (password.getText().toString().length()<8){
                                password.setError(error.getError().getPassword().get(0));
                            } else {
                                password.setError(error.getError().getPassword().get(0));
                            }
                        } else if (!password.getText().toString().equals(confirm.getText().toString())){
                            password.setError(error.getError().getPassword().get(0));
                            confirm.setError(error.getError().getPassword().get(0));
                        }




                    }

                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Gagal register", Toast.LENGTH_SHORT).show();
                }
            });
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
