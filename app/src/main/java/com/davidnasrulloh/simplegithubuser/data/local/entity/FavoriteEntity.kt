package com.davidnasrulloh.simplegithubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "user")
@Parcelize
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String,

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
) : Parcelable