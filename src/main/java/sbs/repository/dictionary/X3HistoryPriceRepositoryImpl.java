package sbs.repository.dictionary;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.x3.X3HistoryPrice;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class X3HistoryPriceRepositoryImpl extends GenericRepositoryAdapter<X3HistoryPrice, String>
		implements X3HistoryPriceRepository {


}
