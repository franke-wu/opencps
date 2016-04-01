/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package org.opencps.usermgt.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.service.persistence.UserPersistence;

import org.opencps.usermgt.model.WorkingUnit;
import org.opencps.usermgt.service.WorkingUnitLocalService;
import org.opencps.usermgt.service.persistence.EmployeePersistence;
import org.opencps.usermgt.service.persistence.JobPosPersistence;
import org.opencps.usermgt.service.persistence.WorkingUnitFinder;
import org.opencps.usermgt.service.persistence.WorkingUnitPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the working unit local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link org.opencps.usermgt.service.impl.WorkingUnitLocalServiceImpl}.
 * </p>
 *
 * @author khoavd
 * @see org.opencps.usermgt.service.impl.WorkingUnitLocalServiceImpl
 * @see org.opencps.usermgt.service.WorkingUnitLocalServiceUtil
 * @generated
 */
public abstract class WorkingUnitLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements WorkingUnitLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link org.opencps.usermgt.service.WorkingUnitLocalServiceUtil} to access the working unit local service.
	 */

	/**
	 * Adds the working unit to the database. Also notifies the appropriate model listeners.
	 *
	 * @param workingUnit the working unit
	 * @return the working unit that was added
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WorkingUnit addWorkingUnit(WorkingUnit workingUnit)
		throws SystemException {
		workingUnit.setNew(true);

		return workingUnitPersistence.update(workingUnit);
	}

	/**
	 * Creates a new working unit with the primary key. Does not add the working unit to the database.
	 *
	 * @param workingunitId the primary key for the new working unit
	 * @return the new working unit
	 */
	@Override
	public WorkingUnit createWorkingUnit(long workingunitId) {
		return workingUnitPersistence.create(workingunitId);
	}

	/**
	 * Deletes the working unit with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param workingunitId the primary key of the working unit
	 * @return the working unit that was removed
	 * @throws PortalException if a working unit with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WorkingUnit deleteWorkingUnit(long workingunitId)
		throws PortalException, SystemException {
		return workingUnitPersistence.remove(workingunitId);
	}

	/**
	 * Deletes the working unit from the database. Also notifies the appropriate model listeners.
	 *
	 * @param workingUnit the working unit
	 * @return the working unit that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WorkingUnit deleteWorkingUnit(WorkingUnit workingUnit)
		throws SystemException {
		return workingUnitPersistence.remove(workingUnit);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(WorkingUnit.class,
			clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		return workingUnitPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.usermgt.model.impl.WorkingUnitModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end)
		throws SystemException {
		return workingUnitPersistence.findWithDynamicQuery(dynamicQuery, start,
			end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.usermgt.model.impl.WorkingUnitModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return workingUnitPersistence.findWithDynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows that match the dynamic query
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery)
		throws SystemException {
		return workingUnitPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows that match the dynamic query
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection) throws SystemException {
		return workingUnitPersistence.countWithDynamicQuery(dynamicQuery,
			projection);
	}

	@Override
	public WorkingUnit fetchWorkingUnit(long workingunitId)
		throws SystemException {
		return workingUnitPersistence.fetchByPrimaryKey(workingunitId);
	}

	/**
	 * Returns the working unit with the primary key.
	 *
	 * @param workingunitId the primary key of the working unit
	 * @return the working unit
	 * @throws PortalException if a working unit with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public WorkingUnit getWorkingUnit(long workingunitId)
		throws PortalException, SystemException {
		return workingUnitPersistence.findByPrimaryKey(workingunitId);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException, SystemException {
		return workingUnitPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the working units.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link org.opencps.usermgt.model.impl.WorkingUnitModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of working units
	 * @param end the upper bound of the range of working units (not inclusive)
	 * @return the range of working units
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<WorkingUnit> getWorkingUnits(int start, int end)
		throws SystemException {
		return workingUnitPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of working units.
	 *
	 * @return the number of working units
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int getWorkingUnitsCount() throws SystemException {
		return workingUnitPersistence.countAll();
	}

	/**
	 * Updates the working unit in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param workingUnit the working unit
	 * @return the working unit that was updated
	 * @throws SystemException if a system exception occurred
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WorkingUnit updateWorkingUnit(WorkingUnit workingUnit)
		throws SystemException {
		return workingUnitPersistence.update(workingUnit);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void addJobPosWorkingUnit(long jobPosId, long workingunitId)
		throws SystemException {
		jobPosPersistence.addWorkingUnit(jobPosId, workingunitId);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void addJobPosWorkingUnit(long jobPosId, WorkingUnit workingUnit)
		throws SystemException {
		jobPosPersistence.addWorkingUnit(jobPosId, workingUnit);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void addJobPosWorkingUnits(long jobPosId, long[] workingunitIds)
		throws SystemException {
		jobPosPersistence.addWorkingUnits(jobPosId, workingunitIds);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void addJobPosWorkingUnits(long jobPosId,
		List<WorkingUnit> WorkingUnits) throws SystemException {
		jobPosPersistence.addWorkingUnits(jobPosId, WorkingUnits);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void clearJobPosWorkingUnits(long jobPosId)
		throws SystemException {
		jobPosPersistence.clearWorkingUnits(jobPosId);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void deleteJobPosWorkingUnit(long jobPosId, long workingunitId)
		throws SystemException {
		jobPosPersistence.removeWorkingUnit(jobPosId, workingunitId);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void deleteJobPosWorkingUnit(long jobPosId, WorkingUnit workingUnit)
		throws SystemException {
		jobPosPersistence.removeWorkingUnit(jobPosId, workingUnit);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void deleteJobPosWorkingUnits(long jobPosId, long[] workingunitIds)
		throws SystemException {
		jobPosPersistence.removeWorkingUnits(jobPosId, workingunitIds);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void deleteJobPosWorkingUnits(long jobPosId,
		List<WorkingUnit> WorkingUnits) throws SystemException {
		jobPosPersistence.removeWorkingUnits(jobPosId, WorkingUnits);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<WorkingUnit> getJobPosWorkingUnits(long jobPosId)
		throws SystemException {
		return jobPosPersistence.getWorkingUnits(jobPosId);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<WorkingUnit> getJobPosWorkingUnits(long jobPosId, int start,
		int end) throws SystemException {
		return jobPosPersistence.getWorkingUnits(jobPosId, start, end);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<WorkingUnit> getJobPosWorkingUnits(long jobPosId, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		return jobPosPersistence.getWorkingUnits(jobPosId, start, end,
			orderByComparator);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int getJobPosWorkingUnitsCount(long jobPosId)
		throws SystemException {
		return jobPosPersistence.getWorkingUnitsSize(jobPosId);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public boolean hasJobPosWorkingUnit(long jobPosId, long workingunitId)
		throws SystemException {
		return jobPosPersistence.containsWorkingUnit(jobPosId, workingunitId);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public boolean hasJobPosWorkingUnits(long jobPosId)
		throws SystemException {
		return jobPosPersistence.containsWorkingUnits(jobPosId);
	}

	/**
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void setJobPosWorkingUnits(long jobPosId, long[] workingunitIds)
		throws SystemException {
		jobPosPersistence.setWorkingUnits(jobPosId, workingunitIds);
	}

	/**
	 * Returns the employee local service.
	 *
	 * @return the employee local service
	 */
	public org.opencps.usermgt.service.EmployeeLocalService getEmployeeLocalService() {
		return employeeLocalService;
	}

	/**
	 * Sets the employee local service.
	 *
	 * @param employeeLocalService the employee local service
	 */
	public void setEmployeeLocalService(
		org.opencps.usermgt.service.EmployeeLocalService employeeLocalService) {
		this.employeeLocalService = employeeLocalService;
	}

	/**
	 * Returns the employee remote service.
	 *
	 * @return the employee remote service
	 */
	public org.opencps.usermgt.service.EmployeeService getEmployeeService() {
		return employeeService;
	}

	/**
	 * Sets the employee remote service.
	 *
	 * @param employeeService the employee remote service
	 */
	public void setEmployeeService(
		org.opencps.usermgt.service.EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * Returns the employee persistence.
	 *
	 * @return the employee persistence
	 */
	public EmployeePersistence getEmployeePersistence() {
		return employeePersistence;
	}

	/**
	 * Sets the employee persistence.
	 *
	 * @param employeePersistence the employee persistence
	 */
	public void setEmployeePersistence(EmployeePersistence employeePersistence) {
		this.employeePersistence = employeePersistence;
	}

	/**
	 * Returns the job pos local service.
	 *
	 * @return the job pos local service
	 */
	public org.opencps.usermgt.service.JobPosLocalService getJobPosLocalService() {
		return jobPosLocalService;
	}

	/**
	 * Sets the job pos local service.
	 *
	 * @param jobPosLocalService the job pos local service
	 */
	public void setJobPosLocalService(
		org.opencps.usermgt.service.JobPosLocalService jobPosLocalService) {
		this.jobPosLocalService = jobPosLocalService;
	}

	/**
	 * Returns the job pos remote service.
	 *
	 * @return the job pos remote service
	 */
	public org.opencps.usermgt.service.JobPosService getJobPosService() {
		return jobPosService;
	}

	/**
	 * Sets the job pos remote service.
	 *
	 * @param jobPosService the job pos remote service
	 */
	public void setJobPosService(
		org.opencps.usermgt.service.JobPosService jobPosService) {
		this.jobPosService = jobPosService;
	}

	/**
	 * Returns the job pos persistence.
	 *
	 * @return the job pos persistence
	 */
	public JobPosPersistence getJobPosPersistence() {
		return jobPosPersistence;
	}

	/**
	 * Sets the job pos persistence.
	 *
	 * @param jobPosPersistence the job pos persistence
	 */
	public void setJobPosPersistence(JobPosPersistence jobPosPersistence) {
		this.jobPosPersistence = jobPosPersistence;
	}

	/**
	 * Returns the working unit local service.
	 *
	 * @return the working unit local service
	 */
	public org.opencps.usermgt.service.WorkingUnitLocalService getWorkingUnitLocalService() {
		return workingUnitLocalService;
	}

	/**
	 * Sets the working unit local service.
	 *
	 * @param workingUnitLocalService the working unit local service
	 */
	public void setWorkingUnitLocalService(
		org.opencps.usermgt.service.WorkingUnitLocalService workingUnitLocalService) {
		this.workingUnitLocalService = workingUnitLocalService;
	}

	/**
	 * Returns the working unit remote service.
	 *
	 * @return the working unit remote service
	 */
	public org.opencps.usermgt.service.WorkingUnitService getWorkingUnitService() {
		return workingUnitService;
	}

	/**
	 * Sets the working unit remote service.
	 *
	 * @param workingUnitService the working unit remote service
	 */
	public void setWorkingUnitService(
		org.opencps.usermgt.service.WorkingUnitService workingUnitService) {
		this.workingUnitService = workingUnitService;
	}

	/**
	 * Returns the working unit persistence.
	 *
	 * @return the working unit persistence
	 */
	public WorkingUnitPersistence getWorkingUnitPersistence() {
		return workingUnitPersistence;
	}

	/**
	 * Sets the working unit persistence.
	 *
	 * @param workingUnitPersistence the working unit persistence
	 */
	public void setWorkingUnitPersistence(
		WorkingUnitPersistence workingUnitPersistence) {
		this.workingUnitPersistence = workingUnitPersistence;
	}

	/**
	 * Returns the working unit finder.
	 *
	 * @return the working unit finder
	 */
	public WorkingUnitFinder getWorkingUnitFinder() {
		return workingUnitFinder;
	}

	/**
	 * Sets the working unit finder.
	 *
	 * @param workingUnitFinder the working unit finder
	 */
	public void setWorkingUnitFinder(WorkingUnitFinder workingUnitFinder) {
		this.workingUnitFinder = workingUnitFinder;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.service.CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.service.CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.service.ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.service.ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.service.UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.service.UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.service.UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();

		PersistedModelLocalServiceRegistryUtil.register("org.opencps.usermgt.model.WorkingUnit",
			workingUnitLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"org.opencps.usermgt.model.WorkingUnit");
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	@Override
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	@Override
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	@Override
	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		if (contextClassLoader != _classLoader) {
			currentThread.setContextClassLoader(_classLoader);
		}

		try {
			return _clpInvoker.invokeMethod(name, parameterTypes, arguments);
		}
		finally {
			if (contextClassLoader != _classLoader) {
				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	protected Class<?> getModelClass() {
		return WorkingUnit.class;
	}

	protected String getModelClassName() {
		return WorkingUnit.class.getName();
	}

	/**
	 * Performs an SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = workingUnitPersistence.getDataSource();

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = org.opencps.usermgt.service.EmployeeLocalService.class)
	protected org.opencps.usermgt.service.EmployeeLocalService employeeLocalService;
	@BeanReference(type = org.opencps.usermgt.service.EmployeeService.class)
	protected org.opencps.usermgt.service.EmployeeService employeeService;
	@BeanReference(type = EmployeePersistence.class)
	protected EmployeePersistence employeePersistence;
	@BeanReference(type = org.opencps.usermgt.service.JobPosLocalService.class)
	protected org.opencps.usermgt.service.JobPosLocalService jobPosLocalService;
	@BeanReference(type = org.opencps.usermgt.service.JobPosService.class)
	protected org.opencps.usermgt.service.JobPosService jobPosService;
	@BeanReference(type = JobPosPersistence.class)
	protected JobPosPersistence jobPosPersistence;
	@BeanReference(type = org.opencps.usermgt.service.WorkingUnitLocalService.class)
	protected org.opencps.usermgt.service.WorkingUnitLocalService workingUnitLocalService;
	@BeanReference(type = org.opencps.usermgt.service.WorkingUnitService.class)
	protected org.opencps.usermgt.service.WorkingUnitService workingUnitService;
	@BeanReference(type = WorkingUnitPersistence.class)
	protected WorkingUnitPersistence workingUnitPersistence;
	@BeanReference(type = WorkingUnitFinder.class)
	protected WorkingUnitFinder workingUnitFinder;
	@BeanReference(type = com.liferay.counter.service.CounterLocalService.class)
	protected com.liferay.counter.service.CounterLocalService counterLocalService;
	@BeanReference(type = com.liferay.portal.service.ResourceLocalService.class)
	protected com.liferay.portal.service.ResourceLocalService resourceLocalService;
	@BeanReference(type = com.liferay.portal.service.UserLocalService.class)
	protected com.liferay.portal.service.UserLocalService userLocalService;
	@BeanReference(type = com.liferay.portal.service.UserService.class)
	protected com.liferay.portal.service.UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private String _beanIdentifier;
	private ClassLoader _classLoader;
	private WorkingUnitLocalServiceClpInvoker _clpInvoker = new WorkingUnitLocalServiceClpInvoker();
}