package rmit.ad.myapplication.messages;

public class MessageList {
    private String fullname,lastMessage,profile_pic,userID,chatKey;
    private int unseenMessage;

    public MessageList(String userID,String fullname, String lastMessage, String profile_pic,int unseenMessage, String chatKey) {
        this.userID = userID;
        this.fullname = fullname;
        this.lastMessage = lastMessage;
        this.profile_pic = profile_pic;
        this.unseenMessage = unseenMessage;
        this.chatKey = chatKey;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnseenMessage() {
        return unseenMessage;
    }

    public void setUnseenMessage(int unseenMessage) {
        this.unseenMessage = unseenMessage;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }
}
