package com.kaustubh.cmpe277termproject.aws.utils;

import android.content.Context;
import android.util.Log;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kaustubh on 11/19/15.
 */
public class AWS {
    private static final boolean FOR_DEV = false;

    public static void getFilesForUser(String user,Context context){


        List<S3ObjectSummary> summaries = getFileObjectsByUser(user,context);

        for (S3ObjectSummary s:summaries) {
           //System.out.printf(s.getKey());
            Log.d("AWS File : ",s.getKey());
        }

    //    updateSharingInfo(context,new FileSharing(),"test");

//        List<String> folders = getFolderObjectsByUser(context);
//
//        for (String s : folders) {
//            //System.out.printf(s.getKey());
//            Log.d("AWS Folder : ", s);
//        }

    }

    public static void shareFileWithUserNamed(String userName, String fileKey, String fileName, Context context) {

        fileName = userName + "/sharedWithMe/" + fileName;

        AmazonS3Client s3Client = Utils.getS3Client(context);
        ObjectMetadata md = new ObjectMetadata();

        CopyObjectResult r = s3Client.copyObject(Constants.BUCKET_NAME, fileKey,
                Constants.BUCKET_NAME, fileName);

        System.out.printf(r.toString());

        return;
    }


    public static void deleteKey(String fileKey, Context context) {

        AmazonS3Client s3Client = Utils.getS3Client(context);
        s3Client.deleteObject(new DeleteObjectRequest(Constants.BUCKET_NAME, fileKey));

        return;
    }


    /**
     * used to get File objects by user name
     * @param user
     * @param context
     * @return  List of S3 object summaries
     */
    public static List<S3ObjectSummary> getFileObjectsByUser(String user,Context context) {
        if(FOR_DEV)
            user="kaustubh.walokar@gmail.com/";

        if (!user.endsWith("/"))
            user = user + "/";

        return getFileObjectsByPrefix(user,context);

    }

    /**
     * used to return Fiel objects by prefix for recursive calls
     * @param prefix
     * @param context
     * @return List of S3 object summaries
     */
    public static List<S3ObjectSummary> getFileObjectsByPrefix(String prefix,Context context) {

        if (!prefix.endsWith("/"))
            prefix = prefix+"/";


        AmazonS3Client s3client = Utils.getS3Client(context);

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(Constants.BUCKET_NAME)
                .withPrefix(prefix)
                .withDelimiter("/") ;

        ObjectListing listings = s3client.listObjects(listObjectsRequest);

        List<S3ObjectSummary> summaries = listings.getObjectSummaries();

        while (listings.isTruncated()) {
            listings = s3client.listNextBatchOfObjects(listings);
            summaries.addAll(listings.getObjectSummaries());
        }

        return summaries;
    }

    public static InfoSummary getFilesSummary(String prefix, Context context) {

        AmazonS3Client s3Client = Utils.getS3Client(context);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(Constants.BUCKET_NAME)
                .withPrefix(prefix);
        ObjectListing objectListing;

        int numbOfFiles = 0;
        long totasize = 0;
        do {
            objectListing = s3Client.listObjects(listObjectsRequest);
            for (S3ObjectSummary objectSummary :
                    objectListing.getObjectSummaries()) {

                System.out.println(" - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() +
                        ")");

                numbOfFiles++;
                totasize += objectSummary.getSize();
            }
            listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());

        return new InfoSummary(numbOfFiles, totasize);
    }

    //Method to fetch Json text file from S3
    public static FileSharing getFileObjectContentByPrefix(String username, Context context) {

        String fileName = username + ".txt";

        AmazonS3Client s3client = Utils.getS3Client(context);
        S3Object s3Object = s3client.getObject(new GetObjectRequest(Constants.BUCKET_NAME, fileName));
        FileSharing fileSharingObject = null;
        try {
            InputStream objectData = s3Object.getObjectContent();

            String json = IOUtils.toString(objectData);
            System.out.println("JSON fetched is " + json);
            fileSharingObject = (FileSharing) new Gson().fromJson(json, FileSharing.class);

        } catch (IOException e) {
            System.out.println("Error @FileSharing.getFileObjectContentByPrefix");
            e.printStackTrace();
        }
        return fileSharingObject;
    }

    public static void uploadToUser(String keyName, Context context, String filePath) {
        AmazonS3Client s3client = Utils.getS3Client(context);
        PutObjectResult r = s3client.putObject(new PutObjectRequest(Constants.BUCKET_NAME, keyName, filePath));
        System.out.printf(r.toString());
    }

    public static void updateSharingInfo(Context context, String sharingUserName, String sharedUserName, String fileS3Key) {

        // Include these values in method parameters.
//            sharingUserName = "kaustubh.walokar@gmail.com";
//            sharedUserName = "madhura.barve@sjsu.edu";
//            fileS3Key = "abc.txt";

        System.out.println("inside update File Sharing Info");
        FileSharing fileSharingInfo = getFileObjectContentByPrefix(sharingUserName, context);
        if (fileSharingInfo == null)
            fileSharingInfo = new FileSharing(sharingUserName);

        HashMap<String, ArrayList<String>> hlist = fileSharingInfo.getSharedByLoggedInUser();
        if (hlist == null)
            hlist = new HashMap<>();

        hlist.put(sharedUserName, fileSharingInfo.appendFileToSharedFileList(sharedUserName, fileS3Key, "byme"));
        fileSharingInfo.setSharedByLoggedInUser(hlist);

        System.out.println("File shared" + fileSharingInfo.getSharedByLoggedInUser());

        ObjectMetadata md = new ObjectMetadata();
        FileSharing fileSharingInfoWithSharedUser = getFileObjectContentByPrefix(sharedUserName, context);

        if (fileSharingInfo == null)
            fileSharingInfo = new FileSharing(sharedUserName);

        hlist = fileSharingInfoWithSharedUser.getSharedWithLoggedInUser();
        if (hlist == null)
            hlist = new HashMap<>();

        hlist.put(sharingUserName, fileSharingInfoWithSharedUser.appendFileToSharedFileList(sharingUserName, fileS3Key, "withme"));
        fileSharingInfoWithSharedUser.setSharedByLoggedInUser(hlist);

        Gson gson = new Gson();
        System.out.println(gson.toJson(fileSharingInfo));
        System.out.println();
        System.out.println(gson.toJson(fileSharingInfoWithSharedUser));


        AmazonS3Client s3client = Utils.getS3Client(context);


        byte[] contentAsBytes = gson.toJson(fileSharingInfo).getBytes();
        ByteArrayInputStream contentsAsStream = new ByteArrayInputStream(contentAsBytes);
        md = new ObjectMetadata();
        md.setContentLength(contentAsBytes.length);
        //s3client.putObject(new PutObjectRequest(Constants.BUCKET_NAME, fileS3Key, contentsAsStream, md));
        PutObjectRequest o = new PutObjectRequest(Constants.BUCKET_NAME, fileS3Key, contentsAsStream, md);
        PutObjectResult result = s3client.putObject(o);

        contentAsBytes = gson.toJson(fileSharingInfoWithSharedUser).getBytes();
        contentsAsStream = new ByteArrayInputStream(contentAsBytes);
        md = new ObjectMetadata();
        md.setContentLength(contentAsBytes.length);
        //s3client.putObject(new PutObjectRequest(Constants.BUCKET_NAME, fileS3Key, contentsAsStream, md));
        o = new PutObjectRequest(Constants.BUCKET_NAME, fileS3Key, contentsAsStream, md);
        result = s3client.putObject(o);


    }

    ///code below is for reference and not mine
    public static List<String> getFolderObjectsByPrefix(String prefix, Context context) {

        if (!prefix.endsWith("/"))
            prefix = prefix + "/";

        AmazonS3Client s3client = Utils.getS3Client(context);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(Constants.BUCKET_NAME)
                .withPrefix(prefix)
                .withDelimiter("/");

        ObjectListing listings = s3client.listObjects(listObjectsRequest);
//        List<String> strs = listings.getCommonPrefixes();

//        String stmp = " ";
//        for (String s:strs ) {
//            stmp = stmp +s+ " , ";
//        }
//        Log.d("AWS Common ",stmp);

        List<String> summaries = listings.getCommonPrefixes();
        while (listings.isTruncated()) {
            listings = s3client.listNextBatchOfObjects(listings);
            summaries.addAll(listings.getCommonPrefixes());
        }

        return summaries;
    }


    /*Kaustubh Code
    public static String updateSharingInfo(Context context,FileSharing fileSharingInfo, String fileName) {

        AmazonS3Client s3Client = Utils.getS3Client(context);
        //File file = new File(path);
        Gson gson = new Gson();
        String json;//
        //get object

        S3Object object = s3Client.getObject(
                new GetObjectRequest(Constants.BUCKET_NAME, fileName));

        if(object==null){
            // create new object
            InputStream objectData = object.getObjectContent();
            // Process the objectData stream.
            try {
                objectData.close();
                json=IOUtils.toString(objectData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            acl.grantPermission(GroupGrantee.AuthenticatedUsers, Permission.Write);

            //PutObjectRequest o = new PutObjectRequest(Constants.BUCKET_NAME, fileName, file).withAccessControlList(acl);
        }else {
            // use this

            json = gson.toJson(fileSharingInfo);

            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            acl.grantPermission(GroupGrantee.AuthenticatedUsers, Permission.Write);

            InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        }


        //downad
        if(FOR_DEV){
            fileSharingInfo = new FileSharing();
            fileSharingInfo.setMe("kaustubh.walokar@gmail.com");
            HashMap<String, String> hm = new HashMap<String, String>(1);
                hm.put("test", "someobjkey");
                hm.put("test2", "someother other file");
            fileSharingInfo.setSharedByMe(hm);

            //fileSharingInfo.setSharedWithMe(hm);

            fileName = "test_json.txt";
        }

//        ObjectMetadata md = new ObjectMetadata();
//        md.setContentLength(json.getBytes().length);
//
//        PutObjectRequest o = new PutObjectRequest(Constants.BUCKET_NAME,fileName,stream,md);
//        PutObjectResult result = s3Client.putObject(o);

//        return (result.getETag());
        return null;

    }*/
////code above this works!!!

    public static class InfoSummary {
        public int numberOfFiles = 0;
        public long sizeOfFiles = 0;

        public InfoSummary(int numberOfFiles, long sizeOfFiles) {
            this.numberOfFiles = numberOfFiles;
            this.sizeOfFiles = sizeOfFiles;
        }
    }
    /*
     public static List<S3ObjectSummary> getFileObjectsByPrefix(String prefix,Context context) {

        if (!prefix.endsWith("/"))
            prefix = prefix+"/";


        AmazonS3Client s3client = Utils.getS3Client(context);

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(Constants.BUCKET_NAME)
                .withPrefix(prefix)
                .withDelimiter("/") ;

        ObjectListing listings = s3client.listObjects(listObjectsRequest);

        List<S3ObjectSummary> summaries = listings.getObjectSummaries();

        while (listings.isTruncated()) {
            listings = s3client.listNextBatchOfObjects(listings);
            summaries.addAll(listings.getObjectSummaries());
        }

        return summaries;
    }
     */

    /*

    private boolean putArtistPage(AmazonS3 s3,String bucketName, String key, String webpage)
    {
        try
        {
            byte[]                  contentAsBytes = webpage.getBytes("UTF-8");
            ByteArrayInputStream    contentsAsStream      = new ByteArrayInputStream(contentAsBytes);
            ObjectMetadata          md = new ObjectMetadata();
            md.setContentLength(contentAsBytes.length);
            s3.putObject(new PutObjectRequest(bucketname, key, contentsAsStream, md));
            return true;
        }
        catch(AmazonServiceException e)
        {
            log.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
        catch(Exception ex)
        {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    */


}
