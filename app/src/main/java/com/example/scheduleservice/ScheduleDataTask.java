package com.example.scheduleservice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ScheduleDataTask extends AsyncTask<Void,Void,ArrayList<ScheduleItemData>> {


    ListView listView;
    Context mContext;

    public ScheduleDataTask(ListView listView, Context context) {
        this.listView = listView;
        this.mContext = context;
    }

    @Override
    protected ArrayList<ScheduleItemData> doInBackground(Void... voids) {
        Document document;
        ArrayList<ScheduleItemData> scheduleItemDataList = new ArrayList<ScheduleItemData>();
        try {
            document = Jsoup.connect("https://ci.nsu.ru/education/schedule/").get(); //получаем страницу
            Elements scheduleSections = document.getElementsByClass("program-card-section-wrap"); // секции из расписания
            for (Element section : scheduleSections) {
                String categoryName = section.getElementsByClass("col-lg-10").get(0).text(); // заголовки секций
                Elements scheduleFiles = section.getElementsByClass("col-xs-12 col-sm-6"); // файлы внутри секции
                for (Element i : scheduleFiles) {
                   String scheduleFileLink = i.getElementsByTag("a").get(0).attr("href"); //ссылка на файл
                    String scheduleFileName = i.getElementsByClass("file-name").get(0).text().replace("Расписание занятий на ",""); //название файла
                    scheduleItemDataList.add(new ScheduleItemData(categoryName, scheduleFileName, scheduleFileLink));
                }
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }
        return scheduleItemDataList;
    }

    @Override
    protected void onPostExecute(ArrayList<ScheduleItemData> scheduleItemData) {
        super.onPostExecute(scheduleItemData);
        listView.setAdapter(new ScheduleDataAdapter(mContext,R.layout.schedule_item, scheduleItemData));
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScheduleItemData item = (ScheduleItemData)parent.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("https://ci.nsu.ru" + item.getFileLink()),"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);

            }
        });
    }
}
