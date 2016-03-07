package com.example.android.listmaker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ArrayList<String> subArrayList;
    private int index;
    EditText addToDoEditText;
    String toDoItem;
    String subListLabel;
    ArrayAdapter nAdapter;
    ListView detailListView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar();
        setListTitle();

        subArrayList = getDataList();
        index = getDataIndex();

        addToDoEditText = (EditText) findViewById(R.id.addToList_editText);
//        subArrayList = new ArrayList<>();
        nAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,subArrayList);
        detailListView = (ListView) findViewById(R.id.detailListView);

        detailListView.setAdapter(nAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toDoItem = addToDoEditText.getText().toString();

                if (toDoItem.isEmpty()){

                    Snackbar.make(view, "You didn't type anything!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                } else {

                    subArrayList.add(toDoItem);
                    nAdapter.notifyDataSetChanged();
                    addToDoEditText.setText("");
                }
            }
        });

        detailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Snackbar.make(view, "Tap and hold to delete.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });

        detailListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                removeCompletedItem(position);
                Snackbar.make(view, "Item deleted.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return true;

            }
        });
    }

    private ArrayList<String> getDataList(){
        Intent subListIntent = getIntent();
        if (subListIntent == null){
            return null;
        }
        return subListIntent.getStringArrayListExtra(MainActivity.DATA_KEY);
    }

    private int getDataIndex(){
        Intent subListIntent = getIntent();
        if (subListIntent == null){
            return MainActivity.ERROR_INDEX;
        }
        return subListIntent.getIntExtra(MainActivity.DATA_INDEX_KEY, MainActivity.ERROR_INDEX);
    }

    private void sendNewListBack(){
        Intent backToMainIntent = getIntent();
        if (backToMainIntent == null){
            return;
        }
        backToMainIntent.putExtra(MainActivity.DATA_KEY, subArrayList);
        backToMainIntent.putExtra(MainActivity.DATA_INDEX_KEY, index);
        setResult(RESULT_OK, backToMainIntent);
        finish();
    }

    /**
     * We handle what happens when user presses back button
     */
    @Override
    public void onBackPressed() {
        sendDataBack();
    }

    /**
     * Handle what needs to happen to send data back
     */
    private void sendDataBack(){

        // send the data back
        sendNewListBack();
    }

    private void setListTitle() {                // renames the toolbar title based on the list user selects

        Intent intent = getIntent();
        subListLabel = intent.getStringExtra("LISTDETAIL");
        setTitle(subListLabel);
    }

    private void removeCompletedItem(int i) {

        subArrayList.remove(i);
        nAdapter.notifyDataSetChanged();

    }

}
