package biz.mercue.impactweb.service;

import biz.mercue.impactweb.model.Downloads;
import biz.mercue.impactweb.model.ListQueryForm;

import java.util.List;

public interface DownloadsService {
    Downloads getById(String id);

    List<Downloads> getAll();

    List<Downloads> getAvailable();

    ListQueryForm getDownloadsList(String categoryId, String text, int page);

    int createDownloads(Downloads downloads);

    int updateDownloads(Downloads downloads);

    int updateDownloadsList(List<Downloads> list);

    int deleteDownloads(Downloads downloads);
}
