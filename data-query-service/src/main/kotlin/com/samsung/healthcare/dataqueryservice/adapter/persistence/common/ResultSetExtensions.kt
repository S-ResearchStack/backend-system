package com.samsung.healthcare.dataqueryservice.adapter.persistence.common

import java.sql.ResultSet
import java.sql.Types

fun ResultSet.get(i: Int): Any? {
    return when (this.metaData.getColumnType(i)) {
        Types.BIGINT, Types.DECIMAL, Types.INTEGER ->
            this.getLong(i)
        Types.DATE ->
            this.getDate(i)
        Types.TIME ->
            this.getTime(i)
        Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE ->
            this.getTimestamp(i)
        else ->
            this.getString(i)
    }
}
