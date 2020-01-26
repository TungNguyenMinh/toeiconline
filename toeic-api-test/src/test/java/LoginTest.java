import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import vn.myclass.core.dao.UserDao;
import vn.myclass.core.daoimpl.UserDaoImpl;
import vn.myclass.core.persistence.entity.UserEntity;

public class LoginTest {
    private final Logger log = Logger.getLogger(this.getClass());

    @Test
    public void checkIsUserExist() {
        UserDao userDao = new UserDaoImpl();
        String name = "minhtung";
        String password = "123456";
        UserEntity entity = userDao.isUserExist(name, password);
        if (entity != null) {
            log.error("login success");
        } else {
            log.error("login fail");
        }
    }

    @Test
    public void checkRoleByUser() {
        UserDao userDao = new UserDaoImpl();
        String name = "minhtung";
        String password = "123456";
        UserEntity entity = userDao.findRoleByUser(name, password);
        log.error(entity.getRoleEntity().getRoleId() + "-" + entity.getRoleEntity().getName());
    }
}
