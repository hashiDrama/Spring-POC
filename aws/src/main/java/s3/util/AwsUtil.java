package s3.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AwsUtil {
    public AmazonS3ClientBuilder getS3ClientBuilder() {
        AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard();
        s3ClientBuilder.setRegion("us-west-2");
        return s3ClientBuilder;
    }

    public AmazonS3 getAwsS3Interface() {
        return getS3ClientBuilder().build();
    }
}
