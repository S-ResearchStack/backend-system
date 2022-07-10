package com.samsung.healthcare.trinoruleupdateservice.adapter.output.web

import com.google.gson.Gson
import com.samsung.healthcare.trinoruleupdateservice.application.config.ApplicationProperties
import com.samsung.healthcare.trinoruleupdateservice.application.port.output.GetUsersPort
import com.samsung.healthcare.trinoruleupdateservice.domain.accountservice.User
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

private val ROLE_REGEX: Regex =
    "^(team-admin|service-account|\\d+:(project-owner|head-researcher|researcher))$".toRegex()

@Component
class AccountServiceAdapter(
    private val config: ApplicationProperties,
) : GetUsersPort {
    var rt = RestTemplate()

    override fun getUsers(): List<User> {
        val response = rt.exchange(
            "${config.accountService.url}/account-service/users",
            HttpMethod.GET,
            HttpEntity(LinkedMultiValueMap<String, String>(), HttpHeaders()),
            String::class.java,
        )

        val users = Gson().fromJson(response.body, Array<User>::class.java).toList()
        users.forEach { user ->
            user.roles.forEach { role ->
                require(role matches ROLE_REGEX)
            }
        }
        return users
    }
}
