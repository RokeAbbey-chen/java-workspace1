package java.com.xqtv.paopao.dataaccess.spring;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReadWriteDataSourceRouter extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
		if (!isReadOnly) {
			return "master";
		}
		// 如果是只读，可以从任意一个slave中执行
		return "slave";
	}
}
