package test;

import com.bondarenko.bean.factory.stereotype.Autowired;
import com.bondarenko.bean.factory.stereotype.Component;

@Component
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MoodService moodService;
}
