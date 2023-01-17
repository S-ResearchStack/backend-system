package com.samsung.healthcare.account.adapter.web.router

const val SERVICE_PATH = "/account-service"

const val INVITATION_PATH = "$SERVICE_PATH/invitations"

const val RESET_PASSWORD_PATH = "$SERVICE_PATH/user/password/reset"

const val SIGN_IN_PATH = "$SERVICE_PATH/signin"

const val SIGN_UP_PATH = "$SERVICE_PATH/signup"

const val ASSIGN_ROLE_PATH = "$SERVICE_PATH/user/roles"

const val REMOVE_USER_ROLE_PATH = "$SERVICE_PATH/user/roles/remove"

const val LIST_PROJECT_USER_PATH = "$SERVICE_PATH/users"

const val REFRESH_TOKEN_PATH = "$SERVICE_PATH/token/refresh"

const val VERIFY_EMAIL_PATH = "$SERVICE_PATH/user/email/verify"

const val RESEND_VERIFICATION_EMAIL_PATH = "$SERVICE_PATH/verification"

// for internal use
const val CREATE_ROLE_PATH = "/internal$SERVICE_PATH/roles"

const val LIST_ALL_USER_PATH = "/internal$SERVICE_PATH/users"
