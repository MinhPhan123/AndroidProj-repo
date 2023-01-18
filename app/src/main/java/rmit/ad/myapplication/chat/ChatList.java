package rmit.ad.myapplication.chat;

public class ChatList {
    private String userID, fullname,message,date,time;

    public ChatList(String userID, String fullname, String message, String date, String time) {
        this.userID = userID;
        this.fullname = fullname;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
