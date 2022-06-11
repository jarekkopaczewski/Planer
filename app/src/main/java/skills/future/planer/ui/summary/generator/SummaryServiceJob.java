package skills.future.planer.ui.summary.generator;

import android.app.job.JobParameters;
import android.app.job.JobService;

import androidx.core.app.NotificationManagerCompat;

import skills.future.planer.db.summary.SummaryType;
import skills.future.planer.notification.NotificationFactory;

public class SummaryServiceJob extends JobService {

    /**
     * Creates summary notifications
     *
     * @param params - type of summary
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        NotificationFactory notificationFactory = new NotificationFactory(getApplicationContext());
        var notify = notificationFactory
                .generateNewNotificationSummary(
                        (int) (params.getExtras()
                                .get(DayChangeBroadcastReceiver.bundleJob)) == 2 ?
                                SummaryType.monthSummary : SummaryType.weekSummary);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(notify.getNotificationId(), notify.getNotification());
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}