/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ap2pc.main;

/**
 *
 * @author sarah
 */
public class User
{
    private String username;
    private String host;
    private String ip;
    private Me.SHOW show;
    private String status;
    private long lastSeen = 0;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public Me.SHOW getShow() {
        return show;
    }

    public void setShow(Me.SHOW show) {
        this.show = show;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdentifier()
    {
        return username+"@"+host;
    }
}
