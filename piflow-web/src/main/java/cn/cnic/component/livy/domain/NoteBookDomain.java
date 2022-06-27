package cn.cnic.component.livy.domain;

import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.livy.mapper.NoteBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class NoteBookDomain {

    private final NoteBookMapper noteBookMapper;

    @Autowired
    public NoteBookDomain(NoteBookMapper noteBookMapper) {
        this.noteBookMapper = noteBookMapper;
    }

    public int addNoteBook(NoteBook noteBook){
        return noteBookMapper.addNoteBook(noteBook);
    }

    public int updateNoteBook(NoteBook noteBook){
        return noteBookMapper.updateNoteBook(noteBook);
    }

    public List<NoteBook> getNoteBookList(boolean isAdmin, String username, String param){
        return noteBookMapper.getNoteBookList(isAdmin, username, param);
    }

    public NoteBook getNoteBookById(boolean isAdmin, String username, String id){
        return noteBookMapper.getNoteBookById(isAdmin, username, id);
    }

    public Integer checkNoteBookByName(boolean isAdmin, String username, String name){
        return noteBookMapper.checkNoteBookByName(isAdmin, username, name);
    }

    public Integer deleteNoteBookById(boolean isAdmin, String username, String noteBookId){
        return noteBookMapper.deleteNoteBookById(isAdmin, username, noteBookId);
    }
}
