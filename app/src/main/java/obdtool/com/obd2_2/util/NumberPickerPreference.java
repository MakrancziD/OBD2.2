//package obdtool.com.obd2_2.util;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.preference.DialogPreference;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.NumberPicker;
//
//import obdtool.com.obd2_2.R;
//
///**
// * Created by Maki on 2017. 04. 18..
// */
//
//public class NumberPickerPreference extends DialogPreference {
//
//    // allowed range
//    private static final int MAX_VALUE = 12;
//    private static final int MIN_VALUE = 0;
//    private static final int DEFAULT_VALUE = 100;
//    // enable or disable the 'circular behavior'
//    private static final boolean WRAP_SELECTOR_WHEEL = true;
//
//    private static String[] valueSet = {"10", "20", "50", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};
//
//    private NumberPicker picker;
//    private int value;
//
//    public NumberPickerPreference(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    protected View onCreateDialogView() {
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.CENTER;
//
//        picker = new NumberPicker(getContext());
//        picker.setLayoutParams(layoutParams);
//
//        FrameLayout dialogView = new FrameLayout(getContext());
//        dialogView.addView(picker);
//
//        return dialogView;
//    }
//
//    @Override
//    protected void onBindDialogView(View view) {
//        super.onBindDialogView(view);
//        picker.setMinValue(MIN_VALUE);
//        picker.setMaxValue(MAX_VALUE);
//        picker.setDisplayedValues(valueSet);
//        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
//        picker.setValue(getValue());
//    }
//
//    @Override
//    protected void onDialogClosed(boolean positiveResult) {
//        if (positiveResult) {
//            picker.clearFocus();
//            int newValue = picker.getValue();
//            if (callChangeListener(newValue)) {
//                setValue(newValue);
//            }
//        }
//    }
//
//    @Override
//    protected Object onGetDefaultValue(TypedArray a, int index) {
//        return a.getInt(index, MIN_VALUE);
//    }
//
//    @Override
//    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
//        int i = getPersistedInt(MIN_VALUE);
//        setValue(restorePersistedValue ? i : DEFAULT_VALUE);
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//        persistInt(this.value);
//    }
//
//    public int getValue() {
//        return this.value;
//    }
//}
