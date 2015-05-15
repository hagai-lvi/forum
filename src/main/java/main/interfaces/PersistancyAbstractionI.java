package main.interfaces;


/**
 * Created by hagai_lvi on 4/6/15.
 */
public interface PersistancyAbstractionI {

	public void save(Object o);

	public void saveOrUpdate(Object o);

	public <T> T load(Class<T> c, String id);

	public <T> T load(Class<T> c, int id);

	public void Update(Object o);

}