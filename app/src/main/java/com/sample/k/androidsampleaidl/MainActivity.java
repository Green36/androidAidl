package com.sample.k.androidsampleaidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ClientActivity";
    private EditText mEditTextLhs;
    private EditText mEditTextRhs;
    private Spinner mSpinnerOp;
    private TextView mTextViewResult;
    private Button mButtonSend;
    private Handler mHandler;
    private ICalculatorService mService;
    private ICalculatorCallback mCallback = new ICalculatorCallback.Stub() {
        @Override
        public void resultSum(final int value) throws RemoteException {
            Log.d(TAG, "resultSum:" + value);
            mHandler.post(new Runnable() {
                public void run() {
                    mTextViewResult.setText("Result:" + value);
                }
            });
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mButtonSend.setEnabled(false);
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ICalculatorService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mButtonSend.setEnabled(true);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextLhs = (EditText) findViewById(R.id.editTextLhs);
        mEditTextRhs = (EditText) findViewById(R.id.editTextRhs);
        mSpinnerOp = (Spinner) findViewById(R.id.spinnerOp);
        mTextViewResult = (TextView) findViewById(R.id.textViewResult);
        mButtonSend = (Button) findViewById(R.id.buttonSend);
        mButtonSend.setEnabled(false);
        View.OnClickListener buttonSendListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onSendButtonClick(v);
                }catch (Exception e){
                    Log.d(TAG, e.toString());
                }
            }
        };
        mButtonSend.setOnClickListener(buttonSendListner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.add("+");
        adapter.add("-");
        adapter.add("*");
        adapter.add("/");
        adapter.add("sum");
        adapter.add("rotate");
        mSpinnerOp.setAdapter(adapter);
        mSpinnerOp.setSelection(5);
        mSpinnerOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {
                    mEditTextRhs.setVisibility(View.INVISIBLE);
                } else {
                    mEditTextRhs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mEditTextRhs.setVisibility(View.VISIBLE);
            }
        });
        mHandler = new Handler();
        Intent intent = new Intent(ICalculatorService.class.getName());
        intent.setPackage("com.sample.k.androidsampleaidl");
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConnection);
        }
    }

    public void onSendButtonClick(View view) throws NumberFormatException, RemoteException {
        String lhs = mEditTextLhs.getText().toString();
        String rhs = mEditTextRhs.getText().toString();
        String op = mSpinnerOp.getSelectedItem().toString();
        String s = "";
        if (op.equals("+")) {
            lhs = new StringTokenizer(lhs, " ").nextToken();
            rhs = new StringTokenizer(rhs, " ").nextToken();
            s += mService.add(Integer.valueOf(lhs), Integer.valueOf(rhs));
        } else if (op.equals("-") || op.equals("*") || op.equals("/")) {
            lhs = new StringTokenizer(lhs, " ").nextToken();
            rhs = new StringTokenizer(rhs, " ").nextToken();
            CalculatorExpression exp = new CalculatorExpression();
            exp.mOp = new CalculatorElement();
            exp.mLhs = new CalculatorElement();
            exp.mRhs = new CalculatorElement();

            exp.mOp.mKind = CalculatorElement.OPERATOR;
            exp.mOp.mValue = op.charAt(0);
            exp.mLhs.mValue = Integer.valueOf(lhs);
            exp.mRhs.mValue = Integer.valueOf(rhs);
            s += mService.eval(exp);
        } else if (op.equals("sum") || op.equals("rotate")) {
            StringTokenizer st  = new StringTokenizer(lhs, " ");
            List<Integer> list = new ArrayList<Integer>();
            while (st.hasMoreTokens()) {
                list.add(Integer.valueOf(st.nextToken()));
            }
            if (op.equals("sum")) {
                mService.sum(list);
                return; // does not need setText of result
            } else {
                int[] values = new int[list.size()];
                for (int i = 0; i < values.length; i++) {
                    values[i] = (Integer)list.get(i);
                    s += values[i];
                    if (i != values.length - 1) {
                        s += " ";
                    } else {
                        s += "\n";
                    }
                }
                mService.rotate(values, Integer.valueOf(rhs));
                for (int i = 0; i < values.length; i++) {
                    s += values[i];
                    if (i != values.length - 1) {
                        s += " ";
                    }
                }
            }
        }
        final String result = s;
        mHandler.post(new Runnable() {
            public void run() {
                mTextViewResult.setText(result);
            }
        });
    }
}
