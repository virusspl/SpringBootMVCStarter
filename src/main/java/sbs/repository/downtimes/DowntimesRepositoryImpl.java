package sbs.repository.downtimes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.controller.downtimes.ReportNotifierLine;
import sbs.controller.downtimes.ReportResponsibleLine;
import sbs.model.downtimes.Downtime;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class DowntimesRepositoryImpl extends GenericRepositoryAdapter<Downtime, Integer>
		implements DowntimesRepository {

	@Override
	public List<Downtime> findAllPending() {
		String hql = "from Downtime d where d.opened = :bool "
				+ " order by d.startDate asc ";
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setBoolean("bool", true)
		.list();

		return result;
	}

	@Override
	public List<Downtime> findWithoutResponseForUser(Long userId) {
		String hql = "from Downtime d where d.responseType.order = :ord "
				+ "and d.cause.responsibleUser.id = :uid";
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 10)
		.setLong("uid", userId)
		.list();

		return result;
	}

	@Override
	public List<ReportNotifierLine> getReportByNotifier(Date startDate, Date endDate) {
		
		String hql = "from Downtime d where d.startDate >= :strDt "
				+ "and d.startDate <= :endDt";
		
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setDate("strDt", startDate)
		.setDate("endDt", endDate)
		.list();

		ReportNotifierLine line;
		List<ReportNotifierLine> list = new ArrayList<>();
		
		for(Downtime dt: result) {
			line = new ReportNotifierLine();
			line.setDate(dt.getStartDate());
			line.setCalculatedLength(dt.getEndDate());
			line.setMachine(dt.getMachineCode());
			line.setType(dt.getType().getCode());
			line.setCause(dt.getCause().getShortText());
			line.setDepartment(dt.getInitDepartment());
			line.setNotifier(dt.getInitLastName() + " " + dt.getInitFirstName());
			line.setOpened(dt.isOpened());
			line.setId(dt.getId());
			line.setInitComment(dt.getComment());
			line.setRespComment(dt.getResponseComment());
			line.setEndComment(dt.getEndComment());
			list.add(line);
		}
		
		return list;
	}

	@Override
	public List<ReportResponsibleLine> getReportByResponsible(Date startDate, Date endDate) {
		String hql = "from Downtime d where d.startDate >= :strDt "
				+ "and d.startDate <= :endDt";
		
		@SuppressWarnings("unchecked")
		List<Downtime> result = (List<Downtime>) 
		currentSession()
		.createQuery(hql)
		.setDate("strDt", startDate)
		.setDate("endDt", endDate)
		.list();

		ReportResponsibleLine line;
		List<ReportResponsibleLine> list = new ArrayList<>();
		
		for(Downtime dt: result) {
			line = new ReportResponsibleLine();
			line.setDate(dt.getStartDate());
			line.setType(dt.getType().getCode());
			line.setCause(dt.getCause().getShortText());
			line.setResponsible(dt.getCause().getResponsibleUser().getName());
			line.setOpened(dt.isOpened());
			line.setResponse(dt.getResponseType().getCode());
			line.setId(dt.getId());
			line.setInitComment(dt.getComment());
			line.setRespComment(dt.getResponseComment());
			line.setEndComment(dt.getEndComment());			
			list.add(line);
		}
		
		return list;
	}
	
}


