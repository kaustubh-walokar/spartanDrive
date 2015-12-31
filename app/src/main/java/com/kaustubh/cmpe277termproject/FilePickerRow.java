package com.kaustubh.cmpe277termproject;

import android.util.Log;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaustubh on 11/22/15.
 */
public class FilePickerRow {

    RowType rowType;
    FileType fileType;
    String name;
    String s3Key;
    String description;

    public FilePickerRow(RowType rowType, FileType fileType, String name, String s3Key, String description) {

        this.rowType = rowType;
        this.fileType = fileType;
        this.name = name;
        this.s3Key = s3Key;
        this.description = description;
    }

    public static ArrayList<FilePickerRow> getRowsFromS3ObjectSummaries(String prefix, List<S3ObjectSummary> summ, ArrayList<FilePickerRow> rowsOfFiles) {

        if (rowsOfFiles == null)
            rowsOfFiles = new ArrayList<>();
        else
            rowsOfFiles = new ArrayList<>(rowsOfFiles);

        for (S3ObjectSummary s : summ) {
            String key = s.getKey();
            String filename = "";
            if ((key.contains(prefix)))
                filename = key.replace(prefix, "");
//            filename = prefix;
            FilePickerRow row = null;
            if (filename != "") {
                row = new FilePickerRow(
                        RowType.file
                        , FileType.na
                        , filename
                        , s.getKey()
                        , s.getLastModified().toString());
            }
            if (row != null) {

                rowsOfFiles.add(row);

            }
        }

        Log.d("FILES", rowsOfFiles.toString());
        return rowsOfFiles;
    }

    public static ArrayList<FilePickerRow> getRowsFromFolderNames(String user, List<String> folders, ArrayList<FilePickerRow> rowsOfFolder) {

        if (rowsOfFolder == null)
            rowsOfFolder = new ArrayList<>();
        else
            rowsOfFolder = new ArrayList<>(rowsOfFolder);

        for (String folderName : folders) {
            String key = folderName;
            String name = "";
            if ((key.contains(user)))
                name = key.replace(user, "");

            FilePickerRow row = null;

            if (folderName != "") {
                row = new FilePickerRow(
                        RowType.folder
                        , FileType.na
                        , name
                        , folderName
                        , folderName + "1");
            }
            if (row != null) {

                rowsOfFolder.add(row);

            }
        }
        Log.d("FOLDER", rowsOfFolder.toString());
        return rowsOfFolder;
    }

    public boolean isFile() {
        if (this.rowType == RowType.file)
            return true;
        return false;
    }

    public RowType getType() {
        return this.rowType;
    }



    @Override
    public String toString() {
        return "FilePickerRow{" +
                "rowType=" + rowType +
                ", fileType=" + fileType +
                ", name='" + name + '\'' +
                ", s3Key='" + s3Key + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public RowType getRowType() {
        return rowType;
    }

    public void setRowType(RowType rowType) {
        this.rowType = rowType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static enum RowType {
        file, folder
    }

    public static enum FileType {
        txt, doc, docx, pdf, na
    }
}
