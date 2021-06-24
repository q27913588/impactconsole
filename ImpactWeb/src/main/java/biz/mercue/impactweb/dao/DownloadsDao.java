package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.Downloads;

import java.util.List;

public interface DownloadsDao {
    void createDownloads(Downloads bean);

    Downloads getById(String id);

    Downloads getByName(String name);

    List<Downloads> getDownloadsList(String categoryId, String text, int page, int pageSize);

    int getDownloadsListCount(String categoryId, String text);

    List<Downloads> getAll();

    List<Downloads> getAvailable();

    int getDownloadsCount();

    void deleteDownloads(Downloads bean);
}
