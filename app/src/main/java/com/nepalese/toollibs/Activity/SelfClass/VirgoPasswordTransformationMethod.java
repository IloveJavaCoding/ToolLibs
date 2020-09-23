package com.nepalese.toollibs.Activity.SelfClass;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class VirgoPasswordTransformationMethod extends PasswordTransformationMethod {
    private char mask;
    public VirgoPasswordTransformationMethod(char mask){
        if(mask==' '){
            this.mask = '*';//default mask
        }else{
            this.mask = mask;
        }
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            this.mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return mask; // This is the important part
        }

        public int length() {
            return mSource.length(); // Return default
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}
