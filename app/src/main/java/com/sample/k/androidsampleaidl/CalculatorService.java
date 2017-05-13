package com.sample.k.androidsampleaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

public class CalculatorService extends Service {
    private final String TAG = "CalculatorService";

    public CalculatorService() {
    }

    private ICalculatorService.Stub mStub = new ICalculatorService.Stub() {

        @Override
        public void registerCallback(ICalculatorCallback callback) {
        }

        @Override
        public void unregisterCallback(ICalculatorCallback callback) {
        }

        @Override
        public int add(int lhs, int rhs) throws RemoteException {
            return lhs + rhs;
        }

        @Override
        public void sum(List values) {
        }

        @Override
        public void rotate(int[] array, int num) {

        }

        @Override
        public int eval(CalculatorExpression exp) throws RemoteException
        {
            Log.d(TAG, "eval:" + (char) exp.mOp.mValue);
            int result;
            switch (exp.mOp.mValue) {
                case '+':
                    result = exp.mLhs.mValue + exp.mRhs.mValue;
                    break;
                case '-':
                    result = exp.mLhs.mValue - exp.mRhs.mValue;
                    break;
                case '*':
                    result = exp.mLhs.mValue * exp.mRhs.mValue;
                    break;
                case '/':
                    result = exp.mLhs.mValue / exp.mRhs.mValue;
                    break;
                default:
                    result = 0;
                    break;
            }
            return result;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mStub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean result = super.onUnbind(intent);
        Log.d(TAG, "onUnbind:" + result);
        return result;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

}
