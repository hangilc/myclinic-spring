package jp.chang.myclinic.hotline.lib;

import jp.chang.myclinic.dto.HotlineDTO;

import java.util.List;

public interface PeriodicFetcherCallback {
    void onPosts(List<HotlineDTO> posts, boolean initialSetup);
}
