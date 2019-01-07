package com.examples.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder>{

    private Context mContext;
    private List<Items> ItemsList;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private ItemClickListener clickListener;
        public TextView location;
        public ImageView image;
        public RelativeLayout main;
        public LinearLayout delete;
        public Button yes,no;

        public MyViewHolder(View view) {
            super(view);
            location = (TextView) view.findViewById(R.id.location);
            image = (ImageView) view.findViewById(R.id.image);
            main = (RelativeLayout) view.findViewById(R.id.main);
            delete = (LinearLayout) view.findViewById(R.id.delete);
            yes = (Button) view.findViewById(R.id.yes);
            no = (Button) view.findViewById(R.id.no);
            view.setTag(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            main.setOnClickListener(this);
            image.setOnClickListener(this);
            main.setOnLongClickListener(this);
            image.setOnLongClickListener(this);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getLayoutPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getLayoutPosition(), true);
            return true;
        }
    }


    public ItemsAdapter(Context contexts, List<Items> ItemsList) {
        this.ItemsList = ItemsList;
        this.mContext = contexts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_list_row, parent, false);
        return new MyViewHolder(itemView);
    }
    private final static int FADE_DURATION = 1000;
    private void setFadeAnimation(View view, int i) {
        AlphaAnimation anim;
        if(i==1)
            anim = new AlphaAnimation(0.0f, 15.0f);
        else
            anim = new AlphaAnimation(1.5f, 0.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Items Item = ItemsList.get(position);
        setFadeAnimation(holder.itemView,1);
        holder.location.setText(Item.getlocation());

        if(Item.gettype().equals("Pothole") && Item.getstatus().equals("Unsolved")){
            holder.image.setBackgroundResource(R.drawable.ic_action_pothole_unsolved);
        }
        else if(Item.gettype().equals("Pothole") && Item.getstatus().equals("Solved")){
            holder.image.setBackgroundResource(R.drawable.ic_action_pothole_solved);
        }
        else if(Item.gettype().equals("Garbage") && Item.getstatus().equals("Unsolved")){
            holder.image.setBackgroundResource(R.drawable.ic_action_garbage_unsolved);
        }
        else{
            holder.image.setBackgroundResource(R.drawable.ic_action_garbage_solved);
        }




        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {
                if (isLongClick) {
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.main.setVisibility(View.GONE);
                    holder.delete.setMinimumHeight(holder.main.getHeight());
                    holder.yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.delete.setVisibility(View.GONE);
                            holder.main.setVisibility(View.VISIBLE);
                            removeAt(position);
                            notifyDataSetChanged();
                            setFadeAnimation(holder.itemView,2);
                            if(ItemsList.size()==0){
                                MainMyList m = new MainMyList();
                                m.text_my_issues.setVisibility(View.VISIBLE);
                            }


                            Toast.makeText(mContext,"Issue removed successfully",Toast.LENGTH_LONG).show();
                        }
                    });
                    holder.no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.main.setVisibility(View.VISIBLE);
                            holder.delete.setVisibility(View.GONE);
                        }
                    });
                } else {
                    if(view.getId() == R.id.main){
                        Intent intent = new Intent(mContext, ViewMyIssues.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("type", ItemsList.get(position).gettype());
                        intent.putExtra("SRN", ItemsList.get(position).getSRN());
                        intent.putExtra("location", ItemsList.get(position).getlocation());
                        intent.putExtra("issue_date", ItemsList.get(position).getissue_date());
                        intent.putExtra("completion_date", ItemsList.get(position).getcompletion_date());
                        intent.putExtra("resolution_period", ItemsList.get(position).getresolution_period());
                        intent.putExtra("status", ItemsList.get(position).getstatus());
                        intent.putExtra("image1", ItemsList.get(position).getimage1());
                        intent.putExtra("image2", ItemsList.get(position).getimage2());
                        mContext.startActivity(intent);
                    }
                    else if(view.getId() == R.id.image){
                        if(ItemsList.get(position).gettype().equals("Pothole") && ItemsList.get(position).getstatus().equals("Solved")){
                            Toast.makeText(mContext, "Pothole: Solved", Toast.LENGTH_SHORT).show();
                        }
                        else if(ItemsList.get(position).gettype().equals("Pothole") && ItemsList.get(position).getstatus().equals("Unsolved")){
                            Toast.makeText(mContext, "Pothole: Unsolved", Toast.LENGTH_SHORT).show();
                        }
                        else if(ItemsList.get(position).gettype().equals("Garbage") && ItemsList.get(position).getstatus().equals("Solved")){
                            Toast.makeText(mContext, "Garbage: Solved", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext, "Garbage: Unsolved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public void removeAt(int position) {
        UserDbHelper db=new UserDbHelper(mContext);
        db.delMainEntry(ItemsList.get(position).getSRN());
        ItemsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, ItemsList.size());
    }
    public void clear() {
        ItemsList.clear();
        notifyDataSetChanged();
    }


}
