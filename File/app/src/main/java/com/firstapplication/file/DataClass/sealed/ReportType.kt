package com.firstapplication.file.DataClass.sealed

sealed class ReportType {
    object Question : ReportType()
    object Reply : ReportType()
}