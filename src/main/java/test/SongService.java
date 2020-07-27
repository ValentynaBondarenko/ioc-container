package test;
import com.bondarenko.bean.factory.stereotype.Component;

@Component
public class SongService {
    private SongRepository songRepository;
    private MoodService moodService;
}
