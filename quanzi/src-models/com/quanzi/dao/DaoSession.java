package com.quanzi.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.quanzi.entities.Province;
import com.quanzi.entities.City;
import com.quanzi.entities.Cometent;
import com.quanzi.entities.School;
import com.quanzi.entities.UserState;
import com.quanzi.entities.Vocation;
import com.quanzi.entities.TodayRecommend;
import com.quanzi.entities.Conversation;
import com.quanzi.entities.Flipper;
import com.quanzi.entities.User;

import com.quanzi.dao.ProvinceDao;
import com.quanzi.dao.CityDao;
import com.quanzi.dao.CometentDao;
import com.quanzi.dao.SchoolDao;
import com.quanzi.dao.UserStateDao;
import com.quanzi.dao.VocationDao;
import com.quanzi.dao.TodayRecommendDao;
import com.quanzi.dao.ConversationDao;
import com.quanzi.dao.FlipperDao;
import com.quanzi.dao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig provinceDaoConfig;
    private final DaoConfig cityDaoConfig;
    private final DaoConfig cometentDaoConfig;
    private final DaoConfig schoolDaoConfig;
    private final DaoConfig userStateDaoConfig;
    private final DaoConfig vocationDaoConfig;
    private final DaoConfig todayRecommendDaoConfig;
    private final DaoConfig conversationDaoConfig;
    private final DaoConfig flipperDaoConfig;
    private final DaoConfig userDaoConfig;

    private final ProvinceDao provinceDao;
    private final CityDao cityDao;
    private final CometentDao cometentDao;
    private final SchoolDao schoolDao;
    private final UserStateDao userStateDao;
    private final VocationDao vocationDao;
    private final TodayRecommendDao todayRecommendDao;
    private final ConversationDao conversationDao;
    private final FlipperDao flipperDao;
    private final UserDao userDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        provinceDaoConfig = daoConfigMap.get(ProvinceDao.class).clone();
        provinceDaoConfig.initIdentityScope(type);

        cityDaoConfig = daoConfigMap.get(CityDao.class).clone();
        cityDaoConfig.initIdentityScope(type);

        cometentDaoConfig = daoConfigMap.get(CometentDao.class).clone();
        cometentDaoConfig.initIdentityScope(type);

        schoolDaoConfig = daoConfigMap.get(SchoolDao.class).clone();
        schoolDaoConfig.initIdentityScope(type);

        userStateDaoConfig = daoConfigMap.get(UserStateDao.class).clone();
        userStateDaoConfig.initIdentityScope(type);

        vocationDaoConfig = daoConfigMap.get(VocationDao.class).clone();
        vocationDaoConfig.initIdentityScope(type);

        todayRecommendDaoConfig = daoConfigMap.get(TodayRecommendDao.class).clone();
        todayRecommendDaoConfig.initIdentityScope(type);

        conversationDaoConfig = daoConfigMap.get(ConversationDao.class).clone();
        conversationDaoConfig.initIdentityScope(type);

        flipperDaoConfig = daoConfigMap.get(FlipperDao.class).clone();
        flipperDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        provinceDao = new ProvinceDao(provinceDaoConfig, this);
        cityDao = new CityDao(cityDaoConfig, this);
        cometentDao = new CometentDao(cometentDaoConfig, this);
        schoolDao = new SchoolDao(schoolDaoConfig, this);
        userStateDao = new UserStateDao(userStateDaoConfig, this);
        vocationDao = new VocationDao(vocationDaoConfig, this);
        todayRecommendDao = new TodayRecommendDao(todayRecommendDaoConfig, this);
        conversationDao = new ConversationDao(conversationDaoConfig, this);
        flipperDao = new FlipperDao(flipperDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Province.class, provinceDao);
        registerDao(City.class, cityDao);
        registerDao(Cometent.class, cometentDao);
        registerDao(School.class, schoolDao);
        registerDao(UserState.class, userStateDao);
        registerDao(Vocation.class, vocationDao);
        registerDao(TodayRecommend.class, todayRecommendDao);
        registerDao(Conversation.class, conversationDao);
        registerDao(Flipper.class, flipperDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        provinceDaoConfig.getIdentityScope().clear();
        cityDaoConfig.getIdentityScope().clear();
        cometentDaoConfig.getIdentityScope().clear();
        schoolDaoConfig.getIdentityScope().clear();
        userStateDaoConfig.getIdentityScope().clear();
        vocationDaoConfig.getIdentityScope().clear();
        todayRecommendDaoConfig.getIdentityScope().clear();
        conversationDaoConfig.getIdentityScope().clear();
        flipperDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
    }

    public ProvinceDao getProvinceDao() {
        return provinceDao;
    }

    public CityDao getCityDao() {
        return cityDao;
    }

    public CometentDao getCometentDao() {
        return cometentDao;
    }

    public SchoolDao getSchoolDao() {
        return schoolDao;
    }

    public UserStateDao getUserStateDao() {
        return userStateDao;
    }

    public VocationDao getVocationDao() {
        return vocationDao;
    }

    public TodayRecommendDao getTodayRecommendDao() {
        return todayRecommendDao;
    }

    public ConversationDao getConversationDao() {
        return conversationDao;
    }

    public FlipperDao getFlipperDao() {
        return flipperDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
