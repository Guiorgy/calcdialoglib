package com.nmaltais.calcdialog;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalcDialogScietific extends CalcDialogFragment{
    private static final String TAG = CalcDialogScietific.class.getSimpleName();

    @NonNull
    @Override
    public String getTitle() {
        return "Scientific";
    }

    /**
     * Do not use the constructor directly for creating
     * an instance, use {@link #newInstance(CalcDialog)} instead
     */
    public CalcDialogScietific() { }

    static CalcDialogScietific newInstance(CalcDialog calcDialog){
        CalcDialogScietific calcDialogScietific = new CalcDialogScietific();
        calcDialogScietific.calcDialog = calcDialog;

        return calcDialogScietific;
    }

    ////////// LIFECYCLE METHODS //////////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle state) {
        final View view = inflater.inflate(R.layout.dialog_calc_scientific, root, false);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout header = view.findViewById(R.id.calc_layout_root);
            header.setBackgroundResource(R.drawable.calc_bg_elevation);
        }

        // Digit buttons
        for (int i = 0; i < 10; i++) {
            TextView digitBtn = view.findViewById(DIGIT_BTN_IDS[i]);
            digitBtn.setText(btnTexts[i]);

            final int digit = i;
            digitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onDigitBtnClicked(digit);
                }
            });
        }

        // Operator buttons
        for (int i = 0; i < 25; i++) {
            final int op = i;
            TextView operatorBtn = view.findViewById(OPERATOR_BTN_IDS[i]);
            operatorBtn.setText(btnTexts[i + 13]);
            if(op >= 9){
                //operatorBtn.setTextSize(0.8f);
                operatorBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            }
            operatorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onOperatorBtnClicked(op);
                }
            });
        }

        // Decimal separator button
        decimalSepBtn = view.findViewById(R.id.calc_btn_decimal);
        decimalSepBtn.setText(btnTexts[11]);
        decimalSepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onDecimalSepBtnClicked();
            }
        });

        // Sign button: +/-
        signBtn = view.findViewById(R.id.calc_btn_sign);
        signBtn.setText(btnTexts[10]);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onSignBtnClicked();
            }
        });

        // Equal button
        equalBtn = view.findViewById(R.id.calc_btn_equal);
        equalBtn.setText(btnTexts[12]);
        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onEqualBtnClicked();
            }
        });

        // Answer button
        answerBtn = view.findViewById(R.id.calc_btn_answer);
        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onAnswerBtnClicked();
            }
        });

        return view;
    }
}
