package id.putraprima.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.Profile;
import id.putraprima.retrofit.api.services.ApiInterface;
import id.putraprima.retrofit.ui.SplashActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    public static final String TOKEN_KEY="token";
    public static final String TOKEN_TYPE="token_type";
    public static final String NAME_KEY="name";
    public static final String EMAIL_KEY="email";
    TextView id, name, email;
    private String token, token_type;
    public static final String ID_KEY="id";
    private static SharedPreferences preference;
    private String nameText, emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        id=findViewById(R.id.idText);
        name=findViewById(R.id.nameText);
        email=findViewById(R.id.emailText);

        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            token=extras.getString(TOKEN_KEY);
            token_type=extras.getString(TOKEN_TYPE);
        }

        showProfile();

    }

    private void showProfile() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Data<Profile>> call = service.getProfile(token_type+" "+token);
        call.enqueue(new Callback<Data<Profile>>() {
            @Override
            public void onResponse(Call<Data<Profile>> call, Response<Data<Profile>> response) {
                if (response.body()!=null){

                    int idText=response.body().data.getId();
                    nameText=response.body().data.getName();
                    emailText=response.body().data.getEmail();

//                    setName(ProfileActivity.this, nameText);
//                    setEmail(ProfileActivity.this, emailText);

                    id.setText(String.valueOf((idText)));
                    name.setText(nameText);
                    email.setText(emailText);

//                    Intent intent=new Intent(ProfileActivity.this, EditProfileActivity.class);
//                    intent.putExtra(NAME_KEY, nameText );
//                    intent.putExtra(EMAIL_KEY, emailText);
//                    startActivity(intent);

                }

            }

            @Override
            public void onFailure(Call<Data<Profile>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Gagal Menampilkan Profile", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void prosesEditProfile(View view) {
        Toast.makeText(ProfileActivity.this, token, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra(TOKEN_TYPE, token_type);
        intent.putExtra(TOKEN_KEY, token);
        intent.putExtra(NAME_KEY, nameText );
        intent.putExtra(EMAIL_KEY, emailText);
        startActivity(intent);
    }



//    public static String getId(Context context) {
//        preference=PreferenceManager.getDefaultSharedPreferences(context);
//        return preference.getString(ID_KEY, "0");
//    }
//
//    public static void setId(Context context, int appId) {
//        preference=PreferenceManager.getDefaultSharedPreferences(context);
//        preference.edit().putInt(ID_KEY, appId).apply();
//    }
//
//    public static String getName(Context context) {
//        preference= PreferenceManager.getDefaultSharedPreferences(context);
//        return preference.getString(NAME_KEY, "name");
//    }
//
//    public static void setName(Context context, String appName) {
//        preference=PreferenceManager.getDefaultSharedPreferences(context);
//        preference.edit().putString(NAME_KEY, appName).apply();
//    }
//    public static String getEmail(Context context) {
//        preference=PreferenceManager.getDefaultSharedPreferences(context);
//        return preference.getString(EMAIL_KEY, "email");
//    }
//
//    public static void setEmail(Context context, String appEmail) {
//        preference=PreferenceManager.getDefaultSharedPreferences(context);
//        preference.edit().putString(EMAIL_KEY, appEmail).apply();
//    }
}
