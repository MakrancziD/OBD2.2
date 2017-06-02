package obdtool.com.obd2_2.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.Fragment.LiveFragment;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.util.DataTypeItem;

public class DataTypeSpinnerAdapter extends ArrayAdapter<DataTypeItem> {

    private Context ctx;
    private List<DataTypeItem> itemList;
    private boolean isFromView = false;
    private LiveFragment fragment;

    public DataTypeSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DataTypeItem> objects, LiveFragment fragment) {
        super(context, resource, objects);
        this.ctx=context;
        this.itemList = objects;
        this.fragment=fragment;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(ctx);
            convertView = layoutInflator.inflate(R.layout.live_spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.txt_type);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.chk_spinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(itemList.get(position).getType());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(itemList.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    int getPosition = (Integer) buttonView.getTag();
                    DataTypeItem item = itemList.get(getPosition);
                    String command = itemList.get(getPosition).getType();
                    if (!command.equals(ctx.getString(R.string.select_data))) {
                        if (command.equals("Accelerometer")) {
                            fragment.setAcc(isChecked);
                        } else if (command.equals("GPS")) {
                            fragment.setGps(isChecked);
                        } else if (isChecked) {
                            fragment.addCommand(command);
                        } else {
                            fragment.removeCommand(command);
                        }
                        item.setSelected(isChecked);
                    }
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
