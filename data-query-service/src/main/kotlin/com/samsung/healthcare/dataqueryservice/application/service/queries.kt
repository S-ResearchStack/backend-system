package com.samsung.healthcare.dataqueryservice.application.service

import com.samsung.healthcare.dataqueryservice.application.port.input.ParticipantListColumn
import com.samsung.healthcare.dataqueryservice.application.port.input.Sort

internal const val SHOW_TABLE_QUERY = "SHOW TABLES"

internal const val TASK_ID_COLUMN = "task_id"
internal const val AVERAGE_COMPLETION_TIME_COLUMN = "avg_completion_time"
internal const val AVERAGE_COMPLETION_TIME_QUERY =
    """
       SELECT $TASK_ID_COLUMN, avg(date_diff('millisecond', started_at, submitted_at)) as $AVERAGE_COMPLETION_TIME_COLUMN
       FROM task_results
    """

internal const val NUMBER_OF_RESPONDED_USERS = "num_users_responded"
internal const val TASK_RESPONSE_COUNT_QUERY =
    """
        SELECT $TASK_ID_COLUMN, count(distinct(tr.user_id)) as $NUMBER_OF_RESPONDED_USERS
        FROM task_results tr
        JOIN user_profiles up on up.user_id = tr.user_id
    """

internal const val FIND_TASK_QUERY =
    """
        SELECT DISTINCT id as $TASK_ID_COLUMN from tasks
    """

internal const val USER_ID_COLUMN = "user_id"
internal const val ITEM_NAME_COLUMN = "item_name"
internal const val REVISION_ID_COLUMN = "revision_id"
internal const val RESULT_COLUMN = "result"
internal const val TASK_ITEM_RESPONSE_QUERY_FORMAT =
    """
        SELECT ir.id as id, ir.task_id as $TASK_ID_COLUMN, ir.revision_id as $REVISION_ID_COLUMN, ir.user_id as $USER_ID_COLUMN,
                $ITEM_NAME_COLUMN, $RESULT_COLUMN %s
        FROM item_results ir
        JOIN user_profiles up on up.user_id = ir.user_id
    """

internal const val AVERAGE_HR_COLUMN = "avg_hr_bpm"

// NOTES check where clause, is this right condition?
internal const val GET_AVERAGE_HR_QUERY =
    """
        SELECT hr.user_id as $USER_ID_COLUMN, avg(hr.bpm) as $AVERAGE_HR_COLUMN
        FROM heartrates as hr
        JOIN user_profiles up on hr.user_id = up.user_id
        WHERE date(hr.time) = date(up.last_synced_at)
    """

internal const val AVERAGE_BG_COLUMN = "avg_bg_mmpl"

// NOTES check where clause, is this right condition?
internal const val GET_AVERAGE_BG_QUERY =
    """
        SELECT bg.user_id as $USER_ID_COLUMN, avg(bg.millimoles_per_liter) as $AVERAGE_BG_COLUMN
        FROM blood_glucoses as bg
        JOIN user_profiles up on bg.user_id = up.user_id
        WHERE date(bg.time) = date(up.last_synced_at)
    """

internal const val AVERAGE_RR_COLUMN = "avg_rr_rpm"

// NOTES check where clause, is this right condition?
internal const val GET_AVERAGE_RR_QUERY =
    """
        SELECT rr.user_id as $USER_ID_COLUMN, avg(rr.rpm) as $AVERAGE_RR_COLUMN
        FROM respiratory_rates as rr
        JOIN user_profiles up on rr.user_id = up.user_id
        WHERE date(rr.time) = date(up.last_synced_at)
    """

internal const val AVERAGE_SPO2_COLUMN = "avg_spo2"

// NOTES check where clause, is this right condition?
internal const val GET_AVERAGE_SPO2_QUERY =
    """
        SELECT spo2.user_id as $USER_ID_COLUMN, avg(spo2.value) as $AVERAGE_SPO2_COLUMN
        FROM oxygen_saturations as spo2
        JOIN user_profiles up on spo2.user_id = up.user_id
        WHERE date(spo2.time) = date(up.last_synced_at)
    """

internal const val AVERAGE_BP_SYSTOLIC_COLUMN = "avg_systolic_mmhg"
internal const val AVERAGE_BP_DIASTOLIC_COLUMN = "avg_diastolic_mmhg"

// NOTES check where clause, is this right condition?
internal const val GET_AVERAGE_BP_QUERY =
    """
        SELECT bp.user_id as $USER_ID_COLUMN, avg(bp.systolic) as $AVERAGE_BP_SYSTOLIC_COLUMN, avg(bp.diastolic) as $AVERAGE_BP_DIASTOLIC_COLUMN
        FROM blood_pressures as bp
        JOIN user_profiles up on bp.user_id = up.user_id
        WHERE date(bp.time) = date(up.last_synced_at)
    """

internal const val PROFILE_COLUMN = "profile"

internal const val LAST_SYNC_TIME_COLUMN = "last_synced_at"

internal const val GET_USER_QUERY =
    """
        SELECT $USER_ID_COLUMN, $PROFILE_COLUMN, $LAST_SYNC_TIME_COLUMN
        FROM user_profiles
    """

internal const val AVERAGE_SLEEP_COLUMN = "avg_sleep_mins"

internal const val GET_AVERAGE_SLEEP_QUERY =
    """
        SELECT $USER_ID_COLUMN, avg(date_diff('minute', start_time, end_time)) as $AVERAGE_SLEEP_COLUMN
        FROM sleepsessions
    """

internal const val LAST_TOTAL_STEP_COLUMN = "total_steps"

internal const val GET_LAST_TOTAL_STEP_QUERY =
    """
      SELECT steps.user_id as $USER_ID_COLUMN, sum(steps.count) as $LAST_TOTAL_STEP_COLUMN
      FROM steps
      JOIN user_profiles up on steps.user_id = up.user_id
      WHERE date(steps.end_time) = date(up.last_synced_at)
    """

internal const val TIME_COLUMN = "time"

internal const val BPM_COLUMN = "bpm"

internal const val GET_HEART_RATE_QUERY =
    """
        SELECT $USER_ID_COLUMN, $TIME_COLUMN, $BPM_COLUMN
        from heartrates
        where time >= ? and time <= ?
    """

internal const val AVERAGE_HEART_RATE_QUERY =
    """
        SELECT $USER_ID_COLUMN, avg(bpm) as $AVERAGE_HR_COLUMN
        FROM heartrates
        WHERE time >= ? and time <= ?
        GROUP BY user_id
    """

internal fun makeQueryToGetAttributesOfUsers(count: Int): String =
    """
        $GET_USER_QUERY
         WHERE user_id IN ${makeInConditionString(count)}
    """

internal fun makeQueryToGetSleepOfUsers(count: Int): String =
    """
        $GET_AVERAGE_SLEEP_QUERY
         WHERE user_id IN ${makeInConditionString(count)}
         GROUP BY $USER_ID_COLUMN
    """

internal fun makeQueryToGetStepOfUsers(count: Int): String =
    """
        $GET_LAST_TOTAL_STEP_QUERY
         AND up.user_id IN ${makeInConditionString(count)}
         group by steps.user_id
    """

internal const val GET_LATEST_STEPS_QUERY =
    """
        SELECT up.user_id, count
        FROM steps
        JOIN user_profiles up on steps.user_id = up.user_id
        WHERE date(steps.end_time) = date(up.last_synced_at)
    """

internal const val GET_LATEST_HEART_RATES_QUERY =
    """
        SELECT up.user_id, bpm
        FROM heartrates as hr
        JOIN user_profiles up on hr.user_id = up.user_id
        WHERE date(hr.time) = date(up.last_synced_at)
    """

internal const val GET_LATEST_BLOOD_GLUCOSES_QUERY =
    """
        SELECT up.user_id, millimoles_per_liter
        FROM blood_glucoses as bg
        JOIN user_profiles up on bg.user_id = up.user_id
        WHERE date(bg.time) = date(up.last_synced_at)
    """

internal const val GET_LATEST_RESPIRATORY_RATES_QUERY =
    """
        SELECT up.user_id, rpm
        FROM respiratory_rates as rr
        JOIN user_profiles up on rr.user_id = up.user_id
        WHERE date(rr.time) = date(up.last_synced_at)
    """

internal const val GET_LATEST_OXYGEN_SATURATIONS_QUERY =
    """
        SELECT up.user_id, value
        FROM oxygen_saturations as spo2
        JOIN user_profiles up on spo2.user_id = up.user_id
        WHERE date(spo2.time) = date(up.last_synced_at)
    """

internal fun makeGetUserQuery(offset: Int, limit: Int, orderByColumn: ParticipantListColumn, orderBySort: Sort) =
    when (orderByColumn) {
        ParticipantListColumn.ID ->
            """
                $GET_USER_QUERY
                ORDER BY $USER_ID_COLUMN $orderBySort
                OFFSET $offset LIMIT $limit
            """
        ParticipantListColumn.EMAIL ->
            """
                SELECT $USER_ID_COLUMN, $PROFILE_COLUMN, $LAST_SYNC_TIME_COLUMN, json_extract_scalar(profile, '$.email') as email
                FROM user_profiles
                ORDER BY email $orderBySort, $USER_ID_COLUMN
                OFFSET $offset LIMIT $limit
            """
        ParticipantListColumn.AVG_HR ->
            """
                SELECT up.user_id as $USER_ID_COLUMN, avg(hr.bpm) as avg_hr_bpm, up.profile as $PROFILE_COLUMN, up.last_synced_at as $LAST_SYNC_TIME_COLUMN
                FROM user_profiles as up
                LEFT JOIN ($GET_LATEST_HEART_RATES_QUERY) hr
                ON up.user_id = hr.user_id
                GROUP BY up.user_id, up.profile, up.last_synced_at
                ORDER BY avg_hr_bpm $orderBySort, up.$USER_ID_COLUMN
                OFFSET $offset LIMIT $limit
            """
        ParticipantListColumn.AVG_BG ->
            """
                SELECT up.user_id as $USER_ID_COLUMN, avg(bg.millimoles_per_liter) as avg_bg_mmpl, up.profile as $PROFILE_COLUMN, up.last_synced_at as $LAST_SYNC_TIME_COLUMN
                FROM user_profiles as up
                LEFT JOIN ($GET_LATEST_BLOOD_GLUCOSES_QUERY) bg
                ON up.user_id = bg.user_id
                GROUP BY up.user_id, up.profile, up.last_synced_at
                ORDER BY avg_bg_mmpl $orderBySort, up.$USER_ID_COLUMN
                OFFSET $offset LIMIT $limit
            """
        ParticipantListColumn.AVG_RR ->
            """
                SELECT up.user_id as $USER_ID_COLUMN, avg(rr.rpm) as avg_rr_rpm, up.profile as $PROFILE_COLUMN, up.last_synced_at as $LAST_SYNC_TIME_COLUMN
                FROM user_profiles as up
                LEFT JOIN ($GET_LATEST_RESPIRATORY_RATES_QUERY) rr
                ON up.user_id = rr.user_id
                GROUP BY up.user_id, up.profile, up.last_synced_at
                ORDER BY avg_rr_rpm $orderBySort, up.$USER_ID_COLUMN
                OFFSET $offset LIMIT $limit
            """
        ParticipantListColumn.AVG_SPO2 ->
            """
                SELECT up.user_id as $USER_ID_COLUMN, avg(spo2.value) as avg_spo2, up.profile as $PROFILE_COLUMN, up.last_synced_at as $LAST_SYNC_TIME_COLUMN
                FROM user_profiles as up
                LEFT JOIN ($GET_LATEST_OXYGEN_SATURATIONS_QUERY) spo2
                ON up.user_id = spo2.user_id
                GROUP BY up.user_id, up.profile, up.last_synced_at
                ORDER BY avg_spo2 $orderBySort, up.$USER_ID_COLUMN
                OFFSET $offset LIMIT $limit
            """
        ParticipantListColumn.TOTAL_STEPS ->
            """
                SELECT up.user_id as $USER_ID_COLUMN, sum(steps.count) as total_steps, up.profile as $PROFILE_COLUMN, up.last_synced_at as $LAST_SYNC_TIME_COLUMN
                FROM user_profiles as up
                LEFT JOIN ($GET_LATEST_STEPS_QUERY) steps
                ON up.user_id = steps.user_id
                GROUP BY up.user_id, up.profile, up.last_synced_at
                ORDER BY total_steps $orderBySort, up.$USER_ID_COLUMN
                OFFSET $offset LIMIT $limit
            """
        ParticipantListColumn.LAST_SYNCED ->
            """
                $GET_USER_QUERY
                ORDER BY $LAST_SYNC_TIME_COLUMN $orderBySort, $USER_ID_COLUMN
                OFFSET $offset LIMIT $limit
            """
    }

internal fun makeAverageHRQuery(count: Int) =
    """
        $GET_AVERAGE_HR_QUERY
        AND up.$USER_ID_COLUMN IN ${makeInConditionString(count)}
        GROUP BY hr.$USER_ID_COLUMN
    """

internal fun makeAverageBGQuery(count: Int) =
    """
        $GET_AVERAGE_BG_QUERY
        AND up.$USER_ID_COLUMN IN ${makeInConditionString(count)}
        GROUP BY bg.$USER_ID_COLUMN
    """

internal fun makeAverageRRQuery(count: Int) =
    """
        $GET_AVERAGE_RR_QUERY
        AND up.$USER_ID_COLUMN IN ${makeInConditionString(count)}
        GROUP BY rr.$USER_ID_COLUMN
    """

internal fun makeAverageSPO2Query(count: Int) =
    """
        $GET_AVERAGE_SPO2_QUERY
        AND up.$USER_ID_COLUMN IN ${makeInConditionString(count)}
        GROUP BY spo2.$USER_ID_COLUMN
    """

internal fun makeAverageBPQuery(count: Int) =
    """
        $GET_AVERAGE_BP_QUERY
        AND up.$USER_ID_COLUMN IN ${makeInConditionString(count)}
        GROUP BY bp.$USER_ID_COLUMN
    """

internal fun makeInConditionString(count: Int): StringBuilder {
    val inConditions = StringBuilder((count * 2) + 2).apply {
        append(" (")
        repeat(count - 1) {
            append("?,")
        }
        append("?)")
    }
    return inConditions
}
