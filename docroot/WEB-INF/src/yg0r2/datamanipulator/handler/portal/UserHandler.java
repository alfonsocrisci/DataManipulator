/**
 * Copyright (c) 2014-present Yg0R2. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package yg0r2.datamanipulator.handler.portal;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import yg0r2.datamanipulator.annotation.Handler;
import yg0r2.datamanipulator.annotation.HandlerType;
import yg0r2.datamanipulator.context.RequestContext;
import yg0r2.datamanipulator.displayfield.DisplayFields;
import yg0r2.datamanipulator.displayfield.FieldKeys;
import yg0r2.datamanipulator.handler.BaseHandler;
/**
 * @author Yg0R2
 */
@Handler(type = HandlerType.PORTAL, displayName = "User Handler")
public class UserHandler extends BaseHandler {

	public UserHandler() throws Exception {
		super("User", "user");
	}

	@Override
	public DisplayFields getDisplayFields(long groupId) throws Exception {
		DisplayFields displayFields = new DisplayFields();

		displayFields.addLabel(getDisplayFieldName());

		displayFields.addOrganizationMultiSelect(
			FieldKeys.MULTI_SELECT_ORGANIZATION_LIST);

		displayFields.addRoleMultiSelect(FieldKeys.MULTI_SELECT_ROLE_LIST);

		displayFields.addSiteMultiSelect(FieldKeys.MULTI_SELECT_SITE_LIST);

		displayFields.addUserGroupMultiSelect(
			FieldKeys.MULTI_SELECT_USERGROUP_LIST);

		displayFields.addCount(
			getDisplayFieldName(FieldKeys.INPUT_COUNT), true);

		return displayFields;
	}

	@Override
	protected Class<?>[] getAddArgClazzs() {
		if (buildNumber < 7000) {
			return new Class<?>[] {
				Long.TYPE, Long.TYPE, Boolean.TYPE, String.class,
				String.class, Boolean.TYPE, String.class, String.class,
				Long.TYPE, String.class, Locale.class, String.class,
				String.class, String.class, Integer.TYPE, Integer.TYPE,
				Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE,
				String.class, Array.newInstance(Long.TYPE, 0).getClass(),
				Array.newInstance(Long.TYPE, 0).getClass(),
				Array.newInstance(Long.TYPE, 0).getClass(),
				Array.newInstance(Long.TYPE, 0).getClass(), Boolean.TYPE,
				ServiceContext.class
			};
		}

		return new Class<?>[] {
			Long.TYPE, Long.TYPE, Boolean.TYPE, String.class,
			String.class, Boolean.TYPE, String.class, String.class,
			Long.TYPE, String.class, Locale.class, String.class,
			String.class, String.class, Long.TYPE, Long.TYPE,
			Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE,
			String.class, Array.newInstance(Long.TYPE, 0).getClass(),
			Array.newInstance(Long.TYPE, 0).getClass(),
			Array.newInstance(Long.TYPE, 0).getClass(),
			Array.newInstance(Long.TYPE, 0).getClass(), Boolean.TYPE,
			ServiceContext.class
		};
	}

	@Override
	protected String[] getAddArgNames() {
		return new String[] {
			"creatorUserId", "companyId", "autoPassword", "password1",
			"password2", "autoScreenName", "screenName", "emailAddress",
			"facebookId", "openId", "locale", "firstName", "middleName",
			"lastName", "prefixId", "suffixId", "male", "birthdayMonth",
			"birthdayDay", "birthdayYear", "jobTitle", "groupIds",
			"organizationIds", "roleIds", "userGroupIds", "sendEmail",
			"serviceContext"
		};
	}

	@Override
	protected Class<?> getAddClass() throws ClassNotFoundException {
		return UserLocalServiceUtil.class;
	}

	@Override
	protected Map<String, Object> getAddEntrySpecifiedArgs(
		RequestContext requestContext) throws Exception {

		StringBuilder postSB = new StringBuilder(3);
		postSB.append(requestContext.getString("entryCount"));
		postSB.append(StringPool.DASH);
		postSB.append(requestContext.getString("rndString"));

		String postString = postSB.toString();

		String screenName = "test-" + postString;

		Map<String, Object> args = new HashMap<String, Object>(16);

		args.put("creatorUserId", requestContext.getUserId());
		args.put("password1", "test");
		args.put("password2", "test");
		args.put("screenName", screenName);
		args.put("emailAddress", screenName + "@liferaz.com");
		args.put("firstName", "Test" + postString);
		args.put("lastName", "Test" + postString);
		args.put("birthdayMonth", 10);
		args.put("birthdayDay", 16);
		args.put("birthdayYear", 1984);
		args.put("male", true);
		args.put("jobTitle", "QA Engineer");

		args.put("groupIds", requestContext.getGroupIds());
		args.put("organizationIds", requestContext.getOrganizationIds());
		args.put("roleIds", requestContext.getRoleIds());
		args.put("userGroupIds", requestContext.getUserGroupIds());

		return args;
	}

	@Override
	protected String getAddMethodName() {
		return "addUser";
	}

	@Override
	protected List<String> getChildHandlerNames() {
		return new ArrayList<String>(0);
	}

	@Override
	protected String getClassPKName() {
		return "userId";
	}

	@Override
	protected String getParentClassPKName() {
		return null;
	}

	@Override
	protected Class<?>[] getUpdateArgClazzs() {
		if (buildNumber < 7000) {
			return new Class<?>[] {
				Long.TYPE, String.class, String.class, String.class,
				Boolean.TYPE, String.class, String.class, String.class,
				String.class, Long.TYPE, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, Integer.TYPE, Integer.TYPE,
				Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class, String.class,
				String.class, String.class, String.class,
				Array.newInstance(Long.TYPE, 0).getClass(),
				Array.newInstance(Long.TYPE, 0).getClass(),
				Array.newInstance(Long.TYPE, 0).getClass(), List.class,
				Array.newInstance(Long.TYPE, 0).getClass(), ServiceContext.class
			};
		}

		return new Class<?>[] {
			Long.TYPE, String.class, String.class, String.class, Boolean.TYPE,
			String.class, String.class, String.class, String.class, Long.TYPE,
			String.class, Boolean.TYPE,
			Array.newInstance(Byte.TYPE, 0).getClass(), String.class,
			String.class, String.class, String.class, String.class,
			String.class, String.class, Long.TYPE, Long.TYPE, Boolean.TYPE,
			Integer.TYPE, Integer.TYPE, Integer.TYPE, String.class,
			String.class, String.class, String.class, String.class,
			String.class, String.class, String.class, String.class,
			String.class, String.class,
			Array.newInstance(Long.TYPE, 0).getClass(),
			Array.newInstance(Long.TYPE, 0).getClass(),
			Array.newInstance(Long.TYPE, 0).getClass(), List.class,
			Array.newInstance(Long.TYPE, 0).getClass(), ServiceContext.class
		};
	}

	@Override
	protected String[] getUpdateArgNames() {
		if (buildNumber < 7000) {
			return new String[] {
				"userId", "oldPassword", "newPassword1", "newPassword2",
				"passwordReset", "reminderQueryQuestion", "reminderQueryAnswer",
				"screenName", "emailAddress", "facebookId", "openId",
				"languageId", "timeZoneId", "greeting", "comments", "firstName",
				"middleName", "lastName", "prefixId", "suffixId", "male",
				"birthdayMonth", "birthdayDay", "birthdayYear", "smsSn",
				"aimSn", "facebookSn", "icqSn", "jabberSn", "msnSn",
				"mySpaceSn", "skypeSn", "twitterSn", "ymSn", "jobTitle",
				"groupIds", "organizationIds", "roleIds", "userGroupRoles",
				"userGroupIds", "serviceContext"
			};
		}

		return new String[] {
			"userId", "oldPassword", "newPassword1", "newPassword2",
			"passwordReset", "reminderQueryQuestion", "reminderQueryAnswer",
			"screenName", "emailAddress", "facebookId", "openId", "portrait",
			"portraitBytes", "languageId", "timeZoneId", "greeting", "comments",
			"firstName", "middleName", "lastName", "prefixId", "suffixId",
			"male", "birthdayMonth", "birthdayDay", "birthdayYear", "smsSn",
			"aimSn", "facebookSn", "icqSn", "jabberSn", "msnSn", "mySpaceSn",
			"skypeSn", "twitterSn", "ymSn", "jobTitle", "groupIds",
			"organizationIds", "roleIds", "userGroupRoles", "userGroupIds",
			"serviceContext"
		};
	}

	@Override
	protected Class<?> getUpdateClass() throws ClassNotFoundException {
		return UserLocalServiceUtil.class;
	}

	@Override
	protected Map<String, Object> getUpdateEntrySpecifiedArgs(
		Object entry, RequestContext requestContext) throws Exception {

		return new HashMap<String, Object>(0);
	}

	@Override
	protected String getUpdateMethodName() {
		return "updateUser";
	}

}