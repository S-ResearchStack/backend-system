package com.samsung.healthcare.platform.adapter.persistence.entity.common

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable

abstract class BaseIdEntity<ID>(
    @Id
    private val id: ID,
) : Persistable<ID> {
    override fun getId(): ID = this.id

    @Transient
    private var isNew: Boolean = false

    fun setNew() {
        this.isNew = true
    }

    override fun isNew(): Boolean = isNew
}
