package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportExample;
import cn.itcast.domain.vo.ExportResult;
import com.github.pagehelper.PageInfo;


public interface ExportService {

    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

	PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize);

	// 根据报运的结果，修改数据库：报运状态、备注、交税金额
    void updateExport(ExportResult exportResult);
}
