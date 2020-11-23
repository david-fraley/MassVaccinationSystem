/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.server.controllers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.mirth.commons.encryption.Digester;
import com.mirth.connect.client.core.ControllerException;
import com.mirth.connect.model.Credentials;
import com.mirth.connect.model.LoginStatus;
import com.mirth.connect.model.LoginStatus.Status;
import com.mirth.connect.model.LoginStrike;
import com.mirth.connect.model.PasswordRequirements;
import com.mirth.connect.model.User;
import com.mirth.connect.server.ExtensionLoader;
import com.mirth.connect.server.mybatis.KeyValuePair;
import com.mirth.connect.server.util.DatabaseUtil;
import com.mirth.connect.server.util.LoginRequirementsChecker;
import com.mirth.connect.server.util.PasswordRequirementsChecker;
import com.mirth.connect.server.util.Pre22PasswordChecker;
import com.mirth.connect.server.util.SqlConfig;
import com.mirth.connect.server.util.StatementLock;

public class DefaultUserController extends UserController {
    public static final String VACUUM_LOCK_PERSON_STATEMENT_ID = "User.vacuumPersonTable";
    public static final String VACUUM_LOCK_PREFERENCES_STATEMENT_ID = "User.vacuumPersonPreferencesTable";

    private Logger logger = Logger.getLogger(this.getClass());
    private ExtensionController extensionController = null;

    private static UserController instance = null;

    private DefaultUserController() {

    }

    public static UserController create() {
        synchronized (DefaultUserController.class) {
            if (instance == null) {
                instance = ExtensionLoader.getInstance().getControllerInstance(UserController.class);

                if (instance == null) {
                    instance = new DefaultUserController();
                }
            }

            return instance;
        }
    }

    public void resetUserStatus() {
        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            SqlConfig.getInstance().getSqlSessionManager().update("User.resetUserStatus");
        } catch (PersistenceException e) {
            logger.error("Could not reset user status.");
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public List<User> getAllUsers() throws ControllerException {
        logger.debug("getting all users");

        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            return SqlConfig.getInstance().getReadOnlySqlSessionManager().selectList("User.getUser");
        } catch (PersistenceException e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public User getUser(Integer userId, String userName) throws ControllerException {
        logger.debug("getting user: " + userId);

        if (userId == null && userName == null) {
            throw new ControllerException("Error getting user: Both user ID and user name cannot be null.");
        }

        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            User user = new User();
            user.setId(userId);
            user.setUsername(userName);
            return SqlConfig.getInstance().getReadOnlySqlSessionManager().selectOne("User.getUser", user);
        } catch (PersistenceException e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public synchronized void updateUser(User user) throws ControllerException {
        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            User existingUserByName = getUser(null, user.getUsername());

            if (user.getId() == null) {
                if (existingUserByName != null) {
                    throw new ControllerException("Error adding user: username must be unique");
                }

                logger.debug("adding user: " + user);
                SqlConfig.getInstance().getSqlSessionManager().insert("User.insertUser", getUserMap(user));
            } else {
                if (existingUserByName != null && !user.getId().equals(existingUserByName.getId())) {
                    throw new ControllerException("Error updating user: username must be unique");
                }

                User existingUserById = getUser(user.getId(), null);
                if (existingUserById == null) {
                    throw new ControllerException("Error updating user: No user found with ID " + user.getId());
                }
                String currentUsername = existingUserById.getUsername();

                logger.debug("updating user: " + user);
                SqlConfig.getInstance().getSqlSessionManager().update("User.updateUser", getUserMap(user));

                // Notify the authorization controller if the username changed
                if (!StringUtils.equals(currentUsername, user.getUsername())) {
                    ControllerFactory.getFactory().createAuthorizationController().usernameChanged(currentUsername, user.getUsername());
                }
            }
        } catch (PersistenceException e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public List<String> checkOrUpdateUserPassword(Integer userId, String plainPassword) throws ControllerException {
        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            Digester digester = ControllerFactory.getFactory().createConfigurationController().getDigester();
            PasswordRequirements passwordRequirements = ControllerFactory.getFactory().createConfigurationController().getPasswordRequirements();
            List<String> responses = PasswordRequirementsChecker.getInstance().doesPasswordMeetRequirements(userId, plainPassword, passwordRequirements);
            if (responses != null) {
                return responses;
            }

            /*
             * If no userId was passed in, stop here and don't try to add the password.
             */
            if (userId == null) {
                return null;
            }

            logger.debug("updating password for user id: " + userId);

            Calendar pruneDate = PasswordRequirementsChecker.getInstance().getLastExpirationDate(passwordRequirements);

            // If a null prune date is returned, do not prune
            if (pruneDate != null) {
                Map<String, Object> userDateMap = new HashMap<String, Object>();
                userDateMap.put("id", userId);
                userDateMap.put("pruneDate", pruneDate);

                try {
                    SqlConfig.getInstance().getSqlSessionManager().delete("User.prunePasswords", userDateMap);
                } catch (Exception e) {
                    // Don't abort changing the password if pruning fails.
                    logger.error("There was an error pruning passwords for user id: " + userId, e);
                }
            }

            Map<String, Object> userPasswordMap = new HashMap<String, Object>();
            userPasswordMap.put("id", userId);
            userPasswordMap.put("password", digester.digest(plainPassword));
            userPasswordMap.put("passwordDate", Calendar.getInstance());
            SqlConfig.getInstance().getSqlSessionManager().insert("User.updateUserPassword", userPasswordMap);
            SqlConfig.getInstance().getSqlSessionManager().update("User.clearGracePeriod", userId);

            return null;
        } catch (PersistenceException e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public synchronized void removeUser(Integer userId, Integer currentUserId) throws ControllerException {
        logger.debug("removing user: " + userId);

        if (userId == null) {
            throw new ControllerException("Error removing user: User Id cannot be null");
        }

        if (userId.equals(currentUserId)) {
            throw new ControllerException("Error removing user: You cannot remove yourself");
        }

        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).writeLock();
        try {
            User user = new User();
            user.setId(userId);
            SqlConfig.getInstance().getSqlSessionManager().delete("User.deleteUser", user);

            if (DatabaseUtil.statementExists("User.vacuumPersonTable")) {
                vacuumPersonTable();
            }
        } catch (PersistenceException e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).writeUnlock();
        }
    }

    /**
     * When calling this method, a StatementLock writeLock should surround it
     */
    public void vacuumPersonTable() {
        SqlSession session = null;
        try {
            session = SqlConfig.getInstance().getSqlSessionManager().openSession(false);
            if (DatabaseUtil.statementExists("User.lockPersonTable")) {
                session.update("User.lockPersonTable");
            }
            session.update("User.vacuumPersonTable");
            session.commit();
        } catch (Exception e) {
            logger.error("Could not compress Person table", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * When calling this method, a StatementLock writeLock should surround it
     */
    public void vacuumPersonPreferencesTable() {
        SqlSession session = null;
        try {
            session = SqlConfig.getInstance().getSqlSessionManager().openSession(false);
            if (DatabaseUtil.statementExists("User.lockPersonPreferencesTable")) {
                session.update("User.lockPersonPreferencesTable");
            }
            session.update("User.vacuumPersonPreferencesTable");
            session.commit();
        } catch (Exception e) {
            logger.error("Could not compress Person Preference table", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public LoginStatus authorizeUser(String username, String plainPassword) throws ControllerException {
        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            // Invoke and return from the Authorization Plugin if one exists
            if (extensionController == null) {
                extensionController = ControllerFactory.getFactory().createExtensionController();
            }

            if (extensionController.getAuthorizationPlugin() != null) {
                LoginStatus loginStatus = extensionController.getAuthorizationPlugin().authorizeUser(username, plainPassword);

                /*
                 * A null return value indicates that the authorization plugin is disabled or is
                 * otherwise delegating control back to the UserController to perform
                 * authentication.
                 */
                if (loginStatus != null) {
                    return handleSecondaryAuthentication(username, loginStatus, null);
                }
            }

            boolean authorized = false;
            Credentials credentials = null;
            LoginRequirementsChecker loginRequirementsChecker = null;

            // Retrieve the matching User
            User validUser = getUser(null, username);

            if (validUser != null) {
                Digester digester = ControllerFactory.getFactory().createConfigurationController().getDigester();
                loginRequirementsChecker = new LoginRequirementsChecker(validUser);
                if (loginRequirementsChecker.isUserLockedOut()) {
                    return new LoginStatus(LoginStatus.Status.FAIL_LOCKED_OUT, "User account \"" + username + "\" has been locked. You may attempt to login again in " + loginRequirementsChecker.getPrintableStrikeTimeRemaining() + ".");
                }

                loginRequirementsChecker.resetExpiredStrikes();

                // Validate the user credentials
                credentials = (Credentials) SqlConfig.getInstance().getReadOnlySqlSessionManager().selectOne("User.getLatestUserCredentials", validUser.getId());

                if (credentials != null) {
                    if (Pre22PasswordChecker.isPre22Hash(credentials.getPassword())) {
                        if (Pre22PasswordChecker.checkPassword(plainPassword, credentials.getPassword())) {
                            checkOrUpdateUserPassword(validUser.getId(), plainPassword);
                            authorized = true;
                        }
                    } else {
                        authorized = digester.matches(plainPassword, credentials.getPassword());
                    }
                }
            }

            PasswordRequirements passwordRequirements = ControllerFactory.getFactory().createConfigurationController().getPasswordRequirements();
            LoginStatus loginStatus = null;

            if (authorized) {
                // If password expiration is enabled, do checks now
                if (passwordRequirements.getExpiration() > 0) {
                    long passwordTime = credentials.getPasswordDate().getTimeInMillis();
                    long currentTime = System.currentTimeMillis();

                    // If the password is expired, do grace period checks
                    if (loginRequirementsChecker.isPasswordExpired(passwordTime, currentTime)) {
                        // Let 0 be infinite grace period, -1 be no grace period
                        if (passwordRequirements.getGracePeriod() == 0) {
                            loginStatus = new LoginStatus(LoginStatus.Status.SUCCESS_GRACE_PERIOD, "Your password has expired. Please change your password now.");
                        } else if (passwordRequirements.getGracePeriod() > 0) {
                            // If there has never been a grace time, start it now
                            long gracePeriodStartTime;
                            if (validUser.getGracePeriodStart() == null) {
                                gracePeriodStartTime = currentTime;

                                Map<String, Object> gracePeriodMap = new HashMap<String, Object>();
                                gracePeriodMap.put("id", validUser.getId());
                                gracePeriodMap.put("gracePeriodStart", Calendar.getInstance());

                                SqlConfig.getInstance().getSqlSessionManager().update("User.startGracePeriod", gracePeriodMap);
                            } else {
                                gracePeriodStartTime = validUser.getGracePeriodStart().getTimeInMillis();
                            }

                            long graceTimeRemaining = loginRequirementsChecker.getGraceTimeRemaining(gracePeriodStartTime, currentTime);
                            if (graceTimeRemaining > 0) {
                                loginStatus = new LoginStatus(LoginStatus.Status.SUCCESS_GRACE_PERIOD, "Your password has expired. You are required to change your password in the next " + loginRequirementsChecker.getPrintableGraceTimeRemaining(graceTimeRemaining) + ".");
                            }
                        }

                        // If there is no grace period or it has passed, FAIL_EXPIRED
                        if (loginStatus == null) {
                            loginStatus = new LoginStatus(LoginStatus.Status.FAIL_EXPIRED, "Your password has expired. Please contact an administrator to have your password reset.");
                        }

                        /*
                         * Reset the user's grace period if it isn't being used but one was
                         * previously set. This should only happen if a user is in a grace period
                         * before grace periods are disabled.
                         */
                        if ((passwordRequirements.getGracePeriod() <= 0) && (validUser.getGracePeriodStart() != null)) {
                            SqlConfig.getInstance().getSqlSessionManager().update("User.clearGracePeriod", validUser.getId());
                        }
                    }
                }
                // End of password expiration and grace period checks

                // If nothing failed (loginStatus != null), set SUCCESS now
                if (loginStatus == null) {
                    loginStatus = new LoginStatus(LoginStatus.Status.SUCCESS, "");

                    // Clear the user's grace period if one exists
                    if (validUser.getGracePeriodStart() != null) {
                        SqlConfig.getInstance().getSqlSessionManager().update("User.clearGracePeriod", validUser.getId());
                    }
                }
            } else {
                LoginStatus.Status status = LoginStatus.Status.FAIL;
                String failMessage = "Incorrect username or password.";

                if (loginRequirementsChecker != null) {
                    loginRequirementsChecker.incrementStrikes();

                    if (loginRequirementsChecker.isLockoutEnabled()) {
                        if (loginRequirementsChecker.isUserLockedOut()) {
                            status = LoginStatus.Status.FAIL_LOCKED_OUT;
                            failMessage += " User account \"" + username + "\" has been locked. You may attempt to login again in " + loginRequirementsChecker.getPrintableStrikeTimeRemaining() + ".";
                        } else {
                            failMessage += " " + loginRequirementsChecker.getAttemptsRemaining() + " login attempt(s) remaining for \"" + username + "\" until the account is locked for " + loginRequirementsChecker.getPrintableLockoutPeriod() + ".";
                        }
                    }
                }

                loginStatus = new LoginStatus(status, failMessage);
            }

            return handleSecondaryAuthentication(username, loginStatus, loginRequirementsChecker);
        } catch (Exception e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public boolean checkPassword(String plainPassword, String encryptedPassword) {
        Digester digester = ControllerFactory.getFactory().createConfigurationController().getDigester();
        return digester.matches(plainPassword, encryptedPassword);
    }

    public void loginUser(User user) throws ControllerException {
        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", user.getId());
            params.put("lastLogin", Calendar.getInstance());

            SqlConfig.getInstance().getSqlSessionManager().update("User.loginUser", params);
        } catch (Exception e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public void logoutUser(User user) throws ControllerException {
        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            SqlConfig.getInstance().getSqlSessionManager().update("User.logoutUser", user.getId());
        } catch (Exception e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    public boolean isUserLoggedIn(Integer userId) throws ControllerException {
        StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readLock();
        try {
            return (Boolean) SqlConfig.getInstance().getReadOnlySqlSessionManager().selectOne("User.isUserLoggedIn", userId);
        } catch (Exception e) {
            throw new ControllerException(e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PERSON_STATEMENT_ID).readUnlock();
        }
    }

    private Map<String, Object> getUserMap(User user) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();

        if (user.getId() != null) {
            parameterMap.put("id", user.getId());
        }

        parameterMap.put("username", user.getUsername());
        parameterMap.put("firstName", user.getFirstName());
        parameterMap.put("lastName", user.getLastName());
        parameterMap.put("organization", user.getOrganization());
        parameterMap.put("industry", user.getIndustry());
        parameterMap.put("email", user.getEmail());
        parameterMap.put("phoneNumber", user.getPhoneNumber());
        parameterMap.put("description", user.getDescription());
        return parameterMap;
    }

    @Override
    public List<Credentials> getUserCredentials(Integer userId) throws ControllerException {
        try {
            return SqlConfig.getInstance().getReadOnlySqlSessionManager().selectList("User.getUserCredentials", userId);
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public LoginStrike incrementStrikes(Integer userId) throws ControllerException {
        try {
            SqlConfig.getInstance().getSqlSessionManager().update("User.incrementStrikes", userId);

            User updatedUser = getUser(userId, null);
            if (updatedUser != null) {
                return new LoginStrike(updatedUser.getStrikeCount() != null ? updatedUser.getStrikeCount() : 0, updatedUser.getLastStrikeTime());
            }
            return null;
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public LoginStrike resetStrikes(Integer userId) throws ControllerException {
        try {
            SqlConfig.getInstance().getSqlSessionManager().update("User.resetStrikes", userId);

            User updatedUser = getUser(userId, null);
            if (updatedUser != null) {
                return new LoginStrike(updatedUser.getStrikeCount() != null ? updatedUser.getStrikeCount() : 0, updatedUser.getLastStrikeTime());
            }
            return null;
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public void setUserPreferences(Integer userId, Properties properties) throws ControllerException {
        for (String property : properties.stringPropertyNames()) {
            setUserPreference(userId, property, properties.getProperty(property));
        }
    }

    @Override
    public void setUserPreference(Integer userId, String name, String value) {
        logger.debug("storing preference: user id=" + userId + ", name=" + name);

        StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).writeLock();
        try {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("person_id", userId);
            parameterMap.put("name", name);
            parameterMap.put("value", value);

            if (getUserPreference(userId, name) == null) {
                SqlConfig.getInstance().getSqlSessionManager().insert("User.insertPreference", parameterMap);
            } else {
                SqlConfig.getInstance().getSqlSessionManager().insert("User.updatePreference", parameterMap);
            }

            if (DatabaseUtil.statementExists("User.vacuumPersonPreferencesTable")) {
                vacuumPersonPreferencesTable();
            }
        } catch (Exception e) {
            logger.error("Could not store preference: user id=" + userId + ", name=" + name, e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).writeUnlock();
        }
    }

    @Override
    public Properties getUserPreferences(Integer userId, Set<String> names) {
        logger.debug("retrieving preferences: user id=" + userId);
        Properties properties = new Properties();

        StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readLock();
        try {
            List<KeyValuePair> result = SqlConfig.getInstance().getReadOnlySqlSessionManager().selectList("User.selectPreferencesForUser", userId);

            for (KeyValuePair pair : result) {
                if (CollectionUtils.isEmpty(names) || names.contains(pair.getKey())) {
                    properties.setProperty(pair.getKey(), StringUtils.defaultString(pair.getValue()));
                }
            }
        } catch (Exception e) {
            logger.error("Could not retrieve preferences: user id=" + userId, e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readUnlock();
        }

        return properties;
    }

    @Override
    public String getUserPreference(Integer userId, String name) {
        logger.debug("retrieving preference: user id=" + userId + ", name=" + name);

        StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readLock();
        try {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("person_id", userId);
            parameterMap.put("name", name);
            return (String) SqlConfig.getInstance().getReadOnlySqlSessionManager().selectOne("User.selectPreference", parameterMap);
        } catch (Exception e) {
            logger.warn("Could not retrieve preference: user id=" + userId + ", name=" + name, e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readUnlock();
        }

        return null;
    }

    public void removePreferencesForUser(int id) {
        logger.debug("deleting all preferences: user id=" + id);

        StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readLock();
        try {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("person_id", id);
            SqlConfig.getInstance().getSqlSessionManager().delete("User.deletePreference", parameterMap);
        } catch (Exception e) {
            logger.error("Could not delete preferences: user id=" + id);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readUnlock();
        }
    }

    @Override
    public void removePreference(int id, String name) {
        logger.debug("deleting preference: user id=" + id + ", name=" + name);

        StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readLock();
        try {
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("category", id);
            parameterMap.put("name", name);
            SqlConfig.getInstance().getSqlSessionManager().delete("User.deletePreference", parameterMap);
        } catch (Exception e) {
            logger.error("Could not delete preference: user id=" + id + ", name=" + name, e);
        } finally {
            StatementLock.getInstance(VACUUM_LOCK_PREFERENCES_STATEMENT_ID).readUnlock();
        }
    }

    private LoginStatus handleSecondaryAuthentication(String username, LoginStatus loginStatus, LoginRequirementsChecker loginRequirementsChecker) {
        if (loginStatus != null && extensionController.getMultiFactorAuthenticationPlugin() != null && (loginStatus.getStatus() == Status.SUCCESS || loginStatus.getStatus() == Status.SUCCESS_GRACE_PERIOD)) {
            loginStatus = extensionController.getMultiFactorAuthenticationPlugin().authenticate(username, loginStatus);
        }

        // Only reset strikes if the final status is successful 
        if (loginStatus.getStatus() == Status.SUCCESS || loginStatus.getStatus() == Status.SUCCESS_GRACE_PERIOD) {
            if (loginRequirementsChecker == null) {
                try {
                    loginRequirementsChecker = new LoginRequirementsChecker(getUser(null, username));
                } catch (ControllerException e) {
                    logger.warn("Unable to reset strikes for user \"" + username + "\": Could not find user.", e);
                }
            }

            if (loginRequirementsChecker != null) {
                loginRequirementsChecker.resetStrikes();
            }
        }

        return loginStatus;
    }
}
