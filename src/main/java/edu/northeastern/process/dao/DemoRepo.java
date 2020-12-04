package edu.northeastern.process.dao;


import edu.northeastern.process.beans.DemoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Jim Z on 12/3/20 21:02
 */

@Repository
public interface DemoRepo extends CrudRepository<DemoEntity, Integer> {
    public DemoEntity findByEmail(String email);
    public boolean existsByEmail(String email);
}
