package radial_design.mapsfoxtrot;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan on 11/04/2016.
 */
@DynamoDBTable(tableName = "Users")
public class User implements Serializable {
    private String UserName;
    private List<Integer> ExpDates;
    private String Name;
    private String Password;
    private String PoolPassword;
    private String eMail;
    //private Map<String, String> settingsMap;

    @DynamoDBHashKey(attributeName = "User Name")
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String UserName) {  //this is the key value, set is not relevant
        this.UserName = UserName;
    }

    @DynamoDBAttribute(attributeName = "ExpDates")
    public List<Integer> getExpDates() {
        return ExpDates;
    }
    public void setExpDates(List<Integer> ExpDates) {
        this.ExpDates = ExpDates;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String Name() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }

    @DynamoDBAttribute(attributeName = "Password")
    public String getPassword() {
        return Password;
    }
    public void setPassword(String Password) { //password is not for changes
        this.Password = Password;
    }

    @DynamoDBAttribute(attributeName = "Pool Password")
    public String getPoolPassword() {
        return PoolPassword;
    }
    public void setPoolPassword(String PoolPassword) {
        this.PoolPassword = PoolPassword;
    }

    @DynamoDBAttribute(attributeName = "e-mail")
    public String geteMail() {
        return eMail;
    }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    /*@DynamoDBAttribute(attributeName = "Settings Map")
    public Map<String, String> getSettingsMap() {
        return settingsMap;
    }
    public void setSettingsMap(Map<String, String> settingsMap) {
        this.settingsMap = settingsMap;
    }*/
}