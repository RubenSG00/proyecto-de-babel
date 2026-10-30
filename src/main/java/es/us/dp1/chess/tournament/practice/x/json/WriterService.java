package es.us.dp1.chess.tournament.practice.x.json;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

@Service
public class WriterService {

    private final WriterRepository writers;
    private final NovelRepository novels;

    @Autowired
    public WriterService(WriterRepository writers, NovelRepository novels) {
        this.writers = writers;
        this.novels = novels;
    }

    @Transactional(readOnly = true)
    public Writer findWriter(Integer id) {
        return writers.findById(id).orElseThrow(() -> new ResourceNotFoundException("Writer", "id", id));
    }

    @Transactional(readOnly = true)
    public Novel findNovel(Integer id) {
        return novels.findById(id).orElseThrow(() -> new ResourceNotFoundException("Novel", "id", id));
    }

    // Navigating the lazy collection inside the transaction (404 if the writer does not exist)
    @Transactional(readOnly = true)
    public List<Novel> findNovelsOfWriter(Integer writerId) {
        return List.copyOf(findWriter(writerId).getNovels());
    }

}
