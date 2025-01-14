package com.mmk.rbac.service;

import com.mmk.rbac.jpa.entity.RoleEntity;
import com.mmk.rbac.jpa.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 *
 * @author 大漠穷秋
 */
public interface IUserService {

    UserEntity createUser(UserEntity userEntity);

    int resetPwd(Integer userId);

    UserEntity resetPwd(UserEntity userEntity);

    UserEntity setUserStatus(UserEntity userEntity);

    UserEntity updateUser(UserEntity userEntity);

    UserEntity updateUserRoleRelation(UserEntity userEntity);

    Page<UserEntity> getUserList(UserEntity userEntity, Pageable pageable);

    Page<UserEntity> getAllocatedList(RoleEntity roleEntity, UserEntity userEntity, Pageable pageable);

    Page<UserEntity> getUnallocatedList(RoleEntity roleEntity, UserEntity userEntity, Pageable pageable);

    UserEntity getUserByUserName(String userName);

    UserEntity getUserByCellphone(String cellphone);

    UserEntity getUserByEmail(String email);

    UserEntity getUserByUserId(Integer userId);

    int deleteByUserId(Integer userId);

    int deleteByIds(Integer[] ids);

    UserEntity saveUser(UserEntity userEntity);


    boolean isUserNameUnique(String userName);

    boolean isPhoneUnique(String cellphone);

    boolean isEmailUnique(String email);

    String getRoleList(Integer userId);

    boolean matches(UserEntity userEntity, String newPassword);

    String encryptPassword(String username, String password, String salt);

    UserEntity checkUser(String userName, String password);

    /**
     * 根据 userId 获取此用户持有的所有权限字符串列表
     * @param userId
     * @return
     */
    Set<String> getPermStringsByUserId(Integer userId);
}
