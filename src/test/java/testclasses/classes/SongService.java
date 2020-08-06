package testclasses.classes;

import com.bondarenko.bean.factory.BeanNameAware;
import com.bondarenko.bean.factory.annotation.Autowired;
import com.bondarenko.bean.factory.annotation.stereotype.Component;
import testclasses.packegeFirst.MetaInfoService;

@Component
public class SongService implements BeanNameAware {

    private String beanName;//before init
    private SongRepository songRepository;//repo constructor -> also bean
    @Autowired
    private MoodService moodService;
    @Autowired
    private MetaInfoService metaInfoService;

    public SongService() {

    }
    @Autowired
    public SongService(MoodService moodService, MetaInfoService metaInfoService, RepositoryContainer repositoryContainer) {
        this.moodService = moodService;
        this.metaInfoService = metaInfoService;
        this.songRepository = (SongRepository) repositoryContainer.repositories.get("songRepository");
    }

    public SongRepository getSongRepository() {
        return songRepository;
    }

    @Autowired
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
