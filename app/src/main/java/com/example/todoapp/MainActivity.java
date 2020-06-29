package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.example.todoapp.adapter.TodoAdapter;
import com.example.todoapp.database.MyDataBase;
import com.example.todoapp.model.ToDoList;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mAddButton;
    private SearchView mSearchView;
    public  RecyclerView mRecyclerView;

    public static MyDataBase mDataBase;
    TodoAdapter mTodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDataBase();
        initViews();
        setListeners();
    }

    /**
     * Function to create and initialize the database instance
     */
    private void initDataBase() {
        mDataBase = Room.databaseBuilder(getApplicationContext(),MyDataBase.class,"infodb")
                       .allowMainThreadQueries().build();
    }

    /**
     * Function to initialize views
     */
    private void initViews() {
        mAddButton    = findViewById(R.id.add_button);
        mSearchView   = findViewById(R.id.search_view);
        mRecyclerView = findViewById(R.id.recycler_view);

        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * Function to set listeners to the views
     */
    private void setListeners() {
        mAddButton.setOnClickListener(mAddButtonClickListener);
        mSearchView.setOnQueryTextListener(mQueryTextListener);
    }


    /**
     * Navigate to DetailsActivity when add button is clicked
     */
    View.OnClickListener mAddButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            startActivity(intent);
        }
    };

    /**
     * Callbacks for changes to the query text.
     */
    SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if(mTodoAdapter != null)
                mTodoAdapter.filter(s);
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        populateTodoList();
    }

    /**
     * Function to create and execute FetchData AsyncTask
     */
    private void populateTodoList() {
        FetchData gd = new FetchData(this);
        gd.execute();
    }

    /**
     * Async task to fetch data from the database and populate in a recycler view
     */
    static class FetchData extends AsyncTask<Void,Void, List<ToDoList>> {
        WeakReference<MainActivity> activityReference;

        FetchData(MainActivity mainActivity) {
            activityReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected List<ToDoList> doInBackground(Void... voids) {
            // fetch and return the list of ToDoList objects
            return MainActivity.mDataBase.myDao().getMyData();
        }

        @Override
        protected void onPostExecute(List<ToDoList> toDoList) {
            MainActivity activity = activityReference.get();

            //set adapter to recycler view
            activity.mTodoAdapter = new TodoAdapter(activity,toDoList);
            activity.mRecyclerView.setAdapter(activity.mTodoAdapter);
            super.onPostExecute(toDoList);
        }
    }
}
