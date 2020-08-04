package testclasses.classes;

import com.bondarenko.bean.factory.BeanNameAware;
import com.bondarenko.bean.factory.annotation.Autowired;
import com.bondarenko.bean.factory.annotation.stereotype.Component;
import testclasses.packegeFirst.MetaInfoService;

@Component
public class SongService implements BeanNameAware {
    private String beanName;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private MoodService moodService;
    private MetaInfoService metaInfoService;

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

    public MetaInfoService getMetaInfoService() {
        return metaInfoService;
    }

    public void setMetaInfoService(MetaInfoService metaInfoService) {
        this.metaInfoService = metaInfoService;
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    public String getBeanName() {
        return beanName;
    }
}