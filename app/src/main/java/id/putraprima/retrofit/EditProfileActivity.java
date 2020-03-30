package id.putraprima.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.Profile;
import id.putraprima.retrofit.api.models.ProfileRequest;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.ui.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TOKEN_KEY="token";
    public static final String TOKEN_TYPE="token_type";
    public static final String NAME_KEY="name";
    public static final String EMAIL_KEY="email";
    int id;
    String token, token_type, nameText, emailText;
    ProfileActivity profile;
    EditText name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name=findViewById(R.id.edtName);
        email=findViewById(R.id.edtEmail);

        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            token=extras.getString(TOKEN_KEY);
            token_type=extras.getString(TOKEN_TYPE);
            nameText=extras.getString(NAME_KEY, nameText);
            emailText=extras.getString(EMAIL_KEY, emailText);
        }

        name.setText(nameText);
        email.setText(emailText);

    }

    public void prosesSubmit(View view) {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<Profile>> call = service.editProfile(token_type+""+token, new ProfileRequest(name.getText().toString(),email.getText().toString()));
        call.enqueue(new Callback<Data<Profile>>() {
            @Override
            public void onResponse(Call<Data<Profile>> call, Response<Data<Profile>> response) {
               if (response.isSuccessful()){
                   Toast.makeText(EditProfileActivity.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
               } else{
                   ApiError error = ErrorUtils.parseError(response);
                   if (name.getText().toString().isEmpty()){
                       name.setError(error.getError().getName().get(0));
                   } else if (email.getText().toString().isEmpty() || isValidEmail(email.getText().toString())==false){
                       email.setError(error.getError().getEmail().get(0));
                   }
               }

            }

            @Override
            public void onFailure(Call<Data<Profile>> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Gagal edit profile", Toast.LENGTH_SHORT).show();
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

    public void prosesEditPassword(View view) {
        Toast.makeText(EditProfileActivity.this, token, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(EditProfileActivity.this, EditPasswordActivity.class);
        intent.putExtra(TOKEN_TYPE, token_type);
        intent.putExtra(TOKEN_KEY, token);
        startActivity(intent);
    }
}
