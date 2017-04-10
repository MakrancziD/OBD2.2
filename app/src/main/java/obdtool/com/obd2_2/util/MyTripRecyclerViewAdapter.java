package obdtool.com.obd2_2.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import obdtool.com.obd2_2.Fragment.TripFragment;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.db.Model.Trip;

import java.util.List;

public class MyTripRecyclerViewAdapter extends RecyclerView.Adapter<MyTripRecyclerViewAdapter.ViewHolder> {

    private final List<Trip> mValues;
    private final TripFragment.OnListFragmentInteractionListener mListener;

    public MyTripRecyclerViewAdapter(List<Trip> items, TripFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trip_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(Integer.toString(mValues.get(position).getID_trip()));
        holder.mContentView.setText(mValues.get(position).getStart_time().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Trip mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
