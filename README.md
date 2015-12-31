CMPE 277 Term Project Plan

* * *

  

### Project Topic : Spartan Drive

#### Team Members

  

Arpit Khare     | 010107214 | [arpit.khare@sjsu.edu ](mailto:arpit.khare@sjsu.edu)

Kaustubh Walokar| 010112362 |[kaustubh.walokar@sjsu.edu](mailto:kaustubh.walokar@sjsu.edu)

Madhura Barve| 010125505 | [madhura.barve@sjsu.edu](mailto:madhura.barve@sjsu.edu)

Pooja Shukla| 009991215 | [pooja.shukla@sjsu.edu](mailto:pooja.shukla@sjsu.edu)

Utkarsh Saxena| 010104120 | [utkarsh.saxena@sjsu.edu](mailto:utkarsh.saxena@sjsu.edu)

  

#### The platform and technology choices

  

Platform: Android Min SDK Kitkat 4.4.4

Services: Google APIs service for Login , Google Drive / DropBox for file storage

Database: Google Drive

  

#### Project abstract 

Spartan Drive is an educational app which is developed to solve the problem of file sharing among different users, with this app user can share or unshare files or full folder among themselves on a single click.

This application is developed for Android users, which allows registered user to upload and stores files on the system of different types like doc, xls, pdf and ppt. The files are arranged in the typical hierarchical tree management structure where user can perform CRUD operations on folders and files. The description for files and folders are stored. Users can browse files and folder, search them from their name, can share/ unshare with other users, email any existing file to a specific email.

The application also sends users the notification of the storage he/she has used and space remaining. This app also sends a push notification for the user  whenever he is being shared/unshared from any files/folder.

  
  

#### Project Features 

This app enables one to upload, manage, and share files among friends. The major features include:

1. Login with Google Plus 
2. File upload. A user can upload files into an folder 
3. Folder management  
    1. An folder has a title and description 
    2. A user can create, update, and delete folders 

4. File information 
    1. File name 
    2. File description 
    3. Supported file types, use different icon for each type 
        1. doc, xls, pdf, ppt 

5. File sharing 
    1. Share and unshare a single file (read-only) 
    2. Share the whole folder 

6. A user must be able to search files (owned or shared) by name and/or text in description 
7. A user must be able to browse files(owned or shared) 
    1. Browse list of folders and list of files within a folder 
    2. Must support icons for folder and different file types 
    3. View each file(only the supported types) 
    4. Email the current file  
    5. Navigate into any level of directories one by one 

8. Cloud storage service (either Dropbox or Google Drive) for storing the files 
9. Usage report: the user should be able to see how many files and folders he owns, and their total size.   
10. Push notification: when a file is shared with you or unshared with you, you get a push notification.  
  

#### High Level Architecture diagram

  

#### Milestones

  

 

Sr No

 

Date

 

Milestone

 

1

 

11/18/2015

 

Get Google Login and Auth working

 

2

 

11/21/2015

 

Get File picker working 

 

3

 

11/24/2015

 

Spartan Drive - Get File upload working

 

4

 

11/28/2015

 

Spartan Drive - Get File sharing working

 

5

 

12/3/2015

 

Get Search Working

 

6

 

12/7/2015

 

Integrate all components

 

7

 

12/13/2015

 

Finalize and Submit

  
  

DUE on 27’TH NOV 2015

 

Feature

 

Size

 

Allocation

 

Login / Auth

 

Small

 

A

 

File Picker

- Folder Management  
- File Information 
 

V Large

 

M,K

 

File Upload

 

Small

 

P

 

Search

 

Small

 

P

 

Usage Report

 

Large

 

U

 

Notification - GCM

 

Medium

 

A

 

File Sharing

 

small

 

K,M