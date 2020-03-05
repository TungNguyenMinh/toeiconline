package vn.myclass.core.service;

import vn.myclass.core.dto.UserDTO;

import java.util.Map;

public interface UserService {
    UserDTO isUserExist(UserDTO dto);

    UserDTO findRoleByUser(UserDTO dto);

    Object[] findByProperty(Map<String, Object> property, String sortExperssion, String sortDirection, Integer offset, Integer limit);

    UserDTO findById(Integer userId);

    void saveUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);
}
