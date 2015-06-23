package main.interfaces;


import main.User.UserForumID;

import java.util.List;

/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface PersistancyAbstractionI {

	public void save(Object o);

	public void saveOrUpdate(Object o);

	public <T> T load(Class<T> c, String id);

	public <T> T load(Class<T> c, int id);

	public <T> T load(Class<T> c, UserForumID idclass);

	public void Update(Object o);

	void Delete(Object o);

	public List executeQuery(String query);
}
