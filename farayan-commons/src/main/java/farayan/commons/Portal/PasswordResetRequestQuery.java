
package farayan.commons.Portal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class PasswordResetRequestQuery {

    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("Success")
    @Expose
    private boolean Success;
    @SerializedName("Title")
    @Expose
    private String Title;

    /**
     * 
     * @return
     *     The Message
     */
    public String getMessage() {
        return Message;
    }

    /**
     * 
     * @param Message
     *     The Message
     */
    public void setMessage(String Message) {
        this.Message = Message;
    }

    public PasswordResetRequestQuery withMessage(String Message) {
        this.Message = Message;
        return this;
    }

    /**
     * 
     * @return
     *     The Success
     */
    public boolean isSuccess() {
        return Success;
    }

    /**
     * 
     * @param Success
     *     The Success
     */
    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public PasswordResetRequestQuery withSuccess(boolean Success) {
        this.Success = Success;
        return this;
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

    public PasswordResetRequestQuery withTitle(String Title) {
        this.Title = Title;
        return this;
    }

}
