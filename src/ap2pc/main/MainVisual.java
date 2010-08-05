/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ap2pc.main;

import ap2pc.conversation.Conversation;

/**
 *
 * @author sarah
 */
public interface MainVisual
{
    public void initConversation(Conversation c);
    public void updateProgramStatus(String programStatus);
    public void updateUsername(String username);

    public boolean requestToJoinConversation(String from, String identifier);

    public void contactListNotify(int index, CHANGETYPE type);

    public enum CHANGETYPE { ADD, REMOVE, MODIFY };
}
