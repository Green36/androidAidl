package com.sample.k.androidsampleaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class CalculatorService extends Service {
    public CalculatorService() {
    }

    private ICalculatorService.stub mStub = new ICalculatorService.stub() {
        @Override
        public int add(int lhs, int rhs) throws RemoteException {
            return lhs + rhs;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
