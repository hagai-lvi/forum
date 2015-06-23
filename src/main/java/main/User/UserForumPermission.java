package main.User;

import main.exceptions.PermissionDeniedException;
import main.exceptions.SubForumAlreadyExistException;
import main.exceptions.SubForumDoesNotExistException;
import main.forum_contents.Forum;
import main.interfaces.*;

import javax.persistence.*;

/**
 * Created by hagai_lvi on 4/20/15.
 */

@Entity
public class UserForumPermission implements ForumPermissionI {

	public UserForumPermission() {
	}
	@Transient
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UserForumPermission.class.getName());

	//TODO add logger

	@OneToOne(targetEntity = Forum.class)
	private ForumI forum;
	private Permissions permissions;

	public UserForumPermission(Permissions permissions, ForumI forum){
		//TODO use state for permissions? should the permissions be final?
		this.forum = forum;
		this.permissions = permissions;
	}

	public static ForumPermissionI createUserForumPermissions(Permissions permissions, ForumI forum){
		if (forum == null){
			throw new IllegalArgumentException("forum can not be null");
		}
		if ((!(permissions.compareTo(Permissions.PERMISSIONS_GUEST) >=  0)
				&& (permissions.compareTo(Permissions.PERMISSIONS_ADMIN) <= 0))){
			throw new IllegalArgumentException("There is no such forum permissions: " + permissions);
		}
		logger.trace("Created permissions for forum " + forum.getName() + " - " + permissions);
		return new UserForumPermission(permissions, forum);
	}

	@Override
	public void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException {
		if (!isAdmin()){
			throw new PermissionDeniedException("User has no permission to create a subforum");
		}
		logger.trace("Created sub-forum " + name);
		forum.addSubForum(name);
	}

	@Override
	public boolean isAdmin() {
		return permissions.compareTo(Permissions.PERMISSIONS_ADMIN) >= 0;
	}

	@Override
	public void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExistException {
		if (!isAdmin()){
			throw new PermissionDeniedException("User has no permission to delete a subforum");
		}
		logger.trace("Deleted sub-forum " + toDelete.getTitle());
		forum.deleteSubForum(toDelete.getTitle());
	}

	@Override
	public void setAdmin(UserI admin) throws PermissionDeniedException {
		if(permissions.equals(Permissions.PERMISSIONS_SUPERADMIN)) {
			logger.trace("User " + admin.getUsername() + " set as admin of forum " + forum.getName());
			forum.setAdmin(admin);
		}
		else {
			throw new PermissionDeniedException("User has no permission to set administrator");
		}
	}

	@Override
	public void setPolicy(ForumPolicyI policy) throws PermissionDeniedException {
		if (! isAdmin()){
			throw new PermissionDeniedException("User has no permission to set forum policy");
		}
		logger.trace("Policy of forum " + forum.getName() + " changed");
		forum.setPolicy(policy);
	}

	@Override
	public String viewStatistics() {
		//TODO - not implemented
		return null;
	}

	@Override
	public boolean findSubforum(String name) {
		for (SubForumI sf : forum.getSubForums()){
			if (sf.getTitle().equals(name)){
				return true;
			}
		}
		return false;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	public Integer getId() {
		return id;
	}

	@Override
	public String getForumName() {
		return forum.getName();
	}

	@Override
	public ForumI getForum() {
		return forum;
	}

	@Override
	public SubForumI getSubForum(String name) {
		for (SubForumI sf : forum.getSubForums()){
			if (sf.getTitle().equals(name)){
				return sf;
			}
		}
		return null;
	}

	public void setId(Integer id) {
		this.id = id;
	}


}
