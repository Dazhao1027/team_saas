package cn.itcast.service.cargo;



import cn.itcast.domain.cargo.PackingList;
import cn.itcast.domain.cargo.PackingListExample;

import java.util.List;

public interface PackingListService {
    int delete(String packingListId);

    int save(PackingList record);

    List<PackingList> findAll(PackingListExample example);

    PackingList findById(String packingListId);

    int update(PackingList record);
}
