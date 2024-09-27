package researchstack.backend.adapter.incoming.mapper.healthdata

import com.google.protobuf.BoolValue
import com.google.protobuf.DoubleValue
import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import researchstack.backend.adapter.incoming.mapper.toEpochMilli
import researchstack.backend.grpc.HealthData

fun HealthData.Data.toDomain(): Map<String, Any> {
    val result = mutableMapOf<String, Any>()
    this.dataMapMap.map {
        val data = when (it.value.typeUrl.toString()) {
            ProtoTypes.BOOLEAN -> it.value.unpack(BoolValue::class.java).value
            ProtoTypes.TIMESTAMP -> it.value.unpack(Timestamp::class.java).toEpochMilli()
            ProtoTypes.DOUBLE -> it.value.unpack(DoubleValue::class.java).value
            ProtoTypes.LONG -> it.value.unpack(Int64Value::class.java).value
            ProtoTypes.STRING -> it.value.unpack(StringValue::class.java).value
            else -> throw IllegalArgumentException("Unsupported data type.")
        }
        result[it.key] = data
    }
    return result
}

private object ProtoTypes {
    const val BOOLEAN = "type.googleapis.com/google.protobuf.BoolValue"
    const val TIMESTAMP = "type.googleapis.com/google.protobuf.Timestamp"
    const val DOUBLE = "type.googleapis.com/google.protobuf.DoubleValue"
    const val LONG = "type.googleapis.com/google.protobuf.Int64Value"
    const val STRING = "type.googleapis.com/google.protobuf.StringValue"
}
