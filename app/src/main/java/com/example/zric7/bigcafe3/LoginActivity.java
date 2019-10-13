package com.example.zric7.bigcafe3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.zric7.bigcafe3.Model.LoginModel;
import com.example.zric7.bigcafe3.Model.LoginValue;
import com.example.zric7.bigcafe3.RetrofitApi.ApiInterface;
import com.example.zric7.bigcafe3.Utils.SharedPrefManager;
import com.example.zric7.bigcafe3.Utils.common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<LoginModel> loginModelList = new ArrayList<>();
    SharedPrefManager sharedPrefManager;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.edt_username)
    EditText editTextUsername;
    @BindView(R.id.edt_password)
    EditText editTextPassword;

    @BindView(R.id.btn_login)
    Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        apiInterface = common.getAPI(); /*Koneksi ke interface API*/
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        sharedPrefManager = new SharedPrefManager(this);
        // Code berikut berfungsi untuk mengecek session, Jika session true ( sudah login )
        // maka langsung memulai MainActivity.
        if (sharedPrefManager.getSPSudahLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

    }

    public void on_login(final View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        Call<LoginValue> jsonData = apiInterface.login(username, password);  /*Panggil method request ke webservice*/
        jsonData.enqueue(new Callback<LoginValue>() {
            @Override
            public void onResponse(@NonNull Call<LoginValue> call, @NonNull Response<LoginValue> response) {
                int status = response.body().getStatus();
                progressBar.setVisibility(View.GONE);
                if (status == 1) {
                    loginModelList = response.body().getResult();
                    String username = loginModelList.get(0).getUsername();
                    String role = loginModelList.get(0).getRole();

                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, username);
                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ROLE, role);
                    // Shared Pref ini berfungsi untuk menjadi trigger session login
                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();

//                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginValue> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("ERROR_Login", t.getMessage());
                Toast.makeText(LoginActivity.this, "Username atau Password salah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
