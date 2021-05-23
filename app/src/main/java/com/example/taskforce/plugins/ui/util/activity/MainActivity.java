package com.example.taskforce.plugins.ui.util.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.taskforce.R;
import com.example.taskforce.adapters.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateTaskActivity.class);
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                System.out.println();
            }
        });

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.deleteTask){
            menu.add(Menu.NONE, 0, 0 , "löschen");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Adapteritem.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                /*
                View taskLayout =(View) button.getParent().getParent();
                ListView taskListView = (ListView) taskLayout.getParent();
                TaskObject obj = data.get(taskListView.getPositionForView(taskLayout));
                TaskObjectDAO.deleteTaskFromDatabase(context, obj.getId());

                 */
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}