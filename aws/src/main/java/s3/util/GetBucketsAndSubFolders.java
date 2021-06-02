package s3.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

/**
 * This class contains POCs related to getting AWS S3 buckets and getting sub folder info.
 * Make sure AWS credentials are stored in the ~/.aws location for linux
 */
public class GetBucketsAndSubFolders {

    public static void main(String[] args) {
        AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard();
        s3ClientBuilder.setRegion("us-west-2");
        AmazonS3 s3 = s3ClientBuilder.build();

        //list S3 buckets names
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
        String bucketName = "tala-integration-archive";
        ListObjectsV2Result result = s3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        //list buckets sub folder details with given folder name
        for (S3ObjectSummary object : objects) {
            String key = object.getKey();
            if(key != null && key.contains("cassandra") && key.contains("cust_bbfb2eceacdd42be86099ad00e126bc0")){
                System.out.println("**********************************************************");
                System.out.println(key + " - " + object.getSize());
                System.out.println("**********************************************************");
            }
        }
        s3.shutdown();
    }
}
