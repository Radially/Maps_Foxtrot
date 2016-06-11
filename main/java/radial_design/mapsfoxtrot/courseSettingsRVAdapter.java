package radial_design.mapsfoxtrot;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

public class courseSettingsRVAdapter extends RecyclerView.Adapter<courseSettingsRVAdapter.AdapterViewHolder> {
    public Map<String, String> settingsMap;
    public Context context = null;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public courseSettingsRVAdapter(Map<String, String> settingsMap,  OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.settingsMap = settingsMap;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(String key);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTV;
        public TextView valTV;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public AdapterViewHolder(View itemView ,OnRecyclerItemClickListener onRecyclerItemClickListener) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.settings_rv_name_textview);
            valTV = (TextView) itemView.findViewById(R.id.settings_rv_val_textview);
            this.onRecyclerItemClickListener = onRecyclerItemClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onRecyclerItemClickListener.onRecyclerItemClick(nameTV.getText().toString());
        }
    }

    public void changeMap(Map<String, String> newMap) {
        settingsMap.putAll(newMap);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.settings_rv_layout, viewGroup, false);
        AdapterViewHolder vh = new AdapterViewHolder(v, onRecyclerItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder avh, int i) {        //on each message template
        Object[] vals = settingsMap.values().toArray();
        Object[] keys = settingsMap.keySet().toArray();   //yes, very efficient code. tell me more about how efficient you are
        avh.nameTV.setText(keys[i].toString());
        avh.valTV.setText(vals[i].toString());
    }

    @Override
    public int getItemCount() {
        return settingsMap.size();
    }
}