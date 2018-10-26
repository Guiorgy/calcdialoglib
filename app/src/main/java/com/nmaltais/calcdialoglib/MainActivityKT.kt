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

package com.nmaltais.calcdialoglib

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView

import com.nmaltais.calcdialog.CalcDialog
import com.nmaltais.calcdialog.CalcDialogStandard

import java.math.BigDecimal

class MainActivityKT : AppCompatActivity(), CalcDialog.CalcDialogCallback {

    private var valueTxv: TextView? = null
    private var signChk: CheckBox? = null

    private var value: BigDecimal? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_main)

        if (state != null) {
            val valueStr = state.getString("value")
            if (valueStr != null) {
                value = BigDecimal(valueStr)
            }
        }

        val calcDialog = CalcDialog.newInstance(DIALOG_REQUEST_CODE)

        signChk = findViewById(R.id.chk_change_sign)
        if (value == null) signChk!!.isEnabled = false

        val showAnswerChk = findViewById<CheckBox>(R.id.chk_answer_btn)
        val showSignChk = findViewById<CheckBox>(R.id.chk_show_sign)
        val clearOnOpChk = findViewById<CheckBox>(R.id.chk_clear_operation)
        val showZeroChk = findViewById<CheckBox>(R.id.chk_show_zero)

        // Max value
        val maxValChk = findViewById<CheckBox>(R.id.chk_max_value)
        val maxValEdt = findViewById<EditText>(R.id.edt_max_value)
        maxValChk.setOnCheckedChangeListener { buttonView, isChecked -> maxValEdt.isEnabled = isChecked }
        maxValEdt.isEnabled = maxValChk.isChecked
        maxValEdt.setText(10000000000L.toString())

        // Max integer digits
        val maxIntChk = findViewById<CheckBox>(R.id.chk_max_int)
        val maxIntEdt = findViewById<EditText>(R.id.edt_max_int)
        maxIntChk.setOnCheckedChangeListener { buttonView, isChecked -> maxIntEdt.isEnabled = isChecked }
        maxIntEdt.isEnabled = maxIntChk.isChecked
        maxIntEdt.setText(10.toString())

        // Max fractional digits
        val maxFracChk = findViewById<CheckBox>(R.id.chk_max_frac)
        val maxFracEdt = findViewById<EditText>(R.id.edt_max_frac)
        maxIntChk.setOnCheckedChangeListener { buttonView, isChecked -> maxFracEdt.isEnabled = isChecked }
        maxFracEdt.isEnabled = maxFracChk.isChecked
        maxFracEdt.setText(8.toString())

        // Value display
        valueTxv = findViewById(R.id.txv_result)
        valueTxv!!.text = if (value == null) getString(R.string.result_value_none) else value!!.toPlainString()

        // Open dialog button
        val openBtn = findViewById<Button>(R.id.btn_open)
        openBtn.setOnClickListener {
            val signCanBeChanged = !signChk!!.isEnabled || signChk!!.isChecked

            val maxValueStr = maxValEdt.text.toString()
            val maxValue = if (maxValChk.isChecked && !maxValueStr.isEmpty())
                BigDecimal(maxValueStr)
            else
                null

            val maxIntStr = maxIntEdt.text.toString()
            val maxInt = if (maxIntChk.isChecked && !maxIntStr.isEmpty())
                Integer.valueOf(maxIntStr)
            else
                CalcDialog.MAX_DIGITS_UNLIMITED

            val maxFracStr = maxFracEdt.text.toString()
            val maxFrac = if (maxFracChk.isChecked && !maxFracStr.isEmpty())
                Integer.valueOf(maxFracStr)
            else
                CalcDialog.MAX_DIGITS_UNLIMITED

            // Set settings and value
            calcDialog.setValue(value)
                    .setShowSignButton(showSignChk.isChecked)
                    .setShowAnswerButton(showAnswerChk.isChecked)
                    .setSignCanBeChanged(signCanBeChanged, if (signCanBeChanged) 0 else value!!.signum())
                    .setClearDisplayOnOperation(clearOnOpChk.isChecked)
                    .setShowZeroWhenNoValue(showZeroChk.isChecked)
                    .setMaxValue(maxValue)
                    .setMaxDigits(maxInt, maxFrac)

            val fm = supportFragmentManager
            if (fm.findFragmentByTag("calc_dialog") == null) {
                calcDialog.show(fm, "calc_dialog")
            }
        }
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)

        if (value != null) {
            state.putString("value", value!!.toString())
        }
    }

    override fun onValueEntered(requestCode: Int, value: BigDecimal) {
        // if (requestCode == DIALOG_REQUEST_CODE) {}  <-- If there's many dialogs

        this.value = value

        valueTxv!!.text = value.toPlainString()
        signChk!!.isEnabled = value.compareTo(BigDecimal.ZERO) != 0
    }

    companion object {

        private val TAG = MainActivityKT::class.java.simpleName

        private val DIALOG_REQUEST_CODE = 0
    }
}