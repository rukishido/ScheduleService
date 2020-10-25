package com.example.scheduleservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static final int JOB_ID = 111;
    private ListView listView;
    private ScheduleDataTask task;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.schedule_listView);
        fab = (FloatingActionButton)findViewById(R.id.floatingButton);
        listView.setClickable(false);
        task = new ScheduleDataTask(listView,this);
        task.execute();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName = new ComponentName(getApplicationContext(),ScheduleJobService.class);
                JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                        .setPeriodic(60*15*1000)
                        .setPersisted(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();
                JobScheduler jobScheduler = (JobScheduler) getSystemService(getApplicationContext().JOB_SCHEDULER_SERVICE);
                int result = jobScheduler.schedule(jobInfo);
                if(result == JobScheduler.RESULT_SUCCESS){
                    Toast.makeText(getApplicationContext(),"Успешно!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Не удалось запустить сервис!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}