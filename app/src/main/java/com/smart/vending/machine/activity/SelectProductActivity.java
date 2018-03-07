package com.smart.vending.machine.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.smart.vending.machine.R;
import com.smart.vending.machine.application.Application;
import com.smart.vending.machine.util.Constatns;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Created by MUVVASR on 3/7/2018.
 */

public class SelectProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener {


    private static final String TAG = SelectProductActivity.class.getSimpleName();
    private Button mProceedButton;
    private TextView mBalanceTextView, mInsertCoinTextView, mReturnTextView, mChangeTextView;
    private Spinner mProductSpinner;
    Application mApplication;
    CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private String mProductList[];
    private String mSelectedProduct;
    private int mSelectedCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecte_product_activity);
        initViews();
        mProductList = getResources().getStringArray(R.array.products);
        mApplication = Application.mApplication;
        // mSelectedProduct=mProductList[0];
    }

    private void initViews() {
        mBalanceTextView = (TextView) findViewById(R.id.tview_balance);
        mProceedButton = (Button) findViewById(R.id.btn_proceed);
        mProductSpinner = (Spinner) findViewById(R.id.select_product_activity_product_spinner);
        mInsertCoinTextView = (TextView) findViewById(R.id.tview_insert_coin);
        mReturnTextView = (TextView) findViewById(R.id.tview_return);
        mChangeTextView = (TextView) findViewById(R.id.tview_change);
        mProductSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.products, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProductSpinner.setAdapter(adapter);
        mProceedButton.setOnClickListener(this);
        mInsertCoinTextView.setOnClickListener(this);
        mReturnTextView.setOnClickListener(this);
        mChangeTextView.setOnClickListener(this);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main_ordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBalance();
    }

    private void setBalance() {
        float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
        mBalanceTextView.setText(balance + " " + Constatns.CURRENCY_SYMBOL);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tview_change:
                float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
                displayAlertDialog("Remaining change is "+balance+Constatns.CURRENCY_SYMBOL,false);
                mApplication.saveSharedPreferenceFloatType(Constatns.BALANCE, 0.0f);
                setBalance();
                break;
            case R.id.tview_insert_coin:
                break;
            case R.id.tview_return:
                 float returnBalance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
                 displayAlertDialog("Returned Balance is "+returnBalance+Constatns.CURRENCY_SYMBOL,true);
                 mApplication.saveSharedPreferenceFloatType(Constatns.BALANCE, 0.0f);
                 setBalance();
                break;
            case R.id.btn_proceed:
                processProductSelction();
                break;
        }

    }

    private void processProductSelction() {
        float balance = mApplication.getSharedPreferenceFloatValue(Constatns.BALANCE);
        Log.e(TAG, "Applicaiton object is " + mApplication);
        Log.e(TAG, "Product map  object is " + mApplication.mProductCountMap);
        Log.e(TAG, "mSelectedProduct object is " + mSelectedProduct);
        Log.e(TAG, "product  count is  " + mApplication.mProductCountMap.get(mSelectedProduct));
        Log.e(TAG, "balance is  " + balance);
        if (mApplication != null && mApplication.mProductCountMap != null) {
            if (mApplication.mProductCountMap.get(mSelectedProduct) <= 0) {
//stock is not there
                displayAlertDialog(mSelectedProduct + " is SOLD OUT ", false);

            } else {
                float productPrice = mApplication.mProductPriceMap.get(mSelectedProduct);
                if (balance >= productPrice) {
                    float newBalance = balance - productPrice;
                    mApplication.saveSharedPreferenceFloatType(Constatns.BALANCE, newBalance);
                    int stackCount = mApplication.mProductCountMap.get(mSelectedProduct);
                    mApplication.mProductCountMap.put(mSelectedProduct, stackCount - 1);
                    setBalance();
                    showSnackbar("THANK YOU.");
                } else if (balance == 0) {
// balance is zero
                    finish();
                } else if (balance < productPrice) {
// balance is low
                    displayAlertDialog("Product Price is " + productPrice + Constatns.CURRENCY_SYMBOL + "\n" + "Balance is " + balance + Constatns.CURRENCY_SYMBOL, false);
                }
            }
        }
    }

    private void showSnackbar(String pText) {
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, pText, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(Color.parseColor("#125688"));
        snackbar.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        mSelectedProduct = mProductList[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void displayAlertDialog(String pMessage, boolean isInsertButtonRequired) {
        // final AlertDialog alertDialog = null;
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(pMessage);
        builder.setPositiveButton("OK", this);
        if (isInsertButtonRequired)
            builder.setNegativeButton("Insert Coin", this);
        builder.show();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case BUTTON_POSITIVE:
                if (dialogInterface != null)
                    dialogInterface.dismiss();
                break;
            case BUTTON_NEGATIVE:
                finish();
                break;
        }

    }
}
