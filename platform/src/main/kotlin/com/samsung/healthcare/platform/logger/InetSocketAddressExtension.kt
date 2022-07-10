package com.samsung.healthcare.platform.logger

import java.net.InetSocketAddress

fun InetSocketAddress.asString(): String {
    return if (isUnresolved) {
        "$hostName:$port"
    } else {
        "$hostName/${address.hostAddress}:$port"
    }
}
