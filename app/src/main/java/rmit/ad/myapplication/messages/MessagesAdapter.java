package rmit.ad.myapplication.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rmit.ad.myapplication.R;
import rmit.ad.myapplication.chat.Chat;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private  List<MessageList> messagesLists;
    private final Context context;

    public MessagesAdapter(List<MessageList> messagesList, Context context) {
        this.messagesLists = messagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageList list2 = messagesLists.get(position);

        if(!list2.getProfile_pic().isEmpty()){
            Picasso.get().load(list2.getProfile_pic()).into(holder.profilePic);
        }
        //Picasso.get().load(list2.getProfile_pic()).into(holder.profilePic);
        holder.full_name.setText(list2.getFullname());
        holder.lastMessage.setText(list2.getLastMessage());

        if(list2.getUnseenMessage()==0){
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMessage()+"");
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("userID",list2.getUserID());
                intent.putExtra("full_name",list2.getFullname());
                intent.putExtra("profile_pic",list2.getProfile_pic());
                intent.putExtra("chat_key",list2.getChatKey());
                context.startActivity(intent);
            }

        });

    }

    public void updateData(List<MessageList> messagesLists){
        this.messagesLists = messagesLists;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profilePic;
        private TextView full_name,lastMessage,unseenMessages;
        private LinearLayout rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile);
            full_name = itemView.findViewById(R.id.full_name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
