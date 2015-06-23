package main.User;

import main.exceptions.ForumNotFoundException;
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

	//@OneToOne(targetEntity = Forum.class)
	private String forumName;
	private Permissions permissions;

	public UserForumPermission(Permissions permissions, String forumName){
		//TODO use state for permissions? should the permissions be final?
		this.forumName = forumName;
		this.permissions = permissions;
	}

	public static ForumPermissionI createUserForumPermissions(Permissions permissions, String forumName){
		Forum forum = Forum.load(forumName);
		if (forum == null){
			throw new IllegalArgumentException("forum can not be null");
		}
		if ((!(permissions.compareTo(Permissions.PERMISSIONS_GUEST) >=  0)
				&& (permissions.compareTo(Permissions.PERMISSIONS_ADMIN) <= 0))){
			throw new IllegalArgumentException("There is no such forum permissions: " + permissions);
		}
		logger.trace("Created permissions for forum " + forumName + " - " + permissions);
		return new UserForumPermission(permissions, forumName);
	}

	@Override
	public SubForumI createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException, ForumNotFoundException {
		if (!isAdmin()){
			throw new PermissionDeniedException("User has no permission to create a subforum");
		}
		logger.trace("Created sub-forum " + name);
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		return forum.addSubForum(name);
	}

	@Override
	public boolean isAdmin() {
		return permissions.compareTo(Permissions.PERMISSIONS_ADMIN) >= 0;
	}

	@Override
	public void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException, SubForumDoesNotExistException, ForumNotFoundException {
		if (!isAdmin()){
			throw new PermissionDeniedException("User has no permission to delete a subforum");
		}
		logger.trace("Deleted sub-forum " + toDelete.getTitle());
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		forum.deleteSubForum(toDelete.getTitle());
	}

	@Override
	public void setAdmin(UserI admin) throws PermissionDeniedException, ForumNotFoundException {
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		if(permissions.equals(Permissions.PERMISSIONS_SUPERADMIN)) {
			logger.trace("User " + admin.getUsername() + " set as admin of forum " + forumName);
			forum.setAdmin(admin);
		}
		else if (permissions.equals(Permissions.PERMISSIONS_ADMIN)) {
			forum.setAdmin(admin);
			this.permissions = Permissions.PERMISSIONS_USER;
		}
		else {
			throw new PermissionDeniedException("User has no permission to set administrator");
		}
	}

	@Override
	public void setPolicy(ForumPolicyI policy) throws PermissionDeniedException, ForumNotFoundException {
		if (! isAdmin()){
			throw new PermissionDeniedException("User has no permission to set forum policy");
		}
		logger.trace("Policy of forum " + forumName + " changed");
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		forum.setPolicy(policy);
	}

	@Override
	public String viewStatistics() {
		//TODO - not implemented
		return null;
	}

	@Override
	public boolean findSubforum(String name) throws ForumNotFoundException {
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
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
		return forumName;
	}

	@Override
	public ForumI getForum() throws ForumNotFoundException {
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		return forum;
	}

	@Override
	public SubForumI getSubForum(String name) throws ForumNotFoundException, SubForumDoesNotExistException {
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		for (SubForumI sf : forum.getSubForums()){
			if (sf.getTitle().equals(name)){
				return sf;
			}
		}
		throw new SubForumDoesNotExistException();
	}

	public void setId(Integer id) {
		this.id = id;
	}


}
