package com.sample.k.androidsampleaidl;

import java.io.Serializable;



public class CalculatorElement implements Serializable {
    private static final long serialVersionUID = 5415786969637871274L;
    public static final int NUMBER = 0;
    public static final int OPERATOR = 1;
    public int mKind;
    public int mValue;

    @Override
    public String toString() {
        String kind;
        String value;
        if (mKind == NUMBER) {
            kind = "NUMBER";
            value = "" + (char) mValue;
        } else {
            kind = "OPERATOR";
            value = "" + mValue;
        }
        return "mKind=[" + kind + "] mValue=[" + value + "]";
    }

}
