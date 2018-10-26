package com.nmaltais.calcdialog;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

abstract class CalcDialogFragment extends Fragment {
    private static final String TAG = CalcDialogFragment.class.getSimpleName();

    protected static final int[] DIGIT_BTN_IDS = {
            R.id.calc_btn_0,
            R.id.calc_btn_1,
            R.id.calc_btn_2,
            R.id.calc_btn_3,
            R.id.calc_btn_4,
            R.id.calc_btn_5,
            R.id.calc_btn_6,
            R.id.calc_btn_7,
            R.id.calc_btn_8,
            R.id.calc_btn_9,
    };

    protected static final int[] OPERATOR_BTN_IDS = {
            R.id.calc_btn_add,
            R.id.calc_btn_sub,
            R.id.calc_btn_mult,
            R.id.calc_btn_div,
            R.id.calc_btn_parentheses_right,
            R.id.calc_btn_parentheses_left,
            R.id.calc_btn_factorial,
            R.id.calc_btn_pi,
            R.id.calc_btn_shift,
            R.id.calc_btn_square,
            R.id.calc_btn_power,
            R.id.calc_btn_sin,
            R.id.calc_btn_cos,
            R.id.calc_btn_tan,
            R.id.calc_btn_sqrt,
            R.id.calc_btn_power_of_ten,
            R.id.calc_btn_log,
            R.id.calc_btn_exp,
            R.id.calc_btn_mod,
            R.id.calc_btn_cube,
            R.id.calc_btn_root,
            R.id.calc_btn_sin_1,
            R.id.calc_btn_cos_1,
            R.id.calc_btn_tan_1,
            R.id.calc_btn_reverse,
            R.id.calc_btn_power_of_exp,
    };

    protected CalcDialog calcDialog;

    protected TextView decimalSepBtn;
    protected TextView equalBtn;
    protected TextView answerBtn;
    protected TextView signBtn;

    protected CharSequence[] btnTexts;
    protected CharSequence[] errorMessages;
    protected int[] maxDialogDimensions;

    @NonNull
    public abstract String getTitle();

    protected CalcDialogFragment() { }

    protected CalcDialogFragment(CalcDialog calcDialog) {
        this();
        this.calcDialog = calcDialog;
    }

    ////////// LIFECYCLE METHODS //////////
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Get strings
        TypedArray ta = calcDialog.getContext().obtainStyledAttributes(R.styleable.CalcDialog);
        btnTexts = ta.getTextArray(R.styleable.CalcDialog_calcButtonTexts);
        errorMessages = ta.getTextArray(R.styleable.CalcDialog_calcErrors);
        maxDialogDimensions = new int[]{
                ta.getDimensionPixelSize(R.styleable.CalcDialog_calcDialogMaxWidth, -1),
                ta.getDimensionPixelSize(R.styleable.CalcDialog_calcDialogMaxHeight, -1)
        };
        ta.recycle();
    }

    @Nullable
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle state);

    @Override
    public void onDetach() {
        super.onDetach();
        calcDialog = null;
    }

    ////////// VIEW METHODS //////////
    public CalcSettings getSettings() {
        return calcDialog.getSettings();
    }

    public CalcPresenter getPresenter() {
        return calcDialog.getPresenter();
    }

    public Locale getDefaultLocale() {
        return CalcDialogUtils.getDefaultLocale(calcDialog.getContext());
    }

    public void exit() {
        calcDialog.exit();
    }

    public void setAnswerBtnVisible(boolean visible) {
        answerBtn.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setEqualBtnVisible(boolean visible) {
        equalBtn.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setSignBtnVisible(boolean visible) {
        signBtn.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setDecimalSepBtnEnabled(boolean enabled) {
        decimalSepBtn.setEnabled(enabled);
    }
}