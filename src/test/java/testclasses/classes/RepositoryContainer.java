package testclasses.classes;

import com.bondarenko.bean.factory.annotation.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RepositoryContainer {
    public Map<String, Object> repositories = new HashMap<>() {
        {
            put("songRepository", new SongRepository());
        }
    };
}
