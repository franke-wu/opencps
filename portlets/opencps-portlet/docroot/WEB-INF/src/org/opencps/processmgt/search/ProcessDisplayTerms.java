/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.processmgt.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;


/**
 * @author khoavd
 *
 */
public class ProcessDisplayTerms extends DisplayTerms{
	public static final String PROCESS_ADMINISTRATIONCODE = "administrationCode";
	public static final String PROCESS_DOMAINCODE = "domainCode";
	public static final String PROCESS_DOSSIERSTATUS = "dossierStatus";
	public static final String PROCESS_DOSSIERTEMPLATE_ID = "dossierTemplateId";
	public static final String PROCESS_DOSSIER_TYPE = "dossierType";
	/**
     * @param request
     */
    public ProcessDisplayTerms(PortletRequest request) {

	    super(request);
	    // TODO Auto-generated constructor stub
    }

}
