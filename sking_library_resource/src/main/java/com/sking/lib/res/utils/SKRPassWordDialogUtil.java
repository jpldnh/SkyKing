package com.sking.lib.res.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sking.lib.interfaces.SKInterfaceListtener;
import com.sking.lib.res.R;
import com.sking.lib.res.classes.SKRPassWordDialogTextWatcher;

/**
 * Created by 谁说青春不能错 on 2016/12/6.
 */

public class SKRPassWordDialogUtil {

    private Context mContext;
    private Dialog mDialog;
    private SKRAnimalUitl animalUitl = SKRAnimalUitl.getInstance();
    private LinearLayout rootView;
    private TextView topTextView;
    private Button submitButton;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6;
    private SKInterfaceListtener.SKOnStringBackClickListener onClickListener;

    public SKRPassWordDialogUtil(Context context) {
        this.mContext = context;
        mDialog = new Dialog(mContext, R.style.SKR_Style_Dialog);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.skr_layout_dialog_password, null);
        rootView = (LinearLayout) view.findViewById(R.id.root_view);
        topTextView = (TextView) view.findViewById(R.id.dialog_text);
        submitButton = (Button) view.findViewById(R.id.dialog_bottom_confirm_butt);
        textView1 = (TextView) view.findViewById(R.id.password_first);
        textView2 = (TextView) view.findViewById(R.id.password_second);
        textView3 = (TextView) view.findViewById(R.id.password_third);
        textView4 = (TextView) view.findViewById(R.id.password_forth);
        textView5 = (TextView) view.findViewById(R.id.password_five);
        textView6 = (TextView) view.findViewById(R.id.password_six);
        setEdiTextListener();
        animalUitl.viewCenterIn(mContext, view);
        mDialog.setCancelable(false);
        mDialog.setContentView(view);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancalInput();
                String password = textView1.getText().toString() + textView2.getText().toString() + textView3.getText().toString() + textView4.getText().toString() + textView5.getText().toString() + textView6.getText().toString();
                mDialog.dismiss();
                if (onClickListener != null)
                    onClickListener.onBackResult(password);
            }
        });
    }

    public void show()
    {
        if(mDialog!=null)
            mDialog.show();
    }

    public void cancle()
    {
        if(mDialog!=null)
            mDialog.cancel();
    }

    public void setText(String text) {
        topTextView.setText(text);
    }

    public void setText(int textID) {
        topTextView.setText(textID);
    }

    public void setButtonText(String text) {
        submitButton.setText(text);
    }

    public void setButtonText(int textID) {
        submitButton.setText(textID);
    }

    public void setTextColor(int color) {
        topTextView.setTextColor(color);
    }

    public void setButtonTextColor(int color) {
        submitButton.setTextColor(color);
    }

    public void setButtonBackground(int resId) {
        submitButton.setBackgroundResource(resId);
    }

    public void setRootViewBackground(int resId) {
        rootView.setBackgroundResource(resId);
    }

    public void setButtonOnClickListener(SKInterfaceListtener.SKOnStringBackClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public SKInterfaceListtener.SKOnStringBackClickListener getButtonOnClickListener() {
        return onClickListener;
    }


    private void setEdiTextListener() {
        textView1.addTextChangedListener(new SKRPassWordDialogTextWatcher(mContext, 1, textView1, textView2));
        textView2.addTextChangedListener(new SKRPassWordDialogTextWatcher(mContext, 2, textView2, textView3));
        textView3.addTextChangedListener(new SKRPassWordDialogTextWatcher(mContext, 3, textView3, textView4));
        textView4.addTextChangedListener(new SKRPassWordDialogTextWatcher(mContext, 4, textView4, textView5));
        textView5.addTextChangedListener(new SKRPassWordDialogTextWatcher(mContext, 5, textView5, textView6));
        textView6.addTextChangedListener(new SKRPassWordDialogTextWatcher(mContext, 6, textView6, textView6));

        textView2.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int state = -1;
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    state = textView2.getText().toString().length();
                }
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && state == 0) {
                    textView1.requestFocus();
                    textView1.setText("");
                }
                return false;
            }
        });
        textView3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int state = -1;
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    state = textView3.getText().toString().length();
                }
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && state == 0) {
                    textView2.requestFocus();
                    textView2.setText("");
                }
                return false;
            }
        });
        textView4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int state = -1;
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    state = textView4.getText().toString().length();
                }
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && state == 0) {
                    textView3.requestFocus();
                    textView3.setText("");
                }
                return false;
            }
        });
        textView5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int state = -1;
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    state = textView5.getText().toString().length();
                }
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && state == 0) {
                    textView4.requestFocus();
                    textView4.setText("");
                }
                return false;
            }
        });
        textView6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int state = -1;
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    state = textView6.getText().toString().length();
                }
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && state == 0) {
                    textView5.requestFocus();
                    textView5.setText("");
                }
                return false;
            }
        });
    }

    //关闭输入法
    private void cancalInput() {
        InputMethodManager mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(topTextView.getWindowToken(), 0);
    }

}
