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
            LocalDate dateMinus = today.minusDays(6);
            summaryRepository.insert(new SummaryData("",
                    "", "", dateMinus, SummaryType.weekSummary));

            if (summaryRepository.getSummary(dateMinus, SummaryType.monthSummary).size() == 0)
                createMonthSummary(context, summaryRepository, dateMinus);
            startJob(context, SummaryType.weekSummary);
        }

        /*
         * Creates new month summary
         */
        if (today.getDayOfMonth() == 1)
            createMonthSummary(context, summaryRepository, today);

    }

    private void createMonthSummary(Context context, SummaryRepository summaryRepository, LocalDate today) {
        summaryRepository.insert(new SummaryData("",
                "", "", today, SummaryType.monthSummary));
        startJob(context, SummaryType.monthSummary);
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
                .setMinimumLatency(12 * ONE_HOUR_MILLISECONDS).build();
        jobScheduler.schedule(jobInfo);
    }

}
