package sbs.repository.downtimes;

import java.util.Date;
import java.util.List;

import sbs.controller.downtimes.ReportNotifierLine;
import sbs.controller.downtimes.ReportResponsibleLine;
import sbs.model.downtimes.Downtime;
import sbs.repository.GenericRepository;

public interface DowntimesRepository extends GenericRepository<Downtime,Integer> {

	List<Downtime> findAllPending();

	List<Downtime> findWithoutResponseForUser(Long userId);

	List<ReportNotifierLine> getReportByNotifier(Date startDate, Date endDate);

	List<ReportResponsibleLine> getReportByResponsible(Date startDate, Date endDate);

	
}

