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

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    /**
     * Do not use the constructor directly for creating
     * an instance, use {@link #newInstance(CalcDialog)} instead
     */
    public CalcDialogStandard() { }

    static CalcDialogStandard newInstance(CalcDialog calcDialog){
        CalcDialogStandard calcDialogStandard = new CalcDialogStandard();
        calcDialogStandard.calcDialog = calcDialog;

        return calcDialogStandard;
    }

    ////////// LIFECYCLE METHODS //////////
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle state) {
        final View view = inflater.inflate(R.layout.dialog_calc_standard, root, false);

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
        for (int i = 0; i < 4; i++) {
            final int op = i;
            TextView operatorBtn = view.findViewById(OPERATOR_BTN_IDS[i]);
            operatorBtn.setText(btnTexts[i + 10]);
            operatorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onOperatorBtnClicked(op);
                }
            });
        }

        // Decimal separator button
        decimalSepBtn = view.findViewById(R.id.calc_btn_decimal);
        decimalSepBtn.setText(btnTexts[15]);
        decimalSepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onDecimalSepBtnClicked();
            }
        });

        // Sign button: +/-
        signBtn = view.findViewById(R.id.calc_btn_sign);
        signBtn.setText(btnTexts[14]);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onSignBtnClicked();
            }
        });

        // Equal button
        equalBtn = view.findViewById(R.id.calc_btn_equal);
        equalBtn.setText(btnTexts[16]);
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