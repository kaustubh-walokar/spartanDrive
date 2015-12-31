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

▪ IDE: Android Studio (Min. API Level:19, Max API Level: 21)
▪ Android Version: KitKat 4.4
▪ AWS SDK: For uploading/downloading files from AWS S3
▪ Google APIs: For Sign-In to the app
▪ Parse: For push notifications
  

#### Project abstract 

Spartan Drive is an educational app which is developed to solve the problem of file sharing among different users, with this app user can share or unshare files or full folder among themselves on a single click.

This application is developed for Android users, which allows registered user to upload and stores files on the system of different types like doc, xls, pdf and ppt. The files are arranged in the typical hierarchical tree management structure where user can perform CRUD operations on folders and files. The description for files and folders are stored. Users can browse files and folder, search them from their name, can share/ unshare with other users, email any existing file to a specific email.

The application also sends users the notification of the storage he/she has used and space remaining. This app also sends a push notification for the user  whenever he is being shared/unshared from any files/folder.

  
Spartan Drive app which we have developed enables its users to upload, manage, and share files among friends. The following are the major features of the application:

1. User can login using the Google Account.
2. User upload a file in uploaded folder of its account.
3. Spartan drive also provides the feature of folder management. Supported file types, use
different icon for each type doc, xls, pdf, ppt
1. User can share file with other registered user of this application.
2. A user is able to browse files (owned or shared), browse list of folders and list of files
within a folder.
3. User can view each files (only the supported types), email the current file, navigate into
any level of directories one by one.
1. We had used AWS-S3 as storage device and has implemented our own file
picker/browser, as opposed to using any stock file or document picker.
2. The user is able to see how many files and folders he owns, and their total size.
3. Application supports push notification, when a file is shared/unshared with a user, he/she will get a push notification about the same.
The motivation behind this project is to develop an app that ease the process of file sharing among user, with the help of this app user can access its file any where any time and also can share files quickly.


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

#### Future Scope:

This is a very useful application for academics and professionals. We restricted our project scope due to strict deadlines but we plan to extend it in the following manner –

1) Integrating it with Dropbox and Google Drive and allowing user to save his files into the choice of his drive.
2) Integrating the camera, so that user can take pictures and they will be uploaded directly to Sparta Drive.
3) Creating a virtualized cloud storage of users unused disk space like old laptops, hard drives, pen drives and uploading the files to it. This feature has a wide scope as user’s file will reside in his own space thus making them more safe and secure. It will also give an added advantage to users to utilize its unused disk space.  
  