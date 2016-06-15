package com.shoppingcatnumber.demo;

import java.text.DecimalFormat;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName ShoppingcartMsgDialog
 * @Description 购物车修改购买数量对话框
 * @author xuxiang
 * @date 2013-1-5
 */
public class ProductNumberDialog implements OnClickListener, TextWatcher {

    private Dialog mDialog;
    private TextView mPriceTv;
    private View mMinusBtn;
    private View mAddBtn;
    private EditText mNumberTv;
    private Button mConfirmBtn;
    private Button mCancelBtn;

    private double itemPrice = 0.0;
    private int initNumber = 0;
    private OnNumberListener onNumberListener;
    private boolean isSetting = false;
    private Context context;

    public ProductNumberDialog(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mDialog = new Dialog(context, R.style.dialog_style);
        mDialog.setCancelable(false);

        View view = LayoutInflater.from(context).inflate(R.layout.home_shoppingcart_msgbox, null);
        mDialog.setContentView(view);
        mPriceTv = (TextView) view.findViewById(R.id.price);
        mMinusBtn = view.findViewById(R.id.minus);
        mAddBtn = view.findViewById(R.id.add);
        mNumberTv = (EditText) view.findViewById(R.id.number);
        mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);
        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        mNumberTv.addTextChangedListener(this);
        mMinusBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mDialog.show();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    String backup = "0";
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isSetting) {
            return;
        }
        isSetting = true;
        String result = mNumberTv.getText().toString().trim();
        if (TextUtils.isEmpty(result) || !TextUtils.isDigitsOnly(result)) {
            Toast.makeText(context, "购买数量输入有误", Toast.LENGTH_SHORT).show();
            mNumberTv.setText("");
            mNumberTv.setSelection(0);
            isSetting = false;
            return;
        } 
        if (Integer.valueOf(result) == 0) {
            Toast.makeText(context, "购买数量不能小于1", Toast.LENGTH_SHORT).show();
            result = "1";
        } 
        int number = Integer.valueOf(result);
        if (number > 10000) {
            Toast.makeText(context, "购买数量不能大于10000", Toast.LENGTH_SHORT).show();
            number = 10000;
        }
        refreshView(number); 
        this.onNumberListener.onNumberSet(number);
    }

    private void refreshView(int number) {
        this.mNumberTv.setText(String.valueOf(number));
        int len = mNumberTv.getText().toString().trim().length();
        this.mNumberTv.setSelection(len);
        
		double total = (itemPrice * 100 * number / 100);
		Double m = Double.parseDouble(String.valueOf(total));
		DecimalFormat df = new DecimalFormat("0.00");
        this.mPriceTv.setText(df.format(m)+ "元");
        
        this.isSetting = false;
    }

    @Override
    public void onClick(View v) {
        if (mConfirmBtn == v) {// 确定
            mDialog.dismiss();
        } else if (mCancelBtn == v) {// 取消,恢复初始数量
            mDialog.dismiss();
            onNumberListener.onNumberSet(initNumber);
        } else {
            isSetting = true;
            String text = mNumberTv.getText().toString();
            if(TextUtils.isEmpty(text) || !TextUtils.isDigitsOnly(text)) {
                text = "0";
            }
            int number = Integer.valueOf(text);
            if (mMinusBtn == v) {// 数量减少
                number--;
                if (number < 1) {
                    number = 1;
                }
            } else {// 数量增加
                number++;
            }
            if (number > 10000) {
                Toast.makeText(context, "购买数量不能大于10000", Toast.LENGTH_SHORT).show();
                number = 10000;
            }
            refreshView(number);
            this.onNumberListener.onNumberSet(number);
        }
    }

    /**
     * 初始化数据及监听
     * 
     * @param number
     * @param price
     * @param li
     */
    public void setOnNumberListener(int number, double price, OnNumberListener li) {
        this.onNumberListener = li;
        this.itemPrice = price;
        this.initNumber = number;

        this.isSetting = true;
        refreshView(number);
    }

    public void show() {
        this.mDialog.show();
    }

    public void dismiss() {
        this.mDialog.dismiss();
    }

    public interface OnNumberListener {
        void onNumberSet(int number);
    }

}
