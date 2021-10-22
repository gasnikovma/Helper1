package app.gasnikov.helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MViewHolder> {
    private Context context;
    private List<User>userList;
    public UserAdapter(Context context,List<User>userList){
        this.userList=userList;
        this.context=context;
    }
    //перечислить используемые компоненты из макета для отдельного элемента списка
    public class MViewHolder extends RecyclerView.ViewHolder{
        public TextView username;


        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
        }
    }


    @NonNull
    @Override
    //указать идентификатор макета для отдельного элемента списка и вернуть объект ViewHolder
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.MViewHolder(view);
    }

    @Override
    //связать используемые текстовые метки с данными
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        User user =userList.get(position);
        holder.username.setText(user.getFullname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Message_Activity.class);
                intent.putExtra("getuserid", user.getId());
                context.startActivity(intent);
            }
        });

    }




    @Override
    public int getItemCount() {
        return userList.size();
    }
}
