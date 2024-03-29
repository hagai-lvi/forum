package main.interfaces;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface SubForumPolicyI {

    boolean isValidMessage(String title, String text);

    int getMaxModerators();

    boolean canAssignModerator(UserI user, SubForumI subforum);
}
