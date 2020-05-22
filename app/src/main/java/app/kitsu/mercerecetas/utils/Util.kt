package app.kitsu.mercerecetas.utils

import android.content.res.Resources
import app.kitsu.mercerecetas.R
import java.util.concurrent.TimeUnit

private val ONE_HOUR_MIN = TimeUnit.MINUTES.convert(1, TimeUnit.HOURS)

fun convertDurationToFormatted(time: Int, res: Resources): String {
    var duration = time
    var totalTimeString = StringBuilder()

    if (duration >= ONE_HOUR_MIN){
        val hours = TimeUnit.HOURS.convert(duration.toLong(), TimeUnit.MINUTES)

        duration -= (hours*ONE_HOUR_MIN).toInt()
        totalTimeString.append(" ").append(res.getString(R.string.hours_length, hours))
    }
    if( duration != 0) {
            totalTimeString.append(" ").append(res.getString(R.string.minutes_length, duration))
        }
    return totalTimeString.toString()
}
