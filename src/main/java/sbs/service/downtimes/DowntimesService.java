package sbs.service.downtimes;

import java.util.Date;
import java.util.List;

import sbs.controller.downtimes.ReportNotifierLine;
import sbs.controller.downtimes.ReportResponsibleLine;
import sbs.model.downtimes.Downtime;
import sbs.service.GenericService;

public interface DowntimesService extends GenericService<Downtime, Integer>{

	List<Downtime> findAllPending();

	List<Downtime> findWithoutResponseForUser(Long userId);

	List<ReportNotifierLine> getReportByNotifier(Date startDate, Date endDate);

	List<ReportResponsibleLine> getReportByResponsible(Date startDate, Date endDate);

	
}
