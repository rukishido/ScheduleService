package com.example.scheduleservice;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.example.scheduleservice.ScheduleDBContract.*;

import java.util.ArrayList;

public class ScheduleDataTask extends AsyncTask<Void,Void,ArrayList<ScheduleItemData>> {

    SQLiteDatabase db;
    SQLiteOpenHelper dbHelper;
    ListView listView;
    Context mContext;

    public ScheduleDataTask(ListView listView, Context context) {
        this.listView = listView;
        this.mContext = context;
    }

    @Override
    protected ArrayList<ScheduleItemData> doInBackground(Void... voids) {
        dbHelper = new ScheduleDBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + ScheduleTableInfo.TABLE_NAME);

        Document document;
        ArrayList<ScheduleItemData> scheduleItemDataList = new ArrayList<>();
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
            Toast.makeText(mContext,"Ошибка : " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        for(ScheduleItemData item:scheduleItemDataList){
            ContentValues contentValues = new ContentValues();
            contentValues.put(ScheduleTableInfo.COLUMN_CATEGORY,item.getCategoryName());
            contentValues.put(ScheduleTableInfo.COLUMN_FILENAME,item.getFileName());
            db.insert(ScheduleTableInfo.TABLE_NAME,null,contentValues);
        }

        db.close();
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
