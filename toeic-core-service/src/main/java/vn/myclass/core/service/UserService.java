package vn.myclass.core.service;
import vn.myclass.core.dto.CheckLogin;
import vn.myclass.core.dto.UserDTO;

import java.util.Map;

public interface UserService {

    Object[] findByProperty(Map<String, Object> property, String sortExperssion, String sortDirection, Integer offset, Integer limit);

    UserDTO findById(Integer userId);

    void saveUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    CheckLogin checkLogin(String name, String password);
}
