package kg.nurtelecom.o_subscriber_service.dto;

public class PhotoUploadResponse {

    private Long subscriberId;
    private String photoPath;
    private String message;

    public PhotoUploadResponse() {
    }

    public PhotoUploadResponse(Long subscriberId, String photoPath, String message) {
        this.subscriberId = subscriberId;
        this.photoPath = photoPath;
        this.message = message;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}