package com.tumme.scrudstudents.data.local

import androidx.room.TypeConverter
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.Level
import com.tumme.scrudstudents.data.local.model.UserRole
import java.util.Date

class Converters {
    // ========== Date Converters ==========

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    // ========== Gender Converters ==========

    @TypeConverter
    fun fromGender(value: String?): Gender? = value?.let { Gender.from(it) }

    @TypeConverter
    fun genderToString(gender: Gender?): String? = gender?.value

    // ========== LevelCourse Converters (ANCIEN - gardé pour compatibilité) ==========

    @TypeConverter
    fun fromLevelCourse(value: String?): LevelCourse? = value?.let { LevelCourse.from(it) }

    @TypeConverter
    fun levelCourseToString(level: LevelCourse?): String? = level?.value

    // ========== Level Converters (NOUVEAU) ==========

    @TypeConverter
    fun fromLevel(level: Level): String {
        return level.value
    }

    @TypeConverter
    fun toLevel(value: String): Level {
        return Level.fromString(value)
    }

    // ========== UserRole Converters (NOUVEAU) ==========

    @TypeConverter
    fun fromUserRole(role: UserRole): String {
        return role.value
    }

    @TypeConverter
    fun toUserRole(value: String): UserRole {
        return UserRole.fromString(value)
    }
}