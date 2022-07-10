package com.samsung.healthcare.trinoruleupdateservice.application.port.output

import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.User

interface GetUsersPort {
    fun getUsers(): List<User>
}
