package sbs.service.downtimes;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.controller.downtimes.ReportNotifierLine;
import sbs.controller.downtimes.ReportResponsibleLine;
import sbs.model.downtimes.Downtime;
import sbs.repository.GenericRepository;
import sbs.repository.downtimes.DowntimesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class DowntimesServiceImpl extends GenericServiceAdapter<Downtime, Integer> implements DowntimesService{
	
	private DowntimesRepository downtimesRepository;
	
    @Autowired
	public DowntimesServiceImpl(@Qualifier("downtimesRepositoryImpl") GenericRepository<Downtime, Integer> genericRepository) {
			super(genericRepository);
			this.downtimesRepository = (DowntimesRepository) genericRepository;
	}

	@Override
	public List<Downtime> findAllPending() {
		return downtimesRepository.findAllPending();
	}

	@Override
	public List<Downtime> findWithoutResponseForUser(Long userId) {
		return downtimesRepository.findWithoutResponseForUser(userId);
	}

	@Override
	public List<ReportNotifierLine> getReportByNotifier(Date startDate, Date endDate) {
		return downtimesRepository.getReportByNotifier(startDate, endDate);
	}

	@Override
	public List<ReportResponsibleLine> getReportByResponsible(Date startDate, Date endDate) {
		return downtimesRepository.getReportByResponsible(startDate, endDate);
	}

}
