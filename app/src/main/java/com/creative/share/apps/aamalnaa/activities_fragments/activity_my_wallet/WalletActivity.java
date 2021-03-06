package com.creative.share.apps.aamalnaa.activities_fragments.activity_my_wallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_transaction.TransactionActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_transfer.TransferActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.bank_activity.BanksActivity;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_addbalance.AddBalanceActivity;
import com.creative.share.apps.aamalnaa.adapters.BankAdapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityWalletBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.BankDataModel;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.models.Wallet_Model;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityWalletBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private String balance = "";
    private BankAdapter bankAdapter;
    private List<BankDataModel.BankModel> bankModelList;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        initView();
    }

    private void initView() {
        bankModelList = new ArrayList<>();
        bankAdapter = new BankAdapter(bankModelList, this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBalance(balance);
        binding.setBackListener(this);
        binding.recbank.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recbank.setItemViewCacheSize(25);
        binding.recbank.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recbank.setDrawingCacheEnabled(true);
        binding.recbank.setAdapter(bankAdapter);
        getBankAccount();
        if (userModel != null) {
            //  getWallet();
            getprofiledata();
        }
        binding.llAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalletActivity.this, BanksActivity.class);
                startActivity(intent);
            }
        });
        binding.llAddTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalletActivity.this, TransactionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getprofiledata() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            //Log.e("data_f",id);

            Api.getService(Tags.base_url)
                    .getmyprofile(userModel.getUser().getId() + "", userModel.getUser().getId() + "")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                updatepruofile(response.body());
                            } else {

                                //Toast.makeText(WalletActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error_data5", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        //   Toast.makeText(WalletActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Toast.makeText(WalletActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            if (dialog != null) {
                dialog.dismiss();
            }

            // Log.e("err", e.getMessage());
        }
    }

    private void updatepruofile(UserModel body) {
        balance = body.getUser().getBalance();
        if (balance.equals("0")) {
            binding.tvBalance.setText("لايوجد نقاط لديك");
        } else {
            binding.setBalance(balance);
        }
    }

    private void getBankAccount() {
        Api.getService(Tags.base_url)
                .getBanks()
                .enqueue(new Callback<BankDataModel>() {
                    @Override
                    public void onResponse(Call<BankDataModel> call, Response<BankDataModel> response) {
                        //  progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            bankModelList.addAll(response.body().getData());
                            bankAdapter.notifyDataSetChanged();
                        } else {
                            try {
                                Log.e("Error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(WalletActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BankDataModel> call, Throwable t) {
                        try {

                            //  progBar.setVisibility(View.GONE);
                            Toast.makeText(WalletActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                        }
                    }
                });
    }

    @Override
    public void back() {
        finish();
    }

    private void getWallet() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getmywallet(userModel.getUser().getId() + "")
                    .enqueue(new Callback<Wallet_Model>() {
                        @Override
                        public void onResponse(Call<Wallet_Model> call, Response<Wallet_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                updatewallet(response.body());
                            } else {

                                //  Toast.makeText(WalletActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (Exception e) {
                                    //   e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Wallet_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(WalletActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WalletActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            if (dialog != null) {
                dialog.dismiss();
            }

            // Log.e("err", e.getMessage());
        }
    }

    private void updatewallet(Wallet_Model body) {
//        if(body.getData().getEvaluation()>=10&&body.getData().getEvaluation()<30){
//            balance="10";
//        }
//        else if(body.getData().getEvaluation()>=30&&body.getData().getEvaluation()<50){
//            balance="35";
//        }
//        else if(body.getData().getEvaluation()==50){
//            balance="60";
//        }
//        else if(body.getData().getEvaluation()>50){
//            balance=getResources().getString(R.string.more_than_60);
//        }
//        else {
//            balance="0";
//        }
        balance = userModel.getUser().getBalance();
        binding.setBalance(balance);
    }

    public void gotoaddbalance() {
        Intent intent = new Intent(WalletActivity.this, AddBalanceActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getprofiledata();
    }
}
