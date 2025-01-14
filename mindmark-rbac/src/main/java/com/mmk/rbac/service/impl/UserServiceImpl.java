package com.mmk.rbac.service.impl;

import com.mmk.rbac.constant.AuthConstants;
import com.mmk.rbac.exception.CellphoneDuplicateException;
import com.mmk.rbac.exception.EmailDuplicateException;
import com.mmk.rbac.exception.UserNameDuplicateException;
import com.mmk.rbac.exception.UserNotExistsException;
import com.mmk.rbac.jpa.entity.*;
import com.mmk.rbac.jpa.repository.IUserRepository;
import com.mmk.rbac.jpa.repository.IUserRoleRepository;
import com.mmk.rbac.service.IUserService;
import com.mmk.rbac.shiro.util.MindMarkSecurityUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 大漠穷秋
 */
@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Override
    public int resetPwd(Integer userId){
        //TODO:生成随机密码

        //TODO:生成密文链接

        //TODO:发送验证邮件

        return 0;
    }

    @Override
    public UserEntity resetPwd(UserEntity userEntity) {
        //TODO:需要发送验证邮件
        UserEntity oldEntity=this.userRepository.findDistinctByUserId(userEntity.getUserId());
        oldEntity.setPassword(userEntity.getPassword());
        return this.userRepository.save(oldEntity);
    }

    @Override
    public UserEntity setUserStatus(UserEntity userEntity) {
        UserEntity oldEntity=this.userRepository.findDistinctByUserId(userEntity.getUserId());
        oldEntity.setPassword(userEntity.getPassword());
        oldEntity.setStatus(userEntity.getStatus());
        return this.userRepository.save(oldEntity);
    }

    //TODO:消息国际化
    @Override
    public UserEntity updateUser(UserEntity userEntity){
        if(!this.userRepository.existsById(userEntity.getUserId())){
            // TODO 等 BigMo 定义模块常量
            throw new UserNotExistsException();
        }
        userEntity=this.userRepository.save(userEntity);
        return userEntity;
    }

    /**
     * 创建或更新 user-role 之间的关联关系
     * @param userEntity
     * @return
     */
    @Override
    public UserEntity updateUserRoleRelation(UserEntity userEntity) {
        //先删除现有的关联关系记录
        this.userRoleRepository.deleteByUserId(userEntity.getUserId());
        //创建新的关联关系
        List<UserRoleEntity> userRoleEntityList=new ArrayList<>();
        Iterable<RoleEntity> roleEntities=userEntity.getRoleEntities();
        roleEntities.forEach(roleEntity -> {
            UserRoleEntity userRoleEntity=new UserRoleEntity();
            userRoleEntity.setUserId(userEntity.getUserId());
            userRoleEntity.setRoleId(roleEntity.getRoleId());
            userRoleEntityList.add(userRoleEntity);
        });
        this.userRoleRepository.saveAll(userRoleEntityList);
        return userEntity;
    }

    @Transactional
    @Override
    public UserEntity createUser(UserEntity userEntity){
        UserEntity temp=this.userRepository.findDistinctByUserName(userEntity.getUserName());

        if(!ObjectUtils.isEmpty(temp)){
            throw new UserNameDuplicateException();
        }

        if(!StringUtils.isEmpty(userEntity.getEmail())){
            temp=this.userRepository.findDistinctByEmail(userEntity.getEmail());
            if(!ObjectUtils.isEmpty(temp)){
                throw new EmailDuplicateException();
            }
        }

        if(!StringUtils.isEmpty(userEntity.getCellphone())){
            temp=this.userRepository.findDistinctTopByCellphone(userEntity.getCellphone());
            if(!ObjectUtils.isEmpty(temp)){
                throw new CellphoneDuplicateException();
            }
        }

        userEntity=this.userRepository.save(userEntity);
        return userEntity;
    }
    
    @Override
    public Page<UserEntity> getUserList(UserEntity userEntity, Pageable pageable) {
        return this.userRepository.findAll(new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                if(!StringUtils.isEmpty(userEntity.getUserName())){
                    predicates.add(criteriaBuilder.like(root.get("userName"),"%"+userEntity.getUserName()+"%"));
                }
                if(!StringUtils.isEmpty(userEntity.getCellphone())){
                    predicates.add(criteriaBuilder.like(root.get("cellphone"),"%"+userEntity.getCellphone()+"%"));
                }
                //FIXME:这里为什么要过滤 status?
                // if(!StringUtils.isEmpty(userEntity.getStatus())){
                //     predicates.add(criteriaBuilder.equal(root.get("status"),userEntity.getStatus()));
                // }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
    }

    @Override
    public Page<UserEntity> getAllocatedList(RoleEntity roleEntity,UserEntity userEntity, Pageable pageable) {
        return this.userRepository.findAll(new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("roleId"),roleEntity.getRoleId()));
                if(!StringUtils.isEmpty(userEntity.getUserName())){
                    predicates.add(criteriaBuilder.equal(root.get("userName"),userEntity.getUserName()));
                }
                if(!StringUtils.isEmpty(userEntity.getCellphone())){
                    predicates.add(criteriaBuilder.equal(root.get("cellphone"),userEntity.getCellphone()));
                }
                if(!StringUtils.isEmpty(userEntity.getEmail())){
                    predicates.add(criteriaBuilder.equal(root.get("email"),userEntity.getEmail()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
    }

    @Override
    public Page<UserEntity> getUnallocatedList(RoleEntity roleEntity, UserEntity userEntity, Pageable pageable) {
        return this.userRepository.findAll(new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                predicates.add(criteriaBuilder.notEqual(root.get("roleId"),roleEntity.getRoleId()));
                if(!StringUtils.isEmpty(userEntity.getUserName())){
                    predicates.add(criteriaBuilder.equal(root.get("userName"),userEntity.getUserName()));
                }
                if(!StringUtils.isEmpty(userEntity.getCellphone())){
                    predicates.add(criteriaBuilder.equal(root.get("cellphone"),userEntity.getCellphone()));
                }
                if(!StringUtils.isEmpty(userEntity.getEmail())){
                    predicates.add(criteriaBuilder.equal(root.get("email"),userEntity.getEmail()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
    }

    @Override
    public UserEntity getUserByUserName(String userName) {
        return this.userRepository.findDistinctByUserName(userName);
    }

    @Override
    public UserEntity getUserByCellphone(String cellphone) {
        return this.userRepository.findDistinctTopByCellphone(cellphone);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return this.userRepository.findDistinctByEmail(email);
    }

    @Override
    public UserEntity getUserByUserId(Integer userId) {
        return this.userRepository.findDistinctByUserId(userId);
    }

    @Override
    @Transactional
    public int deleteByUserId(Integer userId) {
        UserEntity userEntity= this.userRepository.findDistinctByUserId(userId);
        //特权用户状态位-1，不准删除
        if("-1".equals(userEntity.getStatus())){
            return 0;
        }
        return this.userRepository.deleteByUserId(userId);
    }

    @Override
    public int deleteByIds(Integer[] ids) {
        return this.userRepository.deleteByUserIdIn(ids);
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return this.userRepository.save(userEntity);
    }

    @Override
    public boolean isUserNameUnique(String userName) {
        logger.debug(userName);
        UserEntity userEntity=this.userRepository.findDistinctByUserName(userName);
        return userEntity==null?true:false;
    }

    @Override
    public boolean isPhoneUnique(String cellphone) {
        logger.debug(cellphone);
        UserEntity userEntity=this.userRepository.findDistinctTopByCellphone(cellphone);
        return userEntity==null?true:false;
    }

    @Override
    public boolean isEmailUnique(String email) {
        logger.debug(email);
        UserEntity userEntity=this.userRepository.findDistinctByEmail(email);
        return userEntity==null?true:false;
    }

    @Override
    public String getRoleList(Integer userId) {
        return null;
    }

    @Override
    public boolean matches(UserEntity userEntity, String newPassword) {
        return userEntity.getPassword().equals(encryptPassword(userEntity.getUserName(), newPassword, userEntity.getSalt()));
    }

    @Override
    public String encryptPassword(String username, String password, String salt) {
        return new Md5Hash(username + password + salt).toHex().toString();
    }

    @Override
    public UserEntity checkUser(String userName, String password){
        UserEntity userEntity = null;

        if(userName.matches(AuthConstants.EMAIL_PATTERN)){
            userEntity = this.getUserByEmail(userName);
        }else if(userName.matches(AuthConstants.MOBILE_PHONE_NUMBER_PATTERN)){
            userEntity=this.getUserByCellphone(userName);
        }else{
            userEntity=this.getUserByUserName(userName);
        }

        if (ObjectUtils.isEmpty(userEntity)
                ||!this.matches(userEntity,password)) {
            return null;
        }

        //FIXME:设置 session 属性之后，有时候不能同步到数据库
        Session session= MindMarkSecurityUtils.getSession();
        session.setAttribute("userId",userEntity.getUserId());
        session.setAttribute("userName",userEntity.getUserName());

        return userEntity;
    }

    @Override
    public Set<String> getPermStringsByUserId(Integer userId) {
        UserEntity userEntity=this.userRepository.findDistinctByUserId(userId);
        //FIXME:已经被禁用的角色需要排除掉
        List<RoleEntity> roleEntities=userEntity.getRoleEntities();

        Set<String> permStrs=new HashSet<>();
        for (RoleEntity roleEntity:roleEntities) {
            List<ApiPermissionEntity> apiEntities = roleEntity.getApiEntities();
            List<ComponentPermissionEntity> componentEntities=roleEntity.getComponentEntities();
            for(ApiPermissionEntity apiPermissionEntity :apiEntities){
                permStrs.add(apiPermissionEntity.getPermission());
            }
            for(ComponentPermissionEntity componentPermissionEntity :componentEntities){
                permStrs.add(componentPermissionEntity.getPermission());
            }
        }

        logger.debug(permStrs.toString());

        return permStrs;
    }
}
