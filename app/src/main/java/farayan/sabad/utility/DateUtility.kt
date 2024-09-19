package farayan.sabad.utility

import android.os.Build
import com.ibm.icu.text.DateFormat
import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.ULocale
import farayan.sabad.core.commons.localize
import java.time.ZoneId
import java.util.Date


val PERSIAN_LOCALE: ULocale = ULocale("fa_IR@calendar=persian")
val PERSIAN_EN_LOCALE: ULocale = ULocale("en@calendar=persian")
val IRAN_ZONE_ID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    ZoneId.of("Asia/Tehran")
} else {
    TODO("VERSION.SDK_INT < O")
}

fun persian(epoch: Long): String {
    return DateFormat.getDateInstance(DateFormat.DEFAULT, PERSIAN_LOCALE).format(Date(epoch))
}

fun Date.standard(): String {
    val cal: Calendar = Calendar.getInstance(PERSIAN_LOCALE).apply {
        clear()
        time = this@standard
    }
    val dayOfWeek = SimpleDateFormat(SimpleDateFormat.WEEKDAY, PERSIAN_LOCALE).format(cal.time)
    val dayOfMonth = SimpleDateFormat(SimpleDateFormat.DAY, PERSIAN_LOCALE).format(cal.time)
    val month = SimpleDateFormat(SimpleDateFormat.MONTH, PERSIAN_LOCALE).format(cal.time)
    val year = cal.get(DateFormat.YEAR_FIELD)

    return "$dayOfWeek $dayOfMonth $month $year".localize()
}