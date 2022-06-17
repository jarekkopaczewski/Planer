package skills.future.planer.ui.summary.generator;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Random;

import skills.future.planer.db.summary.SummaryData;
import skills.future.planer.db.summary.SummaryRepository;
import skills.future.planer.db.summary.SummaryType;

public class DayChangeBroadcastReceiver extends BroadcastReceiver {
    private static int JOB_ID = 100;
    public static String bundleJob = "summaryType";
    private static final long ONE_HOUR_MILLISECONDS = 3600000;

    @Override
    public void onReceive(Context context, Intent intent) {
        SummaryRepository summaryRepository = new SummaryRepository(context);
        LocalDate today = LocalDate.now();

        /*
         * Creates new week summary
         */
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            summaryRepository.insert(new SummaryData("",
                    "", "", today.minusDays(6), SummaryType.weekSummary));
            startJob(context, SummaryType.weekSummary);
        }

        /*
         * Creates new month summary
         */
        if (today.getDayOfMonth() == 1) {
            summaryRepository.insert(new SummaryData("",
                    "", "", today, SummaryType.monthSummary));
            startJob(context, SummaryType.monthSummary);
        }
    }

    /**
     * Starts job scheduler which show summary notification
     */
    private void startJob(Context context, SummaryType summaryType) {
        JobScheduler jobScheduler = (JobScheduler) context.getApplicationContext()
                .getSystemService(Context.JOB_SCHEDULER_SERVICE);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt(bundleJob, summaryType.getType());


        var jobInfo = new JobInfo.Builder(JOB_ID++, new ComponentName(context, SummaryServiceJob.class))
                .setExtras(bundle)
                .setMinimumLatency(new Random().nextInt((int) (8 * ONE_HOUR_MILLISECONDS)) + 12 * ONE_HOUR_MILLISECONDS).build();
        jobScheduler.schedule(jobInfo);
    }

}
