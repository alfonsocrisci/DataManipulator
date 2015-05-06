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

package yg0r2.datamanipulator.handler.content;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import yg0r2.datamanipulator.annotation.Handler;
import yg0r2.datamanipulator.annotation.HandlerType;
import yg0r2.datamanipulator.context.RequestContext;
import yg0r2.datamanipulator.displayfield.DisplayFields;
import yg0r2.datamanipulator.displayfield.FieldKeys;
import yg0r2.datamanipulator.handler.BaseHandler;

/**
 * @author Yg0R2
 */
@Handler(type = HandlerType.CONTENT, displayName = "Layout Handler")
public class LayoutHandler extends BaseHandler {

	public static final String LAYOUT_TEMPLATE_SELECT_LIST =
		"layout-template-select-list";
	public static final String LAYOUT_TYPE_SELECT_LIST =
		"layout-type-select-list";

	public LayoutHandler() {
		super("Layout", "layout");
	}

	@Override
	public DisplayFields getDisplayFields(long groupId) throws SystemException {
		DisplayFields displayFields = new DisplayFields();

		displayFields.addUserMultiSelect(FieldKeys.MULTI_SELECT_USER_LIST);
		displayFields.addInfo(
			getDisplayFieldName(FieldKeys.MULTI_SELECT_USER_LIST));

		displayFields.addSeparator("");

		displayFields.addSelectGroupedList(
			getDisplayFieldName(FieldKeys.SELECT_PARENT_LIST),
			_getLayoutNameIdPairs(groupId));

		displayFields.addSeparator("");

		displayFields.addLabel(getDisplayFieldName());

		displayFields.addSelectList(
			LAYOUT_TEMPLATE_SELECT_LIST, _getLayoutTemplates());

		displayFields.addSelectList(LAYOUT_TYPE_SELECT_LIST, _getLayoutTypes());

		//hidden
		displayFields.addCheckbox(getDisplayFieldName(FieldKeys.CHECK_BOX));

		displayFields.addCount(
			getDisplayFieldName(FieldKeys.INPUT_COUNT), true);

		displayFields.addDepth(
			getDisplayFieldName(FieldKeys.INPUT_DEPTH));

		displayFields.addSubCount(
			getDisplayFieldName(FieldKeys.INPUT_SUBCOUNT));

		// TODO add portlet handler

		return displayFields;
	}

	@Override
	public Object addEntry(RequestContext requestContext) throws Exception {
		Layout layout = (Layout)super.addEntry(requestContext);

		LayoutPrototype layoutPrototype =
			(LayoutPrototype)requestContext.get(LAYOUT_TEMPLATE_SELECT_LIST);

		if (layoutPrototype != null) {
			layout.setLayoutPrototypeUuid(layoutPrototype.getUuid());
		}

		return layout;
	}

	@Override
	protected Class<?>[] getAddArgClazzs() {
		return new Class<?>[] {
			Long.TYPE, Long.TYPE, Boolean.TYPE, Long.TYPE, String.class,
			String.class, String.class, String.class, Boolean.TYPE,
			String.class, ServiceContext.class
		};
	}

	@Override
	protected String[] getAddArgNames() {
		return new String[] {
			"userId", "groupId", "privateLayout", "parentLayoutId", "name",
			"title", "description", "type", "hidden", "friendlyURL",
			"serviceContext"
		};
	}

	@Override
	protected Class<?> getAddClass() {
		return LayoutLocalServiceUtil.class;
	}

	@Override
	protected Map<String, Object> getAddEntrySpecifiedArgs(
		RequestContext requestContext) throws Exception {

		String layoutType = requestContext.getString(LAYOUT_TYPE_SELECT_LIST);
		if (layoutType.equals(StringPool.BLANK)) {
			layoutType = LayoutConstants.TYPE_PORTLET;
		}

		boolean privateLayout = requestContext.getBoolean(
			getDisplayFieldName(FieldKeys.CHECK_BOX));

		Map<String, Object> entrySpecifiedArgs = new HashMap<String, Object>(3);

		entrySpecifiedArgs.put("hidden", false);
		entrySpecifiedArgs.put("privateLayout", privateLayout);
		entrySpecifiedArgs.put("type", layoutType);

		return entrySpecifiedArgs;
	}

	@Override
	protected String getAddMethodName() {
		return "addLayout";
	}

	@Override
	protected List<String> getChildHandlerNames() {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	@Override
	protected Object getClassPK(Object entry) {
		if (Validator.isNull(entry)) {
			return LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
		}

		return ((Layout)entry).getLayoutId();
	}

	@Override
	protected String getClassPKName() {
		return null;
	}

	@Override
	protected String getParentClassPKName() {
		return "parentLayoutId";
	}

	@Override
	protected Class<?>[] getUpdateArgClazzs() {
		return null;
	}

	@Override
	protected String[] getUpdateArgNames() {
		return new String[0];
	}

	@Override
	protected Class<?> getUpdateClass() {
		return null;
	}

	@Override
	protected Map<String, Object> getUpdateEntrySpecifiedArgs(
		Object entry, RequestContext requestContext) throws Exception {

		return null;
	}

	@Override
	protected String getUpdateMethodName() {
		return null;
	}

	private Map<String, List<KeyValuePair>> _getLayoutNameIdPairs(long groupId)
		throws SystemException {

		Map<String, List<KeyValuePair>> layoutNameIdPairs =
			new TreeMap<String, List<KeyValuePair>>();

		List<KeyValuePair> privateLayoutNameIdPairs = _getLayoutNameIdPairs(
			groupId, false);

		List<KeyValuePair> publicLayoutNameIdPairs = _getLayoutNameIdPairs(
			groupId, true);

		layoutNameIdPairs.put("public-pages", publicLayoutNameIdPairs);

		layoutNameIdPairs.put("private-pages", privateLayoutNameIdPairs);

		return layoutNameIdPairs;
	}

	private List<KeyValuePair> _getLayoutNameIdPairs(
		long groupId, boolean isPublic) throws SystemException {

		List<KeyValuePair> layoutNameIdPairs = new ArrayList<KeyValuePair>();

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			groupId, isPublic);

		for (Layout layout : layouts) {
			if (layout.isHidden() || !layout.isRootLayout() ||
				layout.isTypeControlPanel()|| layout.isTypeControlPanel() ||
				layout.isTypeURL()) {

				continue;
			}

			String layoutId = String.valueOf(layout.getLayoutId());
			String layoutName = layout.getName();

			layoutNameIdPairs.add(
				new KeyValuePair(layoutName, layoutId));
		}

		return layoutNameIdPairs;
	}

	private List<KeyValuePair> _getLayoutTemplates() throws SystemException {
		List<LayoutPrototype> layoutPrototypes =
			LayoutPrototypeLocalServiceUtil.getLayoutPrototypes(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		List<KeyValuePair> layoutPrototypeNameIdPairs =
			new ArrayList<KeyValuePair>();

		layoutPrototypeNameIdPairs.add(new KeyValuePair("", ""));

		for (LayoutPrototype layoutPrototype : layoutPrototypes) {
			layoutPrototypeNameIdPairs.add(
				new KeyValuePair(
					layoutPrototype.getName(),
					String.valueOf(layoutPrototype.getLayoutPrototypeId())));
		}

		return layoutPrototypeNameIdPairs;
	}

	private List<KeyValuePair> _getLayoutTypes() {
		List<KeyValuePair> layoutTypes = new ArrayList<KeyValuePair>();

		layoutTypes.add(new KeyValuePair("", ""));

		layoutTypes.add(
			new KeyValuePair(
				LayoutConstants.TYPE_ARTICLE, LayoutConstants.TYPE_ARTICLE));

		layoutTypes.add(
			new KeyValuePair(
				LayoutConstants.TYPE_EMBEDDED, LayoutConstants.TYPE_EMBEDDED));

		layoutTypes.add(
			new KeyValuePair(
				LayoutConstants.TYPE_PANEL, LayoutConstants.TYPE_PANEL));

		layoutTypes.add(
			new KeyValuePair(
				LayoutConstants.TYPE_PORTLET, LayoutConstants.TYPE_PORTLET));

		return layoutTypes;
	}



}