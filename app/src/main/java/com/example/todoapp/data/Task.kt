package com.example.todoapp.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "tasks")
class Task(@field:PrimaryKey
           @field:ColumnInfo(name = "entryid") val id: String,
           @field:ColumnInfo(name = "title") val title: String?,
           @field:ColumnInfo(name = "description") val description: String?,
           @field:ColumnInfo(name = "completed") val isCompleted: Boolean) {

    val titleForList: String?
        get() = if (!title.isNullOrEmpty()) {
            title
        } else {
            description
        }

    val isActive: Boolean
        get() = !isCompleted

    val isEmpty: Boolean
        get() = title.isNullOrBlank() && description.isNullOrBlank()

    @Ignore
    constructor(title: String?, description: String?) :
            this(UUID.randomUUID().toString(), title, description, false)

    @Ignore
    constructor(title: String?, description: String?, id: String) :
            this(id, title, description, false)

    @Ignore
    constructor(title: String?, description: String?, completed: Boolean) :
            this(UUID.randomUUID().toString(), title, description, completed)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val task = other as Task?
        return id == task?.id &&
                title == task.title &&
                description == task.description
    }

    override fun hashCode(): Int {
        val myTitle = title ?: ""
        val myDescription = description ?: ""
        val values = arrayOf(id, myTitle, myDescription)
        return Arrays.hashCode(values)
    }

    override fun toString(): String {
        return "Task: $title, $description"
    }

}
