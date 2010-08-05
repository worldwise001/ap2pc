/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ap2pc.main.visual;

import ap2pc.main.conversation.ConversationMessage;

/**
 *
 * @author sharvey
 */
public interface ConversationVisual
{
    public void displayMessage(ConversationMessage message);
    public void displayStatus(String status);
    public void listNotify(int index, CHANGETYPE type);

    // boolean requestToVote(String reason);

    public enum CHANGETYPE { ADD, REMOVE, MODIFY };
}
