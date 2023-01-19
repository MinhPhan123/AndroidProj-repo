package rmit.ad.myapplication.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rmit.ad.myapplication.MemoryData;
import rmit.ad.myapplication.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatList> chatLists;
    private String userID;
    private final Context context;

    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userID = MemoryData.getData(context);
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        ChatList list2 = chatLists.get(position);

        if(list2.getUserID().equals(userID)){
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);
            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate()+ "" + list2.getTime());

        } else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate()+ "" + list2.getTime());

        }

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists){
        this.chatLists = chatLists;

    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoTime, myTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.opponentLayout);
            myLayout=itemView.findViewById(R.id.myLayout);
            oppoMessage=itemView.findViewById(R.id.oppoMessage);
            myMessage=itemView.findViewById(R.id.myMessage);
            oppoTime=itemView.findViewById(R.id.oppoMsgTime);
            myTime=itemView.findViewById(R.id.myMsgTime);
        }
    }
}
