package radial_design.mapsfoxtrot;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class markCyclerAdapter  extends RecyclerView.Adapter<markCyclerAdapter.MessageViewHolder> {
    public List<Mark> markList;
    public Context context = null;
    private OnMarkRecyclerItemClickListener onMarkRecyclerItemClickListener;

    public markCyclerAdapter(List<Mark> massages,  OnMarkRecyclerItemClickListener onMarkRecyclerItemClickListener){
        this.markList = massages;
        this.onMarkRecyclerItemClickListener = onMarkRecyclerItemClickListener;
    }
    public markCyclerAdapter( OnMarkRecyclerItemClickListener onMarkRecyclerItemClickListener){
        this.markList = new ArrayList<>();
        this.onMarkRecyclerItemClickListener = onMarkRecyclerItemClickListener;
    }
    public interface OnMarkRecyclerItemClickListener {
        void onMarkRecyclerItemClick(Mark destination);
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView messageTV;
        public Mark mark;
        private OnMarkRecyclerItemClickListener onMarkRecyclerItemClickListener;

        public MessageViewHolder(View itemView,OnMarkRecyclerItemClickListener onMarkRecyclerItemClickListener) {
            super(itemView);
            messageTV = (TextView)itemView.findViewById(R.id.mark_rv_layout_textview);
            this.onMarkRecyclerItemClickListener = onMarkRecyclerItemClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onMarkRecyclerItemClickListener.onMarkRecyclerItemClick(mark);
        }
    }

    public void changeList(List<Mark> list){
        List<Mark> clonie = new ArrayList<>();
        clonie.addAll(list);
        markList.clear();
        markList.addAll(clonie);
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mark_rv_layout, viewGroup, false);
        MessageViewHolder vh = new MessageViewHolder(v , onMarkRecyclerItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int i) {        //on each message template
        messageViewHolder.messageTV.setText("Mark  "+markList.get(i).getName());
        messageViewHolder.mark =markList.get(i);
    }

    @Override
    public int getItemCount() {
        return markList.size();
    }

}