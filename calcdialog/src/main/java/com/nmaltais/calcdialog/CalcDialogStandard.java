/*
 * Copyright (c) 2018 Nicolas Maltais
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.nmaltais.calcdialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


/**
 * Fragment (Support) with calculator for entering and calculating a number
 */
public class CalcDialogStandard extends CalcDialogFragment {
    private static final String TAG = CalcDialogStandard.class.getSimpleName();

    @NonNull
    @Override
    public String getTitle() {
        return "Standard";
    }

    public static CalcDialogStandard newInstance(int requestCode){
        CalcDialogStandard fragment = new CalcDialogStandard();
        fragment.instantiate(requestCode);
        return fragment;
    }

    ////////// LIFECYCLE METHODS //////////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle state) {
        final View view = inflater.inflate(R.layout.dialog_calc_standard, root, false);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout header = view.findViewById(R.id.calc_layout_header);
            header.setBackgroundResource(R.drawable.calc_bg_elevation);
        }

        // Value display
        displayTxv = view.findViewById(R.id.calc_txv_value);
        displayTxv.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(final View v){
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("calc result", displayTxv.getText());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, clip.getItemAt(0).getText() + " has been copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Erase button
        CalcEraseButton eraseBtn = view.findViewById(R.id.calc_btn_erase);
        eraseBtn.setOnEraseListener(new CalcEraseButton.EraseListener() {
            @Override
            public void onErase() {
                presenter.onErasedOnce();
            }

            @Override
            public void onEraseAll() {
                presenter.onErasedAll();
            }
        });

        // Digit buttons
        for (int i = 0; i < 10; i++) {
            TextView digitBtn = view.findViewById(DIGIT_BTN_IDS[i]);
            digitBtn.setText(btnTexts[i]);

            final int digit = i;
            digitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onDigitBtnClicked(digit);
                }
            });
        }

        // Operator buttons
        for (int i = 0; i < 4; i++) {
            final int op = i;
            TextView operatorBtn = view.findViewById(OPERATOR_BTN_IDS[i]);
            operatorBtn.setText(btnTexts[i + 10]);
            operatorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onOperatorBtnClicked(op);
                }
            });
        }

        // Decimal separator button
        decimalSepBtn = view.findViewById(R.id.calc_btn_decimal);
        decimalSepBtn.setText(btnTexts[15]);
        decimalSepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDecimalSepBtnClicked();
            }
        });

        // Sign button: +/-
        signBtn = view.findViewById(R.id.calc_btn_sign);
        signBtn.setText(btnTexts[14]);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSignBtnClicked();
            }
        });

        // Equal button
        equalBtn = view.findViewById(R.id.calc_btn_equal);
        equalBtn.setText(btnTexts[16]);
        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEqualBtnClicked();
            }
        });

        // Answer button
        answerBtn = view.findViewById(R.id.calc_btn_answer);
        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAnswerBtnClicked();
            }
        });

        // Dialog buttons
        Button clearBtn = view.findViewById(R.id.calc_btn_clear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClearBtnClicked();
            }
        });

        Button cancelBtn = view.findViewById(R.id.calc_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCancelBtnClicked();
            }
        });

        Button okBtn = view.findViewById(R.id.calc_btn_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onOkBtnClicked();
            }
        });

        // Presenter
        presenter = new CalcPresenter();
        presenter.attach(CalcDialogStandard.this, state);

        if (state != null) {
            settings.readFromBundle(state);
            displayTxv.setText(state.getString("displayText"));
        }

        return view;
    }

    ////////// VIEW METHODS //////////
    public CalcSettings getSettings() {
        return settings;
    }

    public Locale getDefaultLocale() {
        return CalcDialogUtils.getDefaultLocale(context);
    }

    @Override
    public void exit() {
        //dismissAllowingStateLoss();
    }
}