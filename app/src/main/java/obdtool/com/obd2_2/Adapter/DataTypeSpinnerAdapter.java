package obdtool.com.obd2_2.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.util.DataTypeItem;

/**
 * Created by Maki on 2017. 05. 02..
 */

public class DataTypeSpinnerAdapter extends ArrayAdapter<DataTypeItem> {

    private Context ctx;
    private List<DataTypeItem> itemList;
    private boolean isFromView = false;

    public DataTypeSpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DataTypeItem> objects) {
        super(context, resource, objects);
        this.ctx=context;
        this.itemList = objects;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
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
                int getPosition = (Integer) buttonView.getTag();


            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
