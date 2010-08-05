package ap2pc.conversation;

import java.sql.Timestamp;

/* 

 */
public class ConversationMessage
{
    private String from;
    private long timestamp;
    private String to;
    private String message;
    private String identifier;


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String timestamp()
    {
        Timestamp ts = new Timestamp(timestamp);
        return ts.toString().substring(0, ts.toString().lastIndexOf("."));
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString()
    {
        return "["+timestamp()+"] "+from+": "+message;
    }
}
