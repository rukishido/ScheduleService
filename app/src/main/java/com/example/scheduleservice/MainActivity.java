package com.example.scheduleservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    ScheduleDataAdapter adapter;
    ArrayList<ScheduleItemData> scheduleItemDataList;
    ScheduleDataTask task;
//    public final String dTag = "MainActivityDebugTag";
//    public Document document;
//    Thread thread;
//    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.schedule_listView);
        task = new ScheduleDataTask(listView,this);
        task.execute();
        //initThread();


    }

//    void initThread(){
//        runnable = new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void run() {
//                getPage();
//            }
//        };
//        thread = new Thread(runnable);
//        thread.start();
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    void getPage(){
//        try {
//            ArrayList<String> tempScheduleFileNames = new ArrayList<String>();
//            ArrayList<String> tempScheduleFileLinks = new ArrayList<String>();
//            scheduleDataList = new ArrayList<ScheduleData>();
//            document = Jsoup.connect("https://ci.nsu.ru/education/schedule/").get(); //получаем страницу
//            Elements scheduleSections = document.getElementsByClass("program-card-section-wrap"); // секции из расписания
//            for (Element section : scheduleSections) {
//                String categoryName = section.getElementsByClass("col-lg-10").get(0).text(); // заголовки секций
//                Elements scheduleFiles = section.getElementsByClass("col-xs-12 col-sm-6"); // файлы внутри секции
//                for (Element i : scheduleFiles) {
//                    tempScheduleFileLinks.add(i.getElementsByTag("a").get(0).attr("href"));
//                    tempScheduleFileNames.add(i.getElementsByClass("file-name").get(0).text());
//                }
//                scheduleDataList.add(new ScheduleData(categoryName, tempScheduleFileNames, tempScheduleFileLinks));
//                tempScheduleFileLinks.clear();
//                tempScheduleFileNames.clear();
//            }
//
//
//        } catch (Throwable e) {
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
//        }

}