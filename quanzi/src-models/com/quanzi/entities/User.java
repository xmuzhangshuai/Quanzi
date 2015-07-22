package com.quanzi.entities;

import com.quanzi.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.quanzi.dao.CityDao;
import com.quanzi.dao.ProvinceDao;
import com.quanzi.dao.SchoolDao;
import com.quanzi.dao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table USER.
 */
public class User {

    private Long user_id;
    private String nickname;
    private String gender;
    private String tel;
    private String email;
    private String large_avatar;
    private String small_avatar;
    private String introduce;
    private String identity;
    private String love_state;
    private Integer age;
    private java.util.Date birthday;
    private long schoolID;
    private long cityID;
    private long provinceID;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UserDao myDao;

    private School school;
    private Long school__resolvedKey;

    private City city;
    private Long city__resolvedKey;

    private Province province;
    private Long province__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public User() {
    }

    public User(Long user_id) {
        this.user_id = user_id;
    }

    public User(Long user_id, String nickname, String gender, String tel, String email, String large_avatar, String small_avatar, String introduce, String identity, String love_state, Integer age, java.util.Date birthday, long schoolID, long cityID, long provinceID) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.gender = gender;
        this.tel = tel;
        this.email = email;
        this.large_avatar = large_avatar;
        this.small_avatar = small_avatar;
        this.introduce = introduce;
        this.identity = identity;
        this.love_state = love_state;
        this.age = age;
        this.birthday = birthday;
        this.schoolID = schoolID;
        this.cityID = cityID;
        this.provinceID = provinceID;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLarge_avatar() {
        return large_avatar;
    }

    public void setLarge_avatar(String large_avatar) {
        this.large_avatar = large_avatar;
    }

    public String getSmall_avatar() {
        return small_avatar;
    }

    public void setSmall_avatar(String small_avatar) {
        this.small_avatar = small_avatar;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getLove_state() {
        return love_state;
    }

    public void setLove_state(String love_state) {
        this.love_state = love_state;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public java.util.Date getBirthday() {
        return birthday;
    }

    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }

    public long getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(long schoolID) {
        this.schoolID = schoolID;
    }

    public long getCityID() {
        return cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    public long getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(long provinceID) {
        this.provinceID = provinceID;
    }

    /** To-one relationship, resolved on first access. */
    public School getSchool() {
        long __key = this.schoolID;
        if (school__resolvedKey == null || !school__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SchoolDao targetDao = daoSession.getSchoolDao();
            School schoolNew = targetDao.load(__key);
            synchronized (this) {
                school = schoolNew;
            	school__resolvedKey = __key;
            }
        }
        return school;
    }

    public void setSchool(School school) {
        if (school == null) {
            throw new DaoException("To-one property 'schoolID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.school = school;
            schoolID = school.getId();
            school__resolvedKey = schoolID;
        }
    }

    /** To-one relationship, resolved on first access. */
    public City getCity() {
        long __key = this.cityID;
        if (city__resolvedKey == null || !city__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CityDao targetDao = daoSession.getCityDao();
            City cityNew = targetDao.load(__key);
            synchronized (this) {
                city = cityNew;
            	city__resolvedKey = __key;
            }
        }
        return city;
    }

    public void setCity(City city) {
        if (city == null) {
            throw new DaoException("To-one property 'cityID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.city = city;
            cityID = city.getId();
            city__resolvedKey = cityID;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Province getProvince() {
        long __key = this.provinceID;
        if (province__resolvedKey == null || !province__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProvinceDao targetDao = daoSession.getProvinceDao();
            Province provinceNew = targetDao.load(__key);
            synchronized (this) {
                province = provinceNew;
            	province__resolvedKey = __key;
            }
        }
        return province;
    }

    public void setProvince(Province province) {
        if (province == null) {
            throw new DaoException("To-one property 'provinceID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.province = province;
            provinceID = province.getId();
            province__resolvedKey = provinceID;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
