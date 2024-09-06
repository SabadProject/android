
package farayan.commons.Portal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResultProxy {

    @Expose
    @SerializedName("Success")
    private boolean Success;
    
    @Expose
    @SerializedName("Title")
    private String Title;
    
    @Expose
    @SerializedName("Message")
    private String Message;
    
    @Expose
    @SerializedName("ResultCode")
    private long ResultCode;

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

    /**
     * 
     * @return
     *     The ResultCode
     */
    public long getResultCode() {
        return ResultCode;
    }

    /**
     * 
     * @param ResultCode
     *     The ResultCode
     */
    public void setResultCode(long ResultCode) {
        this.ResultCode = ResultCode;
    }

}
