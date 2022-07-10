package com.samsung.healthcare.trinoruleupdateservice.domain.accountservice

data class User(
    val id: String,
    val email: String,
    val roles: List<String>,
) {
    companion object {
        fun newUser(id: String, email: String, roles: List<String>): User {
            return User(id, email, roles)
        }
    }
}
