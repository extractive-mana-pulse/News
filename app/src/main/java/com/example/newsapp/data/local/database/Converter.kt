package com.example.newsapp.data.local.database

import androidx.room.TypeConverter
import com.example.newsapp.domain.model.Source

class Converter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name.toString()
    }

    @TypeConverter
    fun toSource(name: Source) : Source {
        return Source(name.toString(), name.toString())
    }
}