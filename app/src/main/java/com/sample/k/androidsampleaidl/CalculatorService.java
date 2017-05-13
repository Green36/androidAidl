package com.sample.k.androidsampleaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

public class CalculatorService extends Service {
    public CalculatorService() {
    }

    private ICalculatorService.Stub mStub = new ICalculatorService.Stub() {

        @Override
        public void registerCallback(ICalculatorCallback callback)
        {}

        @Override
        public void unregisterCallback(ICalculatorCallback callback)
        {}

        @Override
        public int add(int lhs, int rhs) throws RemoteException {
            return lhs + rhs;
        }

        @Override
        public void sum(List values)
        {
        }

        @Override
        public void rotate(int[] array, int num)
        {

        }

    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
