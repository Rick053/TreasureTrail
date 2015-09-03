package com.utsdev.treasuretrail;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.utsdev.treasuretrail.adapters.HintAdapter;
import com.utsdev.treasuretrail.stuff.DividerItemDecoration;

public class MainActivity extends AppCompatActivity {

    public Vibrator v;
    public RecyclerView rcv;
    public HintAdapter adapter;

    //test stuff
    public int count = 1;

    public String[] hints = new String[] {
            "Test",
            "Test 2",
            "Hint",
            "Lorem ipsum"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        App.setActivity(this);
        App.startService(MainActivity.this);

//        TextView status = (TextView) App.getActivity().findViewById(R.id.status);
//        Status.assignView(status);

        //Initialize the adapter
        adapter = new HintAdapter(this);

        //Initialize the recyclerview which will display the hints
        rcv = (RecyclerView) findViewById(R.id.hint_list);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        rcv.setAdapter(adapter);
        rcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        adapter.addHint(hints[1]);

        //Initialize the handler which checks the messages every x seconds
        final Handler handler = new Handler();
        final int delay = 10000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addHint(hints[count++]);

                handler.postDelayed(this, delay);
            }
        }, delay);
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
}
