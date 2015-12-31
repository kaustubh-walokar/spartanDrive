//package com.kaustubh.cmpe277termproject.sharing;
//
//import java.util.HashMap;
//
///**
// * Created by kaustubh on 11/20/15.
// */
//public class FileSharing {
//
//    //logged in user name eg kaustubh.walokar@gmail.com
//    String me;
//
//    //user : file_key
//
//    //get json of me
//    HashMap<String,String> sharedWithMe;
//
//    //user : file_key <username : key_filename>
//    HashMap<String,String> sharedByMe;
//
//
//    public String getMe() {
//
//        return me;
//    }
//
//    public void setMe(String me) {
//
//        this.me = me;
//    }
//
//    public HashMap<String, String> getSharedWithMe() {
//
//        return sharedWithMe;
//    }
//
//    public void setSharedWithMe(HashMap<String, String> sharedWithMe) {
//        this.sharedWithMe = sharedWithMe;
//    }
//
//    public HashMap<String, String> getSharedByMe() {
//
//        return sharedByMe;
//    }
//
//    public void setSharedByMe(HashMap<String, String> sharedByMe) {
//
//        this.sharedByMe = sharedByMe;
//    }
//}
package com.kaustubh.cmpe277termproject.aws.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kaustubh on 11/20/15.
 */
public class FileSharing {
    //logged in user name eg kaustubh.walokar@gmail.com
    String loggedInUser;
    //user : file_key
    //get json of loggedInUser
    HashMap<String, ArrayList<String>> sharedWithLoggedInUser;
    //user : file_key <username : key_filename>
    HashMap<String, ArrayList<String>> sharedByLoggedInUser;

    public FileSharing(String user) {
        this.loggedInUser = user;
        sharedByLoggedInUser = new HashMap<>();
        sharedWithLoggedInUser = new HashMap<>();
    }

    public ArrayList<String> getSharedFileForAUser(String userNameToShare) {
        ArrayList<String> listOfFilesShared = sharedWithLoggedInUser.get(userNameToShare);
        if (listOfFilesShared == null)
            listOfFilesShared = new ArrayList<String>();
        return listOfFilesShared;
    }

    public ArrayList<String> appendFileToSharedFileList(String userName, String fileS3Key, String checkForAppendFile) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (checkForAppendFile.equalsIgnoreCase("byme"))
            arrayList = getSharedFileForAUser(userName);
        else
            arrayList = getSharedFileWithLoggedInUser(userName);
        arrayList.add(fileS3Key);
        return arrayList;
    }

    public ArrayList<String> getSharedFileWithLoggedInUser(String userNameToShare) {
        ArrayList<String> listOfFilesShared = sharedWithLoggedInUser.get(userNameToShare);
        if (listOfFilesShared == null)
            listOfFilesShared = new ArrayList<String>();
        return listOfFilesShared;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public HashMap<String, ArrayList<String>> getSharedWithLoggedInUser() {
        return sharedWithLoggedInUser;
    }

    public void setSharedWithLoggedInUser(HashMap<String, ArrayList<String>> sharedWithLoggedInUser) {
        this.sharedWithLoggedInUser = sharedWithLoggedInUser;
    }

    public HashMap<String, ArrayList<String>> getSharedByLoggedInUser() {
        return sharedByLoggedInUser;
    }

    public void setSharedByLoggedInUser(HashMap<String, ArrayList<String>> sharedByLoggedInUser) {
        this.sharedByLoggedInUser = sharedByLoggedInUser;
    }
}