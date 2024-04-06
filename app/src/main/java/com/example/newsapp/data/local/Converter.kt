package com.example.newsapp.data.local

import androidx.room.TypeConverter
import com.example.newsapp.domain.models.Source

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