package main.User;

import main.exceptions.PermissionDeniedException;
import main.exceptions.SubForumAlreadyExistException;
import main.exceptions.SubForumDoesNotExsitsException;
import main.forum_contents.Forum;
import main.interfaces.*;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by hagai_lvi on 4/20/15.
 */

@Entity
public class UserForumPermission implements ForumPermissionI {

	public UserForumPermission() {
	}

	public enum PERMISSIONS{
		PERMISSIONS_GUEST,
		PERMISSIONS_USER,
		PERMISSIONS_ADMIN
	}
	//TODO add logger

	@OneToOne(targetEntity = Forum.class)
	private ForumI forum;
	private PERMISSIONS permissions;

	private UserForumPermission(PERMISSIONS permissions, ForumI forum){
		//TODO use state for permissions? should the permissions be final?
		this.forum = forum;
		this.permissions = permissions;
	}

	public static ForumPermissionI createUserForumPermissions(PERMISSIONS permissions, ForumI forum){
		if (forum == null){
			throw new IllegalArgumentException("forum can not be nul");
		}
		if ((!(permissions.compareTo(PERMISSIONS.PERMISSIONS_GUEST) >=  0)
				&& (permissions.compareTo(PERMISSIONS.PERMISSIONS_ADMIN) <= 0))){
			throw new IllegalArgumentException("There is no such forum permissions: " + permissions);
		}
		return new UserForumPermission(permissions, forum);
	}


	@Override
	public Collection<SubForumI> getSubForums() {
		//TODO
		return null;
	}

	@Override
	public void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException {
		if (!isAdmin()){
			throw new PermissionDeniedException("User has no permission to create a subforum");
		}
		forum.createSubForum(name);
	}

	private boolean isAdmin() {
		return permissions.compareTo(PERMISSIONS.PERMISSIONS_ADMIN) >= 0;
	}

	@Override
	public void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExsitsException {
		if (!isAdmin()){
			throw new PermissionDeniedException("User has no permission to delete a subforum");
		}
		forum.deleteSubForum(toDelete);
	}

	@Override
	public void setAdmin(UserI admin){
		permissions = PERMISSIONS.PERMISSIONS_ADMIN;

	}

	@Override
	public void setPolicy(ForumPolicyI policy) throws PermissionDeniedException {
		if (! isAdmin()){
			throw new PermissionDeniedException("User has no permission to set forum policy");
		}
		forum.setPolicy(policy);
	}

	@Override
	public String viewStatistics() {
		//TODO
		return null;
	}

	@Override
	public boolean findSubforum(String name) {
		return false;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
