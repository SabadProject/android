
package farayan.commons.Portal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class PasswordResetVerifyQuery {

    @SerializedName("Password")
    @Expose
    private String Password;
    @SerializedName("Status")
    @Expose
    private Integer Status;
    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("Success")
    @Expose
    private Boolean Success;
    @SerializedName("Title")
    @Expose
    private String Title;

    /**
     * 
     * @return
     *     The Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * 
     * @param Password
     *     The Password
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    public PasswordResetVerifyQuery withPassword(String Password) {
        this.Password = Password;
        return this;
    }

    /**
     * 
     * @return
     *     The Status
     */
    public Integer getStatus() {
        return Status;
    }

    /**
     * 
     * @param Status
     *     The Status
     */
    public void setStatus(Integer Status) {
        this.Status = Status;
    }

    public PasswordResetVerifyQuery withStatus(Integer Status) {
        this.Status = Status;
        return this;
    }

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

    public PasswordResetVerifyQuery withMessage(String Message) {
        this.Message = Message;
        return this;
    }

    /**
     * 
     * @return
     *     The Success
     */
    public Boolean getSuccess() {
        return Success;
    }

    /**
     * 
     * @param Success
     *     The Success
     */
    public void setSuccess(Boolean Success) {
        this.Success = Success;
    }

    public PasswordResetVerifyQuery withSuccess(Boolean Success) {
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

    public PasswordResetVerifyQuery withTitle(String Title) {
        this.Title = Title;
        return this;
    }

}
