package obdtool.com.obd2_2.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import obdtool.com.obd2_2.Fragment.VehicleListFragment.OnListFragmentInteractionListener;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Vehicle;

import java.util.List;

public class MyVehicleRecyclerViewAdapter extends RecyclerView.Adapter<MyVehicleRecyclerViewAdapter.ViewHolder> {

    private final List<Vehicle> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyVehicleRecyclerViewAdapter(List<Vehicle> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_vehicle, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(Integer.toString(mValues.get(position).getID_vehicle()));
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mMakeModelView.setText(mValues.get(position).getMakeModel());

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

        final Vehicle selectedVeh = mValues.get(position);

        holder.mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog editDialog = new Dialog(context);
                editDialog.setContentView(R.layout.dialog_vehicle);
                editDialog.setTitle(R.string.new_vehicle);


                final EditText edit_name = (EditText) editDialog.findViewById(R.id.edit_veh_name);
                final EditText edit_make = (EditText) editDialog.findViewById(R.id.edit_veh_make);
                final EditText edit_model = (EditText) editDialog.findViewById(R.id.edit_veh_model);
                edit_name.setText(selectedVeh.getName());
                edit_make.setText(selectedVeh.getMake());
                edit_model.setText(selectedVeh.getModel());

                Button btnCreate = (Button) editDialog.findViewById(R.id.btn_create);
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedVeh.setName(edit_name.getText().toString());
                        selectedVeh.setMake(edit_make.getText().toString());
                        selectedVeh.setModel(edit_model.getText().toString());
                        DbHandler.editVehicle(selectedVeh);
                        editDialog.dismiss();
                    }
                });
                editDialog.show();
            }
        });

        holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.delete_veh);
                builder.setMessage(context.getString(R.string.delete_confirm_message)+selectedVeh.getName()+"?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DbHandler.deleteVehicle(selectedVeh);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog diag = builder.create();
                diag.show();
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
        public final TextView mNameView;
        public final TextView mMakeModelView;
        public final ImageButton mBtnEdit;
        public final ImageButton mBtnDelete;
        public Vehicle mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.veh_id);
            mNameView = (TextView) view.findViewById(R.id.veh_name);
            mMakeModelView = (TextView) view.findViewById(R.id.veh_make);
            mBtnEdit = (ImageButton) view.findViewById(R.id.btnEdit);
            mBtnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

    }
}
