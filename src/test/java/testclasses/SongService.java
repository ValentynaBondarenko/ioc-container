package testclasses;

import com.bondarenko.bean.factory.BeanNameAware;
import com.bondarenko.bean.factory.stereotype.Autowired;
import com.bondarenko.bean.factory.stereotype.Component;

@Component
public class SongService implements BeanNameAware {
    private String beanName;

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private MoodService moodService;

    public SongRepository getSongRepository() {
        return songRepository;
    }

    public void setSongRepository(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public MoodService getMoodService() {
        return moodService;
    }

    public void setMoodService(MoodService moodService) {
        this.moodService = moodService;
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    public String getBeanName() {
        return beanName;
    }
}
