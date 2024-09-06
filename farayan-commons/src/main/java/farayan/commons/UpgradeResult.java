
package farayan.commons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class UpgradeResult {

    @SerializedName("Version")
    @Expose
    private Integer Version;

    @SerializedName("Title")
    @Expose
    private String Title;

    @SerializedName("Comment")
    @Expose
    private String Comment;

    @SerializedName("Changes")
    @Expose
    private String Changes;

    @SerializedName("DownloadUrl")
    @Expose
    private String DownloadUrl;

    @SerializedName("Type")
    @Expose
    private Integer Type;

    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    /**
     * 
     * @return
     *     The Version
     */
    public Integer getVersion() {
        return Version;
    }

    /**
     * 
     * @param Version
     *     The Version
     */
    public void setVersion(Integer Version) {
        this.Version = Version;
    }

    /**
     * 
     * @return
     *     The Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * 
     * @param Title
     *     The Title
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * 
     * @return
     *     The Comment
     */
    public String getComment() {
        return Comment;
    }

    /**
     * 
     * @param Comment
     *     The Comment
     */
    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    /**
     * 
     * @return
     *     The Changes
     */
    public String getChanges() {
        return Changes;
    }

    /**
     * 
     * @param Changes
     *     The Changes
     */
    public void setChanges(String Changes) {
        this.Changes = Changes;
    }

    /**
     * 
     * @return
     *     The DownloadUrl
     */
    public String getDownloadUrl() {
        return DownloadUrl;
    }

    /**
     * 
     * @param DownloadUrl
     *     The DownloadUrl
     */
    public void setDownloadUrl(String DownloadUrl) {
        this.DownloadUrl = DownloadUrl;
    }

    /**
     * 
     * @return
     *     The Type
     */
    public Integer getType() {
        return Type;
    }

    /**
     * 
     * @param Type
     *     The Type
     */
    public void setType(Integer Type) {
        this.Type = Type;
    }

    /**
     * 
     * @return
     *     The ErrorMessage
     */
    public String getErrorMessage() {
        return ErrorMessage;
    }

    /**
     * 
     * @param ErrorMessage
     *     The ErrorMessage
     */
    public void setErrorMessage(String ErrorMessage) {
        this.ErrorMessage = ErrorMessage;
    }

}
