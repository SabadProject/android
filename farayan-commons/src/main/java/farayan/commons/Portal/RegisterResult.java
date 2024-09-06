
package farayan.commons.Portal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class RegisterResult {

    @Expose
    @SerializedName("Title")
    private String Title;
    
    @Expose
    @SerializedName("Message")
    private String Message;
    
    @Expose
    @SerializedName("UserName")
    private String UserName;
    
    @Expose
    @SerializedName("Password")
    private String Password;
    
    @Expose
    @SerializedName("Success")
    private boolean Success;
    
    @Expose
    @SerializedName("Duplicated")
    private boolean Duplicated;

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
     *     The UserName
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * 
     * @param UserName
     *     The UserName
     */
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

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

	public boolean isDuplicated() {
		return Duplicated;
	}

	public void setDuplicated(boolean duplicated) {
		Duplicated = duplicated;
	}

}
