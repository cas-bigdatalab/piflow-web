package cn.cnic.component.livy.util;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.livy.entity.NoteBook;
import cn.cnic.component.testData.entity.TestData;

import java.util.Date;

public class NoteBookUtils {

    public static NoteBook setNoteBookBasicInformation(NoteBook noteBook, boolean isSetId, String username) {
        if (null == noteBook) {
            noteBook = new NoteBook();
        }
        if (isSetId) {
            noteBook.setId(UUIDUtils.getUUID32());
        }
        noteBook.setCrtDttm(new Date());
        noteBook.setCrtUser(username);
        noteBook.setLastUpdateDttm(new Date());
        noteBook.setLastUpdateUser(username);
        noteBook.setVersion(0L);
        return noteBook;
    }
}
