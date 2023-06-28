package com.samsung.healthcare.cloudstorageservice.adapter.web.router

const val SERVICE_PATH = "/cloud-storage"

const val UPLOAD_OBJECT_URL_PATH = "$SERVICE_PATH/projects/{projectId}/upload-url"

const val DOWNLOAD_OBJECT_PATH = "$SERVICE_PATH/projects/{projectId}/download"

const val DOWNLOAD_OBJECT_URL_PATH = "$SERVICE_PATH/projects/{projectId}/download-url"

const val LIST_OBJECTS_PATH = "$SERVICE_PATH/projects/{projectId}/list"

const val DELETE_OBJECT_PATH = "$SERVICE_PATH/projects/{projectId}/delete"

const val PARTICIPANT_UPLOAD_OBJECT_URL_PATH = "$SERVICE_PATH/projects/{projectId}/participants/upload-url"

const val PARTICIPANT_DOWNLOAD_OBJECT_PATH = "$SERVICE_PATH/projects/{projectId}/participants/download"
