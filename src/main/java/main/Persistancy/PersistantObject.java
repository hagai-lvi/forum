package main.Persistancy;

/**
 * Created by xkcd on 6/5/2015.
 */
public class PersistantObject {

    public static HibernatePersistancyAbstractor pers = HibernatePersistancyAbstractor.getPersistanceAbstractor();



    public void Save(){    // save the forum to the database
        pers.save(this);
    }

    public void SaveOrUpdate(){    // save the forum to the database
        pers.saveOrUpdate(this);
    }

    public void Update(){    // save the forum to the database
        pers.Update(this);
    }


}
