package main.User;

import main.exceptions.*;
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


	//@OneToOne(targetEntity = Forum.class)
	private String forumName;
	private Permissions permissions;

	public UserForumPermission(Permissions permissions, String forumName){
		this.forumName = forumName;
		this.permissions = permissions;
	}

	public static ForumPermissionI createUserForumPermissions(Permissions permissions, String forumName){
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
		SubForumI s = forum.addSubForum(name);
		forum.getSubForums().replace(name, s);
		forum.Update();
		return s;
	}

	@Override
	public boolean isAdmin() {
		return permissions.compareTo(Permissions.PERMISSIONS_ADMIN) >= 0;
	}

	@Override
	public void deleteSubForum(String toDelete) throws PermissionDeniedException, SubForumDoesNotExistException, ForumNotFoundException {
		if (!isAdmin()){
			throw new PermissionDeniedException("User has no permission to delete a subforum");
		}
		logger.trace("Deleted sub-forum " + toDelete);
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		forum.deleteSubForum(toDelete);

	}

	@Override
	public void setAdmin(UserI admin) throws PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException, UserNotFoundException {
		Forum forum = Forum.load(forumName);
		UserI clone = admin.cloneAs(Permissions.PERMISSIONS_ADMIN);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		if(permissions.equals(Permissions.PERMISSIONS_SUPERADMIN)) {
			logger.trace("User " + admin.getUsername() + " set as admin of forum " + forumName);
			forum.setAdmin(clone);
		}
		else if (permissions.equals(Permissions.PERMISSIONS_ADMIN)) {
			forum.setAdmin(clone);
			this.permissions = Permissions.PERMISSIONS_USER;
		}
		else {
			throw new PermissionDeniedException("User has no permission to set administrator + " +admin.getUsername());
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
	public String viewStatistics() throws PermissionDeniedException {
		if(isAdmin())
			return Forum.load(forumName).viewStatistics();
		throw new PermissionDeniedException("Can not view statistics");
	}

	@Override
	public boolean findSubforum(String name) throws ForumNotFoundException {
		Forum forum = Forum.load(forumName);
		if(forum == null) throw new ForumNotFoundException("Forum not found");
		return forum.getSubForums().containsKey(name);
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
		if(!forum.getSubForums().containsKey(name))
			throw new SubForumDoesNotExistException();
		return
				forum.getSubForums().get(name);
	}

	@Override
	public boolean isGuest() {
		return permissions.equals(Permissions.PERMISSIONS_GUEST);
	}

	@Override
	public Permissions getPermission() {
		return permissions;
	}

//	@Override
//	public void becomeAdmin() {
//		permissions = Permissions.PERMISSIONS_ADMIN;
//	}

	public void setId(Integer id) {
		this.id = id;
	}


}
