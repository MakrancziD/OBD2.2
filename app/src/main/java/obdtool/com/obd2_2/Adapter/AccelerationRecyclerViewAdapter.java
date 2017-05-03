package obdtool.com.obd2_2.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.Fragment.AccelerationFragment;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Acceleration;

public class AccelerationRecyclerViewAdapter extends RecyclerView.Adapter<AccelerationRecyclerViewAdapter.ViewHolder> {

    private AccelerationFragment.OnListFragmentInteractionListener mListener;
    private List<Acceleration> mValues;

    public AccelerationRecyclerViewAdapter(List<Acceleration> items, AccelerationFragment.OnListFragmentInteractionListener listener)
    {
        if(items!=null) {
            mValues=items;
        }
        else {
            mValues=new ArrayList<>();
        }
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_acc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mID.setText(Integer.toString(position));
        holder.mTime.setText(mValues.get(position).getTimestamp().toString());
        holder.mVehicle.setText("Def");//mValues.get(position).getVehicle_ID().getName());
        holder.mFrom.setText(Integer.toString(mValues.get(position).getFrom()));
        holder.mTo.setText(Integer.toString(mValues.get(position).getTo()));
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler.deleteAccResult(mValues.get(position));
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mID;
        TextView mTime;
        TextView mVehicle;
        TextView mFrom;
        TextView mTo;
        ImageButton mDelete;
        Acceleration mItem;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mID = (TextView) itemView.findViewById(R.id.txt_acc_top);
            mTime = (TextView) itemView.findViewById(R.id.txt_acc_time);
            mVehicle = (TextView) itemView.findViewById(R.id.txtVeh);
            mFrom = (TextView) itemView.findViewById(R.id.txtFrom);
            mTo = (TextView) itemView.findViewById(R.id.txtTo);
            mDelete = (ImageButton) itemView.findViewById(R.id.btnDeleteAcc);
        }
    }
}
