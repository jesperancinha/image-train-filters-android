package org.jesperancinha.itf.app.manager

import java.io.File
import java.io.Serializable

class FileManagerItem (
    val filename: String,
    val date: String,
    val fileType: FileType,
    val file: File,
) : Comparable<FileManagerItem>, Serializable
{
    override fun compareTo(other: FileManagerItem): Int =
        this.filename.compareTo(other.filename)

}