package com.nmaltais.calcdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

abstract class CalcDialogFragment extends Fragment {
    private static final String TAG = CalcDialogFragment.class.getSimpleName();

    /**
     * Parameter value to set for {@link #setMaxDigits(int, int)}
     * to have to limit on the number of digits for a part of the number (int or frac).
     */
    public static final int MAX_DIGITS_UNLIMITED = -1;

    /**
     * Parameter to set for {@link #setFormatSymbols(char, char)}
     * to use default locale's format symbol.
     * This is the default value for both decimal and group separators
     */
    public static final char FORMAT_CHAR_DEFAULT = 0;

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
    };

    protected Context context;
    protected CalcPresenter presenter;

    protected CalcSettings settings;

    protected TextView displayTxv;
    protected TextView decimalSepBtn;
    protected TextView equalBtn;
    protected TextView answerBtn;
    protected TextView signBtn;

    protected CharSequence[] btnTexts;
    protected CharSequence[] errorMessages;
    protected int[] maxDialogDimensions;

    @NonNull
    public abstract String getTitle();

    /**
     * Do not use the constructor directly for creating
     * an instance, use {@link #newInstance(int)} instead
     */
    protected CalcDialogFragment() {
        settings = new CalcSettings();
    }

    protected void instantiate(int requestCode){
        this.settings.requestCode = requestCode;
    }

    /**
     * Create a new instance of CalcDialog
     * @param requestCode request code used by callback
     *                    Useful in case there's multiple dialogs at the same time
     * @return the dialog
     */
    public static CalcDialogFragment newInstance(int requestCode){
        return null;
    }

    ////////// LIFECYCLE METHODS //////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Wrap calculator dialog's theme to context
        TypedArray ta = context.obtainStyledAttributes(new int[]{R.attr.calcDialogStyle});
        int style = ta.getResourceId(0, R.style.CalcDialogStyle);
        ta.recycle();
        this.context = new ContextThemeWrapper(context, style);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        // Get strings
        TypedArray ta = context.obtainStyledAttributes(R.styleable.CalcDialog);
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

    public void onDismiss(DialogInterface dialog) {
        //super.onDismiss(dialog);
        if (presenter != null) {
            // On config change, presenter is detached before this is called
            presenter.onDismissed();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        presenter.writeStateToBundle(state);
        settings.writeToBundle(state);

        state.putString("displayText", displayTxv.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detach();

        presenter = null;
        context = null;
    }

    @Nullable
    public CalcDialogCallback getCallback() {
        CalcDialogCallback cb = null;
        if (getParentFragment() != null) {
            try {
                cb = (CalcDialogCallback) getParentFragment();
            } catch (Exception e) {
                // Interface callback is not implemented in fragment
            }
        } else if (getTargetFragment() != null) {
            try {
                cb = (CalcDialogCallback) getTargetFragment();
            } catch (Exception e) {
                // Interface callback is not implemented in fragment
            }
        } else {
            // Caller was an activity
            try {
                cb = (CalcDialogCallback) requireActivity();
            } catch (Exception e) {
                // Interface callback is not implemented in activity
            }
        }
        return cb;
    }

    ////////// VIEW METHODS //////////
    public CalcSettings getSettings() {
        return settings;
    }

    public Locale getDefaultLocale() {
        return CalcDialogUtils.getDefaultLocale(context);
    }

    public abstract void exit();

    public void sendValueResult(BigDecimal value) {
        CalcDialogCallback cb = getCallback();
        if (cb != null) {
            cb.onValueEntered(settings.requestCode, value);
        }
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

    public void displayValueText(String text) {
        displayTxv.setText(text);
    }

    public void displayErrorText(int error) {
        displayTxv.setText(errorMessages[error]);
    }

    public void displayAnswerText() {
        displayTxv.setText(R.string.calc_answer);
    }

    ////////// CALCULATOR SETTINGS //////////
    /**
     * Set initial value to show
     * By default, initial value is null. That means value is 0 but if
     * {@link #setShowZeroWhenNoValue(boolean)} is set to true, no value will be shown.
     * @param value Initial value to display. Setting null will result in 0
     * @return the dialog
     */
    public CalcDialogFragment setValue(@Nullable BigDecimal value) {
        settings.setValue(value);
        return this;
    }

    /**
     * Set maximum value that can be calculated
     * If maximum value is exceeded, an "Out of bounds" error will be shown.
     * Maximum value is effective both for positive and negative values.
     * Default maximum is 10,000,000,000 (1e+10)
     * @param maxValue Maximum value, use null for no maximum
     * @return the dialog
     */
    public CalcDialogFragment setMaxValue(@Nullable BigDecimal maxValue) {
        settings.setMaxValue(maxValue);
        return this;
    }

    /**
     * Set max digits that can be entered on the calculator
     * Use {@link #MAX_DIGITS_UNLIMITED} for no limit
     * @param intPart Max digits for the integer part
     * @param fracPart Max digits for the fractional part.
     *                 A value of 0 means the value can't have a fractional part
     * @return the dialog
     */
    public CalcDialogFragment setMaxDigits(int intPart, int fracPart) {
        settings.setMaxDigits(intPart, fracPart);
        return this;
    }

    /**
     * Set calculator's rounding mode
     * Default rounding mode is RoundingMode.HALF_UP
     * Ex: 5.5 = 6, 5.49 = 5, -5.5 = -6, 2.5 = 3
     * @param roundingMode one of {@link RoundingMode}, except {@link RoundingMode#UNNECESSARY}
     * @return the dialog
     */
    public CalcDialogFragment setRoundingMode(RoundingMode roundingMode) {
        settings.setRoundingMode(roundingMode);
        return this;
    }

    /**
     * Set whether sign can be changed or not
     * By default, sign can be changed
     * @param canBeChanged whether sign can be changed or not
     *                     if true, dialog can't be confirmed with a value of wrong sign
     *                     and an error will be shown
     * @param sign if canBeChanged is true, sign to force, -1 or 1
     *             otherwise use any value
     * @return the dialog
     */
    public CalcDialogFragment setSignCanBeChanged(boolean canBeChanged, int sign) {
        settings.setSignCanBeChanged(canBeChanged, sign);
        return this;
    }

    /**
     * Set symbols for formatting number
     * Use {@link #FORMAT_CHAR_DEFAULT} to use device locale's default symbol
     * By default, formatting will use locale's symbols
     * @param decimalSep decimal separator
     * @param groupSep grouping separator
     * @return the dialog
     */
    public CalcDialogFragment setFormatSymbols(char decimalSep, char groupSep) {
        settings.setFormatSymbols(decimalSep, groupSep);
        return this;
    }

    /**
     * Set whether to clear display when an operation button is pressed (+, -, * and /)
     * If not, display will be cleared on next button press
     * Default is not clearing
     * @param clear whether to clear it or not
     * @return the dialog
     */
    public CalcDialogFragment setClearDisplayOnOperation(boolean clear) {
        settings.clearOnOperation = clear;
        return this;
    }

    /**
     * Set whether zero should be displayed when no value has been entered or just display nothing
     * @param show whether to show it or not
     * @return the dialog
     */
    public CalcDialogFragment setShowZeroWhenNoValue(boolean show) {
        settings.showZeroWhenNoValue = show;
        return this;
    }

    /**
     * Set the size of groups separated by group separators
     * 3 does 000,000,000
     * 4 does 0,0000,0000
     * Default size is 3
     * @param size grouping size, use 0 for no grouping
     * @return the dialog
     */
    public CalcDialogFragment setGroupSize(int size) {
        settings.setGroupSize(size);
        return this;
    }

    /**
     * Set whether to show the answer button when an operation button is clicked or not
     * This button allows the user to reuse previous answer.
     * By default, the answer button is not shown.
     * @param show whether to show it or not
     * @return the dialog
     */
    public CalcDialogFragment setShowAnswerButton(boolean show) {
        settings.showAnswerBtn = show;
        return this;
    }

    /**
     * Set whether the sign button should be shown.
     * By default, the sign button is shown.
     * @param show whether to show it or not
     * @return the dialog
     */
    public CalcDialogFragment setShowSignButton(boolean show) {
        settings.showSignBtn = show;
        return this;
    }

    public interface CalcDialogCallback{
        /**
         * Called when the dialog's OK button is clicked
         * @param value value entered.
         *              To format the value to a String, use {@link BigDecimal#toPlainString()}.
         *              To format the value to a currency String you could do:
         *              {@code NumberFormat.getCurrencyInstance(Locale).format(BigDecimal)}
         * @param requestCode dialog request code given when dialog
         *                    was created with {@link #newInstance(int)}
         */
        void onValueEntered(int requestCode, BigDecimal value);
    }
}
