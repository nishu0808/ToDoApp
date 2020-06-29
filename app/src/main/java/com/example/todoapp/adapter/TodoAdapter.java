package com.example.todoapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.model.ToDoList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private MainActivity   mActivity;
    private List<ToDoList> mTodoList;
    private List<ToDoList> mFilteredList;

    public TodoAdapter(MainActivity activity,List<ToDoList> todoList) {
        this.mActivity = activity;
        this.mTodoList = todoList;

        this.mFilteredList = new ArrayList<>();
        this.mFilteredList.addAll(mTodoList);
    }

    /**
     * Function to filter the TodoList based on the string value
     * @param s String
     */
    public void filter(String s) {
        mTodoList.clear();
        if (s == null || s.length() == 0) {
            mTodoList.addAll(mFilteredList);
        } else {
            String filterString = s.toLowerCase().trim();
            for (ToDoList item : mFilteredList) {
                if (item.getTitle().toLowerCase().contains(filterString)) {
                    mTodoList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Class to describe the item views within the recycler-view
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout mRelativeLayout;
        TextView  mTitleText;
        TextView  mDescText;
        ImageView mImageView;
        CheckBox  mCheckBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initialize item views
            mRelativeLayout = itemView.findViewById(R.id.todo_item_layout);
            mTitleText = itemView.findViewById(R.id.todo_item_title);
            mDescText  = itemView.findViewById(R.id.todo_item_desc);
            mImageView = itemView.findViewById(R.id.todo_item_image);
            mCheckBox  = itemView.findViewById(R.id.todo_item_checkbox);
        }
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, final int position) {

        final ToDoList toDoList = mTodoList.get(position);

        holder.mTitleText.setText(toDoList.getTitle());
        holder.mDescText.setText(toDoList.getDescription());
        holder.mImageView.setImageBitmap(getBitmap(toDoList.getImage(),
                (int) mActivity.getResources().getDimension(R.dimen.todo_item_image_dimens),
                (int) mActivity.getResources().getDimension(R.dimen.todo_item_image_dimens)));

        // disable checkbox if it is selected and update the layout's background accordingly
        if(toDoList.isSelected()) {
            holder.mCheckBox.setChecked(true);
            holder.mCheckBox.setEnabled(false);
            holder.mCheckBox.setClickable(false);
            holder.mRelativeLayout.setBackground(mActivity.getDrawable(R.drawable.greyed_out));
        } else {
            holder.mCheckBox.setChecked(false);
            holder.mCheckBox.setEnabled(true);
            holder.mCheckBox.setClickable(true);
            holder.mRelativeLayout.setBackground(mActivity.getDrawable(R.drawable.black_border));
        }

        // on seelcting the checkbox, move the item to the last position and update the database
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTodoList.remove(position);
                ToDoList newList = new ToDoList(mActivity.getResources().getString(R.string.done)
                        +"  "+toDoList.getTitle(),toDoList.getDescription(),
                        toDoList.getImage(),true);
                mTodoList.add(mTodoList.size(),newList);
                notifyDataSetChanged();

                MainActivity.mDataBase.myDao().delete(toDoList);
                MainActivity.mDataBase.myDao().addData(newList);
            }
        });

    }

    /**
     * Function to convert byte array into a scaled bitmap and return it
     * @param image  byte[]
     * @param height height of image-view
     * @param width  width of image-view
     * @return scaled bitmap
     */
    private Bitmap getBitmap(byte[] image, int height, int width) {
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        return Bitmap.createScaledBitmap(bmp, width, height, false);
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }
}
