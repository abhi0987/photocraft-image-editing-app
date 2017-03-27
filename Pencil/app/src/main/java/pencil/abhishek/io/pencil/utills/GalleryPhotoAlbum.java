package pencil.abhishek.io.pencil.utills;

/**
 * Created by Abhishek on 5/13/2016.
 */
public class GalleryPhotoAlbum  {

    private long bucketId;
    private String bucketName;
    private String dateTaken;
    private String data;
    private int totalCount;
    private String coverArt;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public long getBucketId() {
        return bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(String coverArt) {
        this.coverArt = coverArt;
    }






}
