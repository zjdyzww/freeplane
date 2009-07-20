/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.common.attribute;

import org.freeplane.core.filter.util.SortedComboBoxModel;
import org.freeplane.n3.nanoxml.XMLElement;

/**
 * @author Dimitry Polivaev
 */
public class AttributeRegistryElement {
	private class RegisteredAttributeValues extends SortedComboBoxModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void _add(final Object element) {
			super.add(element);
		}

		public void _remove(final Object element) {
			super.remove(element);
		}

		public void _replace(final Object oldO, final Object newO) {
			super.replace(oldO, newO);
		}

		@Override
		public void add(final Object element) {
			registry.getAttributeController().performRegistryAttributeValue(getKey(), element.toString());
		}

		public String getKey() {
			return key;
		}

		public AttributeRegistry getRegistry() {
			return registry;
		}

		@Override
		public void remove(final Object element) {
			registry.getAttributeController().performRemoveAttributeValue(getKey(), element.toString());
		}

		@Override
		public void replace(final Object oldO, final Object newO) {
			registry.getAttributeController().performReplaceAttributeValue(getKey(), oldO.toString(), newO.toString());
		}
	}

	private boolean isManual;
	private boolean isRestricted;
	private boolean isVisible;
	private String key;
	final private AttributeRegistry registry;
	private Boolean restrictionModel;
	final private RegisteredAttributeValues values;
	private Boolean visibilityModel;

	public AttributeRegistryElement(final AttributeRegistry registry, final String key) {
		super();
		this.key = key;
		this.registry = registry;
		values = new RegisteredAttributeValues();
		isVisible = false;
		visibilityModel = new Boolean(isVisible);
		isRestricted = false;
		isManual = false;
		restrictionModel = new Boolean(isRestricted);
	}

	public void addValue(final String s) {
		values._add(s);
		registry.fireAttributesChanged();
	}

	public Comparable getKey() {
		return key;
	}

	Boolean getRestriction() {
		return restrictionModel;
	}

	public SortedComboBoxModel getValues() {
		return values;
	}

	Boolean getVisibilityModel() {
		return visibilityModel;
	}

	public boolean isManual() {
		return isManual;
	}

	public boolean isRestricted() {
		return isRestricted;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void removeAllValues() {
		values.clear();
		registry.fireAttributesChanged();
	}

	public void removeValue(final String s) {
		values._remove(s);
		registry.fireAttributesChanged();
	}

	public void replaceValue(final String oldValue, final String newValue) {
		values._replace(oldValue, newValue);
		registry.fireAttributesChanged();
	}

	/**
	 */
	public XMLElement save() {
		final XMLElement element = new XMLElement();
		if (isVisible()) {
			element.setAttribute("VISIBLE", "true");
		}
		if (isManual()) {
			element.setAttribute("MANUAL", "true");
		}
		if (isRestricted()) {
			element.setAttribute("RESTRICTED", "true");
			for (int i = 0; i < values.getSize(); i++) {
				final XMLElement value = new XMLElement();
				value.setName(AttributeBuilder.XML_NODE_REGISTERED_ATTRIBUTE_VALUE);
				value.setAttribute("VALUE", values.getElementAt(i).toString());
				element.addChild(value);
			}
		}
		element.setName(AttributeBuilder.XML_NODE_REGISTERED_ATTRIBUTE_NAME);
		element.setAttribute("NAME", key.toString());
		return element;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setManual(final boolean isManual) {
		this.isManual = isManual;
	}

	public void setRestriction(final boolean isRestricted) {
		this.isRestricted = isRestricted;
		restrictionModel = Boolean.valueOf(isRestricted);
		registry.fireAttributesChanged();
	}

	void setRestrictionModel(final Boolean restrictionModel) {
		this.restrictionModel = restrictionModel;
	}

	public void setVisibility(final boolean isVisible) {
		if (this.isVisible != isVisible) {
			this.isVisible = isVisible;
			visibilityModel = Boolean.valueOf(isVisible);
			if (isVisible) {
				registry.incrementVisibleElementsNumber();
			}
			else {
				registry.decrementVisibleElementsNumber();
			}
			registry.fireAttributeLayoutChanged();
		}
	}

	void setVisibilityModel(final Boolean visibilityModel) {
		this.visibilityModel = visibilityModel;
	}
}
