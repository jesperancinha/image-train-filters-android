package org.jesperancinha.itf.app.manager

import java.io.File
import java.io.Serializable

class FileManagerItem (
    private val filename: String,
    private val date: String,
    private val fileType: FileType,
    private val file: File,
) : Comparable<FileManagerItem?>, Serializable
{

}