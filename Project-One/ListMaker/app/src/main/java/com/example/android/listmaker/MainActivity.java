package com.example.android.listmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ArrayList<String>> masterArrayList;
    EditText subArrayListNamerEditText;
    ArrayAdapter<String> mAdapter;
    ListView masterListView;
    ArrayList<String> subArrayListData;
    ArrayList<String> subArrayListLabels;
    int currentMasterArraySize;
    String subArrayListLabel;

    private static final int MAIN_REQUEST_CODE = 27;
    // data key to retrieve data from intent. Public so we can retrieve data in DetailActivity
    public static final String DATA_KEY = "myMySubListDataKey";
    public static final String DATA_INDEX_KEY = "mySubListDataIndexKey";
    public static final int ERROR_INDEX = -1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Set some data on our list of data
        subArrayListData = new ArrayList<>();
        subArrayListLabels = new ArrayList<>();

        // initialize my master data list of lists of data
        masterArrayList = new ArrayList<>();
        masterArrayList.add(subArrayListData);
        currentMasterArraySize = masterArrayList.size();

        subArrayListNamerEditText = (EditText) findViewById(R.id.listNamer_editText);
        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,subArrayListLabels);
        masterListView = (ListView) findViewById(R.id.masterListView);

        masterListView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNewSubArrayList(view);

            }
        });

        masterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                intent.putExtra(DATA_INDEX_KEY, position);
                intent.putExtra(DATA_KEY, masterArrayList.get(position));

                intent.putExtra("LISTDETAIL", subArrayListLabels.get(position));

                startActivityForResult(intent, MAIN_REQUEST_CODE);

            }
        });

        masterListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                removeSubArrayList(position);
                Snackbar.make(view, "List deleted.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return true;

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewSubArrayList(View view) {

        ArrayList<String> newSubArrayList;
        newSubArrayList = new ArrayList<>();
        masterArrayList.add(newSubArrayList);
        subArrayListLabel = subArrayListNamerEditText.getText().toString();

        if (subArrayListLabel.isEmpty()){

            Snackbar.make(view, "Name your list!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        } else {

            masterArrayList.add(newSubArrayList);
            subArrayListLabels.add(subArrayListLabel);
            mAdapter.notifyDataSetChanged();
            subArrayListNamerEditText.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // make sure returned request code is what we are expecting
        if (requestCode == MAIN_REQUEST_CODE){
            // make sure results were handled correctly
            if (resultCode == RESULT_OK){
                // null pointer check
                if (data != null) {
                    // update data list with the new data
                    ArrayList<String> tempItemList = data.getStringArrayListExtra(DATA_KEY);
                    int index = data.getIntExtra(DATA_INDEX_KEY, ERROR_INDEX);
                    if (index != ERROR_INDEX){
                        masterArrayList.set(index, tempItemList);
                    } else {
                        Log.e("Main", "Index is not valid: " + index);
                    }

                    // now print our updated data
//                    printData(masterArrayList.get(index));
                }
            } else  if (requestCode == RESULT_CANCELED){
                Log.w("Main", "Failed to get new list back");
            }
        }
    }

    private void removeSubArrayList(int i) {
        masterArrayList.remove(i);
        subArrayListLabels.remove(i);
        mAdapter.notifyDataSetChanged();

    }
}
