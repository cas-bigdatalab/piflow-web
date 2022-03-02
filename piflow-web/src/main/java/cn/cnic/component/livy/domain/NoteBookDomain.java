package cn.cnic.component.livy.domain;

import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.mapper.NoteBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class NoteBookDomain {

    private final NoteBookMapper noteBookMapper;

    @Autowired
    public NoteBookDomain(NoteBookMapper noteBookMapper) {
        this.noteBookMapper = noteBookMapper;
    }

    public NoteBook getNoteBookById(boolean isAdmin, String username, String id){
        return noteBookMapper.getNoteBookById(isAdmin, username, id);
    }
}
