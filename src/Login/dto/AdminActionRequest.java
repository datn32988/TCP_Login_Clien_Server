package Login.dto;

import java.io.Serializable;

public class AdminActionRequest implements Serializable {
    public enum ActionType {
        DELETE_USER,
        BAN_USER,
        UNBAN_USER,
        SEARCH_USERS
    }

    private ActionType actionType;
    private int targetUserId;
    private String searchTerm;

    public AdminActionRequest() {}

    public AdminActionRequest(ActionType actionType, int targetUserId) {
        this.actionType = actionType;
        this.targetUserId = targetUserId;
    }

    public AdminActionRequest(ActionType actionType, String searchTerm) {
        this.actionType = actionType;
        this.searchTerm = searchTerm;
    }

    // getters/setters
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public int getTargetUserId() { return targetUserId; }
    public void setTargetUserId(int targetUserId) { this.targetUserId = targetUserId; }
    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }
}

