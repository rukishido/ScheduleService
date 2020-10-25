package com.example.scheduleservice;

import android.app.Notification;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.scheduleservice.ScheduleDBContract.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import static com.example.scheduleservice.ScheduleApplication.CHANNEL_ID;

public class ScheduleJobService extends JobService {
    ScheduleDBHelper dbHelper;
    SQLiteDatabase db;
    Document document;
    public boolean isAlive = true;

    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isAlive) {
                    ArrayList<ScheduleItemData> scheduleItemDataList = new ArrayList<ScheduleItemData>();
                    try {
                        document = Jsoup.connect("https://ci.nsu.ru/education/schedule/").get(); //получаем страницу
                        Elements scheduleSections = document.getElementsByClass("program-card-section-wrap"); // секции из расписания
                        for (Element section : scheduleSections) {
                            String categoryName = section.getElementsByClass("col-lg-10").get(0).text(); // заголовки секций
                            Elements scheduleFiles = section.getElementsByClass("col-xs-12 col-sm-6"); // файлы внутри секции
                            for (Element i : scheduleFiles) {
                                String scheduleFileLink = i.getElementsByTag("a").get(0).attr("href"); //ссылка на файл
                                String scheduleFileName = i.getElementsByClass("file-name").get(0).text().replace("Расписание занятий на ", ""); //название файла
                                scheduleItemDataList.add(new ScheduleItemData(categoryName, scheduleFileName, scheduleFileLink));
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    dbHelper = new ScheduleDBHelper(getApplicationContext());
                    db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.query(ScheduleTableInfo.TABLE_NAME, null, null, null, null, null, null);
                    ArrayList<ScheduleItemData> dbItemDataList = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        dbItemDataList.add(new ScheduleItemData(
                                "",
                                cursor.getString(cursor.getColumnIndex(ScheduleTableInfo.COLUMN_FILENAME)),
                                ""
                        ));
                    }

                    int count = (dbItemDataList.size() >= scheduleItemDataList.size()) ? scheduleItemDataList.size() : dbItemDataList.size();//задаем минимальную границу чтобы не вылезти за границы массивов
                    for (int i = 0; i < count; i++) {
                        if (!(scheduleItemDataList.get(i).getFileName().equals(dbItemDataList.get(i).getFileName()))) {
                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                            Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setSmallIcon(R.drawable.schedule_item_icon)
                                    .setContentTitle("Новое расписание!")
                                    .setContentText("Похоже, что вышло новое расписание! Запустите приложение, чтобы проверить.")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .build();

                            notificationManagerCompat.notify(1, notification);
                            jobFinished(params, false);
                        }
                        }
                    }
                }
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        db.close();
        isAlive = false;
        return true;
    }
}
