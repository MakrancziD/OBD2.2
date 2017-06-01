package obdtool.com.obd2_2.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.R;

public class TerminalRecyclerViewAdapter extends RecyclerView.Adapter<TerminalRecyclerViewAdapter.ViewHolder> {

    private List<String> mCommands;

    public TerminalRecyclerViewAdapter(List<String> items) {
        if(items!=null) {
            mCommands = items;
        } else {
            mCommands = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_terminal, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCommand.setText(mCommands.get(position));
    }

    @Override
    public int getItemCount() {
        return mCommands.size();
    }

    public void addCommand(String cmd) {
        mCommands.add(cmd);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView mCommand;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCommand = (TextView) itemView.findViewById(R.id.txtTerminalCmd);
        }

    }
}
